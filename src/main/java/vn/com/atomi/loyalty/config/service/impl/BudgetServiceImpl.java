package vn.com.atomi.loyalty.config.service.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import vn.com.atomi.loyalty.base.data.BaseService;
import vn.com.atomi.loyalty.base.data.ResponsePage;
import vn.com.atomi.loyalty.base.exception.BaseException;
import vn.com.atomi.loyalty.config.dto.input.ApprovalInput;
import vn.com.atomi.loyalty.config.dto.input.BudgetInput;
import vn.com.atomi.loyalty.config.dto.input.BudgetUpdateInput;
import vn.com.atomi.loyalty.config.dto.output.BudgetDetailOutput;
import vn.com.atomi.loyalty.config.dto.output.BudgetOutput;
import vn.com.atomi.loyalty.config.dto.output.HistoryOutput;
import vn.com.atomi.loyalty.config.entity.RuleApproval;
import vn.com.atomi.loyalty.config.enums.*;
import vn.com.atomi.loyalty.config.feign.LoyaltyCoreClient;
import vn.com.atomi.loyalty.config.repository.BudgetRepository;
import vn.com.atomi.loyalty.config.repository.CampaignRepository;
import vn.com.atomi.loyalty.config.repository.RuleApprovalRepository;
import vn.com.atomi.loyalty.config.repository.RuleRepository;
import vn.com.atomi.loyalty.config.service.BudgetService;
import vn.com.atomi.loyalty.config.utils.Utils;

@Service
@RequiredArgsConstructor
public class BudgetServiceImpl extends BaseService implements BudgetService {
  private final BudgetRepository budgetRepository;
  private final CampaignRepository campaignRepository;
  private final LoyaltyCoreClient loyaltyCoreClient;
  private final RuleApprovalRepository ruleApprovalRepository;
  private final RuleRepository ruleRepository;
  @Override
  public void createBudget(BudgetInput budgetInput) {
    var startDate = Utils.convertToLocalDate(budgetInput.getStartDate());
    var endDate = Utils.convertToLocalDate(budgetInput.getEndDate());
//    LocalDate currentDate = LocalDate.now();
//    if (!currentDate.isBefore(startDate) && !currentDate.isAfter(endDate)) {
//      budgetInput.setStatus(BudgetStatus.ACTIVE);
//    }

    LocalDate currentDate = LocalDate.now();
    // Check if startDate and endDate are in the past
    if (startDate.isBefore(currentDate)|| endDate.isBefore(currentDate)){
      throw new BaseException(ErrorCode.INVALID_DATE_RANGE);
    }
    // Check that startDate is before or equal to endDate
    if (startDate.isAfter(endDate)) {
      throw new BaseException(ErrorCode.INVALID_DATE_RANGE);
    }

    if (budgetRepository
        .findByDeletedFalseAndDecisionNumber(budgetInput.getDecisionNumber())
        .isPresent()) {
      throw new BaseException(ErrorCode.EXISTED_DECISION_NUMBER);
    }
    var budget = super.modelMapper.createBudget(budgetInput, startDate, endDate);
    //mac dinh la null khi tao
    budget.setStatus(null);
    budgetRepository.save(budget);
    // tạo code
    var id = ruleApprovalRepository.getSequence();
    var code = Utils.generateCode(id, RuleApproval.class.getSimpleName());
    // tạo bản ghi chờ duyệt
    var budgetApproval =
            super.modelMapper.convetToBudgetApproval(
                    budget.getId(),
                    startDate,
                    endDate,
                    ApprovalStatus.WAITING,
                    ApprovalType.CREATE,
                    id,
                    budget.getDecisionNumber());
    //check Luu - Gui cho duyet
    budgetApproval.setApprovalStatus(budgetInput.getApprovalStatus());
    budgetApproval = ruleApprovalRepository.save(budgetApproval);
    ruleApprovalRepository.save(budgetApproval);
  }


