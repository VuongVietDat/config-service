package vn.com.atomi.loyalty.config.mapper;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.mapstruct.*;
import vn.com.atomi.loyalty.base.constant.DateConstant;
import vn.com.atomi.loyalty.config.dto.input.CreateRuleInput;
import vn.com.atomi.loyalty.config.dto.input.UpdateRuleInput;
import vn.com.atomi.loyalty.config.dto.output.*;
import vn.com.atomi.loyalty.config.dto.projection.RuleApprovalProjection;
import vn.com.atomi.loyalty.config.dto.projection.RuleProjection;
import vn.com.atomi.loyalty.config.entity.*;
import vn.com.atomi.loyalty.config.enums.ApprovalStatus;
import vn.com.atomi.loyalty.config.enums.ApprovalType;

/**
 * @author haidv
 * @version 1.0
 */
@Mapper
public interface ModelMapper {

  default String getApprover(ApprovalStatus approvalStatus, String updateBy) {
    return ApprovalStatus.RECALL.equals(approvalStatus)
            || ApprovalStatus.WAITING.equals(approvalStatus)
        ? null
        : updateBy;
  }

  default LocalDateTime getApproveDate(ApprovalStatus approvalStatus, LocalDateTime updateAt) {
    return ApprovalStatus.RECALL.equals(approvalStatus)
            || ApprovalStatus.WAITING.equals(approvalStatus)
        ? null
        : updateAt;
  }

  default List<RuleBonus> convertToRuleBonuses(
      List<RuleBonusApproval> ruleBonusApprovals, Long ruleId) {
    if (ruleBonusApprovals == null) {
      return null;
    }
    List<RuleBonus> list = new ArrayList<>(ruleBonusApprovals.size());
    for (RuleBonusApproval ruleBonusApproval : ruleBonusApprovals) {
      list.add(convertToRuleBonus(ruleBonusApproval, ruleId));
    }
    return list;
  }

  default List<RuleCondition> convertToRuleConditions(
      List<RuleConditionApproval> ruleConditionApprovals, Long ruleId) {
    if (ruleConditionApprovals == null) {
      return null;
    }
    List<RuleCondition> list = new ArrayList<>(ruleConditionApprovals.size());
    for (RuleConditionApproval ruleConditionApproval : ruleConditionApprovals) {
      list.add(convertToRuleCondition(ruleConditionApproval, ruleId));
    }
    return list;
  }

  default List<RuleAllocation> convertToRuleAllocations(
      List<RuleAllocationApproval> ruleAllocationApprovals, Long ruleId) {
    if (ruleAllocationApprovals == null) {
      return null;
    }
    List<RuleAllocation> list = new ArrayList<>(ruleAllocationApprovals.size());
    for (RuleAllocationApproval ruleAllocationApproval : ruleAllocationApprovals) {
      list.add(convertToRuleAllocation(ruleAllocationApproval, ruleId));
    }
    return list;
  }

  default List<RuleBonusApproval> convertToRuleBonusApprovals(
      List<RuleBonus> ruleBonuses, Long ruleApprovalId) {
    if (ruleBonuses == null) {
      return null;
    }
    List<RuleBonusApproval> list = new ArrayList<>(ruleBonuses.size());
    for (RuleBonus ruleBonus : ruleBonuses) {
      list.add(convertToRuleBonusApproval(ruleBonus, ruleApprovalId));
    }
    return list;
  }

  default List<RuleConditionApproval> convertToRuleConditionApprovals(
      List<RuleCondition> ruleConditions, Long ruleApprovalId) {
    if (ruleConditions == null) {
      return null;
    }
    List<RuleConditionApproval> list = new ArrayList<>(ruleConditions.size());
    for (RuleCondition ruleCondition : ruleConditions) {
      list.add(convertToRuleConditionApproval(ruleCondition, ruleApprovalId));
    }
    return list;
  }

  default List<RuleAllocationApproval> convertToRuleAllocationApprovals(
      List<RuleAllocation> ruleAllocations, Long ruleApprovalId) {
    if (ruleAllocations == null) {
      return null;
    }
    List<RuleAllocationApproval> list = new ArrayList<>(ruleAllocations.size());
    for (RuleAllocation ruleAllocation : ruleAllocations) {
      list.add(convertToRuleAllocationApproval(ruleAllocation, ruleApprovalId));
    }
    return list;
  }

