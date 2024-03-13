package vn.com.atomi.loyalty.base.annotations;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.time.format.DateTimeParseException;
import vn.com.atomi.loyalty.config.dto.input.IDateInput;
import vn.com.atomi.loyalty.config.utils.Utils;

/**
 * @author haidv
 * @version 1.0
 */
@Target({TYPE})
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = DateRangeValidator.Validator.class)
public @interface DateRangeValidator {
  String message() default "endDate không được nhỏ hơn startDate";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};

  class Validator implements ConstraintValidator<DateRangeValidator, IDateInput> {
    DateRangeValidator annotation;

    @Override
    public void initialize(DateRangeValidator constraintAnnotation) {
      annotation = constraintAnnotation;
    }

    @Override
    public boolean isValid(IDateInput value, ConstraintValidatorContext context) {
      try { // đã check @DateTimeValidator trước
        var success =
            value.getEndDate() == null
                || Utils.convertToLocalDate(value.getEndDate())
                    .isAfter(Utils.convertToLocalDate(value.getStartDate()));
        if (!success) {
          context.disableDefaultConstraintViolation();
          context
              .buildConstraintViolationWithTemplate(annotation.message())
              .addConstraintViolation();
        }
        return success;
      } catch (DateTimeParseException e) {
        return true;
      }
    }
  }
}
