package vn.com.atomi.loyalty.config.dto.input;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import vn.com.atomi.loyalty.base.annotations.DateTimeValidator;
import vn.com.atomi.loyalty.config.enums.Status;

/**
 * @author haidv
 * @version 1.0
 */
@Setter
@Getter
public class CampaignInput {
  @Schema(description = "Tên chiến dịch")
  @NotBlank
  @Size(max = 168)
  private String name;

  @Schema(description = "Mô tả")
  @NotBlank
  private String description;

  @Schema(description = "Nhóm khách hàng áp dụng")
  @NotBlank
  private String customerGroup;

  @Schema(description = "Ngày bắt đầu hiệu lực (dd/MM/yyyy)")
  @DateTimeValidator
  private String startDate;

  @Schema(description = "Ngày kết thúc hiệu lực (dd/MM/yyyy)")
  @DateTimeValidator(required = false)
  private String endDate;

  @Schema(description = "Trạng thái:</br> ACTIVE: Hiệu lực</br> INACTIVE: Không hiệu lực")
  @NotNull
  private Status status;
}