  default List<RuleConditionApproval> convertToRuleConditionApprovalsFromInput(
      List<CreateRuleInput.RuleConditionInput> ruleConditionInputs, Long ruleApprovalId) {
    if (ruleConditionInputs == null) {
      return null;
    }
    List<RuleConditionApproval> list = new ArrayList<>(ruleConditionInputs.size());
    for (CreateRuleInput.RuleConditionInput ruleCondition : ruleConditionInputs) {
      list.add(convertToRuleConditionApproval(ruleCondition, ruleApprovalId));
    }
    return list;
  }

  default List<RuleAllocationApproval> convertToRuleAllocationApprovalsFromInput(
      List<CreateRuleInput.RuleAllocationInput> allocationInputs, Long ruleApprovalId) {
    if (allocationInputs == null) {
      return null;
    }
    List<RuleAllocationApproval> list = new ArrayList<>(allocationInputs.size());
    for (CreateRuleInput.RuleAllocationInput ruleAllocationInput : allocationInputs) {
      list.add(convertToRuleAllocationApproval(ruleAllocationInput, ruleApprovalId));
    }
    return list;
  }

  default List<RuleBonusApproval> convertToRuleBonusApprovalsFromInput(
      List<CreateRuleInput.RuleBonusInput> ruleBonusInputs, Long ruleApprovalId) {
    if (ruleBonusInputs == null) {
      return null;
    }
    List<RuleBonusApproval> list = new ArrayList<>(ruleBonusInputs.size());
    for (CreateRuleInput.RuleBonusInput ruleBonusInput : ruleBonusInputs) {
      list.add(convertToRuleBonusApproval(ruleBonusInput, ruleApprovalId));
    }
    return list;
  }

  @Named("findDictionaryName")
  default String findDictionaryName(
      String code, @Context List<DictionaryOutput> dictionaryOutputs) {
    if (code == null || dictionaryOutputs == null) {
      return null;
    }
    for (DictionaryOutput value : dictionaryOutputs) {
      if (value != null && code.equals(value.getCode())) {
        return value.getName();
      }
    }
    return null;
  }

  @Mapping(target = "startDate", source = "ruleStartDate")
  @Mapping(target = "endDate", source = "ruleEndDate")
  @Mapping(target = "approvalType", source = "approvalType")
  @Mapping(target = "approvalStatus", source = "approvalStatus")
  RuleApproval convertToRuleApproval(
      CreateRuleInput createRuleInput,
      LocalDateTime ruleStartDate,
      LocalDateTime ruleEndDate,
      ApprovalStatus approvalStatus,
      ApprovalType approvalType);

  @Mapping(target = "creator", source = "createdBy")
  @Mapping(target = "creationDate", source = "createdAt")
  @Mapping(target = "id", ignore = true)
  Rule convertToRule(RuleApproval ruleApproval);

  @Mapping(target = "ruleId", source = "ruleId")
  RuleBonus convertToRuleBonus(RuleBonusApproval ruleBonusApproval, Long ruleId);

  @Mapping(target = "ruleId", source = "ruleId")
  RuleCondition convertToRuleCondition(RuleConditionApproval ruleConditionApproval, Long ruleId);

