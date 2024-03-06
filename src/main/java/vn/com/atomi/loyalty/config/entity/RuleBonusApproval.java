package vn.com.atomi.loyalty.config.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import lombok.*;
import vn.com.atomi.loyalty.base.data.BaseEntity;
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
@Table(name = "CF_RULE_BONUS_APPROVAL")
public class RuleBonusApproval extends BaseEntity {

  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cf_rule_bonus_arv_id_seq")
  @SequenceGenerator(
      name = "cf_rule_bonus_arv_id_seq",
      sequenceName = "cf_rule_bonus_arv_id_seq",
      allocationSize = 1)
  private Long id;

  @Column(name = "rule_approval_id")
  private Long ruleApprovalId;

  @Column(name = "type")
  private String type;

  @Column(name = "value")
  private String value;

  @Column(name = "plus_type")
  @Enumerated(EnumType.STRING)
  private PlusType plusType;

  @Column(name = "from_date")
  private LocalDate fromDate;

  @Column(name = "to_date")
  private LocalDate toDate;

  @Column(name = "condition")
  private String condition;
}
