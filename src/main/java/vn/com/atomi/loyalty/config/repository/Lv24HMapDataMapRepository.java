package vn.com.atomi.loyalty.config.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.com.atomi.loyalty.config.entity.Lv24ProductDataMap;
import vn.com.atomi.loyalty.config.enums.Status;

/**
 * @author haidv
 * @version 1.0
 */
@Repository
public interface Lv24HMapDataMapRepository extends JpaRepository<Lv24ProductDataMap, Long> {

  Optional<Lv24ProductDataMap> findByDeletedFalseAndProductIdAndStatus(
      Long productId, Status status);
}
