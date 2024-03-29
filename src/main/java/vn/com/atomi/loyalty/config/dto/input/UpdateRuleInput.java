package vn.com.atomi.loyalty.config.dto.input;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import vn.com.atomi.loyalty.base.annotations.DateTimeValidator;
import vn.com.atomi.loyalty.base.constant.DateConstant;
import vn.com.atomi.loyalty.config.enums.*;

/**
 * @author haidv
 * @version 1.0
 */
@Setter
@Getter
public class UpdateRuleInput {

  @NotBlank
  @Schema(description = "Tên quy tắc sinh điểm")
  @Size(max = 168)
  private String name;

  @Schema(description = "Ngày bắt đầu hiệu lực (dd/MM/yyyy)", example = "01/01/2024")
  @DateTimeValidator(pattern = DateConstant.STR_PLAN_DD_MM_YYYY_STROKE)
  private String startDate;

  @Schema(description = "Ngày kết thúc hiệu lực (dd/MM/yyyy)", example = "31/12/2024")
  @DateTimeValidator(required = false, pattern = DateConstant.STR_PLAN_DD_MM_YYYY_STROKE)
  private String endDate;

  @Schema(description = "Trạng thái:</br> ACTIVE: Hiệu lực</br> INACTIVE: Không hiệu lực")
  @NotNull
  private Status status;

  @Schema(
      description =
          "Loại chính sách hết hạn điểm:</br> AFTER_DAY: Sau số ngày</br> AFTER_DATE: Sau ngày</br> FIRST_DATE_OF_MONTH: Ngày đầu tiên của tháng thứ N +")
  private ExpirePolicyType expirePolicyType;

  @Schema(
      description =
          "Giá trị của chính sách hết hạn điểm:</br> AFTER_DAY: number</br> AFTER_DATE: dd/MM/yyyy</br> FIRST_DATE_OF_MONTH: number")
  private String expirePolicyValue;
}
