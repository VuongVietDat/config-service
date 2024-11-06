package vn.com.atomi.loyalty.config.dto.output;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.com.atomi.loyalty.config.enums.ApprovalStatus;
import vn.com.atomi.loyalty.config.enums.ApprovalType;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BudgetDetailOutput {
  @Schema(description = "ID bản ghi")
  private String id;

  @Schema(description = "ID bản ghi ngân sách chờ duyệt ")
  private Long approvalId;

  @Schema(description = "Tên ngân sách")
  private String name;

  @Schema(description = "Số quyết định")
  private String decisionNumber;

  @Schema(description = "Tổng số ngân sách")
  private Long totalBudget;

  @Schema(description = "Tạo bởi")
  private String createdBy;

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

  @Schema(
          description =
                  "Loại phê duyệt: </br>CREATE: Phê duyệt tạo</br>UPDATE: Phê duyệt cập nhật</br>CANCEL: Phê duyệt hủy bỏ")
  private ApprovalType approvalType;

  @Schema(description = "Tổng số ngân sách đã phân bổ")
  private int totalUnSpentBudget;

  @Schema(description = "Tổng số ngân sách chưa phân bổ")
  private int totalSpentBudget;

  @Schema(description = "Ngày cập nhật gần nhất")
  private LocalDate updatedAt;

  @Schema(description = "Người thực hiện")
  private LocalDate createdAt;

  @Schema(description = "Tổng số điểm đã cộng")
  private long totalPointAdded;

  @Schema(description = "Tổng số điểm đã tiêu")
  private long totalPointsSpent;

  @Schema(description = "Lịch sử phê duyệt")
  private List<HistoryOutput> historyOutputs;

}
