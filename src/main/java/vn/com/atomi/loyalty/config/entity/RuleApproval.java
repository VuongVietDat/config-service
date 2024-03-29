package vn.com.atomi.loyalty.config.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import lombok.*;
import vn.com.atomi.loyalty.base.data.BaseEntity;
import vn.com.atomi.loyalty.config.enums.*;

/**
 * @author haidv
 * @version 1.0
 */
@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "CF_RULE_APPROVAL")
public class RuleApproval extends BaseEntity {

  @Id
  @Column(name = "ID")
  private Long id;

  @Column(name = "RULE_ID")
  private Long ruleId;

  @Column(name = "TYPE")
  private String type;

  @Column(name = "CODE")
  private String code;

  @Column(name = "NAME")
  private String name;

  @Column(name = "POINT_TYPE")
  @Enumerated(EnumType.STRING)
  private PointType pointType;

  @Column(name = "CAMPAIGN_ID")
  private Long campaignId;

  @Deprecated
  @Column(name = "CONDITION_TYPE")
  @Enumerated(EnumType.STRING)
  private ConditionType conditionType;

  @Column(name = "START_DATE")
  private LocalDate startDate;

  @Column(name = "END_DATE")
  private LocalDate endDate;

  @Column(name = "EXPIRE_POLICY_TYPE")
  @Enumerated(EnumType.STRING)
  private ExpirePolicyType expirePolicyType;

  @Column(name = "EXPIRE_POLICY_VALUE")
  private String expirePolicyValue;

  @Column(name = "APPROVAL_STATUS")
  @Enumerated(EnumType.STRING)
  private ApprovalStatus approvalStatus;

  @Column(name = "APPROVAL_TYPE")
  @Enumerated(EnumType.STRING)
  private ApprovalType approvalType;

  @Column(name = "APPROVAL_COMMENT")
  private String approvalComment;

  @Column(name = "STATUS")
  @Enumerated(EnumType.STRING)
  private Status status;
}
