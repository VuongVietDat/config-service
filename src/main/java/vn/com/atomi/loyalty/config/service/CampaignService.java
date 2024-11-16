package vn.com.atomi.loyalty.config.service;

import java.util.List;
import org.springframework.data.domain.Pageable;
import vn.com.atomi.loyalty.base.data.ResponsePage;
import vn.com.atomi.loyalty.config.dto.input.ApprovalInput;
import vn.com.atomi.loyalty.config.dto.input.CampaignInput;
import vn.com.atomi.loyalty.config.dto.input.CampaignUpdateInput;
import vn.com.atomi.loyalty.config.dto.output.CampaignApprovalOutput;
import vn.com.atomi.loyalty.config.dto.output.CampaignOutput;
import vn.com.atomi.loyalty.config.dto.output.ComparisonOutput;
import vn.com.atomi.loyalty.config.enums.ApprovalStatus;
import vn.com.atomi.loyalty.config.enums.ApprovalType;
import vn.com.atomi.loyalty.config.enums.Status;

/**
 * @author haidv
 * @version 1.0
 */
public interface CampaignService {

  void createCampaign(CampaignInput campaignInput);

  ResponsePage<CampaignApprovalOutput> getCampaignApprovals(
      Status status,
      ApprovalStatus approvalStatus,
      ApprovalType approvalType,
      String startDate,
      String endDate,
      String startApprovedDate,
      String endApprovedDate,
      String name,
      String code,
      Long budgetAmount,
      Long totalBudget,
      Long budgetId,
      Pageable pageable);

  CampaignApprovalOutput getCampaignApproval(Long id);

  ResponsePage<CampaignOutput> getCampaigns(
          Status status, String startDate, String endDate, String name, String code,Long budgetId, Long budgetAmount, Long totalBudget, ApprovalStatus approvalStatus, Pageable pageable);

  CampaignOutput getCampaign(Long id);

  void approveCampaign(ApprovalInput input);

  void updateCampaign(CampaignUpdateInput campaignUpdateInput);

  void recallCampaignApproval(Long id);

  List<ComparisonOutput> geCampaignApprovalComparison(Long id);

  Boolean checkCampaignActive(Long groupId);
}
