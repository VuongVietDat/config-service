package vn.com.atomi.loyalty.config.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.*;
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
@Table(name = "CF_CUSTOMER_GROUP")
public class CustomerGroup extends BaseEntity {

  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cf_customer_group_id_seq")
  @SequenceGenerator(
      name = "cf_customer_group_id_seq",
      sequenceName = "cf_customer_group_id_seq",
      allocationSize = 1)
  private Long id;

  @Column(name = "code")
  private String code;

  @Column(name = "name")
  private String name;

  @Column(name = "status")
  @Enumerated(EnumType.STRING)
  private Status status;

  @Column(name = "builder")
  private String builder;

  @Column(name = "creator")
  private String creator;

  @Column(name = "creation_date")
  private LocalDateTime creationDate;

  @Column(name = "creation_approval_date")
  private LocalDateTime creationApprovalDate;
}