package vn.com.atomi.loyalty.config.service.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.builder.DiffBuilder;
import org.apache.commons.lang3.builder.DiffResult;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import vn.com.atomi.loyalty.base.data.BaseService;
import vn.com.atomi.loyalty.base.data.ResponsePage;
import vn.com.atomi.loyalty.base.exception.BaseException;
import vn.com.atomi.loyalty.config.dto.input.ApprovalInput;
import vn.com.atomi.loyalty.config.dto.input.CreateRuleInput;
import vn.com.atomi.loyalty.config.dto.input.UpdateRuleInput;
import vn.com.atomi.loyalty.config.dto.output.*;
import vn.com.atomi.loyalty.config.entity.RuleApproval;
import vn.com.atomi.loyalty.config.enums.*;
import vn.com.atomi.loyalty.config.repository.*;
import vn.com.atomi.loyalty.config.service.MasterDataService;
import vn.com.atomi.loyalty.config.service.RuleService;
import vn.com.atomi.loyalty.config.utils.Constants;
import vn.com.atomi.loyalty.config.utils.Utils;

/**
 * @author haidv
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
public class RuleServiceImpl extends BaseService implements RuleService {

  private final RuleRepository ruleRepository;

  private final RuleAllocationRepository ruleAllocationRepository;

  private final RuleConditionRepository ruleConditionRepository;

  private final RuleBonusRepository ruleBonusRepository;

  private final RuleApprovalRepository ruleApprovalRepository;

  private final RuleBonusApprovalRepository ruleBonusApprovalRepository;

  private final RuleAllocationApprovalRepository ruleAllocationApprovalRepository;

  private final RuleConditionApprovalRepository ruleConditionApprovalRepository;

  private final CampaignRepository campaignRepository;

  private final MasterDataService masterDataService;

  @Transactional(isolation = Isolation.READ_COMMITTED)
  @Override
  public void createRule(CreateRuleInput createRuleInput) {
    LocalDate ruleStartDate = Utils.convertToLocalDate(createRuleInput.getStartDate());
    LocalDate ruleEndDate = Utils.convertToLocalDate(createRuleInput.getEndDate());
    // kiểm tra chiến dịch
    var campaign =
        campaignRepository
            .findByDeletedFalseAndIdAndStatus(createRuleInput.getCampaignId(), Status.ACTIVE)
            .orElseThrow(() -> new BaseException(ErrorCode.CAMPAIGN_NOT_EXISTED));
    // kiểm tra thời gian hiệu lực của chiến dịch
    if (campaign.getEndDate() != null && campaign.getEndDate().isBefore(LocalDate.now())) {
      throw new BaseException(ErrorCode.CAMPAIGN_INACTIVE);
    }
    // so sánh ngày bắt đầu quy tắc và ngày bắt đầu chiến dịch
    if (campaign.getStartDate().isAfter(ruleStartDate)) {
      throw new BaseException(ErrorCode.RULE_STARTDATE_GREAT_THAN_CAMPAIGN_STARTDATE);
    }
    // so sánh ngày kết thúc quy tắc và ngày kết thúc chiến dịch
    if (campaign.getEndDate() != null
        && (ruleEndDate == null || ruleEndDate.isAfter(campaign.getEndDate()))) {
      throw new BaseException(ErrorCode.RULE_ENDDATE_LESS_THAN_CAMPAIGN_ENDDATE);
    }
    // lấy master data để map loại quy tắc
    var dictionaryOutputs = masterDataService.getDictionary(Status.ACTIVE);
    // kiểm tra tồn tại loại quy tắc sinh điểm
    if (masterDataService
        .getDictionary(dictionaryOutputs, Constants.DICTIONARY_RULE_TYPE, true)
        .stream()
        .filter(v -> v.getCode().equals(createRuleInput.getType()))
        .findFirst()
        .isEmpty()) {
      throw new BaseException(ErrorCode.RULE_TYPE_NOT_EXISTED);
    }
    // kiểm tra tồn tại điều kiện áp dụng quy tắc
    if (!CollectionUtils.isEmpty(createRuleInput.getRuleConditionInputs())
        && createRuleInput.getRuleConditionInputs().stream()
            .anyMatch(
                conditionInput ->
                    !dictionaryOutputs.stream()
                        .filter(
                            dictionary ->
                                dictionary
                                    .getParentCode()
                                    .equals(Constants.DICTIONARY_RULE_CONDITION))
                        .map(DictionaryOutput::getCode)
                        .toList()
                        .contains(conditionInput.getProperties()))) {
      throw new BaseException(ErrorCode.RULE_CONDITION_NOT_EXISTED);
    }
    // tạo code
    var id = ruleApprovalRepository.getSequence();
    var code = Utils.generateCode(id, RuleApproval.class.getSimpleName());
    // tạo bản ghi chờ duyệt
    var ruleApproval =
        super.modelMapper.convertToRuleApproval(
            createRuleInput,
            ruleStartDate,
            ruleEndDate,
            ApprovalStatus.WAITING,
            ApprovalType.CREATE,
            id,
            code);
    ruleApproval = ruleApprovalRepository.save(ruleApproval);
    // lưu điều kiện áp dụng quy tắc của bản ghi chờ duyệt
    if (ruleApproval.getConditionType() != null) {
      var ruleConditionApprovals =
          super.modelMapper.convertToRuleConditionApprovalsFromInput(
              createRuleInput.getRuleConditionInputs(), ruleApproval.getId());
      ruleConditionApprovalRepository.saveAll(ruleConditionApprovals);
    }
    // lưu quy tắc chung phân bổ điểm của bản ghi chờ duyệt
    var ruleAllocationApprovals =
        super.modelMapper.convertToRuleAllocationApprovalsFromInput(
            createRuleInput.getAllocationInputs(), ruleApproval.getId());
    ruleAllocationApprovalRepository.saveAll(ruleAllocationApprovals);
    // lưu quy tắc tặng thêm điểm của bản ghi chờ duyệt
    var ruleBonusApprovals =
        super.modelMapper.convertToRuleBonusApprovalsFromInput(
            createRuleInput.getRuleBonusInputs(), ruleApproval.getId());
    if (!CollectionUtils.isEmpty(ruleBonusApprovals)) {
      ruleBonusApprovalRepository.saveAll(ruleBonusApprovals);
    }
  }

  @Override
  public ResponsePage<RuleApprovalPreviewOutput> getRuleApprovals(
      String type,
      PointType pointType,
      Status status,
      Long campaignId,
      String startDate,
      String endDate,
      ApprovalStatus approvalStatus,
      ApprovalType approvalType,
      String startApprovedDate,
      String endApprovedDate,
      String name,
      String code,
      Pageable pageable) {
    LocalDateTime stDate = Utils.convertToLocalDateTimeStartDay(startApprovedDate);
    LocalDateTime edDate = Utils.convertToLocalDateTimeEndDay(endApprovedDate);
    // nếu tìm kiếm theo khoảng ngày phê duyệt thì trạng thái phê duyệt phải là đồng ý hoặc từ chối
    if ((stDate != null || edDate != null)
        && (ApprovalStatus.WAITING.equals(approvalStatus)
            || ApprovalStatus.RECALL.equals(approvalStatus))) {
      return new ResponsePage<>(
          pageable.getPageNumber(), pageable.getPageSize(), 0, 0, new ArrayList<>());
    }
    var rulePage =
        ruleApprovalRepository.findByCondition(
            type,
            pointType,
            status,
            campaignId,
            Utils.convertToLocalDate(startDate),
            Utils.convertToLocalDate(endDate),
            approvalStatus,
            approvalType,
            Utils.makeLikeParameter(name),
            Utils.makeLikeParameter(code),
            stDate,
            edDate,
            pageable);
    if (!CollectionUtils.isEmpty(rulePage.getContent())) {
      // lấy master data để map tên loại quy tắc
      var dictionaryOutputs = masterDataService.getDictionary(Constants.DICTIONARY_RULE_TYPE, true);
      return new ResponsePage<>(
          rulePage,
          super.modelMapper.convertToRuleApprovalPreviewOutputs(
              rulePage.getContent(), dictionaryOutputs));
    }
    return new ResponsePage<>(rulePage, new ArrayList<>());
  }

  @Override
  public RuleApprovalOutput getRuleApproval(Long id) {
    var ruleApproval =
        ruleApprovalRepository
            .findByDeletedFalseAndId(id)
            .orElseThrow(() -> new BaseException(ErrorCode.RULE_NOT_EXISTED));
    var ruleApprovalOutput = super.modelMapper.convertToRuleApprovalOutput(ruleApproval);
    // lấy điều kiện áp dụng quy tắc của bản ghi chờ duyệt
    if (ruleApprovalOutput.getConditionType() != null) {
      var ruleConditionApprovals =
          ruleConditionApprovalRepository.findByDeletedFalseAndRuleApprovalId(id);
      ruleApprovalOutput.setRuleConditionApprovalOutputs(
          super.modelMapper.convertToRuleConditionApprovalOutputs(ruleConditionApprovals));
    }
    // lấy quy tắc chung phân bổ điểm của bản ghi chờ duyệt
    var ruleAllocationApprovals =
        ruleAllocationApprovalRepository.findByDeletedFalseAndRuleApprovalId(id);
    ruleApprovalOutput.setRuleAllocationApprovalOutputs(
        super.modelMapper.convertToRuleAllocationApprovalOutputs(ruleAllocationApprovals));
    // lấy quy tắc tặng thêm điểm của bản ghi chờ duyệt
    var ruleBonusApprovals = ruleBonusApprovalRepository.findByDeletedFalseAndRuleApprovalId(id);
    ruleApprovalOutput.setRuleBonusApprovalOutputs(
        super.modelMapper.convertToRuleBonusApprovalOutputs(ruleBonusApprovals));
    //  lấy lịch sử phê duyệt
    ruleApprovalOutput.setHistoryOutputs(this.buildHistoryOutputs(List.of(ruleApproval)));
    return ruleApprovalOutput;
  }

  @Override
  public void recallRuleApproval(Long id) {
    // chỉ được thu hồi những bản ghi ở trạng thái chờ duyệt
    ruleApprovalRepository
        .findByDeletedFalseAndIdAndApprovalStatus(id, ApprovalStatus.WAITING)
        .ifPresentOrElse(
            ruleApproval -> {
              ruleApproval.setApprovalStatus(ApprovalStatus.RECALL);
              ruleApprovalRepository.save(ruleApproval);
            },
            () -> {
              throw new BaseException(ErrorCode.APPROVING_RECORD_NOT_EXISTED);
            });
  }

  @Override
  public ResponsePage<RulePreviewOutput> getRules(
      String type,
      PointType pointType,
      Status status,
      Long campaignId,
      String startDate,
      String endDate,
      String name,
      String code,
      Pageable pageable) {
    var rulePage =
        ruleRepository.findByCondition(
            type,
            pointType,
            status,
            campaignId,
            Utils.convertToLocalDate(startDate),
            Utils.convertToLocalDate(endDate),
            Utils.makeLikeParameter(name),
            Utils.makeLikeParameter(code),
            pageable);
    if (!CollectionUtils.isEmpty(rulePage.getContent())) {
      // lấy master data để map tên loại quy tắc
      var dictionaryOutputs = masterDataService.getDictionary(Constants.DICTIONARY_RULE_TYPE, true);
      return new ResponsePage<>(
          rulePage,
          super.modelMapper.convertToRulePreviewOutputs(rulePage.getContent(), dictionaryOutputs));
    }
    return new ResponsePage<>(rulePage, new ArrayList<>());
  }

  @Override
  public RuleOutput getRule(Long id) {
    var rule =
        ruleRepository
            .findByDeletedFalseAndId(id)
            .orElseThrow(() -> new BaseException(ErrorCode.RULE_NOT_EXISTED));
    var ruleOutput = super.modelMapper.convertToRuleOutput(rule);
    // lấy điều kiện áp dụng quy tắc
    if (ruleOutput.getConditionType() != null) {
      var ruleConditions = ruleConditionRepository.findByDeletedFalseAndRuleId(id);
      ruleOutput.setRuleConditionOutputs(
          super.modelMapper.convertToRuleConditionOutputs(ruleConditions));
    }
    // lấy quy tắc chung phân bổ điểm
    var ruleAllocations = ruleAllocationRepository.findByDeletedFalseAndRuleId(id);
    ruleOutput.setRuleAllocationOutputs(
        super.modelMapper.convertToRuleAllocationOutputs(ruleAllocations));
    // lấy quy tắc tặng thêm điểm
    var ruleBonuses = ruleBonusRepository.findByDeletedFalseAndRuleId(id);
    ruleOutput.setRuleBonusOutputs(super.modelMapper.convertToRuleBonusOutputs(ruleBonuses));
    //  lấy lịch sử phê duyệt
    var ruleApprovals = ruleApprovalRepository.findByDeletedFalseAndRuleId(id);
    ruleOutput.setHistoryOutputs(this.buildHistoryOutputs(ruleApprovals));
    return ruleOutput;
  }

  @Transactional(isolation = Isolation.READ_COMMITTED)
  @Override
  public void updateRule(Long id, UpdateRuleInput updateRuleInput) {
    // lấy quy tắc hiện tại
    var rule =
        ruleRepository
            .findByDeletedFalseAndId(id)
            .orElseThrow(() -> new BaseException(ErrorCode.RULE_NOT_EXISTED));
    // map data mới vào quy tắc hiện tại
    var newRule = super.modelMapper.convertToRule(rule, updateRuleInput);
    // tạo bản ghi chờ duyệt
    var approvalId = ruleApprovalRepository.getSequence();
    var ruleApproval =
        super.modelMapper.convertToRuleApproval(
            newRule, approvalId, ApprovalStatus.WAITING, ApprovalType.UPDATE);
    ruleApproval = ruleApprovalRepository.save(ruleApproval);
    // lưu điều kiện áp dụng quy tắc của bản ghi chờ duyệt
    if (ruleApproval.getConditionType() != null) {
      var ruleConditions = ruleConditionRepository.findByDeletedFalseAndRuleId(id);
      var ruleConditionApprovals =
          super.modelMapper.convertToRuleConditionApprovals(ruleConditions, ruleApproval.getId());
      ruleConditionApprovalRepository.saveAll(ruleConditionApprovals);
    }
    // lưu quy tắc chung phân bổ điểm của bản ghi chờ duyệt
    var ruleAllocations = ruleAllocationRepository.findByDeletedFalseAndRuleId(id);
    var ruleAllocationApprovals =
        super.modelMapper.convertToRuleAllocationApprovals(ruleAllocations, ruleApproval.getId());
    ruleAllocationApprovalRepository.saveAll(ruleAllocationApprovals);
    // lưu quy tắc tặng thêm điểm của bản ghi chờ duyệt
    var ruleBonuses = ruleBonusRepository.findByDeletedFalseAndRuleId(id);
    if (!CollectionUtils.isEmpty(ruleBonuses)) {
      var ruleBonusApprovals =
          super.modelMapper.convertToRuleBonusApprovals(ruleBonuses, ruleApproval.getId());
      ruleBonusApprovalRepository.saveAll(ruleBonusApprovals);
    }
  }

  @Transactional(isolation = Isolation.READ_COMMITTED)
  @Override
  public void approveRule(ApprovalInput input) {
    // tìm kiếm bản ghi chờ duyệt
    var ruleApproval =
        ruleApprovalRepository
            .findByDeletedFalseAndIdAndApprovalStatus(input.getId(), ApprovalStatus.WAITING)
            .orElseThrow(() -> new BaseException(ErrorCode.APPROVING_RECORD_NOT_EXISTED));
    if (input.getIsAccepted()) {
      // trường hợp phê duyệt tạo
      if (ApprovalType.CREATE.equals(ruleApproval.getApprovalType())) {
        // lưu thông tin quy tắc
        var rule = super.modelMapper.convertToRule(ruleApproval, LocalDateTime.now());
        rule = ruleRepository.save(rule);
        ruleApproval.setRuleId(rule.getId());
        // lưu quy tắc chung phân bổ điểm
        var ruleAllocationApprovals =
            ruleAllocationApprovalRepository.findByDeletedFalseAndRuleApprovalId(
                ruleApproval.getId());
        var ruleAllocations =
            super.modelMapper.convertToRuleAllocations(ruleAllocationApprovals, rule.getId());
        ruleAllocationRepository.saveAll(ruleAllocations);
        // lưu điều kiện áp dụng quy tắc
        if (ruleApproval.getConditionType() != null) {
          var ruleConditionApprovals =
              ruleConditionApprovalRepository.findByDeletedFalseAndRuleApprovalId(
                  ruleApproval.getId());
          var ruleConditions =
              super.modelMapper.convertToRuleConditions(ruleConditionApprovals, rule.getId());
          ruleConditionRepository.saveAll(ruleConditions);
        }
        // lưu quy tắc tặng thêm điểm
        var ruleBonusApprovals =
            ruleBonusApprovalRepository.findByDeletedFalseAndRuleApprovalId(ruleApproval.getId());
        if (!CollectionUtils.isEmpty(ruleBonusApprovals)) {
          var ruleBonuses =
              super.modelMapper.convertToRuleBonuses(ruleBonusApprovals, rule.getId());
          ruleBonusRepository.saveAll(ruleBonuses);
        }
      }

      // trường hợp phê duyệt cập nhật
      if (ApprovalType.UPDATE.equals(ruleApproval.getApprovalType())) {
        // lấy thông tin quy tắc hiện tại
        var currentRule =
            ruleRepository
                .findByDeletedFalseAndId(ruleApproval.getRuleId())
                .orElseThrow(() -> new BaseException(ErrorCode.RULE_NOT_EXISTED));
        currentRule = super.modelMapper.convertToRule(currentRule, ruleApproval);
        ruleRepository.save(currentRule);
      }
    }
    // cập nhật trạng thái bản ghi chờ duyệt
    ruleApproval.setApprovalStatus(
        input.getIsAccepted() ? ApprovalStatus.ACCEPTED : ApprovalStatus.REJECTED);
    ruleApproval.setApprovalComment(input.getComment());
    ruleApprovalRepository.save(ruleApproval);
  }

  @Override
  public List<ComparisonOutput> getRuleApprovalComparison(Long id) {
    // tìm kiếm bản ghi duyệt cập nhật
    var newRuleApproval =
        ruleApprovalRepository
            .findByDeletedFalseAndId(id)
            .orElseThrow(() -> new BaseException(ErrorCode.RULE_NOT_EXISTED));
    if (!ApprovalType.UPDATE.equals(newRuleApproval.getApprovalType())) {
      throw new BaseException(ErrorCode.APPROVE_TYPE_NOT_MATCH_UPDATE);
    }
    // tìm kiếm bản ghi đã phê duyệt gần nhất
    var oldRuleApproval =
        ruleApprovalRepository
            .findLatestAcceptedRecord(newRuleApproval.getRuleId(), id)
            .orElseThrow(() -> new BaseException(ErrorCode.RULE_NOT_EXISTED));
    // thực hiện so sánh
    DiffResult<RuleApproval> diffResult =
        new DiffBuilder<>(oldRuleApproval, newRuleApproval, ToStringStyle.DEFAULT_STYLE)
            .append(
                "startDate",
                Utils.formatLocalDateToString(oldRuleApproval.getStartDate()),
                Utils.formatLocalDateToString(newRuleApproval.getStartDate()))
            .append(
                "endDate",
                Utils.formatLocalDateToString(oldRuleApproval.getEndDate()),
                Utils.formatLocalDateToString(newRuleApproval.getEndDate()))
            .append("status", oldRuleApproval.getStatus(), newRuleApproval.getStatus())
            .append("name", oldRuleApproval.getName(), newRuleApproval.getName())
            .append(
                "expirePolicyType",
                oldRuleApproval.getExpirePolicyType(),
                newRuleApproval.getExpirePolicyType())
            .append(
                "expirePolicyValue",
                oldRuleApproval.getExpirePolicyValue(),
                newRuleApproval.getExpirePolicyValue())
            .build();
    return diffResult.getDiffs().stream()
        .map(
            diff ->
                ComparisonOutput.builder()
                    .fileName(diff.getFieldName())
                    .oldValue(diff.getLeft() == null ? null : diff.getLeft().toString())
                    .newValue(diff.getRight() == null ? null : diff.getRight().toString())
                    .build())
        .collect(Collectors.toList());
  }

  @Override
  public WarringOverlapActiveTimeOutput checkOverlapActiveTime(
      String type, Long campaignId, String startDate, String endDate) {
    List<String> codes =
        ruleRepository.findCodeByOverlapActiveTime(
            type,
            campaignId,
            Utils.convertToLocalDate(startDate),
            Utils.convertToLocalDate(endDate));
    // nếu nhiều hơn 3 quy tắc thì chỉ trả về mã của 3 quy tắc đầu tiên
    var varCode =
        CollectionUtils.isEmpty(codes)
            ? null
            : (codes.size() <= 3
                ? String.join(",", codes)
                : String.format("%s...", String.join(",", codes.subList(0, 3))));
    return WarringOverlapActiveTimeOutput.builder()
        .existed(!CollectionUtils.isEmpty(codes))
        .message(String.format(ErrorCode.OVERLAP_ACTIVE_TIME.getMessage(), varCode))
        .build();
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
                .build());
      }
    }
    return historyOutputs;
  }
}
