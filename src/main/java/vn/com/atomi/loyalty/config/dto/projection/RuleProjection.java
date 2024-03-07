package vn.com.atomi.loyalty.config.dto.projection;

import java.time.LocalDate;
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

  String getName();

  PointType getPointType();

  Long getCampaignId();

  String getCampaignName();

  LocalDate getStartDate();

  LocalDate getEndDate();

  Status getStatus();

  String getCreator();

  LocalDateTime getCreationDate();

  LocalDateTime getCreationApprovalDate();
}
