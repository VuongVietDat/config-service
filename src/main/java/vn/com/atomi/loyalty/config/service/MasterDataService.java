package vn.com.atomi.loyalty.config.service;

import java.util.List;
import vn.com.atomi.loyalty.config.dto.output.ConditionOutput;
import vn.com.atomi.loyalty.config.dto.output.DictionaryOutput;
import vn.com.atomi.loyalty.config.enums.Status;

/**
 * @author haidv
 * @version 1.0
 */
public interface MasterDataService {

  List<DictionaryOutput> getDictionary();

  List<DictionaryOutput> getDictionary(String type);

  List<DictionaryOutput> getDictionary(Status status);

  List<DictionaryOutput> getDictionary(String type, Status status);

  List<ConditionOutput> getRuleConditions(boolean isView);

  List<ConditionOutput> getCustomerGroupConditions(boolean isView);
}
