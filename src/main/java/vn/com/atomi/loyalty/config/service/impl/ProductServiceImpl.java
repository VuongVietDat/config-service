package vn.com.atomi.loyalty.config.service.impl;

import java.util.Arrays;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import vn.com.atomi.loyalty.base.data.ResponsePage;
import vn.com.atomi.loyalty.config.dto.output.ProductCategoryOutput;
import vn.com.atomi.loyalty.config.dto.output.ProductOutput;
import vn.com.atomi.loyalty.config.enums.Status;
import vn.com.atomi.loyalty.config.service.ProductService;

/**
 * @author haidv
 * @version 1.0
 */
@RequiredArgsConstructor
@Service
public class ProductServiceImpl implements ProductService {

  @Override
  public ResponsePage<ProductCategoryOutput> getCategories(Status status, Pageable pageable) {
    return new ResponsePage<>(
        1,
        10,
        1,
        1,
        Arrays.asList(
            ProductCategoryOutput.builder().id(1l).code("C1").name("Danh muc san pham 1").build()));
  }

  @Override
  public ResponsePage<ProductOutput> getProducts(
      Long categoryId, Status status, Pageable pageable) {
    return new ResponsePage<>(
        1,
        10,
        1,
        1,
        Arrays.asList(
            ProductOutput.builder()
                .id(1l)
                .categoryCode("C1")
                .code("P1")
                .name("San pham 1")
                .build()));
  }
}
