package vn.com.atomi.loyalty.config.entity;

import jakarta.persistence.*;
import lombok.*;
import vn.com.atomi.loyalty.base.data.BaseEntity;
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
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cf_rule_condition_id_seq")
  @SequenceGenerator(
      name = "cf_rule_condition_id_seq",
      sequenceName = "cf_rule_condition_id_seq",
      allocationSize = 1)
  private Long id;

  @Column(name = "rule_id")
  private Long ruleId;

  @Column(name = "properties")
  private String properties;

  @Column(name = "operators")
  @Enumerated(EnumType.STRING)
  private Operators operators;

  @Column(name = "value")
  private String value;
}