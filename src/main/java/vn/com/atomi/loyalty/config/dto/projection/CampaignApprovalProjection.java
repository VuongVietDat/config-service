package vn.com.atomi.loyalty.config.dto.projection;

import java.time.LocalDate;
import java.time.LocalDateTime;
import vn.com.atomi.loyalty.config.enums.ApprovalStatus;
import vn.com.atomi.loyalty.config.enums.ApprovalType;
import vn.com.atomi.loyalty.config.enums.Status;

public interface CampaignApprovalProjection {
  Long getId();

  String getCode();

  String getName();

  String getDescription();

  String getCustomerGroupId();

  String getCustomerGroupName();

  Status getStatus();

  LocalDate getStartDate();

  LocalDate getEndDate();

  String getCreator();

  LocalDateTime getCreationDate();

  ApprovalStatus getApprovalStatus();

  ApprovalType getApprovalType();

  String getApprover();

  Long getBudgetId();

  Long getBudgetAmount();

  Long getTotalBudget();


}
