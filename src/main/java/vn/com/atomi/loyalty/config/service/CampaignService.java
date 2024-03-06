package vn.com.atomi.loyalty.config.service;

import org.springframework.data.domain.Pageable;
import vn.com.atomi.loyalty.base.data.ResponsePage;
import vn.com.atomi.loyalty.config.dto.input.ApprovalInput;
import vn.com.atomi.loyalty.config.dto.input.CampaignInput;
import vn.com.atomi.loyalty.config.dto.output.CampaignOutput;
import vn.com.atomi.loyalty.config.enums.Status;

/**
 * @author haidv
 * @version 1.0
 */
public interface CampaignService {

  void createCampaign(CampaignInput campaignInput);

  ResponsePage<CampaignOutput> getCampaignApprovals(Pageable pageable);

  CampaignOutput getCampaignApproval(Long id);

  ResponsePage<CampaignOutput> getCampaigns(
      Status status, String startDate, String endDate, Pageable pageable);

  CampaignOutput getCampaign(Long id);

  void approveCampaign(ApprovalInput input);

  void updateCampaignApproval(Long id, CampaignInput campaignInput);

  void updateCampaign(Long id, CampaignInput campaignInput);

  void recallCampaignApproval(Long id);
}
