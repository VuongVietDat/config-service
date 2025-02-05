package vn.com.atomi.loyalty.config.feign;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import vn.com.atomi.loyalty.base.constant.RequestConstant;
import vn.com.atomi.loyalty.base.data.ResponseData;
import vn.com.atomi.loyalty.config.feign.fallback.LoyaltyCoreClientFallbackFactory;

/**
 * @author haidv
 * @version 1.0
 */
@FeignClient(
    name = "loyalty-core-service",
    url = "${custom.properties.loyalty-core-service-url}",
    fallbackFactory = LoyaltyCoreClientFallbackFactory.class)
public interface LoyaltyCoreClient {

  @Operation(summary = "Api (nội bộ) kiểm tra nhóm khách hàng theo id")
  @GetMapping("/internal/customer-groups/existed")
  ResponseData<Boolean> checkCustomerGroupExisted(
      @RequestHeader(RequestConstant.REQUEST_ID) String requestId, @RequestParam Long id);

  @Operation(summary = "Api (nội bộ) lấy tổng ngân sách đã phân bổ")
  @GetMapping("/internal/customers-balance-history/")
  ResponseData<Long> getAmountUsed(
      @RequestHeader(RequestConstant.REQUEST_ID) String requestId, @RequestParam Long budgetId);
}
