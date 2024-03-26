package vn.com.atomi.loyalty.config.dto.input;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import vn.com.atomi.loyalty.base.annotations.DateTimeValidator;
import vn.com.atomi.loyalty.base.constant.DateConstant;

/**
 * @author haidv
 * @version 1.0
 */
@Setter
@Getter
public class WarringOverlapActiveTimeInput {

  @Schema(description = "Loại quy tắc sinh điểm")
  @NotBlank
  private String type;

  @Schema(description = "ID chiến dịch")
  @NotNull
  private Long campaignId;

  @Schema(description = "Ngày bắt đầu hiệu lực (dd/MM/yyyy)", example = "01/01/2024")
  @DateTimeValidator(pattern = DateConstant.STR_PLAN_DD_MM_YYYY_STROKE)
  private String startDate;

  @Schema(description = "Ngày kết thúc hiệu lực (dd/MM/yyyy)", example = "31/12/2024")
  @DateTimeValidator(required = false, pattern = DateConstant.STR_PLAN_DD_MM_YYYY_STROKE)
  private String endDate;

  @Schema(description = "Điều kiện áp dụng quy tắc")
  private RuleConditionInput ruleConditionInput;
}
