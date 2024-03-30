package vn.com.atomi.loyalty.config.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
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
              + "       r.creationApprovalDate as creationApprovalDate, "
              + "       r.updatedAt            as updatedAt, "
              + "       r.updatedBy            as updatedBy "
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
              + "  and (:name is null or lower(r.name) like :name) "
              + "  and (:code is null or lower(r.code) like :code) "
              + "order by r.updatedAt desc ")
  Page<RuleProjection> findByCondition(
      String type,
      PointType pointType,
      Status status,
      Long campaignId,
      LocalDate startDate,
      LocalDate endDate,
      String name,
      String code,
      Pageable pageable);

  @Query(
      value =
          "select r "
              + "from Rule r "
              + "         join Campaign c on r.campaignId = c.id "
              + "where r.deleted = false "
              + "  and c.deleted = false "
              + "  and r.type = :type "
              + "  and r.status = vn.com.atomi.loyalty.config.enums.Status.ACTIVE "
              + "  and r.campaignId = :campaignId "
              + "  and ((:endDate is not null and ((r.endDate is not null and "
              + "                                   (:startDate <= r.startDate and :endDate >= r.startDate) or "
              + "                                   (:startDate >= r.startDate and :startDate <= r.endDate)) or "
              + "                                  (r.endDate is null and :endDate >= r.startDate))) "
              + "    or (:endDate is null and ((r.endDate is not null and (r.endDate >= : startDate)) or r.endDate is null))) "
              + "order by r.updatedAt desc")
  List<Rule> findCodeByOverlapActiveTime(
      String type, Long campaignId, LocalDate startDate, LocalDate endDate);

  @Transactional
  @Modifying
  @Query(
      "update Rule r set r.status = vn.com.atomi.loyalty.config.enums.Status.INACTIVE, r.inactiveBySystem = true where r.endDate < :endDate")
  void automaticallyExpiresRule(LocalDate endDate);

  @Query(
      value =
          "select r "
              + "from Rule r "
              + "where r.deleted = false "
              + "  and r.type = :type "
              + "  and (r.status = vn.com.atomi.loyalty.config.enums.Status.ACTIVE "
              + "    or (r.status = vn.com.atomi.loyalty.config.enums.Status.INACTIVE and r.inactiveBySystem = true)) "
              + "  and ((r.endDate is not null and :now >= r.startDate and :now <= r.endDate) "
              + "    or (r.endDate is null and :now >= r.startDate)) "
              + "order by r.updatedAt desc")
  List<Rule> findAllActiveRule(String type, LocalDate now);
}
