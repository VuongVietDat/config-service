package vn.com.atomi.loyalty.config.dto.projection;

import java.time.LocalDate;
import java.time.LocalDateTime;
import vn.com.atomi.loyalty.config.enums.*;

/**
 * @author haidv
 * @version 1.0
 */
public interface RuleApprovalProjection {

  Long getId();

  String getType();

  String getCode();

  String getName();

  PointType getPointType();

  Long getCampaignId();

  String getCampaignName();

  LocalDate getStartDate();

  LocalDate getEndDate();

  Status getStatus();

  ApprovalStatus getApprovalStatus();

  String getCreatedBy();

  LocalDateTime getCreatedAt();

  LocalDateTime getUpdatedAt();

  ApprovalType getApprovalType();

  String getUpdatedBy();
}
