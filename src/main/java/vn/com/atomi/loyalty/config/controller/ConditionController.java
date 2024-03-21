package vn.com.atomi.loyalty.config.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import vn.com.atomi.loyalty.base.data.*;
import vn.com.atomi.loyalty.base.security.Authority;
import vn.com.atomi.loyalty.config.dto.output.*;
import vn.com.atomi.loyalty.config.service.MasterDataService;

/**
 * @author haidv
 * @version 1.0
 */
@RestController
@RequiredArgsConstructor
public class ConditionController extends BaseController {

  private final MasterDataService masterDataService;

  @Operation(summary = "Api lấy danh sách điều kiện cho quy tắc")
  @PreAuthorize(Authority.Rule.READ_RULE)
  @GetMapping("/rules/conditions")
  public ResponseEntity<ResponseData<List<ConditionOutput>>> getRuleConditions(
      @Parameter(
              description =
                  "(isView = true) Call khi xem chi tiết sẽ lấy tất cả bản ghi</br>(isView = false) Call khi tạo mới hoặc cập nhật, chỉ lấy những bản ghi hiệu lực")
          @RequestParam(required = false, defaultValue = "false")
          Boolean isView) {
    return ResponseUtils.success(masterDataService.getRuleConditions(isView));
  }

  @Operation(summary = "Api lấy danh sách điều kiện cho nhóm khách hàng")
  @PreAuthorize(Authority.CustomerGroup.READ_CUSTOMER_GROUP)
  @GetMapping("/customer-groups/conditions")
  public ResponseEntity<ResponseData<List<ConditionOutput>>> getCustomerGroupConditions(
      @Parameter(
              description =
                  "(isView = true) Call khi xem chi tiết sẽ lấy tất cả bản ghi</br>(isView = false) Call khi tạo mới hoặc cập nhật, chỉ lấy những bản ghi hiệu lực")
          @RequestParam(required = false, defaultValue = "false")
          Boolean isView) {
    return ResponseUtils.success(masterDataService.getCustomerGroupConditions(isView));
  }
}
