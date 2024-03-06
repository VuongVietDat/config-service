package vn.com.atomi.loyalty.config.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import vn.com.atomi.loyalty.base.data.BaseService;
import vn.com.atomi.loyalty.base.data.ResponsePage;
import vn.com.atomi.loyalty.config.dto.input.ApprovalInput;
import vn.com.atomi.loyalty.config.dto.input.CampaignInput;
import vn.com.atomi.loyalty.config.dto.output.CampaignOutput;
import vn.com.atomi.loyalty.config.enums.Status;
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

  private final CampaignRepository campaignRepository;

  @Override
  public void createCampaign(CampaignInput campaignInput) {}

  @Override
  public ResponsePage<CampaignOutput> getCampaignApprovals(Pageable pageable) {
    return null;
  }

  @Override
  public CampaignOutput getCampaignApproval(Long id) {
    return null;
  }

  @Override
  public ResponsePage<CampaignOutput> getCampaigns(
      Status status, String startDate, String endDate, Pageable pageable) {
    var campaignPage =
        campaignRepository.findByCondition(
            status,
            Utils.convertToLocalDateTime(startDate),
            Utils.convertToLocalDateTime(endDate),
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
  public void updateCampaignApproval(Long id, CampaignInput campaignInput) {}

  @Override
  public void updateCampaign(Long id, CampaignInput campaignInput) {}

  @Override
  public void recallCampaignApproval(Long id) {}
}
