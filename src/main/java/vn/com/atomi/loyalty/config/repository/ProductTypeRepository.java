package vn.com.atomi.loyalty.config.repository;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import vn.com.atomi.loyalty.config.entity.ProductType;
import vn.com.atomi.loyalty.config.enums.Status;

/**
 * @author haidv
 * @version 1.0
 */
@Repository
public interface ProductTypeRepository extends JpaRepository<ProductType, Long> {

  @Query(
      value =
          "select p "
              + "from ProductType p "
              + "where p.deleted = false "
              + "  and (:customerType is null or p.customerType = :customerType) "
              + "  and (:status is null or p.status = :status) ")
  Page<ProductType> findByDeletedFalseAndCustomerType(
      String customerType, Status status, Pageable pageable);

  List<ProductType> findByDeletedFalseAndStatus(Status status);
}
