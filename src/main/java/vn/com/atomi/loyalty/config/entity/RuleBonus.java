package vn.com.atomi.loyalty.config.entity;

import jakarta.persistence.*;
import lombok.*;
import vn.com.atomi.loyalty.base.data.BaseEntity;
import vn.com.atomi.loyalty.config.enums.BonusType;
import vn.com.atomi.loyalty.config.enums.PlusType;

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
@Table(name = "CF_RULE_BONUS")
public class RuleBonus extends BaseEntity {

  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cf_rule_bonus_id_seq")
  @SequenceGenerator(
      name = "cf_rule_bonus_id_seq",
      sequenceName = "cf_rule_bonus_id_seq",
      allocationSize = 1)
  private Long id;

  @Column(name = "rule_id")
  private Long ruleId;

  @Column(name = "type")
  @Enumerated(EnumType.STRING)
  private BonusType type;

  @Column(name = "value")
  private double value;

  @Column(name = "plus_type")
  @Enumerated(EnumType.STRING)
  private PlusType plusType;

  @Column(name = "condition")
  private String condition;

  @Column(name = "child_condition")
  private String childCondition;
}
