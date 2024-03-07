package vn.com.atomi.loyalty.config.dto.output;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.*;
import vn.com.atomi.loyalty.base.constant.DateConstant;
import vn.com.atomi.loyalty.config.enums.*;

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
public class RuleApprovalPreviewOutput {

  @Schema(description = "ID bản ghi")
  private Long id;

  @Schema(description = "Loại qui tắc sinh điểm")
  private String type;

  @Schema(description = "Tên loại qui tắc sinh điểm")
  private String typeName;

  @Schema(description = "Mã qui tắc sinh điểm")
  private String code;

  @Schema(description = "Tên quy tắc sinh điểm")
  private String name;

  @Schema(
      description =
          "Loại điểm:</br> ALL: Tất cả loại điẻm</br> RANK_POINT: Điểm xếp hạng</br> CONSUMPTION_POINT: Điểm tích lũy dùng để tiêu dùng")
  private PointType pointType;

  @Schema(description = "ID chiến dịch")
  private Long campaignId;

  @Schema(description = "Tên chiến dịch")
  private String campaignName;

  @Schema(description = "Ngày bắt đầu hiệu lực (dd-MM-yyyy)")
  @JsonFormat(pattern = DateConstant.STR_PLAN_DD_MM_YYYY)
  private LocalDate startDate;

  @Schema(description = "Ngày kết thúc hiệu lực (dd-MM-yyyy)")
  @JsonFormat(pattern = DateConstant.STR_PLAN_DD_MM_YYYY)
  private LocalDate endDate;

  @Schema(description = "Trạng thái:</br> ACTIVE: Hiệu lực</br> INACTIVE: Không hiệu lực")
  private Status status;

  @Schema(
      description =
          "Trạng thái phê duyệt:</br> WAITING: Chờ duyệt</br> ACCEPTED: Đồng ý</br> REJECTED: Từ chối</br> RECALL: Thu hồi")
  private ApprovalStatus approvalStatus;

  @Schema(description = "Người tạo")
  private String creator;

  @Schema(description = "Ngày tạo")
  private LocalDateTime creationDate;

  @Schema(description = "Ngày duyệt")
  private LocalDateTime approveDate;

  @Schema(
      description =
          "Loại phê duyệt: </br>CREATE: Phê duyệt tạo</br>UPDATE: Phê duyệt cập nhật</br>CANCEL: Phê duyệt hủy bỏ")
  private ApprovalType approvalType;

  @Schema(description = "Người duyệt")
  private String approver;
}
