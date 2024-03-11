package vn.com.atomi.loyalty.config.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import vn.com.atomi.loyalty.config.entity.CampaignApproval;

/**
 * @author haidv
 * @version 1.0
 */
@Repository
public interface CampaignApprovalRepository extends JpaRepository<CampaignApproval, Long> {

  @Query(value = "select " + CampaignApproval.GENERATOR + ".nextval from DUAL", nativeQuery = true)
  Long getSequence();
}
