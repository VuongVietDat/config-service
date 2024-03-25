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
@Table(name = "CF_TRANSACTION_TYPE")
public class TransactionType extends BaseEntity {

  @Id
  @Column(name = "ID")
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CF_TRANSACTION_TYPE_ID_SEQ")
  @SequenceGenerator(
      name = "CF_TRANSACTION_TYPE_ID_SEQ",
      sequenceName = "CF_TRANSACTION_TYPE_ID_SEQ",
      allocationSize = 1)
  private Long id;

  @Column(name = "GROUP_CODE")
  private String groupCode;

  @Column(name = "TYPE_CODE")
  private String typeCode;

  @Column(name = "TYPE_NAME")
  private String typeName;

  @Column(name = "ORDER_NO")
  private Long orderNo;

  @Column(name = "STATUS")
  @Enumerated(EnumType.STRING)
  private Status status;
}
