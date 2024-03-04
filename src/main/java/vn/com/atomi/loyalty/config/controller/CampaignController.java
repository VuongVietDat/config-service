package vn.com.atomi.loyalty.config.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.com.atomi.loyalty.base.data.*;
import vn.com.atomi.loyalty.config.dto.input.ApprovalInput;
import vn.com.atomi.loyalty.config.dto.input.CampaignInput;
import vn.com.atomi.loyalty.config.dto.output.CampaignOutput;
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
      @RequestBody CampaignInput campaignInput) {
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
          String sort) {
    return ResponseUtils.success(
        campaignService.getCampaignApprovals(super.pageable(pageNo, pageSize, sort)));
  }

  @Operation(summary = "Api lấy chi tiết bản ghi duyệt chiến dịch theo id")
  @GetMapping("/campaigns/approvals/{id}")
  public ResponseEntity<ResponseData<CampaignOutput>> getCampaignApproval(
      @Parameter(description = "ID bản ghi chờ duyệt") @PathVariable Long id) {
    return ResponseUtils.success(campaignService.getCampaignApproval(id));
  }

  @Operation(summary = "Api cập nhật bản ghi chờ duyệt chiến dịch theo id")
  @PutMapping("/campaigns/approvals/{id}")
  public ResponseEntity<ResponseData<Void>> updateCampaignApproval(
      @Parameter(description = "ID bản ghi chờ duyệt") @PathVariable Long id,
      @RequestBody CampaignInput campaignInput) {
    campaignService.updateCampaignApproval(id, campaignInput);
    return ResponseUtils.success();
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
          String sort) {
    return ResponseUtils.success(
        campaignService.getCampaigns(super.pageable(pageNo, pageSize, sort)));
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
