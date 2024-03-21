package vn.com.atomi.loyalty.config.dto.input;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import vn.com.atomi.loyalty.config.annotations.CreateRuleValidator;
import vn.com.atomi.loyalty.config.enums.*;

/**
 * @author haidv
 * @version 1.0
 */
@Setter
@Getter
@CreateRuleValidator
public class CreateRuleInput {

  @Schema(description = "Loại quy tắc sinh điểm", example = "TRANSACTION")
  @NotBlank
  private String type;

  @Schema(description = "Tên quy tắc sinh điểm", example = "Giao dịch")
  @NotBlank
  @Size(max = 168)
  private String name;

  @Schema(
      description =
          "Loại điểm:</br> ALL: Tất cả loại điẻm</br> RANK_POINT: Điểm xếp hạng</br> CONSUMPTION_POINT: Điểm tích lũy dùng để tiêu dùng")
  @NotNull
  private PointType pointType;

  @Schema(description = "ID chiến dịch")
  @NotNull
  private Long campaignId;

  @Schema(
      description =
          "Loại điều kiện:</br> ALL_MATCH: Tất cả điều kiện thỏa mãn</br> ANY_MATCH: Bất kỳ một điều kiện thỏa mãn")
  private ConditionType conditionType;

  @Schema(description = "Ngày bắt đầu hiệu lực (dd/MM/yyyy)", example = "01/01/2024")
  private String startDate;

  @Schema(description = "Ngày kết thúc hiệu lực (dd/MM/yyyy)", example = "31/12/2024")
  private String endDate;

  @Schema(description = "Trạng thái:</br> ACTIVE: Hiệu lực</br> INACTIVE: Không hiệu lực")
  @NotNull
  private Status status;

  @Schema(
      description =
          "Loại chính sách hết hạn điểm:</br> AFTER_DAY: Sau số ngày</br> AFTER_DATE: Sau ngày</br> FIRST_DATE_OF_MONTH: Ngày đầu tiên của tháng thứ N +",
      example = "AFTER_DAY")
  private ExpirePolicyType expirePolicyType;

  @Schema(
      description =
          "Giá trị của chính sách hết hạn điểm:</br> AFTER_DAY: number</br> AFTER_DATE: dd/MM/yyyy</br> FIRST_DATE_OF_MONTH: number",
      example = "180")
  private String expirePolicyValue;

  @Schema(description = "Quy tắc chung phân bổ điểm")
  @NotEmpty
  @Valid
  private List<RuleAllocationInput> allocationInputs;

  @Schema(description = "Quy tắc tặng thêm điểm")
  private List<RuleBonusInput> ruleBonusInputs;

  @Schema(description = "Điều kiện áp dụng quy tắc")
  private List<RuleConditionInput> ruleConditionInputs;

  @Setter
  @Getter
  public static class RuleAllocationInput {
    @Schema(description = "true = Quy đổi theo giá trị giao dịch / false = Quy đổi theo số lần giao dịch")
    @NotNull
    private Boolean isExchangeByValue;

    @Schema(description = "Số điểm cố định")
    private long fixPointAmount;

    @Schema(description = "Giá trị quy đổi (VND)", example = "1000")
    private long exchangeValue;

    @Schema(description = "Giá trị điểm", example = "1")
    private long exchangePoint;

    @Schema(description = "Giá trị giao dịch tối thiểu", example = "50000")
    @Min(0)
    private long minTransaction;

    @Schema(
        description = "Tích điểm với số tiền thực khách hàng thanh toán (sau khi trừ khuyến mãi)",
        example = "true")
    @NotNull
    private Boolean isNetValue;

    @Schema(description = "Giới hạn số điểm tối đa phân bổ trên một giao dịch", example = "10000")
    private long limitPointPerTransaction;

    @Schema(description = "Giới hạn số điểm tối đa phân bổ trên một khách hàng", example = "1000")
    private long limitPointPerUser;

    @Schema(
        description =
            "Tần suất giới hạn số điểm tối đa phân bổ trên một khách hàng:</br> MINUTE: Phút</br> HOURS: Giờ</br> DAY: Ngày</br> WEEK: Tuần</br> MONTH: Tháng</br> YEAR: Năm",
        example = "DAY")
    private Frequency frequencyLimitPointPerUser;

    @Schema(description = "Giới hạn số lần tối đa phân bổ trên một khách hàng")
    private long limitEventPerUser;

    @Schema(
        description =
            "Tần suất giới hạn số lần tối đa phân bổ trên một khách hàng:</br> MINUTE: Phút</br>HOURS: Giờ</br> DAY: Ngày</br> WEEK: Tuần</br> MONTH: Tháng</br> YEAR: Năm")
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
  public static class RuleBonusInput {

    @Schema(description = "Loại thưởng thêm")
    private BonusType type;

    @Schema(description = "Giá trị thưởng")
    private Double value;

    @Schema(
        description =
            "Loại giá trị thưởng:</br> PERCENTAGE: Phần trăm base điểm</br> FIX: Số điểm cụ thể")
    private PlusType plusType;

    @Schema(description = "Điều kiện cha nhận thưởng thêm")
    private String condition;

    @Schema(description = "Điều kiện con nhận thưởng thêm")
    private String childCondition;
  }

  @Setter
  @Getter
  public static class RuleConditionInput {

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
