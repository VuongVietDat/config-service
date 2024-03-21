package vn.com.atomi.loyalty.config.service;

import java.util.List;
import vn.com.atomi.loyalty.config.dto.output.ConditionOutput;
import vn.com.atomi.loyalty.config.dto.output.DictionaryOutput;
import vn.com.atomi.loyalty.config.dto.output.TransactionGroupOutput;
import vn.com.atomi.loyalty.config.dto.output.TransactionTypeOutput;
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

  List<TransactionGroupOutput> getTransactionGroups(String customerType, Boolean isView);

  List<TransactionTypeOutput> getTransactionTypes(String transactionGroup, Boolean isView);
}
