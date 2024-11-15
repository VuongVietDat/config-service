package vn.com.atomi.loyalty.config.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.com.atomi.loyalty.config.entity.RulePOC;

import java.util.Optional;

public interface RulePOCRepository extends JpaRepository<RulePOC, Long> {

    Optional<RulePOC> findByDeletedFalseAndType(String type);
}
