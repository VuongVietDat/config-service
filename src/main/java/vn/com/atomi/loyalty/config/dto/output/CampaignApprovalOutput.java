package vn.com.atomi.loyalty.config.dto.output;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.*;
import vn.com.atomi.loyalty.base.constant.DateConstant;
import vn.com.atomi.loyalty.config.enums.ApprovalStatus;
import vn.com.atomi.loyalty.config.enums.ApprovalType;
import vn.com.atomi.loyalty.config.enums.Status;

/**
 * @author haidv
 * @version 1.0
 */
@Builder
@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CampaignApprovalOutput {

  @Schema(description = "ID bản ghi")
  private Long id;

  @Schema(description = "ID chiến dịch")
  private Long campaignId;

  @Schema(description = "Tên chiến dịch")
  private String name;

  @Schema(description = "Mã chiến dịch")
  private String code;

  @Schema(description = "Mô tả")
  private String description;

  @Schema(description = "ID nhóm khách hàng")
  private Long customerGroupId;

  @Schema(description = "Tên nhóm khách hàng")
  private String customerGroupName;

  @Schema(description = "Trạng thái:</br> ACTIVE: Hiệu lực</br> INACTIVE: Không hiệu lực")
  private Status status;

  @Schema(description = "Ngày bắt đầu hiệu lực (dd/MM/yyyy)", example = "01/01/2024")
  @JsonFormat(pattern = DateConstant.STR_PLAN_DD_MM_YYYY_STROKE)
  private LocalDate startDate;

  @Schema(description = "Ngày kết thúc hiệu lực (dd/MM/yyyy)", example = "31/12/2024")
  @JsonFormat(pattern = DateConstant.STR_PLAN_DD_MM_YYYY_STROKE)
  private LocalDate endDate;

  @Schema(description = "Người tạo")
  private String creator;

  @Schema(description = "Ngày tạo")
  private LocalDateTime creationDate;

  @Schema(
      description =
          "Trạng thái phê duyệt:</br> WAITING: Chờ duyệt</br> ACCEPTED: Đồng ý</br> REJECTED: Từ chối</br> RECALL: Thu hồi")
  private ApprovalStatus approvalStatus;

  @Schema(
      description =
          "Loại phê duyệt: </br>CREATE: Phê duyệt tạo</br>UPDATE: Phê duyệt cập nhật</br>CANCEL: Phê duyệt hủy bỏ")
  private ApprovalType approvalType;

  @Schema(description = "Lý do đồng ý hoặc từ chối")
  private String approvalComment;

  @Schema(description = "Người duyệt")
  private String approver;

  @Schema(description = "Ngân sách chiến dịch")
  private Long budgetAmount;

  @Schema(description = "Ngân sách nguồn")
  private Long totalBudget;

  @Schema(description = "ID ngân sách")
  private Long budgetId;
}
