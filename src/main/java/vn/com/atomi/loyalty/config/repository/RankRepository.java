package vn.com.atomi.loyalty.config.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import vn.com.atomi.loyalty.config.entity.Rank;
import vn.com.atomi.loyalty.config.enums.Status;

/**
 * @author haidv
 * @version 1.0
 */
@Repository
public interface RankRepository extends JpaRepository<Rank, Long> {

  @Query(
      value =
          "select r "
              + "from Rank r "
              + "where r.deleted = false "
              + "  and (:status is null or r.status = :status) "
              + "order by r.updatedAt desc ")
  Page<Rank> findByCondition(Status status, Pageable pageable);
}
