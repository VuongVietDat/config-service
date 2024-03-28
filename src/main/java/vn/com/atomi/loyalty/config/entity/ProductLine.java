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
@Table(name = "CF_PRODUCT_LINE")
public class ProductLine extends BaseEntity {

  @Id
  @Column(name = "ID")
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CF_PRODUCT_LINE_ID_SEQ")
  @SequenceGenerator(
      name = "CF_PRODUCT_LINE_ID_SEQ",
      sequenceName = "CF_PRODUCT_LINE_ID_SEQ",
      allocationSize = 1)
  private Long id;

  @Column(name = "PRODUCT_TYPE")
  private String productType;

  @Column(name = "LINE_CODE")
  private String lineCode;

  @Column(name = "LINE_NAME")
  private String lineName;

  @Column(name = "ORDER_NO")
  private Long orderNo;

  @Column(name = "STATUS")
  @Enumerated(EnumType.STRING)
  private Status status;
}
