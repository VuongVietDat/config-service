package vn.com.atomi.loyalty.config.service.impl;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import vn.com.atomi.loyalty.base.data.BaseService;
import vn.com.atomi.loyalty.base.utils.RequestUtils;
import vn.com.atomi.loyalty.config.dto.output.DictionaryOutput;
import vn.com.atomi.loyalty.config.enums.Status;
import vn.com.atomi.loyalty.config.feign.LoyaltyCommonClient;
import vn.com.atomi.loyalty.config.repository.redis.MasterDataRepository;
import vn.com.atomi.loyalty.config.service.MasterDataService;

/**
 * @author haidv
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
public class MasterDataServiceImpl extends BaseService implements MasterDataService {

  private final LoyaltyCommonClient loyaltyCommonClient;

  private final MasterDataRepository masterDataRepository;

  @Override
  public List<DictionaryOutput> getDictionary() {
    var out = masterDataRepository.getDictionary();
    if (CollectionUtils.isEmpty(out)) {
      return loyaltyCommonClient
          .getDictionaries(RequestUtils.extractRequestId(), null, null)
          .getData();
    }
    return out;
  }

  @Override
  public List<DictionaryOutput> getDictionary(String type) {
    if (StringUtils.isEmpty(type)) {
      return this.getDictionary();
    }
    var out = masterDataRepository.getDictionary();
    if (CollectionUtils.isEmpty(out)) {
      return loyaltyCommonClient
          .getDictionaries(RequestUtils.extractRequestId(), type, null)
          .getData();
    }
    return out.stream().filter(v -> v.getType().equals(type)).collect(Collectors.toList());
  }

  @Override
  public List<DictionaryOutput> getDictionary(Status status) {
    if (status == null) {
      return this.getDictionary();
    }
    var out = masterDataRepository.getDictionary();
    if (CollectionUtils.isEmpty(out)) {
      return loyaltyCommonClient
          .getDictionaries(RequestUtils.extractRequestId(), null, status)
          .getData();
    }
    return out.stream().filter(v -> v.getStatus().equals(status)).collect(Collectors.toList());
  }

  @Override
  public List<DictionaryOutput> getDictionary(String type, Status status) {
    if (StringUtils.isEmpty(type) && status == null) {
      return this.getDictionary();
    }
    if (!StringUtils.isEmpty(type) && status == null) {
      return this.getDictionary(type);
    }
    if (StringUtils.isEmpty(type) && status != null) {
      return this.getDictionary(status);
    }
    var out = masterDataRepository.getDictionary();
    if (CollectionUtils.isEmpty(out)) {
      return loyaltyCommonClient
          .getDictionaries(RequestUtils.extractRequestId(), type, status)
          .getData();
    }
    return out.stream().filter(v -> v.getType().equals(type)).collect(Collectors.toList());
  }
}
