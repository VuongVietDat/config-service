package vn.com.atomi.loyalty.config.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.*;
import vn.com.atomi.loyalty.base.data.BaseEntity;
import vn.com.atomi.loyalty.config.enums.ConditionType;
import vn.com.atomi.loyalty.config.enums.ExpirePolicyType;
import vn.com.atomi.loyalty.config.enums.PointType;
import vn.com.atomi.loyalty.config.enums.Status;

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
@Table(name = "CF_RULE")
public class Rule extends BaseEntity {

  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cf_rule_id_seq")
  @SequenceGenerator(name = "cf_rule_id_seq", sequenceName = "cf_rule_id_seq", allocationSize = 1)
  private Long id;

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

  @Column(name = "creator")
  private String creator;

  @Column(name = "creation_date")
  private LocalDateTime creationDate;

  @Column(name = "creation_approval_date")
  private LocalDateTime creationApprovalDate;
}
