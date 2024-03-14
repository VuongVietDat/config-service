package vn.com.atomi.loyalty.config.dto.input;

import vn.com.atomi.loyalty.base.annotations.DateRangeValidator;

@DateRangeValidator
public interface IDateInput {
  String getStartDate();

  String getEndDate();
}
