package vn.com.atomi.loyalty.config.repository;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import vn.com.atomi.loyalty.config.entity.TransactionGroup;
import vn.com.atomi.loyalty.config.enums.Status;

/**
 * @author haidv
 * @version 1.0
 */
@Repository
public interface TransactionGroupRepository extends JpaRepository<TransactionGroup, Long> {

  List<TransactionGroup> findByDeletedFalseAndStatusAndCustomerType(
      Status status, String customerType);

  @Query(
      value =
          "select t "
              + "from TransactionGroup t "
              + "where t.deleted = false "
              + "  and (:customerType is null or t.customerType = :customerType) "
              + "  and (:status is null or t.status = :status) ")
  Page<TransactionGroup> findByDeletedFalseAndCustomerType(
      String customerType, Status status, Pageable pageable);
}
