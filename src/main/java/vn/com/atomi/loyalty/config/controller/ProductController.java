package vn.com.atomi.loyalty.config.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import vn.com.atomi.loyalty.base.data.*;
import vn.com.atomi.loyalty.base.security.Authority;
import vn.com.atomi.loyalty.config.dto.output.*;
import vn.com.atomi.loyalty.config.enums.Status;
import vn.com.atomi.loyalty.config.service.ProductService;

/**
 * @author haidv
 * @version 1.0
 */
@RestController
@RequiredArgsConstructor
public class ProductController extends BaseController {

  private final ProductService productService;

  @Operation(summary = "Api lấy danh sách danh mục sản phẩm")
  @PreAuthorize(Authority.Product.READ_PRODUCT)
  @GetMapping("/categories-product")
  public ResponseEntity<ResponseData<ResponsePage<ProductCategoryOutput>>> getCategories(
      @Parameter(description = "Số trang, bắt đầu từ 1") @RequestParam Integer pageNo,
      @Parameter(description = "Số lượng bản ghi 1 trang, tối đa 200") @RequestParam
          Integer pageSize,
      @Parameter(description = "Sắp xếp, Pattern: ^[a-z0-9]+:(asc|desc)")
          @RequestParam(required = false)
          String sort,
      @Parameter(description = "Trạng thái:</br> ACTIVE: Hiệu lực</br> INACTIVE: Không hiệu lực")
          @RequestParam(required = false)
          Status status) {
    return ResponseUtils.success(
        productService.getCategories(status, super.pageable(pageNo, pageSize, sort)));
  }

  @Operation(summary = "Api lấy danh sách sản phẩm theo danh mục")
  @PreAuthorize(Authority.Product.READ_PRODUCT)
  @GetMapping("/products")
  public ResponseEntity<ResponseData<ResponsePage<ProductOutput>>> getProducts(
      @Parameter(description = "Số trang, bắt đầu từ 1") @RequestParam Integer pageNo,
      @Parameter(description = "Số lượng bản ghi 1 trang, tối đa 200") @RequestParam
          Integer pageSize,
      @Parameter(description = "Sắp xếp, Pattern: ^[a-z0-9]+:(asc|desc)")
          @RequestParam(required = false)
          String sort,
      @Parameter(description = "Trạng thái:</br> ACTIVE: Hiệu lực</br> INACTIVE: Không hiệu lực")
          @RequestParam(required = false)
          Status status,
      @Parameter(description = "ID danh mục") @RequestParam(required = false) Long categoryId) {
    return ResponseUtils.success(
        productService.getProducts(categoryId, status, super.pageable(pageNo, pageSize, sort)));
  }
}
