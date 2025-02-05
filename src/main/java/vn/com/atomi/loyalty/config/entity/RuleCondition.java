package vn.com.atomi.loyalty.config.entity;

import jakarta.persistence.*;
import lombok.*;
import vn.com.atomi.loyalty.base.data.BaseEntity;
import vn.com.atomi.loyalty.config.enums.ConditionProperties;
import vn.com.atomi.loyalty.config.enums.Operators;

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
@Table(name = "CF_RULE_CONDITION")
public class RuleCondition extends BaseEntity {

  @Id
  @Column(name = "ID")
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CF_RULE_CONDITION_ID_SEQ")
  @SequenceGenerator(
      name = "CF_RULE_CONDITION_ID_SEQ",
      sequenceName = "CF_RULE_CONDITION_ID_SEQ",
      allocationSize = 1)
  private Long id;

  @Column(name = "RULE_ID")
  private Long ruleId;

  @Column(name = "PROPERTIES")
  @Enumerated(EnumType.STRING)
  private ConditionProperties properties;

  @Column(name = "OPERATORS")
  @Enumerated(EnumType.STRING)
  private Operators operators;

  @Column(name = "VALUE")
  private String value;
}