  @Override
  public ResponsePage<BudgetOutput> getListBudget(
          String decisionNumber,
          String totalBudget,
          String startDate,
          String endDate,
          BudgetStatus status,
          String name,
          ApprovalStatus approvalStatus,
          Pageable pageable) {
    var page =
            budgetRepository.getListBudget(
                    decisionNumber,
                    totalBudget,
                    status,
                    name,
                    approvalStatus,
                    Utils.convertToLocalDate(startDate),
                    Utils.convertToLocalDate(endDate),
                    pageable);
    var budgetOutputs = page.getContent().stream().map(budget -> {
      var output = modelMapper.convertToBudgetOutPut(budget);
      var ruleApproval = ruleApprovalRepository.findByBudgetId(budget.getId());
      if (ruleApproval.isPresent()) {
        output.setApprovalStatus(ruleApproval.get().getApprovalStatus());
      }
      return output;
    }).collect(Collectors.toList());
    return new ResponsePage<>(page, budgetOutputs);
  }

  @Override
  public void updateBudget(BudgetUpdateInput budgetUpdateInput) {
    var budget =
        budgetRepository
            .findByDeletedFalseAndId(budgetUpdateInput.getId())
            .orElseThrow(() -> new BaseException(ErrorCode.RECORD_NOT_EXISTED));

    if (budgetUpdateInput.getApprovalStatus()==ApprovalStatus.RECALL) {
      if (budgetUpdateInput.getName() != null) {
        budget.setName(budgetUpdateInput.getName());
      }
      //check cap nhat trang thai INACTIVE --> ACTIVE
      if (!budgetUpdateInput.getStatus().equals(budget.getStatus()) && budget.getStatus() == BudgetStatus.INACTIVE) {
        LocalDate currentDate = LocalDate.now();
        if (budget.getEndDate().isBefore(currentDate)) {
          throw new BaseException(ErrorCode.CONDITION_BUDGET_FAILED);
        }
        budget.setStatus(budgetUpdateInput.getStatus());
      }
      if (!budgetUpdateInput.getStatus().equals(budget.getStatus())) {
        budget.setStatus(budgetUpdateInput.getStatus());
      }
      //Budget new > Budget current
      if (budgetUpdateInput.getTotalBudget() != null && budgetUpdateInput.getTotalBudget() > budget.getTotalBudget()) {
        budget.setTotalBudget(budgetUpdateInput.getTotalBudget());
      }
      budgetRepository.save(budget);
    }
    else {
      throw new BaseException(ErrorCode.INVALID_APPROVAL_STATUS);
    }
  }

  @Override
  public BudgetDetailOutput detailBudget(Long id) {
    var budget =
        budgetRepository
            .findByDeletedFalseAndId(id)
            .orElseThrow(() -> new BaseException(ErrorCode.RECORD_NOT_EXISTED));

    var ruleApproval = ruleApprovalRepository.findByBudgetId(budget.getId());
    var budgetOutput = super.modelMapper.getDetailBudget(budget);
    if (ruleApproval.isPresent()) {
      budgetOutput.setApprovalStatus(ruleApproval.get().getApprovalStatus());
      budgetOutput.setCreatedBy(ruleApproval.get().getCreatedBy());
      budgetOutput.setApprovalId(ruleApproval.get().getId());
      //totalUnSpentBudget, TotalSpentBudget
      budgetOutput.setTotalUnSpentBudget(0);
      budgetOutput.setTotalSpentBudget(0);
      var campaign = campaignRepository.findByDeletedFalseAndId(ruleApproval.get().getCampaignId());
    }
    //  lấy lịch sử phê duyệt
    var ruleApprovals = ruleApprovalRepository.findByDeletedFalseAndBudgetId(id);
    budgetOutput.setHistoryOutputs(this.buildHistoryOutputs(ruleApprovals));
    return budgetOutput;
  }

