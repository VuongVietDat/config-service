package vn.com.atomi.loyalty.config.service;

import java.util.List;
import org.springframework.data.domain.Pageable;
import vn.com.atomi.loyalty.base.data.ResponsePage;
import vn.com.atomi.loyalty.config.dto.input.ApprovalInput;
import vn.com.atomi.loyalty.config.dto.input.CreateRuleInput;
import vn.com.atomi.loyalty.config.dto.input.UpdateRuleInput;
import vn.com.atomi.loyalty.config.dto.input.WarringOverlapActiveTimeInput;
import vn.com.atomi.loyalty.config.dto.output.*;
import vn.com.atomi.loyalty.config.enums.ApprovalStatus;
import vn.com.atomi.loyalty.config.enums.ApprovalType;
import vn.com.atomi.loyalty.config.enums.PointType;
import vn.com.atomi.loyalty.config.enums.Status;

/**
 * @author haidv
 * @version 1.0
 */
public interface RuleService {

  void createRule(CreateRuleInput createRuleInput);

  ResponsePage<RuleApprovalPreviewOutput> getRuleApprovals(
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
      Pageable pageable);

  RuleApprovalOutput getRuleApproval(Long id);

  void recallRuleApproval(Long id);

  ResponsePage<RulePreviewOutput> getRules(
      String type,
      PointType pointType,
      Status status,
      Long campaignId,
      String startDate,
      String endDate,
      String name,
      String code,
      Pageable pageable);

  RuleOutput getRule(Long id);

  void updateRule(Long id, UpdateRuleInput updateRuleInput);

  void approveRule(ApprovalInput input);

  List<ComparisonOutput> getRuleApprovalComparison(Long id);

  WarringOverlapActiveTimeOutput checkOverlapActiveTime(
      WarringOverlapActiveTimeInput warringOverlapActiveTimeInput);

  void automaticallyExpiresRule();

  List<RuleOutput> getAllActiveRule(String type);
}
