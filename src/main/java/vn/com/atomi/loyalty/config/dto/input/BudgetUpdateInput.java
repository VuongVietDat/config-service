package vn.com.atomi.loyalty.config.dto.input;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import vn.com.atomi.loyalty.config.enums.ApprovalStatus;
import vn.com.atomi.loyalty.config.enums.BudgetStatus;
import vn.com.atomi.loyalty.config.enums.Status;

@Setter
@Getter
public class BudgetUpdateInput {
  @Schema(description = "ID bản ghi chờ duyệt")
  @NotNull
  private Long id;

  @Schema(description = "Tên ngân sách")
  @NotNull
  @Size(max = 168, message = "String length must be less than or equal to 168")
  private String name;

  @Schema(description = "Tổng ngân sách")
  @NotNull
  @Max(value = 999999999999999999L, message = "String length must be less than or equal to 18")
  private Long totalBudget;

  @Schema(description = "Trạng thái")
  private BudgetStatus status;

  @Schema(description = "Trạng thái duyet")
  @NotNull
  private ApprovalStatus approvalStatus;
}