  @Mapping(target = "ruleId", source = "ruleId")
  RuleAllocation convertToRuleAllocation(
      RuleAllocationApproval ruleAllocationApproval, Long ruleId);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "createdAt", ignore = true)
  @Mapping(target = "updatedAt", ignore = true)
  @Mapping(target = "createdBy", ignore = true)
  @Mapping(target = "updatedBy", ignore = true)
  @Mapping(target = "deleted", ignore = true)
  Rule convertToRule(@MappingTarget Rule currentRule, RuleApproval ruleApproval);

  @Mapping(target = "typeName", source = "rule.type", qualifiedByName = "findDictionaryName")
  RulePreviewOutput convertToRulePreviewOutput(
      RuleProjection rule, @Context List<DictionaryOutput> dictionaryOutputs);

  List<RulePreviewOutput> convertToRulePreviewOutputs(
      List<RuleProjection> rules, @Context List<DictionaryOutput> dictionaryOutputs);

  @Mapping(target = "typeName", source = "approval.type", qualifiedByName = "findDictionaryName")
  @Mapping(target = "creator", source = "createdBy")
  @Mapping(target = "creationDate", source = "createdAt")
  @Mapping(
      target = "approveDate",
      expression = "java(getApproveDate(approval.getApprovalStatus(), approval.getUpdatedAt()))")
  @Mapping(
      target = "approver",
      expression = "java(getApprover(approval.getApprovalStatus(), approval.getUpdatedBy()))")
  RuleApprovalPreviewOutput convertToRuleApprovalPreviewOutput(
      RuleApprovalProjection approval, @Context List<DictionaryOutput> dictionaryOutputs);

  List<RuleApprovalPreviewOutput> convertToRuleApprovalPreviewOutputs(
      List<RuleApprovalProjection> approvals, @Context List<DictionaryOutput> dictionaryOutputs);

  RuleOutput convertToRuleOutput(Rule rule);

  List<RuleOutput.RuleConditionOutput> convertToRuleConditionOutputs(
      List<RuleCondition> ruleConditions);

  List<RuleOutput.RuleAllocationOutput> convertToRuleAllocationOutputs(
      List<RuleAllocation> ruleAllocations);

  List<RuleOutput.RuleBonusOutput> convertToRuleBonusOutputs(List<RuleBonus> ruleBonuses);

  RuleApprovalOutput convertToRuleApprovalOutput(RuleApproval ruleApproval);

  List<RuleApprovalOutput.RuleConditionApprovalOutput> convertToRuleConditionApprovalOutputs(
      List<RuleConditionApproval> ruleConditionApprovals);

  List<RuleApprovalOutput.RuleAllocationApprovalOutput> convertToRuleAllocationApprovalOutputs(
      List<RuleAllocationApproval> ruleAllocationApprovals);

  List<RuleApprovalOutput.RuleBonusApprovalOutput> convertToRuleBonusApprovalOutputs(
      List<RuleBonusApproval> ruleBonusApprovals);

  Rule convertToRule(@MappingTarget Rule rule, UpdateRuleInput updateRuleInput);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "ruleId", source = "rule.id")
  @Mapping(target = "approvalType", source = "approvalType")
  @Mapping(target = "approvalStatus", source = "approvalStatus")
  RuleApproval convertToRuleApproval(
      Rule rule, ApprovalStatus approvalStatus, ApprovalType approvalType);

  @Mapping(target = "ruleApprovalId", source = "ruleApprovalId")
  RuleBonusApproval convertToRuleBonusApproval(RuleBonus ruleBonus, Long ruleApprovalId);

  @Mapping(target = "ruleApprovalId", source = "ruleApprovalId")
  RuleConditionApproval convertToRuleConditionApproval(
      RuleCondition ruleCondition, Long ruleApprovalId);

  @Mapping(target = "ruleApprovalId", source = "ruleApprovalId")
  RuleAllocationApproval convertToRuleAllocationApproval(
      RuleAllocation ruleAllocation, Long ruleApprovalId);

  @Mapping(target = "ruleApprovalId", source = "ruleApprovalId")
  RuleConditionApproval convertToRuleConditionApproval(
      CreateRuleInput.RuleConditionInput ruleCondition, Long ruleApprovalId);

  @Mapping(target = "ruleApprovalId", source = "ruleApprovalId")
  RuleAllocationApproval convertToRuleAllocationApproval(
      CreateRuleInput.RuleAllocationInput ruleAllocation, Long ruleApprovalId);

  @Mapping(target = "fromDate", dateFormat = DateConstant.STR_PLAN_DD_MM_YYYY)
  @Mapping(target = "toDate", dateFormat = DateConstant.STR_PLAN_DD_MM_YYYY)
  @Mapping(target = "ruleApprovalId", source = "ruleApprovalId")
  RuleBonusApproval convertToRuleBonusApproval(
      CreateRuleInput.RuleBonusInput ruleBonus, Long ruleApprovalId);

  List<CampaignOutput> convertToCampaignOutput(List<Campaign> campaigns);
}
