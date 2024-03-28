package vn.com.atomi.loyalty.config.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.com.atomi.loyalty.base.data.*;
import vn.com.atomi.loyalty.config.dto.output.*;
import vn.com.atomi.loyalty.config.enums.Status;
import vn.com.atomi.loyalty.config.service.MasterDataService;

/**
 * @author haidv
 * @version 1.0
 */
@RestController
@RequiredArgsConstructor
public class MasterDataController extends BaseController {

  private final MasterDataService masterDataService;

  @Operation(summary = "Api lấy danh sách danh mục loại sản phẩm/dịch vụ")
  @GetMapping("/master-data/product-types")
  public ResponseEntity<ResponseData<ResponsePage<ProductTypeOutput>>> getProductTypes(
      @Parameter(description = "Số trang, bắt đầu từ 1", example = "1") @RequestParam
          Integer pageNo,
      @Parameter(description = "Số lượng bản ghi 1 trang, tối đa 200", example = "10") @RequestParam
          Integer pageSize,
      @Parameter(description = "Sắp xếp, Pattern: ^[a-z0-9]+:(asc|desc)")
          @RequestParam(required = false, defaultValue = "orderNo:asc")
          String sort,
      @Parameter(description = "Trạng thái:</br> ACTIVE: Hiệu lực</br> INACTIVE: Không hiệu lực")
          @RequestParam(required = false)
          Status status,
      @Parameter(description = "Loại khách hàng", example = "INDIVIDUAL")
          @RequestParam(required = false)
          String customerType) {
    return ResponseUtils.success(
        masterDataService.getProductTypes(
            status, customerType, super.pageable(pageNo, pageSize, sort)));
  }

  @Operation(summary = "Api lấy danh sách danh mục dòng sản phẩm dịch vụ")
  @GetMapping("/master-data/product-lines")
  public ResponseEntity<ResponseData<ResponsePage<ProductLineOutput>>> getProductLines(
      @Parameter(description = "Số trang, bắt đầu từ 1", example = "1") @RequestParam
          Integer pageNo,
      @Parameter(description = "Số lượng bản ghi 1 trang, tối đa 200", example = "10") @RequestParam
          Integer pageSize,
      @Parameter(description = "Sắp xếp, Pattern: ^[a-z0-9]+:(asc|desc)")
          @RequestParam(required = false, defaultValue = "orderNo:asc")
          String sort,
      @Parameter(description = "Trạng thái:</br> ACTIVE: Hiệu lực</br> INACTIVE: Không hiệu lực")
          @RequestParam(required = false)
          Status status,
      @Parameter(description = "Danh sách loại sản phẩm", example = "CREDITCARD,LV24H")
          @RequestParam
          List<String> productTypes) {
    return ResponseUtils.success(
        masterDataService.getProductLines(
            status, productTypes, super.pageable(pageNo, pageSize, sort)));
  }

  @Operation(summary = "Api lấy danh sách nhóm giao dịch")
  @GetMapping("/master-data/transaction-groups")
  public ResponseEntity<ResponseData<ResponsePage<TransactionGroupOutput>>> getTransactionGroups(
      @Parameter(description = "Số trang, bắt đầu từ 1", example = "1") @RequestParam
          Integer pageNo,
      @Parameter(description = "Số lượng bản ghi 1 trang, tối đa 200", example = "10") @RequestParam
          Integer pageSize,
      @Parameter(description = "Sắp xếp, Pattern: ^[a-z0-9]+:(asc|desc)")
          @RequestParam(required = false, defaultValue = "orderNo:asc")
          String sort,
      @Parameter(
              description =
                  "Trạng thái:</br> ACTIVE: Hiệu lực</br> INACTIVE: Không hiệu lực "
                      + "</br>Khi xem chi tiết sẽ lấy tất cả bản ghi (status = null), khi tạo mới hoặc cập nhật, chỉ lấy những bản ghi hiệu lực (status = 'ACTIVE')")
          @RequestParam(required = false)
          Status status,
      @Parameter(description = "Loại khách hàng", example = "INDIVIDUAL") @RequestParam
          String customerType) {
    return ResponseUtils.success(
        masterDataService.getTransactionGroups(
            customerType, status, super.pageable(pageNo, pageSize, sort)));
  }

  @Operation(summary = "Api lấy danh sách loại giao dịch")
  @GetMapping("/master-data/transaction-types")
  public ResponseEntity<ResponseData<ResponsePage<TransactionTypeOutput>>> getTransactionType(
      @Parameter(description = "Số trang, bắt đầu từ 1", example = "1") @RequestParam
          Integer pageNo,
      @Parameter(description = "Số lượng bản ghi 1 trang, tối đa 200", example = "10") @RequestParam
          Integer pageSize,
      @Parameter(description = "Sắp xếp, Pattern: ^[a-z0-9]+:(asc|desc)")
          @RequestParam(required = false, defaultValue = "orderNo:asc")
          String sort,
      @Parameter(
              description =
                  "Trạng thái:</br> ACTIVE: Hiệu lực</br> INACTIVE: Không hiệu lực "
                      + "</br>Khi xem chi tiết sẽ lấy tất cả bản ghi (status = null), khi tạo mới hoặc cập nhật, chỉ lấy những bản ghi hiệu lực (status = 'ACTIVE')")
          @RequestParam(required = false)
          Status status,
      @Parameter(description = "Danh sách nhóm giao dịch", example = "CREDITCARD,LOAN,SAVING")
          @RequestParam
          List<String> transactionGroups) {
    return ResponseUtils.success(
        masterDataService.getTransactionTypes(
            transactionGroups, status, super.pageable(pageNo, pageSize, sort)));
  }
}