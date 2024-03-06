package vn.com.atomi.loyalty.config.dto.projection;

import java.time.LocalDateTime;
import lombok.*;
import vn.com.atomi.loyalty.config.enums.*;

/**
 * @author haidv
 * @version 1.0
 */
public interface RuleProjection {

  Long getId();

  String getType();

  String getCode();

  PointType getPointType();

  Long getCampaignId();

  String getCampaignName();

  LocalDateTime getStartDate();

  LocalDateTime getEndDate();

  Status getStatus();

  String getCreator();

  LocalDateTime getCreationDate();

  LocalDateTime getCreationApprovalDate();
}
