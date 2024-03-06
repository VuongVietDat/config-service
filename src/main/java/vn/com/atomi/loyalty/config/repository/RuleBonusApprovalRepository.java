package vn.com.atomi.loyalty.config.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.com.atomi.loyalty.config.entity.RuleBonusApproval;

/**
 * @author haidv
 * @version 1.0
 */
@Repository
public interface RuleBonusApprovalRepository extends JpaRepository<RuleBonusApproval, Long> {

  List<RuleBonusApproval> findByDeletedFalseAndRuleApprovalId(Long ruleApprovalId);
}
