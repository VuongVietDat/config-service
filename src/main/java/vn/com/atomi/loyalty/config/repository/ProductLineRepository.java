package vn.com.atomi.loyalty.config.repository;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import vn.com.atomi.loyalty.config.entity.ProductLine;
import vn.com.atomi.loyalty.config.enums.Status;

/**
 * @author haidv
 * @version 1.0
 */
@Repository
public interface ProductLineRepository extends JpaRepository<ProductLine, Long> {

  @Query(
      value =
          "select p "
              + "from ProductLine p "
              + "where p.deleted = false "
              + "  and p.productType in (:productTypes) "
              + "  and (:status is null or p.status = :status) ")
  Page<ProductLine> findByDeletedFalseAndProductTypeInAndStatus(
      List<String> productTypes, Status status, Pageable pageable);

  List<ProductLine> findByDeletedFalseAndStatus(Status status);
}
