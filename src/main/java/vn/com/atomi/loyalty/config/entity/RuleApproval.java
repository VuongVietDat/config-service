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
@Table(name = "cf_rule_approval")
public class RuleApproval extends BaseEntity {

  @Id
  @Column(name = "id")
  private Long id;

  @Column(name = "rule_id")
  private Long ruleId;

  @Column(name = "type")
  private String type;

  @Column(name = "code")
  private String code;

  @Column(name = "name")
  private String name;

  @Column(name = "point_type")
  @Enumerated(EnumType.STRING)
  private PointType pointType;

  @Column(name = "campaign_id")
  private Long campaignId;

  @Column(name = "condition_type")
  @Enumerated(EnumType.STRING)
  private ConditionType conditionType;

  @Column(name = "start_date")
  private LocalDate startDate;

  @Column(name = "end_date")
  private LocalDate endDate;

  @Column(name = "status")
  @Enumerated(EnumType.STRING)
  private Status status;

  @Column(name = "expire_policy_type")
  @Enumerated(EnumType.STRING)
  private ExpirePolicyType expirePolicyType;

  @Column(name = "expire_policy_value")
  private String expirePolicyValue;

  @Column(name = "approval_status")
  @Enumerated(EnumType.STRING)
  private ApprovalStatus approvalStatus;

  @Column(name = "approval_type")
  @Enumerated(EnumType.STRING)
  private ApprovalType approvalType;

  @Column(name = "approval_comment")
  private String approvalComment;
}
