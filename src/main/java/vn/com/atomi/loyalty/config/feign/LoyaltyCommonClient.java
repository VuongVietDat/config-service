package vn.com.atomi.loyalty.config.feign;

import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import vn.com.atomi.loyalty.base.constant.RequestConstant;
import vn.com.atomi.loyalty.base.data.ResponseData;
import vn.com.atomi.loyalty.config.dto.output.DictionaryOutput;
import vn.com.atomi.loyalty.config.enums.Status;
import vn.com.atomi.loyalty.config.feign.fallback.LoyaltyCommonClientFallbackFactory;

/**
 * @author haidv
 * @version 1.0
 */
@FeignClient(
    name = "loyalty-common-service",
    url = "${custom.properties.loyalty-common-service-url}",
    fallbackFactory = LoyaltyCommonClientFallbackFactory.class)
public interface LoyaltyCommonClient {

  @GetMapping("/internal/dictionaries")
  ResponseData<List<DictionaryOutput>> getDictionaries(
      @RequestHeader(RequestConstant.REQUEST_ID) String requestId,
      @RequestParam(required = false) String type,
      @RequestParam(required = false) Status status);
}
