package vn.com.atomi.loyalty.config.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.com.atomi.loyalty.config.entity.SourceDataMap;
import vn.com.atomi.loyalty.config.enums.SourceGroup;
import vn.com.atomi.loyalty.config.enums.Status;

/**
 * @author haidv
 * @version 1.0
 */
@Repository
public interface SourceDataMapRepository extends JpaRepository<SourceDataMap, Long> {

  Optional<SourceDataMap> findByDeletedFalseAndStatusAndSourceIdAndSourceTypeAndSourceGroup(
      Status status, String sourceId, String sourceType, SourceGroup sourceGroup);

  List<SourceDataMap> findByDeletedFalseAndStatusAndSourceGroup(
      Status status, SourceGroup sourceGroup);
}
