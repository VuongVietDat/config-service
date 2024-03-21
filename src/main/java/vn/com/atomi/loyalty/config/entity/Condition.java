package vn.com.atomi.loyalty.config.entity;

import jakarta.persistence.*;
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
@Table(name = "CF_CONDITION")
public class Condition extends BaseEntity {

  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cf_condition_id_seq")
  @SequenceGenerator(
      name = "cf_condition_id_seq",
      sequenceName = "cf_condition_id_seq",
      allocationSize = 1)
  private Long id;

  @Column(name = "group_code")
  private String groupCode;

  @Column(name = "properties")
  private String properties;

  @Column(name = "depends_in")
  private String dependsIn;

  @Column(name = "operators")
  @Enumerated(EnumType.STRING)
  private Operators operators;

  @Column(name = "input_type")
  @Enumerated(EnumType.STRING)
  private ConditionInputType inputType;

  @Column(name = "component_type")
  @Enumerated(EnumType.STRING)
  private ComponentType componentType;

  @Column(name = "min_value")
  private String minValue;

  @Column(name = "max_value")
  private String maxValue;

  @Column(name = "default_value")
  private String defaultValue;

  @Column(name = "source_type")
  @Enumerated(EnumType.STRING)
  private SourceType sourceType;

  @Column(name = "source_value")
  private String sourceValue;

  @Column(name = "status")
  @Enumerated(EnumType.STRING)
  private Status status;
}
