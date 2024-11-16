package vn.com.atomi.loyalty.config.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import lombok.*;
import vn.com.atomi.loyalty.base.data.BaseEntity;
import vn.com.atomi.loyalty.config.enums.ApprovalStatus;
import vn.com.atomi.loyalty.config.enums.ApprovalType;
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
@Table(name = "cf_campaign_approval")
public class CampaignApproval extends BaseEntity {
  public static final String GENERATOR = "cf_campaign_arv_id_seq";

  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = GENERATOR)
  @SequenceGenerator(name = GENERATOR, sequenceName = GENERATOR, allocationSize = 1)
  private Long id;

  @Column(name = "campaign_id")
  private Long campaignId;

  @Column(name = "name")
  private String name;

  @Column(name = "code")
  private String code;

  @Column(name = "description")
  private String description;

  @Column(name = "customer_group_id")
  private Long customerGroupId;

  @Column(name = "status")
  @Enumerated(EnumType.STRING)
  private Status status;

  @Column(name = "start_date")
  private LocalDate startDate;

  @Column(name = "end_date")
  private LocalDate endDate;

  @Column(name = "approval_status")
  @Enumerated(EnumType.STRING)
  private ApprovalStatus approvalStatus;

  @Column(name = "approval_type")
  @Enumerated(EnumType.STRING)
  private ApprovalType approvalType;

  @Column(name = "approval_comment")
  private String approvalComment;

  @Column(name = "approver")
  private String approver;

  @Column(name = "budget_amount")
  private Long budgetAmount;

  @Column(name = "budget_id")
  private Long budgetId;

  @Column(name = "total_budget")
  private Long totalBudget;
}
