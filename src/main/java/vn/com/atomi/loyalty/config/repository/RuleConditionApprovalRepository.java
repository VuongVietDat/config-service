package vn.com.atomi.loyalty.config.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.com.atomi.loyalty.config.entity.RuleConditionApproval;

/**
 * @author haidv
 * @version 1.0
 */
@Repository
public interface RuleConditionApprovalRepository
    extends JpaRepository<RuleConditionApproval, Long> {

  List<RuleConditionApproval> findByDeletedFalseAndRuleApprovalId(Long ruleApprovalId);
}
