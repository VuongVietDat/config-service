package vn.com.atomi.loyalty.config.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import vn.com.atomi.loyalty.base.annotations.DateTimeValidator;
import vn.com.atomi.loyalty.base.constant.DateConstant;
import vn.com.atomi.loyalty.base.constant.RequestConstant;
import vn.com.atomi.loyalty.base.data.BaseController;
import vn.com.atomi.loyalty.base.data.ResponseData;
import vn.com.atomi.loyalty.base.data.ResponsePage;
import vn.com.atomi.loyalty.base.data.ResponseUtils;
import vn.com.atomi.loyalty.base.security.Authority;
import vn.com.atomi.loyalty.config.dto.input.ApprovalInput;
import vn.com.atomi.loyalty.config.dto.input.BudgetInput;
import vn.com.atomi.loyalty.config.dto.input.BudgetUpdateInput;
import vn.com.atomi.loyalty.config.dto.output.BudgetDetailOutput;
import vn.com.atomi.loyalty.config.dto.output.BudgetOutput;
import vn.com.atomi.loyalty.config.enums.ApprovalStatus;
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
      @Parameter(description = "Tổng ngân sách") @RequestParam(required = false)
          String totalBudget,
      @Parameter(
              description =
                      "Trạng thái phê duyệt:</br> WAITING: Chờ duyệt</br> ACCEPTED: Đồng ý</br> REJECTED: Từ chối</br> RECALL: Lưu nháp")
      @RequestParam(required = false)
          ApprovalStatus approvalStatus,
      @Parameter(description = "Trạng thái") @RequestParam(required = false) BudgetStatus status,
      @Parameter(description = "Tên tài liệu") @RequestParam(required = false) String name,
      @Parameter(description = "Thời gian hiệu lực từ ngày (dd/MM/yyyy)", example = "01/01/2024")
          @DateTimeValidator(required = false, pattern = DateConstant.STR_PLAN_DD_MM_YYYY_STROKE)
          @RequestParam(required = false)
          String startDate,
      @Parameter(description = "Thời gian hiệu lực đến ngày (dd/MM/yyyy)", example = "31/12/2024")
          @DateTimeValidator(required = false, pattern = DateConstant.STR_PLAN_DD_MM_YYYY_STROKE)
          @RequestParam(required = false)
          String endDate) {
    return ResponseUtils.success(
        budgetService.getListBudget(
            decisionNumber,
            totalBudget,
            startDate,
            endDate,
            status,
            name,
            approvalStatus,
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

    @Operation(summary = "Api phê duyệt ngân sách")
//    @PreAuthorize(Authority.Rule.APPROVE_BUDGET)
    @PutMapping("/budget/approvals")
    public ResponseEntity<ResponseData<Void>> approveBudget(@Valid @RequestBody ApprovalInput input) {
        budgetService.approveBudget(input);
        return ResponseUtils.success();
    }

    @Operation(
            summary = "Api (nội bộ) tự động chuyển trạng thái hết hiệu lực ngan sach khi hết ngày kết thúc")
    @PreAuthorize(Authority.ROLE_SYSTEM)
    @PutMapping("/internal/budgets/automatically-expires")
    public ResponseEntity<ResponseData<Void>> automaticallyExpiresBudget(
            @Parameter(
                    description = "Chuỗi xác thực khi gọi api nội bộ",
                    example = "eb6b9f6fb84a45d9c9b2ac5b2c5bac4f36606b13abcb9e2de01fa4f066968cd0")
            @RequestHeader(RequestConstant.SECURE_API_KEY)
            @SuppressWarnings("unused")
            String apiKey) {
        budgetService.automaticallyExpiresBudget();
        return ResponseUtils.success();
    }
}
