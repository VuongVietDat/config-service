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
@Table(name = "cf_transaction_type")
public class TransactionType extends BaseEntity {

  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cf_transaction_type_id_seq")
  @SequenceGenerator(
      name = "cf_transaction_type_id_seq",
      sequenceName = "cf_transaction_type_id_seq",
      allocationSize = 1)
  private Long id;

  @Column(name = "group_code")
  private String groupCode;

  @Column(name = "type_code")
  private String typeCode;

  @Column(name = "type_name")
  private String typeName;

  @Column(name = "order_no")
  private Long orderNo;

  @Column(name = "status")
  @Enumerated(EnumType.STRING)
  private Status status;
}
