package vn.com.atomi.loyalty.config.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import vn.com.atomi.loyalty.config.entity.Budget;
import vn.com.atomi.loyalty.config.enums.ApprovalStatus;
import vn.com.atomi.loyalty.config.enums.BudgetStatus;
import vn.com.atomi.loyalty.config.enums.Status;

@Repository
public interface BudgetRepository extends JpaRepository<Budget, Long> {

  Optional<Budget> findByDeletedFalseAndDecisionNumber(String decisionNumber);
  Optional<Budget> findByDeletedFalseAndIdAndStatus(Long id, BudgetStatus status);
  @Query(
//          value =
//                  "SELECT a "
//                          + "FROM Budget a "
//                          + "WHERE "
//                          + "    (:decisionNumber IS NULL OR a.decisionNumber LIKE '%' || :decisionNumber || '%') " +
//                          "and (:totalBudget IS NULL OR a.totalBudget =: totalStatus)"
//                          + "    And (a.deleted = false ) "
//                          + "    And (:status IS NULL OR a.status = :status) "
//                          + "    AND (:name IS NULL OR a.name LIKE '%' || :name || '%')"
//                          + "    AND (:startDate IS NULL OR a.startDate >= :startDate) "
//                          + "    AND (:endDate IS NULL OR a.endDate <= :endDate)"+
//          "select b from RuleApproval b where (:approvalStatus IS NULL OR b.approvalStatus =:approvalStatus"
//                          )
          value = "SELECT a "
                  + "FROM Budget a "
                  + "WHERE (:decisionNumber IS NULL OR a.decisionNumber LIKE %:decisionNumber%) "
                  + "AND (:totalBudget IS NULL OR a.totalBudget = :totalBudget) "
                  + "AND (a.deleted = false) "
                  + "AND (:status IS NULL OR a.status = :status) "
                  + "AND (:name IS NULL OR a.name LIKE %:name%) "
                  + "AND (:startDate IS NULL OR a.startDate >= :startDate) "
                  + "AND (:endDate IS NULL OR a.endDate <= :endDate) "
                  + "AND (:approvalStatus IS NULL OR EXISTS ( "
                  + "   SELECT 1 "
                  + "   FROM RuleApproval b "
                  + "   WHERE b.budgetId = a.id "
                  + "   AND b.approvalStatus = :approvalStatus))"
  )

  Page<Budget> getListBudget(
          String decisionNumber,
          String totalBudget,
          BudgetStatus status,
          String name,
          ApprovalStatus approvalStatus,
          LocalDate startDate,
          LocalDate endDate,
          Pageable pageable);
  @Query("SELECT b, r.approvalStatus " +
          "FROM Budget b LEFT JOIN RuleApproval r " +
          "ON b.id = r.budgetId " +
          "WHERE b.id = :id AND b.deleted = false AND r.deleted = false")
  Optional<Budget> findByDeletedFalseAndId(Long id);


  @Query("SELECT b, r.approvalStatus FROM Budget b LEFT JOIN RuleApproval r ON b.id = r.budgetId WHERE b.id = :id")
  List<Object[]> findBudgetWithApprovalStatus(Long id);

}
