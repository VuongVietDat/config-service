package vn.com.atomi.loyalty.config.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import vn.com.atomi.loyalty.base.annotations.DateTimeValidator;
import vn.com.atomi.loyalty.base.constant.DateConstant;
import vn.com.atomi.loyalty.base.data.*;
import vn.com.atomi.loyalty.base.security.Authority;
import vn.com.atomi.loyalty.config.dto.input.ApprovalInput;
import vn.com.atomi.loyalty.config.dto.input.CreateRuleInput;
import vn.com.atomi.loyalty.config.dto.input.UpdateRuleInput;
import vn.com.atomi.loyalty.config.dto.input.WarringOverlapActiveTimeInput;
import vn.com.atomi.loyalty.config.dto.output.*;
import vn.com.atomi.loyalty.config.enums.ApprovalStatus;
import vn.com.atomi.loyalty.config.enums.ApprovalType;
import vn.com.atomi.loyalty.config.enums.PointType;
import vn.com.atomi.loyalty.config.enums.Status;
import vn.com.atomi.loyalty.config.service.RuleService;

/**
 * @author haidv
 * @version 1.0
 */
@RestController
@RequiredArgsConstructor
public class RuleController extends BaseController {

  private final RuleService ruleService;

  @Operation(summary = "Api tạo mới quy tắc sinh điểm (bản ghi chờ duyệt)")
  @PreAuthorize(Authority.Rule.CREATE_RULE)
  @PostMapping("/rules/approvals")
  public ResponseEntity<ResponseData<Void>> createRule(
      @Valid @RequestBody CreateRuleInput createRuleInput) {
    ruleService.createRule(createRuleInput);
    return ResponseUtils.success();
  }

  @Operation(
      summary =
          "Api kiểm tra trùng quy tắc bằng việc kiểm tra đồng thời 3 điều kiện: Loại quy tắc, Chiến dịch, Thời gian hiệu lực")
  @PreAuthorize(Authority.Rule.READ_RULE)
  @PostMapping("/rules/approvals/overlap-active-time")
  public ResponseEntity<ResponseData<WarringOverlapActiveTimeOutput>> checkOverlapActiveTime(
      @RequestBody WarringOverlapActiveTimeInput warringOverlapActiveTimeInput) {
    return ResponseUtils.success(ruleService.checkOverlapActiveTime(warringOverlapActiveTimeInput));
  }

  @Operation(summary = "Api lấy danh sách duyệt quy tắc sinh điểm")
  @PreAuthorize(Authority.Rule.READ_RULE)
  @GetMapping("/rules/approvals")
  public ResponseEntity<ResponseData<ResponsePage<RuleApprovalPreviewOutput>>> getRuleApprovals(
      @Parameter(description = "Số trang, bắt đầu từ 1", example = "1") @RequestParam
          Integer pageNo,
      @Parameter(description = "Số lượng bản ghi 1 trang, tối đa 200", example = "10") @RequestParam
          Integer pageSize,
      @Parameter(description = "Sắp xếp, Pattern: ^[a-z0-9]+:(asc|desc)")
          @RequestParam(required = false)
          String sort,
      @Parameter(description = "Loại quy tắc sinh điểm") @RequestParam(required = false)
          String type,
      @Parameter(
              description =
                  "Loại điểm:</br> ALL: Tất cả loại điẻm</br> RANK_POINT: Điểm xếp hạng</br> CONSUMPTION_POINT: Điểm tích lũy dùng để tiêu dùng")
          @RequestParam(required = false)
          PointType pointType,
      @Parameter(description = "ID chiến dịch") @RequestParam(required = false) Long campaignId,
      @Parameter(description = "Trạng thái:</br> ACTIVE: Hiệu lực</br> INACTIVE: Không hiệu lực")
          @RequestParam(required = false)
          Status status,
      @Parameter(
              description =
                  "Trạng thái phê duyệt:</br> WAITING: Chờ duyệt</br> ACCEPTED: Đồng ý</br> REJECTED: Từ chối</br> RECALL: Thu hồi")
          @RequestParam(required = false)
          ApprovalStatus approvalStatus,
      @Parameter(
              description =
                  "Loại phê duyệt: </br>CREATE: Phê duyệt tạo</br>UPDATE: Phê duyệt cập nhật</br>CANCEL: Phê duyệt hủy bỏ")
          @RequestParam(required = false)
          ApprovalType approvalType,
      @Parameter(description = "Ngày bắt đầu hiệu lực (dd/MM/yyyy)", example = "01/01/2024")
          @DateTimeValidator(required = false, pattern = DateConstant.STR_PLAN_DD_MM_YYYY_STROKE)
          @RequestParam(required = false)
          String startDate,
      @Parameter(description = "Ngày kết thúc hiệu lực (dd/MM/yyyy)", example = "31/12/2024")
          @DateTimeValidator(required = false, pattern = DateConstant.STR_PLAN_DD_MM_YYYY_STROKE)
          @RequestParam(required = false)
          String endDate,
      @Parameter(description = "Thời gian duyệt từ ngày (dd/MM/yyyy)", example = "01/01/2024")
          @DateTimeValidator(required = false, pattern = DateConstant.STR_PLAN_DD_MM_YYYY_STROKE)
          @RequestParam(required = false)
          String startApprovedDate,
      @Parameter(description = "Thời gian duyệt đến ngày (dd/MM/yyyy)", example = "31/12/2024")
          @DateTimeValidator(required = false, pattern = DateConstant.STR_PLAN_DD_MM_YYYY_STROKE)
          @RequestParam(required = false)
          String endApprovedDate,
      @Parameter(description = "Tên quy tắc") @RequestParam(required = false) String name,
      @Parameter(description = "Mã quy tắc") @RequestParam(required = false) String code) {
    return ResponseUtils.success(
        ruleService.getRuleApprovals(
            type,
            pointType,
            status,
            campaignId,
            startDate,
            endDate,
            approvalStatus,
            approvalType,
            startApprovedDate,
            endApprovedDate,
            name,
            code,
            super.pageable(pageNo, pageSize, sort)));
  }

