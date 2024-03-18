package vn.com.atomi.loyalty.config.service;

import org.springframework.data.domain.Pageable;
import vn.com.atomi.loyalty.base.data.ResponsePage;
import vn.com.atomi.loyalty.config.dto.output.ProductCategoryOutput;
import vn.com.atomi.loyalty.config.dto.output.ProductOutput;
import vn.com.atomi.loyalty.config.enums.Status;

/**
 * @author haidv
 * @version 1.0
 */
public interface ProductService {

  ResponsePage<ProductCategoryOutput> getCategories(Status status, Pageable pageable);

  ResponsePage<ProductOutput> getProducts(Long categoryId, Status status, Pageable pageable);
}
