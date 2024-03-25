package vn.com.atomi.loyalty.config.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import vn.com.atomi.loyalty.config.dto.projection.RuleApprovalProjection;
import vn.com.atomi.loyalty.config.entity.RuleApproval;
import vn.com.atomi.loyalty.config.enums.ApprovalStatus;
import vn.com.atomi.loyalty.config.enums.ApprovalType;
import vn.com.atomi.loyalty.config.enums.PointType;
import vn.com.atomi.loyalty.config.enums.Status;

/**
 * @author haidv
 * @version 1.0
 */
@Repository
public interface RuleApprovalRepository extends JpaRepository<RuleApproval, Long> {

  @Query(value = "select CF_RULE_ARV_ID_SEQ.nextval from DUAL", nativeQuery = true)
  Long getSequence();

  Optional<RuleApproval> findByDeletedFalseAndId(Long id);

  @Query(
      "select r from RuleApproval r where r.deleted = false "
          + "and r.approvalStatus = vn.com.atomi.loyalty.config.enums.ApprovalStatus.ACCEPTED "
          + "and r.ruleId = ?1 and r.id < ?2 "
          + "order by r.updatedAt desc "
          + "limit 1")
  Optional<RuleApproval> findLatestAcceptedRecord(Long ruleId, Long id);

  Optional<RuleApproval> findByDeletedFalseAndIdAndApprovalStatus(
      Long id, ApprovalStatus approvalStatus);

  @Query(
      value =
          "select r.id             as id, "
              + "       r.type           as type, "
              + "       r.code           as code, "
              + "       r.name           as name, "
              + "       r.pointType      as pointType, "
              + "       c.id             as campaignId, "
              + "       c.name           as campaignName, "
              + "       r.startDate      as startDate, "
              + "       r.endDate        as endDate, "
              + "       r.status         as status, "
              + "       r.createdAt      as createdAt, "
              + "       r.createdBy      as createdBy, "
              + "       r.updatedBy      as updatedBy, "
              + "       r.approvalType   as approvalType, "
              + "       r.approvalStatus as approvalStatus, "
              + "       r.updatedAt      as updatedAt "
              + "from RuleApproval r "
              + "         join Campaign c on r.campaignId = c.id "
              + "where r.deleted = false "
              + "  and c.deleted = false "
              + "  and (:type is null or r.type = :type) "
              + "  and (:pointType is null or r.pointType = :pointType) "
              + "  and (:status is null or r.status = :status) "
              + "  and (:approvalStatus is null or r.approvalStatus = :approvalStatus)"
              + "  and (:approvalType is null or r.approvalType = :approvalType)"
              + "  and (:campaignId is null or r.campaignId = :campaignId) "
              + "  and (:startDate is null or r.startDate >= :startDate) "
              + "  and (:endDate is null or r.endDate <= :endDate) "
              + "  and (:name is null or lower(r.name) like :name) "
              + "  and (:code is null or lower(r.code) like :code) "
              + "  and (:startApprovedDate is null or (r.updatedAt >= :startApprovedDate "
              + "       and r.approvalStatus in (vn.com.atomi.loyalty.config.enums.ApprovalStatus.ACCEPTED, vn.com.atomi.loyalty.config.enums.ApprovalStatus.REJECTED))) "
              + "  and (:endApprovedDate is null or (r.updatedAt <= :endApprovedDate "
              + "       and r.approvalStatus in (vn.com.atomi.loyalty.config.enums.ApprovalStatus.ACCEPTED, vn.com.atomi.loyalty.config.enums.ApprovalStatus.REJECTED))) "
              + "order by r.updatedAt desc ")
  Page<RuleApprovalProjection> findByCondition(
      String type,
      PointType pointType,
      Status status,
      Long campaignId,
      LocalDate startDate,
      LocalDate endDate,
      ApprovalStatus approvalStatus,
      ApprovalType approvalType,
      String name,
      String code,
      LocalDateTime startApprovedDate,
      LocalDateTime endApprovedDate,
      Pageable pageable);

  List<RuleApproval> findByDeletedFalseAndRuleId(Long ruleId);
}