  @Operation(summary = "Api lấy chi tiết bản ghi duyệt quy tắc sinh điểm theo id")
  @PreAuthorize(Authority.Rule.READ_RULE)
  @GetMapping("/rules/approvals/{id}")
  public ResponseEntity<ResponseData<RuleApprovalOutput>> getRuleApproval(
      @Parameter(description = "ID bản ghi chờ duyệt") @PathVariable Long id) {
    return ResponseUtils.success(ruleService.getRuleApproval(id));
  }

  @Operation(
      summary =
          "Api so sánh bản ghi duyệt cập nhật hiện tại với bản ghi đã được phê duyệt trước đó")
  @PreAuthorize(Authority.Rule.READ_RULE)
  @GetMapping("/rules/approvals/{id}/comparison")
  public ResponseEntity<ResponseData<List<ComparisonOutput>>> getRuleApprovalComparison(
      @Parameter(description = "ID bản ghi duyệt cập nhật") @PathVariable Long id) {
    return ResponseUtils.success(ruleService.getRuleApprovalComparison(id));
  }

  @Operation(summary = "Api thu hồi yêu cầu chờ duyệt quy tắc sinh điểm theo id")
  @PreAuthorize(Authority.Rule.UPDATE_RULE)
  @PutMapping("/rules/approvals/{id}/recall")
  public ResponseEntity<ResponseData<Void>> recallRuleApproval(
      @Parameter(description = "ID bản ghi chờ duyệt") @PathVariable Long id) {
    ruleService.recallRuleApproval(id);
    return ResponseUtils.success();
  }

  @Operation(summary = "Api lấy danh sách quy tắc sinh điểm")
  @PreAuthorize(Authority.Rule.READ_RULE)
  @GetMapping("/rules")
  public ResponseEntity<ResponseData<ResponsePage<RulePreviewOutput>>> getRules(
      @Parameter(description = "Số trang, bắt đầu từ 1", example = "1") @RequestParam
          Integer pageNo,
      @Parameter(description = "Số lượng bản ghi 1 trang, tối đa 200", example = "10") @RequestParam
          Integer pageSize,
      @Parameter(description = "Sắp xếp, Pattern: ^[a-z0-9]+:(asc|desc)")
          @RequestParam(required = false)
          String sort,
      @Parameter(description = "Loại quy tắc sinh điểm") @RequestParam(required = false)
          String type,
      @Parameter(
              description =
                  "Loại điểm:</br> ALL: Tất cả loại điẻm</br> RANK_POINT: Điểm xếp hạng</br> CONSUMPTION_POINT: Điểm tích lũy dùng để tiêu dùng")
          @RequestParam(required = false)
          PointType pointType,
      @Parameter(description = "ID chiến dịch") @RequestParam(required = false) Long campaignId,
      @Parameter(description = "Trạng thái:</br> ACTIVE: Hiệu lực</br> INACTIVE: Không hiệu lực")
          @RequestParam(required = false)
          Status status,
      @Parameter(description = "Ngày bắt đầu hiệu lực (dd/MM/yyyy)", example = "01/01/2024")
          @DateTimeValidator(required = false, pattern = DateConstant.STR_PLAN_DD_MM_YYYY_STROKE)
          @RequestParam(required = false)
          String startDate,
      @Parameter(description = "Ngày kết thúc hiệu lực (dd/MM/yyyy)", example = "31/12/2024")
          @DateTimeValidator(required = false, pattern = DateConstant.STR_PLAN_DD_MM_YYYY_STROKE)
          @RequestParam(required = false)
          String endDate,
      @Parameter(description = "Tên quy tắc") @RequestParam(required = false) String name,
      @Parameter(description = "Mã quy tắc") @RequestParam(required = false) String code) {
    return ResponseUtils.success(
        ruleService.getRules(
            type,
            pointType,
            status,
            campaignId,
            startDate,
            endDate,
            name,
            code,
            super.pageable(pageNo, pageSize, sort)));
  }

  @Operation(summary = "Api lấy chi tiết bản ghi quy tắc sinh điểm theo id")
  @PreAuthorize(Authority.Rule.READ_RULE)
  @GetMapping("/rules/{id}")
  public ResponseEntity<ResponseData<RuleOutput>> getRule(
      @Parameter(description = "ID bản ghi quy tắc sinh điểm") @PathVariable Long id) {
    return ResponseUtils.success(ruleService.getRule(id));
  }

  @Operation(
      summary =
          "Api cập nhật bản ghi quy tắc sinh điểm theo id (tương đương với việc tạo mới bản ghi chờ duyệt từ 1 bản ghi đã có)")
  @PreAuthorize(Authority.Rule.UPDATE_RULE)
  @PutMapping("/rules/{id}")
  public ResponseEntity<ResponseData<Void>> updateRule(
      @Parameter(description = "ID bản ghi quy tắc sinh điểm") @PathVariable Long id,
      @Valid @RequestBody UpdateRuleInput updateRuleInput) {
    ruleService.updateRule(id, updateRuleInput);
    return ResponseUtils.success();
  }

  @Operation(summary = "Api phê duyệt quy tắc sinh điểm")
  @PreAuthorize(Authority.Rule.APPROVE_RULE)
  @PutMapping("/rules/approvals")
  public ResponseEntity<ResponseData<Void>> approveRule(@Valid @RequestBody ApprovalInput input) {
    ruleService.approveRule(input);
    return ResponseUtils.success();
  }
}
