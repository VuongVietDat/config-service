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
@Table(name = "CF_TRANSACTION_GROUP")
public class TransactionGroup extends BaseEntity {

  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cf_transaction_group_id_seq")
  @SequenceGenerator(
      name = "cf_transaction_group_id_seq",
      sequenceName = "cf_transaction_group_id_seq",
      allocationSize = 1)
  private Long id;

  @Column(name = "customer_type")
  private String customerType;

  @Column(name = "group_code")
  private String groupCode;

  @Column(name = "group_name")
  private String groupName;

  @Column(name = "order_no")
  private Long orderNo;

  @Column(name = "status")
  @Enumerated(EnumType.STRING)
  private Status status;
}
