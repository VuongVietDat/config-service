package vn.com.atomi.loyalty.config.repository;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import vn.com.atomi.loyalty.config.entity.Product;
import vn.com.atomi.loyalty.config.entity.ProductLine;
import vn.com.atomi.loyalty.config.enums.Status;

/**
 * @author hiendv
 * @version 1.0
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

  @Query(
      value =
          "select p "
              + "from Product p "
              + "where p.deleted = false "
              + "  and p.productLine in (:productLines) "
              + "  and (:status is null or p.status = :status) ")
  Page<Product> findByDeletedFalseAndProductInAndStatus(
      List<String> productLines, Status status, Pageable pageable);

  @Query(
      value =
          "select p "
              + "from Product p "
              + "where p.deleted = false "
              + "  and p.productLine in (:productLines) "
              + "  and (:status is null or p.status = :status) ")
  List<Product> findByDeletedFalseAndProductInAndStatus(
      List<String> productLines, Status status);

  @Query(
      value =
          "select p "
              + "from Product p "
              + "where p.deleted = false "
              + "  and (:status is null or p.status = :status) ")
  Page<Product> findByDeletedFalseAndStatus(Status status, Pageable pageable);

  List<Product> findByDeletedFalseAndStatus(Status status);
}
