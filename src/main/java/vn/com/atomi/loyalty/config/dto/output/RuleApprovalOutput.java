package vn.com.atomi.loyalty.config.dto.output;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import java.time.LocalDate;
import java.util.List;
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
public class RuleApprovalOutput {

  @Schema(description = "ID bản ghi")
  private Long id;

  @Schema(description = "Loại quy tắc sinh điểm")
  private String type;

  @Schema(description = "Mã quy tắc sinh điểm")
  private String code;

  @Schema(description = "Tên quy tắc sinh điểm")
  private String name;

  @Schema(
      description =
          "Loại điểm:</br> ALL: Tất cả loại điẻm</br> RANK_POINT: Điểm xếp hạng</br> CONSUMPTION_POINT: Điểm tích lũy dùng để tiêu dùng")
  private PointType pointType;

  @Schema(description = "ID chiến dịch")
  private Long campaignId;

  @Schema(description = "Mã chiến dịch")
  private String campaignCode;

  @Deprecated
  @Schema(
      description =
          "Loại điều kiện:</br> ALL_MATCH: Tất cả điều kiện thỏa mãn</br> ANY_MATCH: Bất kỳ một điều kiện thỏa mãn")
  private ConditionType conditionType;

  @Schema(description = "Ngày bắt đầu hiệu lực (dd/MM/yyyy)", example = "01/01/2024")
  @JsonFormat(pattern = DateConstant.STR_PLAN_DD_MM_YYYY_STROKE)
  private LocalDate startDate;

  @Schema(description = "Ngày kết thúc hiệu lực (dd/MM/yyyy)", example = "31/12/2024")
  @JsonFormat(pattern = DateConstant.STR_PLAN_DD_MM_YYYY_STROKE)
  private LocalDate endDate;

  @Schema(description = "Trạng thái:</br> ACTIVE: Hiệu lực</br> INACTIVE: Không hiệu lực")
  private Status status;

  @Schema(
      description =
          "Trạng thái phê duyệt:</br> WAITING: Chờ duyệt</br> ACCEPTED: Đồng ý</br> REJECTED: Từ chối</br> RECALL: Thu hồi")
  private ApprovalStatus approvalStatus;

  @Schema(
      description =
          "Loại chính sách hết hạn điểm:</br> AFTER_DAY: Sau số ngày</br> AFTER_DATE: Sau ngày</br> FIRST_DATE_OF_MONTH: Ngày đầu tiên của tháng thứ N +")
  private ExpirePolicyType expirePolicyType;

  @Schema(
      description =
          "Giá trị của chính sách hết hạn điểm:</br> AFTER_DAY: number</br> AFTER_DATE: dd/MM/yyyy</br> FIRST_DATE_OF_MONTH: number")
  private String expirePolicyValue;

  @Schema(
      description =
          "Loại phê duyệt: </br>CREATE: Phê duyệt tạo</br>UPDATE: Phê duyệt cập nhật</br>CANCEL: Phê duyệt hủy bỏ")
  private ApprovalType approvalType;

  @Schema(description = "Quy tắc chung phân bổ điểm")
  @NotEmpty
  private List<RuleAllocationOutput> ruleAllocationApprovalOutputs;

  @Schema(description = "Quy tắc tặng thêm điểm")
  private List<RuleBonusOutput> ruleBonusApprovalOutputs;

  @Schema(description = "Điều kiện áp dụng quy tắc")
  private List<RuleConditionOutput> ruleConditionApprovalOutputs;

  @Schema(description = "Lịch sử phê duyệt")
  private List<HistoryOutput> historyOutputs;
}
