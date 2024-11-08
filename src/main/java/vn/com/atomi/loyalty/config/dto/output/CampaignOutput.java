package vn.com.atomi.loyalty.config.dto.output;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.*;
import vn.com.atomi.loyalty.base.constant.DateConstant;
import vn.com.atomi.loyalty.config.enums.ApprovalStatus;
import vn.com.atomi.loyalty.config.enums.Status;

/**
 * @author haidv
 * @version 1.0
 */
@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class CampaignOutput {

  @Schema(description = "ID chiến dịch")
  private Long id;

  @Schema(description = "Tên chiến dịch")
  private String name;

  @Schema(description = "Trạng thái:</br> ACTIVE: Hiệu lực</br> INACTIVE: Không hiệu lực")
  private Status status;

  @Schema(description = "Mã chiến dịch")
  private String code;

  @Schema(description = "Ngày bắt đầu hiệu lực (dd/MM/yyyy)", example = "01/01/2024")
  @JsonFormat(pattern = DateConstant.STR_PLAN_DD_MM_YYYY_STROKE)
  private LocalDate startDate;

  @Schema(description = "Ngày kết thúc hiệu lực (dd/MM/yyyy)", example = "31/12/2024")
  @JsonFormat(pattern = DateConstant.STR_PLAN_DD_MM_YYYY_STROKE)
  private LocalDate endDate;

  @Schema(description = "Người tạo")
  private String creator;

  @Schema(description = "Người tạo created")
  private String createdBy;

  @Schema(description = "Người cập nhật")
  private String updatedBy;

  @Schema(description = "Ghi chú")
  private String description;

  @Schema(description = "Thời gian tạo (dd/MM/yyyy HH:mm:ss)")
  @JsonFormat(pattern = DateConstant.STR_PLAN_DD_MM_YYYY_HH_MM_SS_STROKE)
  private LocalDateTime createdAt;

  @Schema(description = "Thời gian chỉnh sửa gần nhất (dd/MM/yyyy HH:mm:ss)")
  @JsonFormat(pattern = DateConstant.STR_PLAN_DD_MM_YYYY_HH_MM_SS_STROKE)
  private LocalDateTime updatedAt;

  @Schema(description = "Ngày tạo (dd/MM/yyyy HH:mm:ss)")
  @JsonFormat(pattern = DateConstant.STR_PLAN_DD_MM_YYYY_HH_MM_SS_STROKE)
  private LocalDateTime creationDate;

  @Schema(description = "Ngày duyệt tạo (dd/MM/yyyy HH:mm:ss)")
  @JsonFormat(pattern = DateConstant.STR_PLAN_DD_MM_YYYY_HH_MM_SS_STROKE)
  private LocalDateTime creationApprovalDate;

//  @Schema(description = "Tổng ngân sách")
//  private LocalDateTime creationApprovalDate;

  @Schema(description = "Ngân sách chiến dịch")
  private Long budgetAmount;

  @Schema(description = "Ngân sách nguon")
  private Long totalBudget;

  @Schema(description = "ID ngân sách")
  private Long budgetId;

  @Schema(description = "Tổng số điểm đã cộng")
  private long totalAllocationPoint;
  @Schema(description = "Tổng số điểm đã tiêu")
  private long totalPointsSpent;
  @Schema(description = "Trạng thái phê duyệt")
  private ApprovalStatus approvalStatus;

}
