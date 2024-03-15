package vn.com.atomi.loyalty.config.dto.output;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
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
public class RuleOutput {

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
          "Loại chính sách hết hạn điểm:</br> AFTER_DAY: Sau số ngày</br> AFTER_DATE: Sau ngày</br> FIRST_DATE_OF_MONTH: Ngày đầu tiên của tháng thứ N +")
  private ExpirePolicyType expirePolicyType;

  @Schema(
      description =
          "Giá trị của chính sách hết hạn điểm:</br> AFTER_DAY: number</br> AFTER_DATE: dd/MM/yyyy</br> FIRST_DATE_OF_MONTH: number")
  private String expirePolicyValue;

  @Schema(description = "Quy tắc chung phân bổ điểm")
  @NotEmpty
  private List<RuleAllocationOutput> ruleAllocationOutputs;

  @Schema(description = "Quy tắc tặng thêm điểm")
  private List<RuleBonusOutput> ruleBonusOutputs;

  @Schema(description = "Điều kiện áp dụng quy tắc")
  private List<RuleConditionOutput> ruleConditionOutputs;

  @Schema(description = "Lịch sử phê duyệt")
  private List<HistoryOutput> historyOutputs;

  @Setter
  @Getter
  public static class RuleAllocationOutput {

    @Schema(description = "ID bản ghi")
    private Long id;

    @Schema(description = "true = Quy đổi theo giá trị giao dịch / false = Quy đổi theo số lần giao dịch")
    @NotNull
    private Boolean isExchangeByValue;

    @Schema(description = "Số điểm cố định")
    private long fixPointAmount;

    @Schema(description = "Giá trị quy đổi (VND)")
    private long exchangeValue;

    @Schema(description = "Giá trị điểm")
    private long exchangePoint;

    @Schema(description = "Giá trị giao dịch tối thiểu")
    private long minTransaction;

    @Schema(
        description = "Tích điểm với số tiền thực khách hàng thanh toán (sau khi trừ khuyến mãi)")
    private Boolean isNetValue;

    @Schema(description = "Giới hạn số điểm tối đa phân bổ trên một giao dịch")
    private long limitPointPerTransaction;

    @Schema(description = "Giới hạn số điểm tối đa phân bổ trên một khách hàng")
    private long limitPointPerUser;

    @Schema(
        description =
            "Tần suất giới hạn số điểm tối đa phân bổ trên một khách hàng:</br> MINUTE: Phút</br> HOURS: Giờ</br> DAY: Ngày</br> WEEK: Tuần</br> MONTH: Tháng</br> YEAR: Năm")
    private Frequency frequencyLimitPointPerUser;

    @Schema(description = "Giới hạn số lần tối đa phân bổ trên một khách hàng")
    private long limitEventPerUser;

    @Schema(
        description =
            "Tần suất giới hạn số lần tối đa phân bổ trên một khách hàng:</br> MINUTE: Phút</br> HOURS: Giờ</br> DAY: Ngày</br> WEEK: Tuần</br> MONTH: Tháng</br> YEAR: Năm")
    private Frequency frequencyLimitEventPerUser;

    @Schema(description = "Thời gian chờ giữa 2 lần")
    private long timeWait;

    @Schema(
        description =
            "Đơn vị thời gian chờ giữa 2 lần:</br> MINUTE: Phút</br>HOURS: Giờ</br> DAY: Ngày</br> WEEK: Tuần</br> MONTH: Tháng</br> YEAR: Năm")
    private Frequency frequencyTimeWait;
  }

  @Setter
  @Getter
  public static class RuleBonusOutput {

    @Schema(description = "ID bản ghi")
    private Long id;

    @Schema(description = "Loại thưởng thêm")
    private String type;

    @Schema(description = "Giá trị thưởng")
    private String value;

    @Schema(
        description =
            "Loại giá trị thưởng:</br> PERCENTAGE: Phần trăm base điểm</br> FIX: Số điểm cụ thể")
    private PlusType plusType;

    @Schema(description = "Từ ngày (dd/MM/yyyy)")
    @JsonFormat(pattern = DateConstant.STR_PLAN_DD_MM_YYYY_STROKE)
    private LocalDate fromDate;

    @Schema(description = "Đến ngày (dd/MM/yyyy)")
    @JsonFormat(pattern = DateConstant.STR_PLAN_DD_MM_YYYY_STROKE)
    private LocalDate toDate;

    @Schema(description = "Điều kiện cha nhận thưởng thêm")
    private String condition;

    @Schema(description = "Điều kiện con nhận thưởng thêm")
    private String childCondition;
  }

  @Setter
  @Getter
  public static class RuleConditionOutput {

    @Schema(description = "ID bản ghi")
    private Long id;

    @Schema(description = "Thuộc tính điều kiện áp dụng quy tắc")
    private String properties;

    @Schema(
        description =
            "Điều kiện:</br> EQUAL: Bằng</br> LESS_THAN: Nhỏ hơn</br> LESS_THAN_EQUAL: Nhỏ hơn hoặc bằng</br> GREATER_THAN: Lớn hơn</br> GREATER_THAN_EQUAL: Lớn hơn hoặc bằng</br> CONTAIN: Chứa</br> START_WITH: Bắt đầu bằng</br> END_WITH: Kết thúc bằng</br> IN: Nằm trong</br> NOT_IN: Không nằm trong")
    private Operators operators;

    @Schema(description = "Giá trị thuộc tính")
    private String value;
  }
}
