package vn.com.atomi.loyalty.config.dto.input;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;
import vn.com.atomi.loyalty.config.enums.BonusType;
import vn.com.atomi.loyalty.config.enums.PlusType;

/**
 * @author haidv
 * @version 1.0
 */
@Setter
@Getter
public class RuleBonusInput {

  @Schema(description = "Loại thưởng thêm")
  private BonusType type;

  @Schema(description = "Giá trị thưởng")
  @Min(0)
  @Max(value = 99999999999999L)
  private Double value;

  @Schema(
      description =
          "Loại giá trị thưởng:</br> PERCENTAGE: Phần trăm base điểm</br> FIX: Số điểm cụ thể")
  private PlusType plusType;

  @Schema(description = "Điều kiện cha nhận thưởng thêm")
  private String condition;

  @Schema(description = "Điều kiện con nhận thưởng thêm")
  private String childCondition;
}
