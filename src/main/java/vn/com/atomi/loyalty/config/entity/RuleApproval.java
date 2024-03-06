package vn.com.atomi.loyalty.config.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
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
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cf_rule_arv_id_seq")
  @SequenceGenerator(
      name = "cf_rule_arv_id_seq",
      sequenceName = "cf_rule_arv_id_seq",
      allocationSize = 1)
  private Long id;

  @Column(name = "rule_id")
  private Long ruleId;

  @Column(name = "type")
  private String type;

  @Column(name = "code")
  private String code;

  @Column(name = "point_type")
  @Enumerated(EnumType.STRING)
  private PointType pointType;

  @Column(name = "campaign_id")
  private Long campaignId;

  @Column(name = "condition_type")
  @Enumerated(EnumType.STRING)
  private ConditionType conditionType;

  @Column(name = "start_date")
  private LocalDateTime startDate;

  @Column(name = "end_date")
  private LocalDateTime endDate;

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
