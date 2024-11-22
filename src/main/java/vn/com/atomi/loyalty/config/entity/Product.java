package vn.com.atomi.loyalty.config.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.com.atomi.loyalty.base.data.BaseEntity;
import vn.com.atomi.loyalty.config.enums.Status;

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
@Table(name = "CF_PRODUCT")
public class Product extends BaseEntity {

  @Id
  @Column(name = "ID")
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CF_PRODUCT_ID_SEQ")
  @SequenceGenerator(
      name = "CF_PRODUCT_ID_SEQ",
      sequenceName = "CF_PRODUCT_ID_SEQ",
      allocationSize = 1)
  private Long id;

  @Column(name = "PRODUCT_CODE")
  private String productCode;

  @Column(name = "PRODUCT_NAME")
  private String productName;

  @Column(name = "ORDER_NO")
  private Long orderNo;
  
  @Column(name = "PRODUCT_LINE")
  private String productLine;

  @Column(name = "STATUS")
  @Enumerated(EnumType.STRING)
  private Status status;
}
