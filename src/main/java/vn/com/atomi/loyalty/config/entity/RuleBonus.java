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
  @Column(name = "ID")
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CF_RULE_BONUS_ID_SEQ")
  @SequenceGenerator(
      name = "CF_RULE_BONUS_ID_SEQ",
      sequenceName = "CF_RULE_BONUS_ID_SEQ",
      allocationSize = 1)
  private Long id;

  @Column(name = "RULE_ID")
  private Long ruleId;

  @Column(name = "TYPE")
  @Enumerated(EnumType.STRING)
  private BonusType type;

  @Column(name = "VALUE")
  private double value;

  @Column(name = "PLUS_TYPE")
  @Enumerated(EnumType.STRING)
  private PlusType plusType;

  @Column(name = "CONDITION")
  private String condition;

  @Column(name = "CHILD_CONDITION")
  private String childCondition;
}
