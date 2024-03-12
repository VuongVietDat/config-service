package vn.com.atomi.loyalty.config.repository.redis;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import vn.com.atomi.loyalty.base.utils.JsonUtils;
import vn.com.atomi.loyalty.config.dto.output.ConditionOutput;
import vn.com.atomi.loyalty.config.dto.output.DictionaryOutput;

/**
 * @author haidv
 * @version 1.0
 */
@Repository
@RequiredArgsConstructor
public class MasterDataRepository {

  private static final String KEY_DICTIONARY_ALL = "LOYALTY_DICTIONARY_ALL";
  private static final String KEY_RULE_CONDITION = "LOYALTY_CONDITION:RULE";
  private static final String KEY_CUSTOMER_GROUP_BUILDER = "LOYALTY_CONDITION:CUSTOMER_GROUP";
  private final RedisTemplate<String, Object> redisTemplate;

  public List<DictionaryOutput> getDictionary() {
    var opt = (String) this.redisTemplate.opsForValue().get(KEY_DICTIONARY_ALL);
    return opt == null
        ? new ArrayList<>()
        : JsonUtils.fromJson(opt, List.class, DictionaryOutput.class);
  }

  public List<ConditionOutput> getRuleCondition() {
    var opt = (String) this.redisTemplate.opsForValue().get(KEY_RULE_CONDITION);
    return opt == null
        ? new ArrayList<>()
        : JsonUtils.fromJson(opt, List.class, ConditionOutput.class);
  }

  public void putRuleCondition(List<ConditionOutput> conditionOutputs) {
    redisTemplate
        .opsForValue()
        .set(KEY_RULE_CONDITION, JsonUtils.toJson(conditionOutputs), Duration.ofDays(30));
  }

  public List<ConditionOutput> getCustomerGroupBuilder() {
    var opt = (String) this.redisTemplate.opsForValue().get(KEY_CUSTOMER_GROUP_BUILDER);
    return opt == null
        ? new ArrayList<>()
        : JsonUtils.fromJson(opt, List.class, ConditionOutput.class);
  }

  public void putCustomerGroupBuilder(List<ConditionOutput> conditionOutputs) {
    redisTemplate
        .opsForValue()
        .set(KEY_CUSTOMER_GROUP_BUILDER, JsonUtils.toJson(conditionOutputs), Duration.ofDays(30));
  }
}
