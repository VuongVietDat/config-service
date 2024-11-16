package vn.com.atomi.loyalty.config.dto.input;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import vn.com.atomi.loyalty.config.enums.ApprovalStatus;
import vn.com.atomi.loyalty.config.enums.BudgetStatus;
import vn.com.atomi.loyalty.config.enums.Status;

@Setter
@Getter
public class BudgetInput {
  @Schema(description = "ID bản ghi chờ duyệt")
  @NotNull
  private Long id;

  @Schema(description = "Số quyết định")
  @NotNull
  @Size(max = 64, message = "String length must be less than or equal to 64")
  private String decisionNumber;

  @Schema(description = "Tên ngân sách")
  @NotNull
  @Size(max = 168, message = "String length must be less than or equal to 168")
//  @Pattern(regexp = "^[a-zA-Z0-9 ]*$", message = "Budget name must not contain special characters or HTML tags")
  private String name;

  @Schema(description = "Tổng ngân sách")
  @NotNull
  @Max(value = 999999999999999999L, message = "String length must be less than or equal to 18")
  private Long totalBudget;

  @Schema(description = "Ngày bắt đầu thời gian hiệu lực")
  @NotNull
  private String startDate;

  @Schema(description = "Ngày kết thúc thời gian hiệu lực")
  @NotNull
  private String endDate;

  @Schema(description = "Trạng thái")
  private BudgetStatus status;

  @Schema(description = "Trạng thái duyet")
  @NotNull
  private ApprovalStatus approvalStatus;
}
