package vn.com.atomi.loyalty.config.dto.output;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
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
public class RulePreviewOutput {

  @Schema(description = "ID bản ghi")
  private Long id;

  @Schema(description = "Loại qui tắc sinh điểm")
  private String type;

  @Schema(description = "Tên loại qui tắc sinh điểm")
  private String typeName;

  @Schema(description = "Mã qui tắc sinh điểm")
  private String code;

  @Schema(
      description =
          "Loại điểm:</br> ALL: Tất cả loại điẻm</br> RANK_POINT: Điểm xếp hạng</br> CONSUMPTION_POINT: Điểm tích lũy dùng để tiêu dùng")
  private PointType pointType;

  @Schema(description = "ID chiến dịch")
  private Long campaignId;

  @Schema(description = "Tên chiến dịch")
  private String campaignName;

  @Schema(description = "Ngày bắt đầu hiệu lực (dd-MM-yyyy HH:mm:ss)")
  @JsonFormat(pattern = DateConstant.STR_PLAN_DD_MM_YYYY_HH_MM_SS)
  private LocalDateTime startDate;

  @Schema(description = "Ngày kết thúc hiệu lực (dd-MM-yyyy HH:mm:ss)")
  @JsonFormat(pattern = DateConstant.STR_PLAN_DD_MM_YYYY_HH_MM_SS)
  private LocalDateTime endDate;

  @Schema(description = "Trạng thái:</br> ACTIVE: Hiệu lực</br> INACTIVE: Không hiệu lực")
  private Status status;

  @Schema(description = "Người tạo")
  private String creator;

  @Schema(description = "Ngày tạo")
  @JsonFormat(pattern = DateConstant.STR_PLAN_DD_MM_YYYY_HH_MM_SS)
  private LocalDateTime creationDate;

  @Schema(description = "Ngày duyệt tạo")
  @JsonFormat(pattern = DateConstant.STR_PLAN_DD_MM_YYYY_HH_MM_SS)
  private LocalDateTime creationApprovalDate;
}
