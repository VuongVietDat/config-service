package vn.com.atomi.loyalty.config.repository;

import java.time.LocalDateTime;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import vn.com.atomi.loyalty.config.entity.Campaign;
import vn.com.atomi.loyalty.config.enums.Status;

/**
 * @author haidv
 * @version 1.0
 */
@Repository
public interface CampaignRepository extends JpaRepository<Campaign, Long> {

  Optional<Campaign> findByDeletedFalseAndIdAndStatus(Long id, Status status);

  @Query(
      value =
          "select c "
              + "from Campaign c "
              + "where c.deleted = false "
              + "  and (:status is null or c.status = :status) "
              + "  and (:startDate is null or c.startDate >= :startDate) "
              + "  and (:endDate is null or c.endDate <= :endDate) "
              + "order by c.updatedAt desc ")
  Page<Campaign> findByCondition(
      Status status, LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);
}
