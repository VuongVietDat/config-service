package vn.com.atomi.loyalty.config.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.com.atomi.loyalty.config.entity.TransactionType;
import vn.com.atomi.loyalty.config.enums.Status;

/**
 * @author haidv
 * @version 1.0
 */
@Repository
public interface TransactionTypeRepository extends JpaRepository<TransactionType, Long> {

  List<TransactionType> findByDeletedFalseAndStatusAndGroupCode(Status status, String groupCode);

  List<TransactionType> findByDeletedFalseAndGroupCode(String groupCode);
}
