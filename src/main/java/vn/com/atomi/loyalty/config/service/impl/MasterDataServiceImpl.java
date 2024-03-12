package vn.com.atomi.loyalty.config.service.impl;

import jakarta.persistence.EntityManager;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import vn.com.atomi.loyalty.base.data.BaseService;
import vn.com.atomi.loyalty.base.data.ResponseData;
import vn.com.atomi.loyalty.base.utils.RequestUtils;
import vn.com.atomi.loyalty.config.dto.output.ConditionOutput;
import vn.com.atomi.loyalty.config.dto.output.DictionaryOutput;
import vn.com.atomi.loyalty.config.enums.SourceType;
import vn.com.atomi.loyalty.config.enums.Status;
import vn.com.atomi.loyalty.config.feign.LoyaltyCommonClient;
import vn.com.atomi.loyalty.config.repository.ConditionRepository;
import vn.com.atomi.loyalty.config.repository.redis.MasterDataRepository;
import vn.com.atomi.loyalty.config.service.MasterDataService;
import vn.com.atomi.loyalty.config.utils.Constants;

/**
 * @author haidv
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
public class MasterDataServiceImpl extends BaseService implements MasterDataService {

  private final LoyaltyCommonClient loyaltyCommonClient;

  private final MasterDataRepository masterDataRepository;

  private final ConditionRepository conditionRepository;

  private final ApplicationContext applicationContext;

  private final EntityManager entityManager;

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
    return out.stream().filter(v -> type.equals(v.getParentCode())).collect(Collectors.toList());
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
    return out.stream()
        .filter(v -> type.equals(v.getParentCode()) && v.getStatus().equals(status))
        .collect(Collectors.toList());
  }

  @Override
  public List<ConditionOutput> getRuleConditions(boolean isView) {
    var out = masterDataRepository.getRuleCondition();
    if (!CollectionUtils.isEmpty(out)) {
      return out;
    }
    out = this.getConditions(Constants.DICTIONARY_RULE_CONDITION, isView);
    // lưu kết quả vào redis
    if (!CollectionUtils.isEmpty(out)) {
      masterDataRepository.putRuleCondition(out);
    }
    return out;
  }

  @Override
  public List<ConditionOutput> getCustomerGroupConditions(boolean isView) {
    var out = masterDataRepository.getCustomerGroupBuilder();
    if (!CollectionUtils.isEmpty(out)) {
      return out;
    }
    out = this.getConditions(Constants.DICTIONARY_CUSTOMER_GROUP_CONDITION, isView);
    // lưu kết quả vào redis
    if (!CollectionUtils.isEmpty(out)) {
      masterDataRepository.putRuleCondition(out);
    }
    return out;
  }

  @SuppressWarnings({"unchecked", "rawtypes"})
  private List<ConditionOutput> getConditions(String type, boolean isView) {
    // lấy danh sách điều kiện từ database
    var conditions = conditionRepository.findByDeletedFalseAndStatus(Status.ACTIVE);
    List<ConditionOutput> rawOutput = super.modelMapper.convertToConditionOutputs(conditions);
    var dictionaries = this.getDictionary(isView ? null : Status.ACTIVE);
    var activeConfig =
        dictionaries.stream()
            .filter(
                dictionaryOutput ->
                    type.equals(dictionaryOutput.getParentCode())
                        && (isView || dictionaryOutput.getStatus().equals(Status.ACTIVE)))
            .toList();
    var activeConfigCode = activeConfig.stream().map(DictionaryOutput::getCode).toList();
    rawOutput =
        rawOutput.stream().filter(v -> activeConfigCode.contains(v.getProperties())).toList();
    for (ConditionOutput conditionOutput : rawOutput) {
      // nếu nguồn là từ bảng DICTIONARY lấy data theo type
      if (SourceType.DICTIONARY.equals(conditionOutput.getSourceType())) {
        conditionOutput.setSourceValues(
            dictionaries.stream()
                .filter(
                    v ->
                        conditionOutput.getSourceValue().equals(v.getParentCode())
                            && v.getStatus().equals(Status.ACTIVE))
                .map(v -> new ConditionOutput.ConditionSourceValue(v.getCode(), v.getName()))
                .collect(Collectors.toList()));
      }
      // nếu nguồn là SQL thì thực thi câu SQL và lấy data từ database, trả về 2 trường theo thứ tự
      // code, name
      if (SourceType.SQL.equals(conditionOutput.getSourceType())) {
        List<Object[]> objects =
            entityManager.createNativeQuery(conditionOutput.getSourceValue()).getResultList();
        conditionOutput.setSourceValues(
            objects.stream()
                .map(
                    object ->
                        new ConditionOutput.ConditionSourceValue(
                            (String) object[0], (String) object[1]))
                .collect(Collectors.toList()));
      }
      // nếu nguồn là API thì call api để lấy data
      if (SourceType.API.equals(conditionOutput.getSourceType())) {
        String[] str = conditionOutput.getSourceValue().split("\\|");
        String handleEventClassName = str[0];
        // tên method là duy nhất
        String handleEventFunctionName = str[1];
        try {
          Object obj = applicationContext.getBean(Class.forName(handleEventClassName));
          Arrays.stream(obj.getClass().getMethods())
              .filter(v -> v.getName().equals(handleEventFunctionName))
              .findFirst()
              .ifPresent(
                  method -> {
                    try {
                      // call api
                      List<LinkedHashMap> result =
                          ((ResponseData<List<LinkedHashMap>>)
                                  method.invoke(obj, RequestUtils.extractRequestId()))
                              .getData();
                      // set data vào object output, field tương ứng code, name lấy config từ
                      // sourceValue
                      conditionOutput.setSourceValues(
                          result.stream()
                              .map(
                                  object ->
                                      new ConditionOutput.ConditionSourceValue(
                                          (String) object.get(str[2]), (String) object.get(str[3])))
                              .collect(Collectors.toList()));
                    } catch (IllegalAccessException | InvocationTargetException e) {
                      LOGGER.error(e.getMessage(), e);
                    }
                  });
        } catch (ClassNotFoundException e) {
          LOGGER.error(e.getMessage(), e);
        }
      }
    }
    // nhóm output lại theo định dạng trả về cho client
    var map = rawOutput.stream().collect(Collectors.groupingBy(ConditionOutput::getProperties));
    List<ConditionOutput> out = new ArrayList<>();
    for (Map.Entry<String, List<ConditionOutput>> entry : map.entrySet()) {
      String k = entry.getKey();
      List<ConditionOutput> v = entry.getValue();
      out.add(
          ConditionOutput.builder()
              .properties(k)
              .propertiesName(
                  activeConfig.stream()
                      .filter(dic -> dic.getCode().equals(k))
                      .findFirst()
                      .orElse(new DictionaryOutput())
                      .getName())
              .data(super.modelMapper.convertToConditionOutputDatas(v))
              .build());
    }
    return out;
  }
}
