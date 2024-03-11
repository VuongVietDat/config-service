package vn.com.atomi.loyalty.config.repository;

import java.time.LocalDateTime;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import vn.com.atomi.loyalty.config.dto.projection.RuleProjection;
import vn.com.atomi.loyalty.config.entity.Rule;
import vn.com.atomi.loyalty.config.enums.PointType;
import vn.com.atomi.loyalty.config.enums.Status;

/**
 * @author haidv
 * @version 1.0
 */
@Repository
public interface RuleRepository extends JpaRepository<Rule, Long> {

  Optional<Rule> findByDeletedFalseAndId(Long id);

  @Query(
      value =
          "select r.id                   as id, "
              + "       r.type                 as type, "
              + "       r.code                 as code, "
              + "       r.name                 as name, "
              + "       r.pointType            as pointType, "
              + "       c.id                   as campaignId, "
              + "       c.name                 as campaignName, "
              + "       r.startDate            as startDate, "
              + "       r.endDate              as endDate, "
              + "       r.status               as status, "
              + "       r.creator              as creator, "
              + "       r.creationDate         as creationDate, "
              + "       r.creationApprovalDate as creationApprovalDate "
              + "from Rule r "
              + "         join Campaign c on r.campaignId = c.id "
              + "where r.deleted = false "
              + "  and c.deleted = false "
              + "  and (:type is null or r.type = :type) "
              + "  and (:pointType is null or r.pointType = :pointType) "
              + "  and (:status is null or r.status = :status) "
              + "  and (:campaignId is null or r.campaignId = :campaignId) "
              + "  and (:startDate is null or r.startDate >= :startDate) "
              + "  and (:endDate is null or r.endDate <= :endDate) "
              + "  and (:name is null or r.name like :name) "
              + "  and (:code is null or r.code like :code) "
              + "order by r.updatedAt desc ")
  Page<RuleProjection> findByCondition(
      String type,
      PointType pointType,
      Status status,
      Long campaignId,
      LocalDateTime startDate,
      LocalDateTime endDate,
      String name,
      String code,
      Pageable pageable);
}
