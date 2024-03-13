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
@Table(name = "CF_RULE_ALLOCATION")
public class RuleAllocation extends BaseEntity {

  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cf_rule_allocation_id_seq")
  @SequenceGenerator(
      name = "cf_rule_allocation_id_seq",
      sequenceName = "cf_rule_allocation_id_seq",
      allocationSize = 1)
  private Long id;

  @Column(name = "rule_id")
  private Long ruleId;

  @Column(name = "fix_point_amount")
  private Long fixPointAmount;

  @Column(name = "exchange_value")
  private Long exchangeValue;

  @Column(name = "exchange_point")
  private Long exchangePoint;

  @Column(name = "min_transaction")
  private Long minTransaction;

  @Column(name = "is_net_value")
  private Boolean isNetValue;

  @Column(name = "limit_point_per_transaction")
  private Long limitPointPerTransaction;

  @Column(name = "limit_point_per_user")
  private Long limitPointPerUser;

  @Column(name = "frequency_limit_point_per_user")
  @Enumerated(EnumType.STRING)
  private Frequency frequencyLimitPointPerUser;

  @Column(name = "limit_event_per_user")
  private Long limitEventPerUser;

  @Column(name = "frequency_limit_event_per_user")
  @Enumerated(EnumType.STRING)
  private Frequency frequencyLimitEventPerUser;

  @Column(name = "time_wait")
  private Long timeWait;

  @Column(name = "frequency_time_wait")
  @Enumerated(EnumType.STRING)
  private Frequency frequencyTimeWait;
}
