package vn.com.atomi.loyalty.config.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.com.atomi.loyalty.base.data.BaseService;
import vn.com.atomi.loyalty.base.data.ListRequest;
import vn.com.atomi.loyalty.base.data.ResponsePage;
import vn.com.atomi.loyalty.config.dto.input.ApprovalInput;
import vn.com.atomi.loyalty.config.dto.input.CampaignInput;
import vn.com.atomi.loyalty.config.dto.output.CampaignOutput;
import vn.com.atomi.loyalty.config.service.CampaignService;

/**
 * @author haidv
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
public class CampaignServiceImpl extends BaseService implements CampaignService {

  @Override
  public void createCampaign(CampaignInput campaignInput) {}

  @Override
  public ResponsePage<CampaignOutput> getCampaignApprovals(ListRequest listRequest) {
    return null;
  }

  @Override
  public CampaignOutput getCampaignApproval(Long id) {
    return null;
  }

  @Override
  public ResponsePage<CampaignOutput> getCampaigns(ListRequest listRequest) {
    return null;
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
