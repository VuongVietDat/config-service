package vn.com.atomi.loyalty.config.controller;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.com.atomi.loyalty.base.data.ListRequest;
import vn.com.atomi.loyalty.base.data.ResponseData;
import vn.com.atomi.loyalty.base.data.ResponsePage;
import vn.com.atomi.loyalty.base.data.ResponseUtils;
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
public class CampaignController {

  private final CampaignService campaignService;

  @ApiOperation("APi tạo mới chiến dịch (bản ghi chờ duyệt)")
  @PostMapping("/campaigns/approvals")
  public ResponseEntity<ResponseData<Void>> createCampaign(
      @RequestBody CampaignInput campaignInput) {
    campaignService.createCampaign(campaignInput);
    return ResponseUtils.success();
  }

  @ApiOperation("Api lấy danh sách duyệt chiến dịch")
  @GetMapping("/campaigns/approvals")
  public ResponseEntity<ResponseData<ResponsePage<CampaignOutput>>> getCampaignApprovals(
      @ModelAttribute ListRequest listRequest) {
    return ResponseUtils.success(campaignService.getCampaignApprovals(listRequest));
  }

  @ApiOperation("Api lấy chi tiết bản ghi duyệt chiến dịch theo id")
  @GetMapping("/campaigns/approvals/{id}")
  public ResponseEntity<ResponseData<CampaignOutput>> getCampaignApproval(@PathVariable Long id) {
    return ResponseUtils.success(campaignService.getCampaignApproval(id));
  }

  @ApiOperation("Api cập nhật bản ghi chờ duyệt chiến dịch theo id")
  @PutMapping("/campaigns/approvals/{id}")
  public ResponseEntity<ResponseData<Void>> updateCampaignApproval(
      @PathVariable Long id, @RequestBody CampaignInput campaignInput) {
    campaignService.updateCampaignApproval(id, campaignInput);
    return ResponseUtils.success();
  }

  @ApiOperation("Api thu hồi yêu cầu chờ duyệt chiến dịch theo id")
  @PutMapping("/campaigns/approvals/{id}/recall")
  public ResponseEntity<ResponseData<Void>> recallCampaignApproval(@PathVariable Long id) {
    campaignService.recallCampaignApproval(id);
    return ResponseUtils.success();
  }

  @ApiOperation("Api lấy danh sách chiến dịch")
  @GetMapping("/campaigns")
  public ResponseEntity<ResponseData<ResponsePage<CampaignOutput>>> getCampaigns(
      @ModelAttribute ListRequest listRequest) {
    return ResponseUtils.success(campaignService.getCampaigns(listRequest));
  }

  @ApiOperation("Api lấy chi tiết bản ghi chiến dịch theo id")
  @GetMapping("/campaigns/{id}")
  public ResponseEntity<ResponseData<CampaignOutput>> getCampaign(@PathVariable Long id) {
    return ResponseUtils.success(campaignService.getCampaign(id));
  }

  @ApiOperation(
      "Api cập nhật bản ghi chiến dịch theo id (tương đương với việc tạo mới bản ghi chờ duyệt từ 1 bản ghi đã có)")
  @PutMapping("/campaigns/{id}")
  public ResponseEntity<ResponseData<Void>> updateCampaign(
      @PathVariable Long id, @RequestBody CampaignInput campaignInput) {
    campaignService.updateCampaign(id, campaignInput);
    return ResponseUtils.success();
  }

  @ApiOperation("Api phê duyệt chiến dịch")
  @PutMapping("/campaigns/approvals")
  public ResponseEntity<ResponseData<Void>> approveCampaign(@RequestBody ApprovalInput input) {
    campaignService.approveCampaign(input);
    return ResponseUtils.success();
  }
}
