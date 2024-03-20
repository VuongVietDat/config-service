package vn.com.atomi.loyalty.config.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
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

  List<TransactionGroup> findByDeletedFalseAndCustomerType(String customerType);
}
