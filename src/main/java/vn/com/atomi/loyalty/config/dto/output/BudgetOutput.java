package vn.com.atomi.loyalty.config.dto.output;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.com.atomi.loyalty.config.enums.Status;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BudgetOutput {
  @Schema(description = "ID bản ghi")
  private String id;

  @Schema(description = "Tên ngân sách")
  private String name;

  @Schema(description = "Số quyết định")
  private String decisionNumber;

  @Schema(description = "Tổng số ngân sách")
  private String totalBudget;

  @Schema(description = "Ngày bắt đầu hiệu lực")
  private LocalDate startDate;

  @Schema(description = "Ngày kết thúc hiệu lực")
  private LocalDate endDate;

  @Schema(description = "Trạng thái")
  private Status status;
}
