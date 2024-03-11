package vn.com.atomi.loyalty.config.service.impl;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import vn.com.atomi.loyalty.base.data.BaseService;
import vn.com.atomi.loyalty.base.data.ResponsePage;
import vn.com.atomi.loyalty.base.exception.BaseException;
import vn.com.atomi.loyalty.config.dto.input.ApprovalInput;
import vn.com.atomi.loyalty.config.dto.input.CampaignInput;
import vn.com.atomi.loyalty.config.dto.output.CampaignOutput;
import vn.com.atomi.loyalty.config.dto.output.ComparisonOutput;
import vn.com.atomi.loyalty.config.entity.CampaignApproval;
import vn.com.atomi.loyalty.config.enums.ApprovalStatus;
import vn.com.atomi.loyalty.config.enums.ApprovalType;
import vn.com.atomi.loyalty.config.enums.ErrorCode;
import vn.com.atomi.loyalty.config.enums.Status;
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

  @Override
  public void createCampaign(CampaignInput campaignInput) {
    val startDate = Utils.convertToLocalDate(campaignInput.getStartDate());
    val endDate = Utils.convertToLocalDate(campaignInput.getEndDate());

    // validate
    if (endDate != null && !endDate.isAfter(startDate))
      throw new BaseException(ErrorCode.ENDDATE_AFTER_STARTDATE);

    // tạo code
    val id = campaignApprovalRepository.getSequence();
    val code = Utils.generateCode(id, CampaignApproval.class.getSimpleName());

    // lưu bản ghi chờ duyệt
    val approval =
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
  public ResponsePage<CampaignOutput> getCampaignApprovals(
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
    return null;
  }

  @Override
  public CampaignOutput getCampaignApproval(Long id) {
    return null;
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
            status,
            Utils.convertToLocalDate(startDate),
            Utils.convertToLocalDate(endDate),
            pageable);
    return new ResponsePage<>(
        campaignPage, super.modelMapper.convertToCampaignOutput(campaignPage.getContent()));
  }

  @Override
  public CampaignOutput getCampaign(Long id) {
    return null;
  }

  @Override
  public void approveCampaign(ApprovalInput input) {}

  @Override
  public void updateCampaign(Long id, CampaignInput campaignInput) {}

  @Override
  public void recallCampaignApproval(Long id) {}

  @Override
  public List<ComparisonOutput> geCampaignApprovalComparison(Long id) {
    return null;
  }
}
