package vn.com.atomi.loyalty.config.dto.output;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.com.atomi.loyalty.config.enums.ApprovalStatus;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BudgetDetailOutput {
  @Schema(description = "ID bản ghi")
  private String id;

  @Schema(description = "Tên ngân sách")
  private String name;

  @Schema(description = "Số quyết định")
  private String decisionNumber;

  @Schema(description = "Tổng số ngân sách")
  private Long totalBudget;

  @Schema(description = "Ngày bắt đầu hiệu lực")
  private LocalDate startDate;

  @Schema(description = "Ngày kết thúc hiệu lực")
  private LocalDate endDate;

  @Schema(description = "Trạng thái")
  @Enumerated(EnumType.STRING)
  private String status;

  @Schema(description = "Trạng thái phê duyệt")
  @Enumerated(EnumType.STRING)
  private ApprovalStatus approvalStatus;

  @Schema(description = "Tổng số ngân sách đã phân bổ")
  private Long totalUnSpentBudget;

  @Schema(description = "Tổng số ngân sách chưa phân bổ")
  private Long totalSpentBudget;

  @Schema(description = "Ngày cập nhật gần nhất")
  private LocalDate updatedAt;

  @Schema(description = "Người thực hiện")
  private LocalDate createdAt;

  @Schema(description = "Tổng số điểm đã cộng")
  private int totalAllocationPoint;

  @Schema(description = "Tổng số điểm đã tiêu")
  private int totalPointsSpent;



}
