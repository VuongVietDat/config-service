package vn.com.atomi.loyalty.config.entity;

import jakarta.persistence.*;
import lombok.*;
import vn.com.atomi.loyalty.base.data.BaseEntity;
import vn.com.atomi.loyalty.config.enums.Frequency;

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
@Table(name = "CF_RULE_ALLOCATION_APPROVAL")
public class RuleAllocationApproval extends BaseEntity {

  @Id
  @Column(name = "ID")
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CF_RULE_ALLOCATION_ARV_ID_SEQ")
  @SequenceGenerator(
      name = "CF_RULE_ALLOCATION_ARV_ID_SEQ",
      sequenceName = "CF_RULE_ALLOCATION_ARV_ID_SEQ",
      allocationSize = 1)
  private Long id;

  @Column(name = "RULE_APPROVAL_ID")
  private Long ruleApprovalId;

  @Column(name = "IS_EXCHANGE_BY_VALUE")
  private Boolean isExchangeByValue;

  @Column(name = "FIX_POINT_AMOUNT")
  private Long fixPointAmount;

  @Column(name = "EXCHANGE_VALUE")
  private Long exchangeValue;

  @Column(name = "EXCHANGE_POINT")
  private Long exchangePoint;

  @Column(name = "MIN_TRANSACTION")
  private Long minTransaction;

  @Column(name = "IS_NET_VALUE")
  private Boolean isNetValue;

  @Column(name = "LIMIT_POINT_PER_TRANSACTION")
  private Long limitPointPerTransaction;

  @Column(name = "LIMIT_POINT_PER_USER")
  private Long limitPointPerUser;

  @Deprecated
  @Column(name = "FREQUENCY_LIMIT_POINT_PER_USER")
  @Enumerated(EnumType.STRING)
  private Frequency frequencyLimitPointPerUser;

  @Column(name = "LIMIT_EVENT_PER_USER")
  private Long limitEventPerUser;

  @Column(name = "FREQUENCY_LIMIT_EVENT_PER_USER")
  @Enumerated(EnumType.STRING)
  private Frequency frequencyLimitEventPerUser;

  @Column(name = "TIME_WAIT")
  private Long timeWait;

  @Column(name = "FREQUENCY_TIME_WAIT")
  @Enumerated(EnumType.STRING)
  private Frequency frequencyTimeWait;
}
