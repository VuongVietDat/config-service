package vn.com.atomi.loyalty.config.repository;

import java.time.LocalDate;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import vn.com.atomi.loyalty.config.entity.Budget;
import vn.com.atomi.loyalty.config.enums.BudgetStatus;

@Repository
public interface BudgetRepository extends JpaRepository<Budget, Long> {

  Optional<Budget> findByDeletedFalseAndDecisionNumber(String decisionNumber);

  @Query(
      value =
          "SELECT a "
              + "FROM Budget a "
              + "WHERE "
              + "    (:decisionNumber IS NULL OR a.decisionNumber LIKE '%' || :decisionNumber || '%') "
              + "    And (a.deleted = false ) "
              + "    And (:status IS NULL OR a.status = :status) "
              + "    AND (:name IS NULL OR a.name LIKE '%' || :name || '%')"
              + "    AND (:startDate IS NULL OR a.startDate >= :startDate) "
              + "    AND (:endDate IS NULL OR a.endDate <= :endDate)")
  Page<Budget> getListBudget(
      String decisionNumber,
      BudgetStatus status,
      String name,
      LocalDate startDate,
      LocalDate endDate,
      Pageable pageable);

  Optional<Budget> findByDeletedFalseAndId(Long id);
}
