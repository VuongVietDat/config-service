package vn.com.atomi.loyalty.config.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import vn.com.atomi.loyalty.base.annotations.DateTimeValidator;
import vn.com.atomi.loyalty.base.constant.DateConstant;
import vn.com.atomi.loyalty.base.data.BaseController;
import vn.com.atomi.loyalty.base.data.ResponseData;
import vn.com.atomi.loyalty.base.data.ResponsePage;
import vn.com.atomi.loyalty.base.data.ResponseUtils;
import vn.com.atomi.loyalty.config.dto.input.BudgetInput;
import vn.com.atomi.loyalty.config.dto.input.BudgetUpdateInput;
import vn.com.atomi.loyalty.config.dto.output.BudgetDetailOutput;
import vn.com.atomi.loyalty.config.dto.output.BudgetOutput;
import vn.com.atomi.loyalty.config.enums.BudgetStatus;
import vn.com.atomi.loyalty.config.service.BudgetService;

@RestController
@RequiredArgsConstructor
public class BudgetController extends BaseController {
  private final BudgetService budgetService;

  //    @PreAuthorize(Authority.Campaign.CREATE_CAMPAIGN)
  @Operation(summary = "APi tạo mới ngân sách")
  @PostMapping("/budget")
  public ResponseEntity<ResponseData<Void>> createBudget(
      @RequestBody @Valid BudgetInput budgetInput) {
    budgetService.createBudget(budgetInput);
    return ResponseUtils.success();
  }

  @Operation(summary = "Api tra cứu danh sách ngân sách")
  //  @PreAuthorize(Authority.CardTransaction.LIST_TRANSACTION_FILE)
  @GetMapping("/budget")
  public ResponseEntity<ResponseData<ResponsePage<BudgetOutput>>> getListTransactionFile(
      @Parameter(description = "Số trang, bắt đầu từ 1", example = "1") @RequestParam
          Integer pageNo,
      @Parameter(description = "Số lượng bản ghi 1 trang, tối đa 200", example = "10") @RequestParam
          Integer pageSize,
      @Parameter(description = "Sắp xếp, Pattern: ^[a-z0-9]+:(asc|desc)")
          @RequestParam(required = false)
          String sort,
      @Parameter(description = "Số quyết định") @RequestParam(required = false)
          String decisionNumber,
      @Parameter(description = "Trạng thái") @RequestParam(required = false) BudgetStatus status,
      @Parameter(description = "Tên tài liệu") @RequestParam(required = false) String name,
      @Parameter(description = "Thời gian hiệu lực từ ngày (dd/MM/yyyy)", example = "01/01/2024")
          @DateTimeValidator(required = false, pattern = DateConstant.STR_PLAN_DD_MM_YYYY_STROKE)
          @RequestParam(required = false)
          String startDate,
      @Parameter(description = "Thời gian hiệu lực đến ngày (dd/MM/yyyy)", example = "01/01/2024")
          @DateTimeValidator(required = false, pattern = DateConstant.STR_PLAN_DD_MM_YYYY_STROKE)
          @RequestParam(required = false)
          String endDate) {
    return ResponseUtils.success(
        budgetService.getListBudget(
            decisionNumber,
            startDate,
            endDate,
            status,
            name,
            super.pageable(pageNo, pageSize, sort)));
  }

  @Operation(summary = "Api chỉnh sửa ngân sách")
  //  @PreAuthorize(Authority.CardTransaction.UPDATE_CARD_TRANSACTION)
  @PutMapping("/budget")
  public ResponseEntity<ResponseData<Void>> updateCardTransaction(@RequestBody @Valid
      BudgetUpdateInput budgetUpdateInput) {
    budgetService.updateBudget(budgetUpdateInput);
    return ResponseUtils.success();
  }

  @Operation(summary = "Api xem chi tiết ngân sách")
  @GetMapping("/budget/detail")
  public BudgetDetailOutput detailBudget(
      @Parameter(description = "Id bản ghi ngân sách") @RequestParam(required = false) Long id) {
    return budgetService.detailBudget(id);
  }
}