  private List<HistoryOutput> buildHistoryOutputs(List<RuleApproval> ruleApprovals) {
    List<HistoryOutput> historyOutputs = new ArrayList<>();
    long recordNo = 0L;
    for (RuleApproval ruleApproval : ruleApprovals) {
      if (ruleApproval.getApprovalStatus().equals(ApprovalStatus.WAITING)) {
        historyOutputs.add(
                HistoryOutput.builder()
                        .id(++recordNo)
                        .approvalStatus(ruleApproval.getApprovalStatus())
                        .actionAt(ruleApproval.getCreatedAt())
                        .userAction(ruleApproval.getCreatedBy())
                        .approvalId(ruleApproval.getId())
                        .approvalType(ruleApproval.getApprovalType())
                        .approvalComment(ruleApproval.getApprovalComment())
                        .build());
      } else {
        historyOutputs.add(
                HistoryOutput.builder()
                        .id(++recordNo)
                        .approvalStatus(ApprovalStatus.WAITING)
                        .actionAt(ruleApproval.getCreatedAt())
                        .userAction(ruleApproval.getCreatedBy())
                        .approvalId(ruleApproval.getId())
                        .approvalType(ruleApproval.getApprovalType())
                        .approvalComment(ruleApproval.getApprovalComment())
                        .build());
        historyOutputs.add(
                HistoryOutput.builder()
                        .id(++recordNo)
                        .approvalComment(ruleApproval.getApprovalComment())
                        .approvalStatus(ruleApproval.getApprovalStatus())
                        .actionAt(ruleApproval.getUpdatedAt())
                        .userAction(ruleApproval.getUpdatedBy())
                        .approvalId(ruleApproval.getId())
                        .approvalType(ruleApproval.getApprovalType())
                        .approvalComment(ruleApproval.getApprovalComment())
                        .build());
      }
    }
    historyOutputs.sort(Comparator.comparing(HistoryOutput::getActionAt));
    return historyOutputs;
  }
  @Transactional(isolation = Isolation.READ_COMMITTED)
  @Override
  public void approveBudget(ApprovalInput input) {
    // tìm kiếm bản ghi chờ duyệt
    var budgetApproval =
            ruleApprovalRepository
                    .findByDeletedFalseAndIdAndApprovalStatus(input.getId(), ApprovalStatus.WAITING)
                    .orElseThrow(() -> new BaseException(ErrorCode.APPROVING_RECORD_NOT_EXISTED));
    if (input.getIsAccepted()) {
      // trường hợp phê duyệt tạo
      if (ApprovalType.CREATE.equals(budgetApproval.getApprovalType())) {
        Status status =
                budgetApproval.getEndDate() != null && budgetApproval.getEndDate().isBefore(LocalDate.now())
                        ? Status.INACTIVE
                        : Status.ACTIVE;
        // lưu thông tin quy tắc
        var budget = super.modelMapper.convertToRule(budgetApproval, status, LocalDateTime.now());
        budget = ruleRepository.save(budget);
        budgetApproval.setBudgetId(budget.getBudgetId());
        budgetApproval.setStatus(Status.ACTIVE);
      }

      // trường hợp phê duyệt cập nhật
      if (ApprovalType.UPDATE.equals(budgetApproval.getApprovalType())) {
        // lấy thông tin quy tắc hiện tại
        var currentRule =
                ruleRepository
                        .findByDeletedFalseAndId(budgetApproval.getBudgetId())
                        .orElseThrow(() -> new BaseException(ErrorCode.RULE_NOT_EXISTED));
        currentRule = super.modelMapper.convertToRule(currentRule, budgetApproval);
        ruleRepository.save(currentRule);
      }
    }
    // cập nhật trạng thái bản ghi chờ duyệt
    budgetApproval.setApprovalStatus(
            input.getIsAccepted() ? ApprovalStatus.ACCEPTED : ApprovalStatus.REJECTED);
    budgetApproval.setApprovalComment(input.getComment());
    ruleApprovalRepository.save(budgetApproval);
  }
}
