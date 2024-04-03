package vn.com.atomi.loyalty.config.repository;

import java.util.Collection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import vn.com.atomi.loyalty.config.entity.TransactionType;
import vn.com.atomi.loyalty.config.enums.Status;

/**
 * @author haidv
 * @version 1.0
 */
@Repository
public interface TransactionTypeRepository extends JpaRepository<TransactionType, Long> {

  @Query(
      value =
          "select p "
              + "from TransactionType p "
              + "where p.deleted = false "
              + "  and p.groupCode in (:groupCode) "
              + "  and (:status is null or p.status = :status) ")
  Page<TransactionType> findByDeletedFalseAndStatusAndGroupCodeIn(
      Status status, Collection<String> groupCode, Pageable pageable);

  @Query(
      value =
          "select p "
              + "from TransactionType p "
              + "where p.deleted = false "
              + "  and (:status is null or p.status = :status) ")
  Page<TransactionType> findByDeletedFalseAndStatus(Status status, Pageable pageable);
}
