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
  @Column(name = "ID")
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CF_RULE_ID_SEQ")
  @SequenceGenerator(name = "CF_RULE_ID_SEQ", sequenceName = "CF_RULE_ID_SEQ", allocationSize = 1)
  private Long id;

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

  @Column(name = "CONDITION_TYPE")
  @Enumerated(EnumType.STRING)
  private ConditionType conditionType;

  @Column(name = "START_DATE")
  private LocalDate startDate;

  @Column(name = "END_DATE")
  private LocalDate endDate;

  @Column(name = "STATUS")
  @Enumerated(EnumType.STRING)
  private Status status;

  @Column(name = "EXPIRE_POLICY_TYPE")
  @Enumerated(EnumType.STRING)
  private ExpirePolicyType expirePolicyType;

  @Column(name = "EXPIRE_POLICY_VALUE")
  private String expirePolicyValue;

  @Column(name = "CREATOR")
  private String creator;

  @Column(name = "CREATION_DATE")
  private LocalDateTime creationDate;

  @Column(name = "CREATION_APPROVAL_DATE")
  private LocalDateTime creationApprovalDate;

  @Column(name = "IS_INACTIVE_BY_SYSTEM")
  private boolean inactiveBySystem;
}
