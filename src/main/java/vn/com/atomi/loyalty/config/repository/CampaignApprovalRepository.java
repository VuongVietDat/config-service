package vn.com.atomi.loyalty.config.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import vn.com.atomi.loyalty.config.dto.projection.CampaignApprovalProjection;
import vn.com.atomi.loyalty.config.entity.CampaignApproval;
import vn.com.atomi.loyalty.config.enums.ApprovalStatus;
import vn.com.atomi.loyalty.config.enums.ApprovalType;
import vn.com.atomi.loyalty.config.enums.Status;

/**
 * @author haidv
 * @version 1.0
 */
@Repository
public interface CampaignApprovalRepository extends JpaRepository<CampaignApproval, Long> {

  @Query(
      value = "select {h-schema}" + CampaignApproval.GENERATOR + ".nextval from DUAL",
      nativeQuery = true)
  Long getSequence();

  @Query(
      value =
          "select cam.id           as id, "
              + "       cam.code         as code, "
              + "       cam.name         as name, "
              + "       cam.description         as description, "
              + "       cam.status       as status, "
              + "       cam.customerGroupId       as customerGroupId, "
              + "       cus.name       as customerGroupName, "
              + "       cam.startDate    as startDate, "
              + "       cam.endDate      as endDate, "
              + "       cam.approvalStatus as approvalStatus, "
              + "       cam.approvalComment as approvalComment, "
              + "       cam.approvalType as approvalType, "
              + "       cam.approver as approver, "
              + "       cam.createdAt      as creationDate, "
              + "       cam.createdBy    as creator "
              + "from CampaignApproval cam "
              + "         join CustomerGroup cus on cam.customerGroupId = cus.id "
              + "where cam.deleted = false "
              + "  and cus.deleted = false "
              + "  and (:status is null or cam.status = :status) "
              + "  and (:approvalStatus is null or cam.approvalStatus = :approvalStatus)"
              + "  and (:approvalType is null or cam.approvalType = :approvalType)"
              + "  and (:startDate is null or cam.startDate >= :startDate) "
              + "  and (:endDate is null or cam.endDate <= :endDate) "
              + "  and (:name is null or cam.name like :name) "
              + "  and (:code is null or cam.code like :code) "
              + "  and (:startApprovedDate is null or (cam.updatedAt >= :startApprovedDate "
              + "       and cam.approvalStatus in (vn.com.atomi.loyalty.config.enums.ApprovalStatus.ACCEPTED, vn.com.atomi.loyalty.config.enums.ApprovalStatus.REJECTED))) "
              + "  and (:endApprovedDate is null or (cam.updatedAt >= :endApprovedDate "
              + "       and cam.approvalStatus in (vn.com.atomi.loyalty.config.enums.ApprovalStatus.ACCEPTED, vn.com.atomi.loyalty.config.enums.ApprovalStatus.REJECTED))) "
              + "order by cam.updatedAt desc ")
  Page<CampaignApprovalProjection> findByCondition(
      Status status,
      LocalDate startDate,
      LocalDate endDate,
      ApprovalStatus approvalStatus,
      ApprovalType approvalType,
      String name,
      String code,
      LocalDateTime startApprovedDate,
      LocalDateTime endApprovedDate,
      Pageable pageable);
}
