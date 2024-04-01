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
@Table(name = "CF_LV24_PRODUCT_DATA_MAP")
public class Lv24ProductDataMap extends BaseEntity {

  @Id
  @Column(name = "ID")
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CF_LV24_PRO_DATA_MAP_ID_SEQ")
  @SequenceGenerator(
      name = "CF_LV24_PRO_DATA_MAP_ID_SEQ",
      sequenceName = "CF_LV24_PRO_DATA_MAP_ID_SEQ",
      allocationSize = 1)
  private Long id;

  @Column(name = "PRODUCT_ID")
  private Long productId;

  @Column(name = "PRODUCT_CODE")
  private String productCode;

  @Column(name = "PRODUCT_NAME")
  private String productName;

  @Column(name = "TRANSACTION_GROUP")
  private String transactionGroup;

  @Column(name = "STATUS")
  @Enumerated(EnumType.STRING)
  private Status status;
}
