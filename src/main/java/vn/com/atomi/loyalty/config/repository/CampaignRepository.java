package vn.com.atomi.loyalty.config.repository;

import java.time.LocalDate;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import vn.com.atomi.loyalty.config.entity.Campaign;
import vn.com.atomi.loyalty.config.enums.ApprovalStatus;
import vn.com.atomi.loyalty.config.enums.Status;

/**
 * @author haidv
 * @version 1.0
 */
@Repository
public interface CampaignRepository extends JpaRepository<Campaign, Long> {

  Optional<Campaign> findByDeletedFalseAndId(Long id);

  Optional<Campaign> findByDeletedFalseAndIdAndStatus(Long id, Status status);

  @Query(
          value =
                  "select count(1) "
                          + " from Campaign c "
                          + " where c.deleted = false "
                          + "   and c.status = vn.com.atomi.loyalty.config.enums.Status.ACTIVE "
                          + "   and c.customerGroupId = :groupId "
                          + "   and (c.startDate >= :startDate or c.endDate >= :startDate)")
  boolean existsByActiveCampaign(Long groupId, LocalDate startDate);

  @Query(
          value =
                  "select c "
                          + "from Campaign c "
                          + "where c.deleted = false "
                          + "  and (:code is null or lower(c.code) = :code) "
                          + "  and (:name is null or lower(c.name) = :name) "
                          + "  and (:status is null or c.status = :status) "
                          + "  and (:startDate is null or c.startDate >= :startDate) "
                          + "  and (:endDate is null or c.endDate <= :endDate) "
                          + "  and (:budgetId is null or c.budgetId  = :budgetId) "
                          + "AND (:budgetAmount IS NULL or c.budgetAmount =: budgetAmount) "
                          + "AND (:totalBudget IS NULL OR EXISTS ( "
                          + "   SELECT 1 "
                          + "   FROM Budget b "
                          + "   WHERE b.id = c.budgetId "
                          + "   AND b.totalBudget = :totalBudget))"
                          + "AND (:approvalStatus IS NULL OR EXISTS ( "
                          + "   SELECT 1 "
                          + "   FROM CampaignApproval a "
                          + "   WHERE a.campaignId = c.id "
                          + "   AND a.approvalStatus = :approvalStatus))"
                          + "order by c.updatedAt desc ")
  Page<Campaign> findByCondition(
          String code,
          String name,
          Status status,
          LocalDate startDate,
          LocalDate endDate,
          Long budgetId,
          Long budgetAmount,
          Long totalBudget,
          ApprovalStatus approvalStatus,
          Pageable pageable);

  Optional<Campaign> findByDeletedFalseAndBudgetId(Long id);

  @Query("SELECT c FROM Campaign c WHERE c.deleted = false ORDER BY c.createdAt DESC LIMIT 1")
  Optional<Campaign> findTopByOrderByCreatedAtDesc();
}
