package vn.com.atomi.loyalty.config.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
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
@Table(name = "cf_campaign")
public class Campaign extends BaseEntity {

  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cf_campaign_id_seq")
  @SequenceGenerator(
      name = "cf_campaign_id_seq",
      sequenceName = "cf_campaign_id_seq",
      allocationSize = 1)
  private Long id;

  @Column(name = "code")
  private String code;

  @Column(name = "name")
  private String name;

  @Column(name = "BUDGET_ID")
  private Long budgetId;

  @Column(name = "BUDGET_CODE")
  private String budgetCode;

  @Column(name = "BUDGET_AMOUNT")
  private long budgetAmount;

  @Column(name = "TOTAL_ALLOCATION_POINT")
  private long totalAllocationPoint;

  @Column(name = "status")
  @Enumerated(EnumType.STRING)
  private Status status;

  @Column(name = "start_date")
  private LocalDate startDate;

  @Column(name = "end_date")
  private LocalDate endDate;

  @Column(name = "customer_group_id")
  private Long customerGroupId;

  @Column(name = "creator")
  private String creator;

  @Column(name = "created_by")
  private String createdBy;

  @Column(name = "creation_date")
  private LocalDateTime creationDate;

  @Column(name = "creation_approval_date")
  private LocalDateTime creationApprovalDate;

  @Column(name = "description")
  private String description;
}
