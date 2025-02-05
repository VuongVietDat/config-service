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
import vn.com.atomi.loyalty.base.utils.RequestUtils;
import vn.com.atomi.loyalty.config.dto.input.ApprovalInput;
import vn.com.atomi.loyalty.config.dto.input.CampaignInput;
import vn.com.atomi.loyalty.config.dto.output.CampaignApprovalOutput;
import vn.com.atomi.loyalty.config.dto.output.CampaignOutput;
import vn.com.atomi.loyalty.config.dto.output.ComparisonOutput;
import vn.com.atomi.loyalty.config.entity.CampaignApproval;
import vn.com.atomi.loyalty.config.enums.ApprovalStatus;
import vn.com.atomi.loyalty.config.enums.ApprovalType;
import vn.com.atomi.loyalty.config.enums.ErrorCode;
import vn.com.atomi.loyalty.config.enums.Status;
import vn.com.atomi.loyalty.config.feign.LoyaltyCoreClient;
import vn.com.atomi.loyalty.config.repository.CampaignApprovalRepository;
import vn.com.atomi.loyalty.config.repository.CampaignRepository;
import vn.com.atomi.loyalty.config.service.CampaignService;
import vn.com.atomi.loyalty.config.utils.Utils;

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

  @Override
  public void createCampaign(CampaignInput campaignInput) {
    var startDate = Utils.convertToLocalDate(campaignInput.getStartDate());
    var endDate = Utils.convertToLocalDate(campaignInput.getEndDate());

    // kiểm tra customer group
    if (Boolean.FALSE.equals(
        loyaltyCoreClient
            .checkCustomerGroupExisted(
                RequestUtils.extractRequestId(), campaignInput.getCustomerGroupId())
            .getData())) throw new BaseException(ErrorCode.CUSTOMER_GROUP_NOT_EXISTED);

    // tạo code
    var id = campaignApprovalRepository.getSequence();
    var code = Utils.generateCode(id, CampaignApproval.class.getSimpleName());

    // lưu bản ghi chờ duyệt
    var approval =
        modelMapper.convertToCampaignApproval(
            campaignInput,
            startDate,
            endDate,
            ApprovalStatus.WAITING,
            ApprovalType.CREATE,
            id,
            code);
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
      Pageable pageable) {
    var campaignPage =
        campaignRepository.findByCondition(
            code,
            name,
            status,
            Utils.convertToLocalDate(startDate),
            Utils.convertToLocalDate(endDate),
            pageable);
    return new ResponsePage<>(
        campaignPage, super.modelMapper.convertToCampaignOutput(campaignPage.getContent()));
  }

  @Override
  public CampaignOutput getCampaign(Long id) {
    var campaign =
        campaignRepository
            .findByDeletedFalseAndId(id)
            .orElseThrow(() -> new BaseException(ErrorCode.CAMPAIGN_NOT_EXISTED));
    return super.modelMapper.convertToCampaignOutput(campaign);
  }

  @Override
  public void approveCampaign(ApprovalInput input) {
    // tìm kiếm bản ghi chờ duyệt
    var campaignApproval =
        campaignApprovalRepository
            .findByDeletedFalseAndIdAndApprovalStatus(input.getId(), ApprovalStatus.WAITING)
            .orElseThrow(() -> new BaseException(ErrorCode.APPROVING_RECORD_NOT_EXISTED));
    if (input.getIsAccepted()) {
      // trường hợp phê duyệt tạo
      if (ApprovalType.CREATE.equals(campaignApproval.getApprovalType())) {
        // lưu thông tin
        var campaign = super.modelMapper.convertToCampaign(campaignApproval);
        campaign = campaignRepository.save(campaign);
        campaignApproval.setCampaignId(campaign.getId());
      }

      // trường hợp phê duyệt cập nhật
      if (ApprovalType.UPDATE.equals(campaignApproval.getApprovalType())) {
        // lấy thông tin hiện tại
        var currentRule =
            campaignRepository
                .findByDeletedFalseAndId(campaignApproval.getCampaignId())
                .orElseThrow(() -> new BaseException(ErrorCode.CAMPAIGN_NOT_EXISTED));
        currentRule = super.modelMapper.convertToCampaign(currentRule, campaignApproval);
        campaignRepository.save(currentRule);
      }
    }
    // cập nhật trạng thái bản ghi chờ duyệt
    campaignApproval.setApprovalStatus(
        input.getIsAccepted() ? ApprovalStatus.ACCEPTED : ApprovalStatus.REJECTED);
    campaignApproval.setApprovalComment(input.getComment());
    campaignApprovalRepository.save(campaignApproval);
  }

  @Override
  public void updateCampaign(Long id, CampaignInput campaignInput) {
    // lấy record hiện tại
    var campaign =
        campaignRepository
            .findByDeletedFalseAndId(id)
            .orElseThrow(() -> new BaseException(ErrorCode.CAMPAIGN_NOT_EXISTED));

    // todo: check các field ảnh hưởng tới rules
    // map data mới vào chiến dịch hiện tại
    var newCampaign = super.modelMapper.convertToCampaign(campaign, campaignInput);
    // tạo bản ghi chờ duyệt
    var campaignApproval =
        super.modelMapper.convertToCampaignApproval(
            newCampaign, ApprovalStatus.WAITING, ApprovalType.UPDATE);
    campaignApprovalRepository.save(campaignApproval);
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
    if (!ApprovalType.UPDATE.equals(newCampaignApproval.getApprovalType())) {
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
}
