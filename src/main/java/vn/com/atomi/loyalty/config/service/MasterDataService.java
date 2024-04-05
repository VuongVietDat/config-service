package vn.com.atomi.loyalty.config.service;

import java.util.List;
import org.springframework.data.domain.Pageable;
import vn.com.atomi.loyalty.base.data.ResponsePage;
import vn.com.atomi.loyalty.config.dto.output.*;
import vn.com.atomi.loyalty.config.enums.SourceGroup;
import vn.com.atomi.loyalty.config.enums.Status;

/**
 * @author haidv
 * @version 1.0
 */
public interface MasterDataService {

  List<DictionaryOutput> getDictionary();

  List<DictionaryOutput> getDictionary(String type, boolean isSubLeaf);

  List<DictionaryOutput> getDictionary(Status status);

  List<DictionaryOutput> getDictionary(String type, Status status, boolean isSubLeaf);

  List<DictionaryOutput> getDictionary(List<DictionaryOutput> node, String type, boolean isSubLeaf);

  List<DictionaryOutput> getDictionary(
      List<DictionaryOutput> node, String type, Status status, boolean isSubLeaf);

  List<ConditionOutput> getRuleConditions(boolean isView);

  List<ConditionOutput> getCustomerGroupConditions(boolean isView);

  ResponsePage<TransactionGroupOutput> getTransactionGroups(
      String customerType, Status status, Pageable pageable);

  ResponsePage<TransactionTypeOutput> getTransactionTypes(
      List<String> transactionGroup, Status status, Pageable pageable);

  ResponsePage<ProductTypeOutput> getProductTypes(
      Status status, String customerType, Pageable pageable);

  ResponsePage<ProductLineOutput> getProductLines(
      Status status, List<String> productTypes, Pageable pageable);

  SourceDataMapOutput getSourceDataMap(String sourceId, String sourceType, SourceGroup sourceGroup);

  List<SourceDataMapOutput> getAllSourceDataMap(SourceGroup sourceGroup);
}
