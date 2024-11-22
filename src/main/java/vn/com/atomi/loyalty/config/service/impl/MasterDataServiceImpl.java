package vn.com.atomi.loyalty.config.service.impl;

import jakarta.persistence.EntityManager;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import vn.com.atomi.loyalty.base.data.BaseService;
import vn.com.atomi.loyalty.base.data.ResponseData;
import vn.com.atomi.loyalty.base.data.ResponsePage;
import vn.com.atomi.loyalty.base.exception.BaseException;
import vn.com.atomi.loyalty.base.utils.RequestUtils;
import vn.com.atomi.loyalty.config.dto.output.*;
import vn.com.atomi.loyalty.config.entity.Product;
import vn.com.atomi.loyalty.config.entity.ProductLine;
import vn.com.atomi.loyalty.config.entity.TransactionType;
import vn.com.atomi.loyalty.config.enums.ErrorCode;
import vn.com.atomi.loyalty.config.enums.SourceGroup;
import vn.com.atomi.loyalty.config.enums.SourceType;
import vn.com.atomi.loyalty.config.enums.Status;
import vn.com.atomi.loyalty.config.feign.LoyaltyCommonClient;
import vn.com.atomi.loyalty.config.repository.*;
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

  private final TransactionGroupRepository transactionGroupRepository;

  private final TransactionTypeRepository transactionTypeRepository;

  private final ProductTypeRepository productTypeRepository;

  private final ProductLineRepository productLineRepository;
  
  private final ProductRepository productRepository;

  private final SourceDataMapRepository sourceDataMapRepository;

  private final ApplicationContext applicationContext;

  private final EntityManager entityManager;

  @Override
  public List<DictionaryOutput> getDictionary() {
    var out = masterDataRepository.getDictionary();
    if (CollectionUtils.isEmpty(out)) {
      return loyaltyCommonClient
          .getDictionaries(RequestUtils.extractRequestId(), null, null, null)
          .getData();
    }
    return out;
  }

  @Override
  public List<DictionaryOutput> getDictionary(String type, boolean isSubLeaf) {
    if (StringUtils.isEmpty(type)) {
      return this.getDictionary();
    }
    var node = masterDataRepository.getDictionary();
    if (CollectionUtils.isEmpty(node)) {
      return loyaltyCommonClient
          .getDictionaries(RequestUtils.extractRequestId(), type, null, isSubLeaf)
          .getData();
    }
    return this.getDictionary(node, type, isSubLeaf);
  }

  @Override
  public List<DictionaryOutput> getDictionary(Status status) {
    if (status == null) {
      return this.getDictionary();
    }
    var out = masterDataRepository.getDictionary();
    if (CollectionUtils.isEmpty(out)) {
      return loyaltyCommonClient
          .getDictionaries(RequestUtils.extractRequestId(), null, status, null)
          .getData();
    }
    return out.stream().filter(v -> v.getStatus().equals(status)).collect(Collectors.toList());
  }

  @Override
  public List<DictionaryOutput> getDictionary(String type, Status status, boolean isSubLeaf) {
    if (StringUtils.isEmpty(type) && status == null) {
      return this.getDictionary();
    }
    if (!StringUtils.isEmpty(type) && status == null) {
      return this.getDictionary(type, isSubLeaf);
    }
    if (StringUtils.isEmpty(type) && status != null) {
      return this.getDictionary(status);
    }
    var out = masterDataRepository.getDictionary();
    if (CollectionUtils.isEmpty(out)) {
      return loyaltyCommonClient
          .getDictionaries(RequestUtils.extractRequestId(), type, status, isSubLeaf)
          .getData();
    }
    return this.getDictionary(out, type, status, isSubLeaf);
  }

  @Override
  public List<DictionaryOutput> getDictionary(
      List<DictionaryOutput> node, String type, boolean isSubLeaf) {
    List<DictionaryOutput> leafs =
        node.stream().filter(v -> type.equals(v.getParentCode())).collect(Collectors.toList());
    return this.appendSubLeaf(node, isSubLeaf, leafs);
  }

  @Override
  public List<DictionaryOutput> getDictionary(
      List<DictionaryOutput> node, String type, Status status, boolean isSubLeaf) {
    List<DictionaryOutput> leafs =
        node.stream()
            .filter(v -> type.equals(v.getParentCode()) && v.getStatus().equals(status))
            .collect(Collectors.toList());
    return this.appendSubLeaf(node, isSubLeaf, leafs);
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

  @Override
  public ResponsePage<TransactionGroupOutput> getTransactionGroups(
      String customerType, Status status, Pageable pageable) {
    var page =
        transactionGroupRepository.findByDeletedFalseAndCustomerType(
            customerType, status, pageable);
    return new ResponsePage<>(
        page, super.modelMapper.convertToTransactionGroupOutputs(page.getContent()));
  }

  @Override
  public ResponsePage<TransactionTypeOutput> getTransactionTypes(
      List<String> transactionGroup, Status status, Pageable pageable) {
    Page<TransactionType> page;
    if (!CollectionUtils.isEmpty(transactionGroup)) {
      page =
          transactionTypeRepository.findByDeletedFalseAndStatusAndGroupCodeIn(
              status, transactionGroup, pageable);
    } else {
      page = transactionTypeRepository.findByDeletedFalseAndStatus(status, pageable);
    }
    return new ResponsePage<>(
        page, super.modelMapper.convertToTransactionTypeOutputs(page.getContent()));
  }

  @Override
  public ResponsePage<ProductTypeOutput> getProductTypes(
      Status status, String customerType, Pageable pageable) {
    var page =
        productTypeRepository.findByDeletedFalseAndCustomerType(customerType, status, pageable);
    return new ResponsePage<>(
        page, super.modelMapper.convertToProductTypeOutputs(page.getContent()));
  }

  @Override
  public ResponsePage<ProductLineOutput> getProductLines(
		  Status status, List<String> productTypes, Pageable pageable) {
	  Page<ProductLine> page;
	  if (!CollectionUtils.isEmpty(productTypes)) {
		  page =
				  productLineRepository.findByDeletedFalseAndProductTypeInAndStatus(
						  productTypes, status, pageable);
	  } else {
		  page = productLineRepository.findByDeletedFalseAndStatus(status, pageable);
	  }
	  return new ResponsePage<>(
			  page, super.modelMapper.convertToProductLineOutputs(page.getContent()));
  }
  
  @Override
  public ResponsePage<ProductOutput> getProducts(
      Status status, List<String> productLines, Pageable pageable) {
    Page<Product> page;
    if (!CollectionUtils.isEmpty(productLines)) {
      page =
          productRepository.findByDeletedFalseAndProductInAndStatus(productLines, status, pageable);
    } else {
      page = productRepository.findByDeletedFalseAndStatus(status, pageable);
    }
    return new ResponsePage<>(
        page, super.modelMapper.convertToProductOutputs(page.getContent()));
  }

  @Override
  public SourceDataMapOutput getSourceDataMap(
      String sourceId, String sourceType, SourceGroup sourceGroup) {
    var data =
        sourceDataMapRepository
            .findByDeletedFalseAndStatusAndSourceIdAndSourceTypeAndSourceGroup(
                Status.ACTIVE, sourceId, sourceType, sourceGroup)
            .orElseThrow(() -> new BaseException(ErrorCode.NOT_SOURCE_DATA_MAP));
    return super.modelMapper.convertToSourceDataMapOutputOutput(data);
  }

  @Override
  public List<SourceDataMapOutput> getAllSourceDataMap(SourceGroup sourceGroup) {
    return super.modelMapper.convertToSourceDataMapOutputOutputs(
        sourceDataMapRepository.findByDeletedFalseAndStatusAndSourceGroup(
            Status.ACTIVE, sourceGroup));
  }

  @Override
  public List<ProductLineOutput> getProductLines(List<String> productTypes) {
    List<ProductLine> productLines;
    if (!CollectionUtils.isEmpty(productTypes)) {
      productLines =
          productLineRepository.findByDeletedFalseAndProductTypeInAndStatus(
              productTypes, Status.ACTIVE);
    } else {
      productLines = productLineRepository.findByDeletedFalseAndStatus(Status.ACTIVE);
    }
    return super.modelMapper.convertToProductLineOutputs(productLines);
  }

  private List<DictionaryOutput> appendSubLeaf(
      List<DictionaryOutput> node, boolean isSubLeaf, List<DictionaryOutput> leafs) {
    if (isSubLeaf) {
      List<String> leafCode = leafs.stream().map(DictionaryOutput::getCode).toList();
      var subLeaf = node.stream().filter(v -> leafCode.contains(v.getParentCode())).toList();
      leafs.addAll(subLeaf);
    }
    return leafs;
  }

  @SuppressWarnings({"unchecked", "rawtypes"})
  private List<ConditionOutput> getConditions(String type, boolean isView) {
    // lấy danh sách điều kiện từ database
    var conditions = conditionRepository.findByDeletedFalseAndStatus(Status.ACTIVE);
    List<ConditionOutput> rawOutput = super.modelMapper.convertToConditionOutputs(conditions);
    var dictionaries = this.getDictionary(isView ? null : Status.ACTIVE);
    List<DictionaryOutput> groupProperties = new ArrayList<>();
    if (Constants.DICTIONARY_RULE_CONDITION.equals(type)) {
      groupProperties =
          dictionaries.stream()
              .filter(
                  dictionaryOutput ->
                      Constants.DICTIONARY_RULE_INDICATOR.equals(dictionaryOutput.getParentCode())
                          && (isView || dictionaryOutput.getStatus().equals(Status.ACTIVE)))
              .toList();
    }
    var typeDictionary =
        dictionaries.stream()
            .filter(
                dictionaryOutput ->
                    type.equals(dictionaryOutput.getParentCode())
                        && (isView || dictionaryOutput.getStatus().equals(Status.ACTIVE)))
            .toList();
    var activeConfigCode = typeDictionary.stream().map(DictionaryOutput::getCode).toList();
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
                .map(
                    v ->
                        new ConditionOutput.ConditionSourceValue(
                            v.getCode(), v.getName(), null, null))
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
                            (String) object[0],
                            (String) object[1],
                            object.length == 4 ? (String) object[2] : null,
                            object.length == 4 ? (Long) object[3] : null))
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
                                          (String) object.get(str[2]),
                                          (String) object.get(str[3]),
                                          str.length == 6 ? (String) object.get(str[4]) : null,
                                          str.length == 6 ? (Long) object.get(str[5]) : null))
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
      ConditionOutput output =
          ConditionOutput.builder()
              .properties(k)
              .propertiesName(
                  typeDictionary.stream()
                      .filter(dic -> dic.getCode().equals(k))
                      .findFirst()
                      .orElse(new DictionaryOutput())
                      .getName())
              .groupCode(v.get(0).getGroupCode())
              .data(super.modelMapper.convertToConditionOutputDatas(v))
              .build();
      output.setGroupName(
          groupProperties.stream()
              .filter(groupPropertie -> groupPropertie.getCode().equals(output.getGroupCode()))
              .findFirst()
              .orElse(new DictionaryOutput())
              .getName());
      out.add(output);
    }
    return out;
  }
}
