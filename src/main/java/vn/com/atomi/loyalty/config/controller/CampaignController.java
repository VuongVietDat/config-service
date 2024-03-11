package vn.com.atomi.loyalty.config.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.com.atomi.loyalty.base.annotations.DateTimeValidator;
import vn.com.atomi.loyalty.base.data.*;
import vn.com.atomi.loyalty.config.dto.input.ApprovalInput;
import vn.com.atomi.loyalty.config.dto.input.CampaignInput;
import vn.com.atomi.loyalty.config.dto.output.CampaignOutput;
import vn.com.atomi.loyalty.config.dto.output.ComparisonOutput;
import vn.com.atomi.loyalty.config.enums.ApprovalStatus;
import vn.com.atomi.loyalty.config.enums.ApprovalType;
import vn.com.atomi.loyalty.config.enums.Status;
import vn.com.atomi.loyalty.config.service.CampaignService;

/**
 * @author haidv
 * @version 1.0
 */
@RestController
@RequiredArgsConstructor
public class CampaignController extends BaseController {

  private final CampaignService campaignService;

  @Operation(summary = "APi tạo mới chiến dịch (bản ghi chờ duyệt)")
  @PostMapping("/campaigns/approvals")
  public ResponseEntity<ResponseData<Void>> createCampaign(
      @RequestBody @Valid CampaignInput campaignInput) {
    campaignService.createCampaign(campaignInput);
    return ResponseUtils.success();
  }

  @Operation(summary = "Api lấy danh sách duyệt chiến dịch")
  @GetMapping("/campaigns/approvals")
  public ResponseEntity<ResponseData<ResponsePage<CampaignOutput>>> getCampaignApprovals(
      @Parameter(description = "Số trang, bắt đầu từ 1") @RequestParam Integer pageNo,
      @Parameter(description = "Số lượng bản ghi 1 trang, tối đa 200") @RequestParam
          Integer pageSize,
      @Parameter(description = "Sắp xếp, Pattern: ^[a-z0-9]+:(asc|desc)")
          @RequestParam(required = false)
          String sort,
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
      @Parameter(description = "Ngày bắt đầu hiệu lực (dd/MM/yyyy)")
          @DateTimeValidator(required = false)
          @RequestParam(required = false)
          String startDate,
      @Parameter(description = "Ngày kết thúc hiệu lực (dd/MM/yyyy)")
          @DateTimeValidator(required = false)
          @RequestParam(required = false)
          String endDate,
      @Parameter(description = "Thời gian duyệt từ ngày (dd/MM/yyyy)")
          @DateTimeValidator(required = false)
          @RequestParam(required = false)
          String startApprovedDate,
      @Parameter(description = "Thời gian duyệt đến ngày (dd/MM/yyyy)")
          @DateTimeValidator(required = false)
          @RequestParam(required = false)
          String endApprovedDate,
      @Parameter(description = "Tên chiến dịch") @RequestParam(required = false) String name,
      @Parameter(description = "Mã chiến dịch") @RequestParam(required = false) String code) {
    return ResponseUtils.success(
        campaignService.getCampaignApprovals(
            status,
            approvalStatus,
            approvalType,
            startDate,
            endDate,
            startApprovedDate,
            endApprovedDate,
            name,
            code,
            super.pageable(pageNo, pageSize, sort)));
  }

  @Operation(summary = "Api lấy chi tiết bản ghi duyệt chiến dịch theo id")
  @GetMapping("/campaigns/approvals/{id}")
  public ResponseEntity<ResponseData<CampaignOutput>> getCampaignApproval(
      @Parameter(description = "ID bản ghi chờ duyệt") @PathVariable Long id) {
    return ResponseUtils.success(campaignService.getCampaignApproval(id));
  }

  @Operation(
      summary =
          "Api so sánh bản ghi duyệt cập nhật hiện tại với bản ghi đã được phê duyệt trước đó")
  @GetMapping("/campaigns/approvals/{id}/comparison")
  public ResponseEntity<ResponseData<List<ComparisonOutput>>> getRuleApprovalComparison(
      @Parameter(description = "ID bản ghi duyệt cập nhật") @PathVariable Long id) {
    return ResponseUtils.success(campaignService.geCampaignApprovalComparison(id));
  }

  @Operation(summary = "Api thu hồi yêu cầu chờ duyệt chiến dịch theo id")
  @PutMapping("/campaigns/approvals/{id}/recall")
  public ResponseEntity<ResponseData<Void>> recallCampaignApproval(
      @Parameter(description = "ID bản ghi chờ duyệt") @PathVariable Long id) {
    campaignService.recallCampaignApproval(id);
    return ResponseUtils.success();
  }

  @Operation(summary = "Api lấy danh sách chiến dịch")
  @GetMapping("/campaigns")
  public ResponseEntity<ResponseData<ResponsePage<CampaignOutput>>> getCampaigns(
      @Parameter(description = "Số trang, bắt đầu từ 1") @RequestParam Integer pageNo,
      @Parameter(description = "Số lượng bản ghi 1 trang, tối đa 200") @RequestParam
          Integer pageSize,
      @Parameter(description = "Sắp xếp, Pattern: ^[a-z0-9]+:(asc|desc)")
          @RequestParam(required = false)
          String sort,
      @Parameter(description = "Trạng thái:</br> ACTIVE: Hiệu lực</br> INACTIVE: Không hiệu lực")
          @RequestParam(required = false)
          Status status,
      @Parameter(description = "Ngày bắt đầu hiệu lực (dd/MM/yyyy)")
          @DateTimeValidator(required = false)
          @RequestParam(required = false)
          String startDate,
      @Parameter(description = "Ngày kết thúc hiệu lực (dd/MM/yyyy)")
          @DateTimeValidator(required = false)
          @RequestParam(required = false)
          String endDate,
      @Parameter(description = "Tên chiến dịch") @RequestParam(required = false) String name,
      @Parameter(description = "Mã chiến dịch") @RequestParam(required = false) String code) {
    return ResponseUtils.success(
        campaignService.getCampaigns(
            status, startDate, endDate, name, code, super.pageable(pageNo, pageSize, sort)));
  }

  @Operation(summary = "Api lấy chi tiết bản ghi chiến dịch theo id")
  @GetMapping("/campaigns/{id}")
  public ResponseEntity<ResponseData<CampaignOutput>> getCampaign(
      @Parameter(description = "ID bản ghi chiến dịch") @PathVariable Long id) {
    return ResponseUtils.success(campaignService.getCampaign(id));
  }

  @Operation(
      summary =
          "Api cập nhật bản ghi chiến dịch theo id (tương đương với việc tạo mới bản ghi chờ duyệt từ 1 bản ghi đã có)")
  @PutMapping("/campaigns/{id}")
  public ResponseEntity<ResponseData<Void>> updateCampaign(
      @Parameter(description = "ID bản ghi chiến dịch") @PathVariable Long id,
      @RequestBody CampaignInput campaignInput) {
    campaignService.updateCampaign(id, campaignInput);
    return ResponseUtils.success();
  }

  @Operation(summary = "Api phê duyệt chiến dịch")
  @PutMapping("/campaigns/approvals")
  public ResponseEntity<ResponseData<Void>> approveCampaign(@RequestBody ApprovalInput input) {
    campaignService.approveCampaign(input);
    return ResponseUtils.success();
  }
}
