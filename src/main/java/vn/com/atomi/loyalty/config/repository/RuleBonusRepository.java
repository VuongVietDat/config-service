package vn.com.atomi.loyalty.config.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import vn.com.atomi.loyalty.config.entity.RuleBonus;

/**
 * @author haidv
 * @version 1.0
 */
@Repository
public interface RuleBonusRepository extends JpaRepository<RuleBonus, Long> {

  List<RuleBonus> findByDeletedFalseAndRuleId(Long ruleId);

  @Query(
      value = "SELECT * FROM CF_RULE_BONUS WHERE RULE_ID in (?1) AND IS_DELETED = 0",
      nativeQuery = true)
  List<RuleBonus> findByDeletedFalseAndRuleIdIn(List<Long> ids);
}
