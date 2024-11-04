package vn.com.atomi.loyalty.config.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import lombok.*;
import vn.com.atomi.loyalty.base.data.BaseEntity;
import vn.com.atomi.loyalty.config.dto.projection.CampaignApprovalProjection;
import vn.com.atomi.loyalty.config.enums.ApprovalStatus;
import vn.com.atomi.loyalty.config.enums.BudgetStatus;
import vn.com.atomi.loyalty.config.enums.Status;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "CF_BUDGET")
public class Budget extends BaseEntity {
  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CF_BUDGET_ID_SEQ")
  @SequenceGenerator(
      name = "CF_BUDGET_ID_SEQ",
      sequenceName = "CF_BUDGET_ID_SEQ",
      allocationSize = 1)
  private Long id;

  @Column(name = "DECISION_NUMBER")
  private String decisionNumber;

  @Column(name = "TOTAL_BUDGET")
  private Long totalBudget;

  @Column(name = "TOTAL_POINTS_SPENT")
  private Long totalPointsSpent;

  @Column(name = "TOTAL_POINTS_ADDED")
  private Long totalPointsAdded;

  @Column(name = "NAME")
  private String name;

  @Column(name = "START_DATE")
  private LocalDate startDate;

  @Column(name = "END_DATE")
  private LocalDate endDate;

  @Column(name = "STATUS")
  @Enumerated(EnumType.STRING)
  private BudgetStatus status;

  @OneToOne
  @JoinColumn(name = "id", referencedColumnName = "ID")
  private RuleApproval ruleApproval;
//  @OneToMany(mappedBy = "budget")
//  private List<RuleApproval> orderItems = new ArrayList<>();

}

