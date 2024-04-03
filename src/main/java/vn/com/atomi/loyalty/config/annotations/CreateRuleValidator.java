package vn.com.atomi.loyalty.config.annotations;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import vn.com.atomi.loyalty.base.constant.DateConstant;
import vn.com.atomi.loyalty.config.dto.input.CreateRuleInput;
import vn.com.atomi.loyalty.config.dto.input.RuleAllocationInput;
import vn.com.atomi.loyalty.config.dto.input.RuleBonusInput;
import vn.com.atomi.loyalty.config.dto.input.RuleConditionInput;
import vn.com.atomi.loyalty.config.enums.BonusType;
import vn.com.atomi.loyalty.config.enums.ExpirePolicyType;
import vn.com.atomi.loyalty.config.utils.Utils;

/**
 * @author haidv
 * @version 1.0
 */
@Target(TYPE)
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = CreateRuleValidator.Validator.class)
public @interface CreateRuleValidator {

  String message() default "{validation.constraints.CreateRuleValidator.message}";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};

  class Validator implements ConstraintValidator<CreateRuleValidator, CreateRuleInput> {

    @SuppressWarnings("unused")
    private CreateRuleValidator annotation;

    @Override
    public void initialize(CreateRuleValidator annotation) {
      this.annotation = annotation;
    }

    @Override
    public boolean isValid(CreateRuleInput value, ConstraintValidatorContext context) {
      LocalDate startDateRule = null;
      LocalDate endDateRule = null;
      boolean isValid = true;
      var ruleConditionInputs = value.getRuleConditionInputs();
      if (!CollectionUtils.isEmpty(ruleConditionInputs)) {
        value.setRuleConditionInputs(
            ruleConditionInputs.stream()
                .filter(
                    v ->
                        v.getOperators() != null
                            || StringUtils.isNotBlank(v.getValue())
                            || v.getProperties() != null)
                .toList());
      }
      var ruleBonus = value.getRuleBonusInputs();
      if (!CollectionUtils.isEmpty(ruleBonus)) {
        value.setRuleBonusInputs(
            ruleBonus.stream()
                .filter(
                    v ->
                        v.getValue() != null
                            || StringUtils.isNotBlank(v.getChildCondition())
                            || StringUtils.isNotBlank(v.getCondition())
                            || v.getType() != null)
                .toList());
      }
      if (StringUtils.isBlank(value.getStartDate())) {
        context.disableDefaultConstraintViolation();
        context
            .buildConstraintViolationWithTemplate("must not be blank")
            .addPropertyNode("startDate")
            .addConstraintViolation();
        isValid = false;
      } else {
        try {
          startDateRule = Utils.convertToLocalDate(value.getStartDate());
        } catch (DateTimeParseException e) {
          context.disableDefaultConstraintViolation();
          context
              .buildConstraintViolationWithTemplate(
                  String.format(
                      "Text '%s' could not be match format %s",
                      value.getStartDate(), DateConstant.STR_PLAN_DD_MM_YYYY_STROKE))
              .addPropertyNode("startDate")
              .addConstraintViolation();
          isValid = false;
        }
      }
      if (!StringUtils.isBlank(value.getEndDate())) {
        try {
          endDateRule = Utils.convertToLocalDate(value.getEndDate());
          if (startDateRule != null && startDateRule.isAfter(endDateRule)) {
            context.disableDefaultConstraintViolation();
            context
                .buildConstraintViolationWithTemplate("endDate must be after startDate")
                .addPropertyNode("endDate")
                .addConstraintViolation();
            isValid = false;
          }
        } catch (DateTimeParseException e) {
          context.disableDefaultConstraintViolation();
          context
              .buildConstraintViolationWithTemplate(
                  String.format(
                      "Text '%s' could not be match format %s",
                      value.getEndDate(), DateConstant.STR_PLAN_DD_MM_YYYY_STROKE))
              .addPropertyNode("endDate")
              .addConstraintViolation();
          isValid = false;
        }
      }
      if (value.getExpirePolicyType() != null) {
        if (ExpirePolicyType.FIRST_DATE_OF_MONTH.equals(value.getExpirePolicyType())
            || ExpirePolicyType.AFTER_DAY.equals(value.getExpirePolicyType())) {
          try {
            Long.parseLong(value.getExpirePolicyValue());
          } catch (Exception e) {
            context.disableDefaultConstraintViolation();
            context
                .buildConstraintViolationWithTemplate(
                    String.format(
                        "Text '%s' could not be match style number", value.getExpirePolicyValue()))
                .addPropertyNode("expirePolicyValue")
                .addConstraintViolation();
            isValid = false;
          }
        }
        if (ExpirePolicyType.AFTER_DATE.equals(value.getExpirePolicyType())) {
          context.disableDefaultConstraintViolation();
          try {
            Utils.LOCAL_DATE_FORMATTER.parse(value.getExpirePolicyValue());
          } catch (DateTimeParseException e) {
            context.disableDefaultConstraintViolation();
            context
                .buildConstraintViolationWithTemplate(
                    String.format(
                        "Text '%s' could not be match format %s",
                        value.getExpirePolicyValue(), DateConstant.STR_PLAN_DD_MM_YYYY_STROKE))
                .addPropertyNode("expirePolicyValue")
                .addConstraintViolation();
            isValid = false;
          }
        }
        if (ExpirePolicyType.NEVER.equals(value.getExpirePolicyType())) {
          value.setExpirePolicyValue(null);
        }
      }
      if (!CollectionUtils.isEmpty(value.getAllocationInputs())) {
        for (int i = 0; i < value.getAllocationInputs().size(); i++) {
          RuleAllocationInput allocationInput = value.getAllocationInputs().get(i);
          if (allocationInput.getExchangePoint() != null
              && allocationInput.getExchangePoint() <= 0
              && allocationInput.getExchangeValue() != null
              && allocationInput.getExchangeValue() <= 0
              && allocationInput.getFixPointAmount() != null
              && allocationInput.getFixPointAmount() <= 0) {
            context
                .buildConstraintViolationWithTemplate("must be greater than or equal to 0")
                .addPropertyNode(String.format("allocationInputs[%s].fixPointAmount", i))
                .addConstraintViolation();
            isValid = false;
          }
          if (allocationInput.getExchangePoint() != null
              && allocationInput.getExchangePoint() > 0
              && allocationInput.getExchangeValue() != null
              && allocationInput.getExchangeValue() == 0) {
            context
                .buildConstraintViolationWithTemplate("must be greater than or equal to 0")
                .addPropertyNode(String.format("allocationInputs[%s].exchangeValue", i))
                .addConstraintViolation();
            isValid = false;
          }
          if (allocationInput.getExchangeValue() != null
              && allocationInput.getExchangeValue() > 0
              && allocationInput.getExchangePoint() != null
              && allocationInput.getExchangePoint() == 0) {
            context
                .buildConstraintViolationWithTemplate("must be greater than or equal to 0")
                .addPropertyNode(String.format("allocationInputs[%s].exchangePoint", i))
                .addConstraintViolation();
            isValid = false;
          }
          if (allocationInput.getExchangeValue() != null
              && allocationInput.getExchangeValue() > 0
              && allocationInput.getExchangePoint() != null
              && allocationInput.getExchangePoint() > 0
              && allocationInput.getFixPointAmount() != null
              && allocationInput.getFixPointAmount() > 0) {
            allocationInput.setExchangePoint(0L);
            allocationInput.setExchangeValue(0L);
          }
          if (allocationInput.getLimitEventPerUser() != null
              && allocationInput.getLimitEventPerUser() > 0
              && allocationInput.getFrequencyLimitEventPerUser() == null) {
            context
                .buildConstraintViolationWithTemplate("must not be null")
                .addPropertyNode(
                    String.format("allocationInputs[%s].frequencyLimitEventPerUser", i))
                .addConstraintViolation();
            isValid = false;
          }
          if (allocationInput.getTimeWait() != null
              && allocationInput.getTimeWait() > 0
              && allocationInput.getFrequencyTimeWait() == null) {
            context
                .buildConstraintViolationWithTemplate("must not be null")
                .addPropertyNode(String.format("allocationInputs[%s].frequencyTimeWait", i))
                .addConstraintViolation();
            isValid = false;
          }
        }
      }
      if (!CollectionUtils.isEmpty(value.getRuleBonusInputs())) {
        for (int i = 0; i < value.getRuleBonusInputs().size(); i++) {
          RuleBonusInput ruleBonusInput = value.getRuleBonusInputs().get(i);
          if (ruleBonusInput.getType() == null) {
            context
                .buildConstraintViolationWithTemplate("must not be null")
                .addPropertyNode(String.format("ruleBonusInputs[%s].type", i))
                .addConstraintViolation();
            isValid = false;
          } else {
            if (ruleBonusInput.getType().equals(BonusType.BONUS_RANK)
                || ruleBonusInput.getType().equals(BonusType.BONUS_PRODUCT)
                || ruleBonusInput.getType().equals(BonusType.BONUS_EXCEED_THRESHOLD)) {
              if (StringUtils.isBlank(ruleBonusInput.getCondition())) {
                context
                    .buildConstraintViolationWithTemplate("must not be blank")
                    .addPropertyNode(String.format("ruleBonusInputs[%s].condition", i))
                    .addConstraintViolation();
                isValid = false;
              }
            }
            if (ruleBonusInput.getType().equals(BonusType.BONUS_EXCEED_THRESHOLD)
                && !StringUtils.isBlank(ruleBonusInput.getCondition())) {
              try {
                Long.parseLong(ruleBonusInput.getCondition());
              } catch (Exception e) {
                context.disableDefaultConstraintViolation();
                context
                    .buildConstraintViolationWithTemplate(
                        String.format(
                            "Text '%s' could not be match style number",
                            ruleBonusInput.getCondition()))
                    .addPropertyNode(String.format("ruleBonusInputs[%s].condition", i))
                    .addConstraintViolation();
                isValid = false;
              }
            }
            if (ruleBonusInput.getType().equals(BonusType.BONUS_SPECIAL_DATE)) {
              if (StringUtils.isBlank(ruleBonusInput.getCondition())) {
                context.disableDefaultConstraintViolation();
                context
                    .buildConstraintViolationWithTemplate("must not be blank")
                    .addPropertyNode(String.format("ruleBonusInputs[%s].condition", i))
                    .addConstraintViolation();
                isValid = false;
              }
              if (StringUtils.isBlank(ruleBonusInput.getChildCondition())) {
                context.disableDefaultConstraintViolation();
                context
                    .buildConstraintViolationWithTemplate("must not be blank")
                    .addPropertyNode(String.format("ruleBonusInputs[%s].childCondition", i))
                    .addConstraintViolation();
                isValid = false;
              }
              if (!StringUtils.isBlank(ruleBonusInput.getChildCondition())
                  && !StringUtils.isBlank(ruleBonusInput.getCondition())) {
                LocalDate fromDate = null;
                try {
                  fromDate = Utils.convertToLocalDate(ruleBonusInput.getCondition());
                } catch (DateTimeParseException e) {
                  context.disableDefaultConstraintViolation();
                  context
                      .buildConstraintViolationWithTemplate(
                          String.format(
                              "Text '%s' could not be match format %s",
                              ruleBonusInput.getCondition(),
                              DateConstant.STR_PLAN_DD_MM_YYYY_STROKE))
                      .addPropertyNode(String.format("ruleBonusInputs[%s].condition", i))
                      .addConstraintViolation();
                  isValid = false;
                }
                LocalDate toDate = null;
                try {
                  toDate = Utils.convertToLocalDate(ruleBonusInput.getChildCondition());
                } catch (DateTimeParseException e) {
                  context.disableDefaultConstraintViolation();
                  context
                      .buildConstraintViolationWithTemplate(
                          String.format(
                              "Text '%s' could not be match format %s",
                              ruleBonusInput.getChildCondition(),
                              DateConstant.STR_PLAN_DD_MM_YYYY_STROKE))
                      .addPropertyNode(String.format("ruleBonusInputs[%s].childCondition", i))
                      .addConstraintViolation();
                  isValid = false;
                }
                if (fromDate != null && toDate != null && fromDate.isAfter(toDate)) {
                  context.disableDefaultConstraintViolation();
                  context
                      .buildConstraintViolationWithTemplate("toDate must be after fromDate")
                      .addPropertyNode(String.format("ruleBonusInputs[%s].childCondition", i))
                      .addConstraintViolation();
                  isValid = false;
                }
                if (fromDate != null && startDateRule != null && fromDate.isBefore(startDateRule)) {
                  context
                      .buildConstraintViolationWithTemplate("fromDate must be after startDate")
                      .addPropertyNode(String.format("ruleBonusInputs[%s].condition", i))
                      .addConstraintViolation();
                  isValid = false;
                }
                if (fromDate != null && endDateRule != null && fromDate.isAfter(endDateRule)) {
                  context
                      .buildConstraintViolationWithTemplate("fromDate must be before endDate")
                      .addPropertyNode(String.format("ruleBonusInputs[%s].condition", i))
                      .addConstraintViolation();
                  isValid = false;
                }
                if (toDate != null && startDateRule != null && toDate.isBefore(startDateRule)) {
                  context
                      .buildConstraintViolationWithTemplate("toDate must be after fromDate")
                      .addPropertyNode(String.format("ruleBonusInputs[%s].childCondition", i))
                      .addConstraintViolation();
                  isValid = false;
                }
                if (toDate != null && endDateRule != null && toDate.isAfter(endDateRule)) {
                  context
                      .buildConstraintViolationWithTemplate("toDate must be before endDate")
                      .addPropertyNode(String.format("ruleBonusInputs[%s].childCondition", i))
                      .addConstraintViolation();
                  isValid = false;
                }
              }
            }
          }
          if (ruleBonusInput.getValue() == null || ruleBonusInput.getValue() <= 0) {
            context
                .buildConstraintViolationWithTemplate("must be greater than to 0")
                .addPropertyNode(String.format("ruleBonusInputs[%s].value", i))
                .addConstraintViolation();
            isValid = false;
          }
          if (ruleBonusInput.getPlusType() == null) {
            context
                .buildConstraintViolationWithTemplate("must not be null")
                .addPropertyNode(String.format("ruleBonusInputs[%s].plusType", i))
                .addConstraintViolation();
            isValid = false;
          }
        }
      }
      for (int i = 0; i < value.getRuleConditionInputs().size(); i++) {
        RuleConditionInput ruleConditionInput = value.getRuleConditionInputs().get(i);
        if (ruleConditionInput.getProperties() == null) {
          context.disableDefaultConstraintViolation();
          context
              .buildConstraintViolationWithTemplate("must not be blank")
              .addPropertyNode(String.format("ruleConditionInputs[%s].properties", i))
              .addConstraintViolation();
          isValid = false;
        }
        if (StringUtils.isBlank(ruleConditionInput.getValue())) {
          context.disableDefaultConstraintViolation();
          context
              .buildConstraintViolationWithTemplate("must not be blank")
              .addPropertyNode(String.format("ruleConditionInputs[%s].value", i))
              .addConstraintViolation();
          isValid = false;
        }
        if (ruleConditionInput.getOperators() == null) {
          context.disableDefaultConstraintViolation();
          context
              .buildConstraintViolationWithTemplate("must not be null")
              .addPropertyNode(String.format("ruleConditionInputs[%s].operators", i))
              .addConstraintViolation();
          isValid = false;
        }
      }
      return isValid;
    }
  }
}
