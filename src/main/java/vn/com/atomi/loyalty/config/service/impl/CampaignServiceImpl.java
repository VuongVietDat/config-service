package vn.com.atomi.loyalty.config.service.impl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.builder.DiffBuilder;
import org.apache.commons.lang3.builder.DiffResult;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import vn.com.atomi.loyalty.base.data.BaseService;
import vn.com.atomi.loyalty.base.data.ResponsePage;
import vn.com.atomi.loyalty.base.exception.BaseException;
import vn.com.atomi.loyalty.config.dto.input.ApprovalInput;
import vn.com.atomi.loyalty.config.dto.input.CampaignInput;
import vn.com.atomi.loyalty.config.dto.input.CampaignUpdateInput;
import vn.com.atomi.loyalty.config.dto.output.CampaignApprovalOutput;
import vn.com.atomi.loyalty.config.dto.output.CampaignOutput;
import vn.com.atomi.loyalty.config.dto.output.ComparisonOutput;
import vn.com.atomi.loyalty.config.entity.CampaignApproval;
import vn.com.atomi.loyalty.config.enums.*;
import vn.com.atomi.loyalty.config.feign.LoyaltyCoreClient;
import vn.com.atomi.loyalty.config.repository.BudgetRepository;
import vn.com.atomi.loyalty.config.repository.CampaignApprovalRepository;
import vn.com.atomi.loyalty.config.repository.CampaignRepository;
import vn.com.atomi.loyalty.config.service.CampaignService;
import vn.com.atomi.loyalty.config.utils.Utils;
import static vn.com.atomi.loyalty.config.enums.ApprovalType.UPDATE;
import static vn.com.atomi.loyalty.config.enums.Status.INACTIVE;
import vn.com.atomi.loyalty.config.entity.Campaign;
/**
 * @author haidv
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
public class CampaignServiceImpl extends BaseService implements CampaignService {

  private final CampaignApprovalRepository campaignApprovalRepository;
  private final CampaignRepository campaignRepository;
  private final LoyaltyCoreClient loyaltyCoreClient;
  private final BudgetRepository budgetRepository;

  @Override
  public void createCampaign(CampaignInput campaignInput) {
    var startDate = Utils.convertToLocalDate(campaignInput.getStartDate());
    var endDate = Utils.convertToLocalDate(campaignInput.getEndDate());

    // kiểm tra customer group
//    if (Boolean.FALSE.equals(
//        loyaltyCoreClient
//            .checkCustomerGroupExisted(
//                RequestUtils.extractRequestId(), campaignInput.getCustomerGroupId())
//            .getData())) throw new BaseException(ErrorCode.CUSTOMER_GROUP_NOT_EXISTED);
    var lastestCampaign = campaignRepository.findTopByOrderByCreatedAtDesc();
//    var code = lastestCampaign.get().getCode();
//    campaignInput.setCode(code);

    String lastCode = lastestCampaign.get().getCode();
    String prefix = lastCode.replaceAll("\\d", ""); // Lấy phần tiền tố (e.g., "CD")
    String numberPart = lastCode.replaceAll("\\D", ""); // Lấy phần số (e.g., "000")

// Chuyển phần số thành Integer để tăng giá trị
    int newNumber = Integer.parseInt(numberPart) + 1;

// Định dạng lại mã mới với phần số có độ dài cố định (ví dụ: 3 chữ số)
    String newCode = prefix + String.format("%03d", newNumber);

    campaignInput.setCode(newCode);
// Gán lại cho campaign
     campaignInput.setCode(newCode);
    // kiểm tra budget
    var budget =
            budgetRepository
                    .findByDeletedFalseAndIdAndStatus(campaignInput.getBudgetId(), BudgetStatus.ACTIVE)
                    .orElseThrow(() -> new BaseException(ErrorCode.BUDGET_NOT_EXISTED));
    var campaign = super.modelMapper.createCampaign(campaignInput, startDate, endDate);
    campaign.setStatus(INACTIVE);
    if (campaign.getBudgetAmount()<=budget.getTotalBudget()){
      campaignRepository.save(campaign);
    }
    else {
      throw new BaseException(ErrorCode.ERROR_BUDGET_AMOUNT);
    }
    // tạo code
    var id = campaignApprovalRepository.getSequence();
    // lưu bản ghi chờ duyệt
    var approval =
        modelMapper.convertToCampaignApproval(
            campaign.getId(),
            campaign.getCode(),
            campaign.getName(),
            campaign.getBudgetAmount(),
            campaign.getDescription(),
            startDate,
            endDate,
            ApprovalStatus.WAITING,
            ApprovalType.CREATE,
            id,
            budget.getTotalBudget());
    approval.setApprovalStatus(campaignInput.getApprovalStatus());
    approval = campaignApprovalRepository.save(approval);
    campaignApprovalRepository.save(approval);
  }

  @Override
  public ResponsePage<CampaignApprovalOutput> getCampaignApprovals(
      Status status,
      ApprovalStatus approvalStatus,
      ApprovalType approvalType,
      String startDate,
      String endDate,
      String startApprovedDate,
      String endApprovedDate,
      String name,
      String code,
      Long totalBudget,
      Long budgetAmount,
      Long budgetId,
      Pageable pageable) {
    var stDate = Utils.convertToLocalDateTimeStartDay(startApprovedDate);
    var edDate = Utils.convertToLocalDateTimeEndDay(endApprovedDate);
    // nếu tìm kiếm theo khoảng ngày phê duyệt thì trạng thái phê duyệt phải là đồng ý hoặc từ chối
    if ((stDate != null || edDate != null)
        && (ApprovalStatus.WAITING.equals(approvalStatus)
            || ApprovalStatus.RECALL.equals(approvalStatus))) {
      return new ResponsePage<>(
          pageable.getPageNumber(), pageable.getPageSize(), 0, 0, new ArrayList<>());
    }

    var campaignPage =
        campaignApprovalRepository.findByCondition(
            status,
            Utils.convertToLocalDate(startDate),
            Utils.convertToLocalDate(endDate),
            approvalStatus,
            approvalType,
            Utils.makeLikeParameter(name),
            Utils.makeLikeParameter(code),
            stDate,
            edDate,
            budgetAmount,
            totalBudget,
            budgetId,
            pageable);

    if (CollectionUtils.isEmpty(campaignPage.getContent()))
      return new ResponsePage<>(campaignPage, new ArrayList<>());

    return new ResponsePage<>(
        campaignPage, modelMapper.convertToCampaignOutputs(campaignPage.getContent()));
  }

  @Override
  public CampaignApprovalOutput getCampaignApproval(Long id) {
    var campaignApproval =
        campaignApprovalRepository
            .findByDeletedFalseAndId(id)
            .orElseThrow(() -> new BaseException(ErrorCode.RULE_NOT_EXISTED));
    return super.modelMapper.convertToCampaignApprovalOutput(campaignApproval);
  }

  @Override
  public ResponsePage<CampaignOutput> getCampaigns(
      Status status,
      String startDate,
      String endDate,
      String name,
      String code,
      Long budgetId,
      Long budgetAmount,
      Long totalAmount,
      ApprovalStatus approvalStatus,
      Pageable pageable) {
    var campaignPage =
        campaignRepository.findByCondition(
            code,
            name,
            status,
            Utils.convertToLocalDate(startDate),
            Utils.convertToLocalDate(endDate),
            budgetId,
            budgetAmount,
            totalAmount,
            approvalStatus,
            pageable);
    var campaignOutputs = campaignPage.getContent().stream().map(campaign -> {
      var output = modelMapper.convertToCampaignOutput(campaign);
      var campaignApproval = campaignApprovalRepository.findByCampaignId(campaign.getId());
      if (campaignApproval.isPresent()) {
        output.setApprovalStatus(campaignApproval.get().getApprovalStatus());
      }
      return output;
    }).collect(Collectors.toList());
    return new ResponsePage<>(campaignPage, campaignOutputs);
//    return new ResponsePage<>(
//        campaignPage, super.modelMapper.convertToCampaignOutput(campaignPage.getContent()));
  }

  @Override
  public CampaignOutput getCampaign(Long id) {
    var campaign =
        campaignRepository
            .findByDeletedFalseAndId(id)
            .orElseThrow(() -> new BaseException(ErrorCode.CAMPAIGN_NOT_EXISTED));
    var out =  super.modelMapper.convertToCampaignOutput(campaign);
    var approvalId = campaignApprovalRepository.findByCampaignId(campaign.getId());
    out.setApprovalCampaignId(approvalId.get().getId());
    out.setApprovalStatus(approvalId.get().getApprovalStatus());
    var budgetId = budgetRepository.findByDeletedFalseAndId(campaign.getBudgetId());
    out.setBudgetName(budgetId.get().getName());
    return out;
  }

  @Override
  public void approveCampaign(ApprovalInput input) {
    // tìm kiếm bản ghi chờ duyệt
    var campaignApproval =
        campaignApprovalRepository
            .findByDeletedFalseAndIdAndApprovalStatus(input.getId(), ApprovalStatus.WAITING)
            .orElseThrow(() -> new BaseException(ErrorCode.APPROVING_RECORD_NOT_EXISTED));
//    if (input.getIsAccepted()) {
//      // trường hợp phê duyệt tạo
//      if (ApprovalType.CREATE.equals(campaignApproval.getApprovalType())) {
//        // lưu thông tin
//        var campaign = super.modelMapper.convertToCampaign(campaignApproval);
//        campaign = campaignRepository.save(campaign);
//        campaignApproval.setCampaignId(campaign.getId());
//      }
//
//      // trường hợp phê duyệt cập nhật
//      if (UPDATE.equals(campaignApproval.getApprovalType())) {
//        // lấy thông tin hiện tại
//        var currentCampaign =
//            campaignRepository
//                .findByDeletedFalseAndId(campaignApproval.getCampaignId())
//                .orElseThrow(() -> new BaseException(ErrorCode.CAMPAIGN_NOT_EXISTED));
//        currentCampaign = super.modelMapper.convertToCampaign(currentCampaign, campaignApproval);
//        campaignRepository.save(currentCampaign);
//      }
//    }
    // cập nhật trạng thái bản ghi chờ duyệt
    campaignApproval.setApprovalStatus(
        input.getIsAccepted() ? ApprovalStatus.ACCEPTED : ApprovalStatus.REJECTED);
    campaignApproval.setApprovalComment(input.getComment());
    campaignApproval.setApprover(campaignApproval.getCreatedBy());
    campaignApproval.setStatus(campaignApproval.getStatus());
    campaignApprovalRepository.save(campaignApproval);
  }

  @Override
//  public void updateCampaign1(Long id, CampaignInput campaignInput) {
//    // lấy record hiện tại
//    var campaign =
//        campaignRepository
//            .findByDeletedFalseAndId(id)
//            .orElseThrow(() -> new BaseException(ErrorCode.CAMPAIGN_NOT_EXISTED));
//
//    // todo: check các field ảnh hưởng tới rules
//    // map data mới vào chiến dịch hiện tại
//    var newCampaign = super.modelMapper.convertToCampaign(campaign, campaignInput);
//    // tạo bản ghi chờ duyệt
//    var campaignApproval =
//        super.modelMapper.convertToCampaignApproval(
//            newCampaign, ApprovalStatus.WAITING, ApprovalType.UPDATE);
//    campaignApprovalRepository.save(campaignApproval);
//  }

  public void updateCampaign(CampaignUpdateInput campaignUpdateInput) {
    var campaign =
            campaignRepository
                    .findByDeletedFalseAndId(campaignUpdateInput.getId())
                    .orElseThrow(() -> new BaseException(ErrorCode.RECORD_NOT_EXISTED));
    var campaignApproval = campaignApprovalRepository.findByCampaignId(campaign.getId());
    //check trang thai phe duyet duoi db cua budget
    if (campaignApproval.get().getApprovalStatus()==ApprovalStatus.RECALL) {
      if (campaignUpdateInput.getName() != null) {
        campaign.setName(campaignUpdateInput.getName());
        campaignApproval.get().setName(campaignUpdateInput.getName());
      }
      campaign.setBudgetAmount(campaignUpdateInput.getBudgetAmount());
      campaignApproval.get().setBudgetAmount(campaignUpdateInput.getBudgetAmount());
      campaign.setDescription(campaignUpdateInput.getDescription());
      campaignApproval.get().setDescription(campaignUpdateInput.getDescription());
      if (campaignUpdateInput.getApprovalStatus()==ApprovalStatus.WAITING){
        campaignApproval.get().setApprovalStatus(ApprovalStatus.WAITING);
      }
      else if (campaignUpdateInput.getApprovalStatus()==ApprovalStatus.RECALL){
        campaignApproval.get().setApprovalStatus(ApprovalStatus.RECALL);
      }
      campaignApproval.get().setApprovalType(UPDATE);
      campaignRepository.save(campaign);
    }
    //trang thai duyet ACCEPTED Va nam trong khoang thoi gian hieu luc: INACTIVE -> ACTIVE
    else if(campaignApproval.get().getApprovalStatus()==ApprovalStatus.ACCEPTED){
      LocalDate currentDate = LocalDate.now();
      if (campaign.getStartDate().isAfter(currentDate) && campaign.getEndDate().isBefore(currentDate)){
        throw new BaseException(ErrorCode.CHANGE_STATUS_FAILED);
      }
      else {
        campaign.setStatus(campaignUpdateInput.getStatus());
      }
      var budget = budgetRepository.findByDeletedFalseAndId(campaign.getBudgetId());
      campaignApproval.get().setApprovalType(UPDATE);
      campaignRepository.save(campaign);
    }
    else {
      throw new BaseException(ErrorCode.INVALID_APPROVAL_STATUS);
    }
  }

  @Override
  public void recallCampaignApproval(Long id) {
    // chỉ được thu hồi những bản ghi ở trạng thái chờ duyệt
    campaignApprovalRepository
        .findByDeletedFalseAndIdAndApprovalStatus(id, ApprovalStatus.WAITING)
        .ifPresentOrElse(
            ruleApproval -> {
              ruleApproval.setApprovalStatus(ApprovalStatus.RECALL);
              campaignApprovalRepository.save(ruleApproval);
            },
            () -> {
              throw new BaseException(ErrorCode.APPROVING_RECORD_NOT_EXISTED);
            });
  }

  @Override
  public List<ComparisonOutput> geCampaignApprovalComparison(Long id) {
    // tìm kiếm bản ghi duyệt cập nhật
    var newCampaignApproval =
        campaignApprovalRepository
            .findByDeletedFalseAndId(id)
            .orElseThrow(() -> new BaseException(ErrorCode.CAMPAIGN_NOT_EXISTED));
    if (!UPDATE.equals(newCampaignApproval.getApprovalType())) {
      throw new BaseException(ErrorCode.APPROVE_TYPE_NOT_MATCH_UPDATE);
    }
    // tìm kiếm bản ghi đã phê duyệt gần nhất
    var oldCampaignApproval =
        campaignApprovalRepository
            .findLatestAcceptedRecord(newCampaignApproval.getCampaignId(), id)
            .orElseThrow(() -> new BaseException(ErrorCode.RULE_NOT_EXISTED));
    // thực hiện so sánh
    DiffResult<CampaignApproval> diffResult =
        new DiffBuilder<>(oldCampaignApproval, newCampaignApproval, ToStringStyle.DEFAULT_STYLE)
            .append(
                "startDate",
                Utils.formatLocalDateToString(oldCampaignApproval.getStartDate()),
                Utils.formatLocalDateToString(newCampaignApproval.getStartDate()))
            .append(
                "endDate",
                Utils.formatLocalDateToString(oldCampaignApproval.getEndDate()),
                Utils.formatLocalDateToString(newCampaignApproval.getEndDate()))
            .append("status", oldCampaignApproval.getStatus(), newCampaignApproval.getStatus())
            .append("name", oldCampaignApproval.getName(), newCampaignApproval.getName())
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
  public Boolean checkCampaignActive(Long groupId) {
    return campaignRepository.existsByActiveCampaign(groupId, LocalDate.now());
  }


  public String generateNewCampaignCode() {
    // Lấy campaign mới nhất
    var latestCampaign = campaignRepository.findTopByOrderByCreatedAtDesc();

    // Mặc định nếu không có campaign nào
    String lastCode = latestCampaign.map(Campaign::getCode).orElse("CAM-000");

    // Phân tách tiền tố và số thứ tự
    String prefix = lastCode.replaceAll("\\d", ""); // Lấy phần tiền tố (ví dụ: "CAM-")
    String numberPart = lastCode.replaceAll("\\D", ""); // Lấy phần số (ví dụ: "000")

    // Chuyển phần số sang Integer và tăng giá trị
    int newNumber = Integer.parseInt(numberPart) + 1;

    // Tạo mã mới với phần số có độ dài cố định (ví dụ: 3 chữ số)
    return prefix + String.format("%03d", newNumber);
  }
}
