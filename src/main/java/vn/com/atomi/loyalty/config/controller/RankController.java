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
import vn.com.atomi.loyalty.config.service.RankService;

/**
 * @author haidv
 * @version 1.0
 */
@RestController
@RequiredArgsConstructor
public class RankController extends BaseController {

  private final RankService rankService;

  @Operation(summary = "Api lấy danh sách hạng")
  @PreAuthorize(Authority.Rank.READ_RANK)
  @GetMapping("/ranks")
  public ResponseEntity<ResponseData<ResponsePage<RankOutput>>> getRanks(
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
        rankService.getRanks(status, super.pageable(pageNo, pageSize, sort)));
  }
}
