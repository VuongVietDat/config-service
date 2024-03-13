package vn.com.atomi.loyalty.config.dto.input;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import vn.com.atomi.loyalty.base.annotations.DateRangeValidator;
import vn.com.atomi.loyalty.base.annotations.DateTimeValidator;
import vn.com.atomi.loyalty.base.constant.DateConstant;
import vn.com.atomi.loyalty.config.enums.*;

/**
 * @author haidv
 * @version 1.0
 */
@Setter
@Getter
@DateRangeValidator
public class CreateRuleInput implements IDateInput {

  @Schema(description = "Loại qui tắc sinh điểm")
  @NotBlank
  private String type;

  @Schema(description = "Tên quy tắc sinh điểm")
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

  @Schema(description = "Ngày bắt đầu hiệu lực (dd/MM/yyyy)")
  @DateTimeValidator
  private String startDate;

  @Schema(description = "Ngày kết thúc hiệu lực (dd/MM/yyyy)")
  @DateTimeValidator(required = false)
  private String endDate;

  @Schema(description = "Trạng thái:</br> ACTIVE: Hiệu lực</br> INACTIVE: Không hiệu lực")
  @NotNull
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
  private List<RuleAllocationInput> allocationInputs;

  @Schema(description = "Quy tắc tặng thêm điểm")
  private List<RuleBonusInput> ruleBonusInputs;

  @Schema(description = "Điều kiện áp dụng quy tắc")
  private List<RuleConditionInput> ruleConditionInputs;

  @Setter
  @Getter
  public static class RuleAllocationInput {

    @Schema(description = "Số điểm cố định")
    private Long fixPointAmount;

    @Schema(description = "Giá trị quy đổi (VND)")
    private Long exchangeValue;

    @Schema(description = "Giá trị điểm")
    private Long exchangePoint;

    @Schema(description = "Giá trị giao dịch tối thiểu")
    private Long minTransaction;

    @Schema(
        description = "Tích điểm với số tiền thực khách hàng thanh toán (sau khi trừ khuyến mãi)")
    private Boolean isNetValue;

    @Schema(description = "Giới hạn số điểm tối đa phân bổ trên một giao dịch")
    private Long limitPointPerTransaction;

    @Schema(description = "Giới hạn số điểm tối đa phân bổ trên một khách hàng")
    private Long limitPointPerUser;

    @Schema(
        description =
            "Tần suất giới hạn số điểm tối đa phân bổ trên một khách hàng:</br> HOURS: Giờ</br> DAY: Ngày</br> WEEK: Tuần</br> MONTH: Tháng</br> YEAR: Năm")
    private Frequency frequencyLimitPointPerUser;

    @Schema(description = "Giới hạn số lần tối đa phân bổ trên một khách hàng")
    private Long limitEventPerUser;

    @Schema(
        description =
            "Tần suất giới hạn số lần tối đa phân bổ trên một khách hàng:</br> HOURS: Giờ</br> DAY: Ngày</br> WEEK: Tuần</br> MONTH: Tháng</br> YEAR: Năm")
    private Frequency frequencyLimitEventPerUser;

    @Schema(description = "Thời gian chờ giữa 2 lần")
    private Long timeWait;
  }

  @Setter
  @Getter
  public static class RuleBonusInput {

    @Schema(description = "Loại thưởng thêm")
    private String type;

    @Schema(description = "Giá trị thưởng")
    private String value;

    @Schema(
        description =
            "Loại giá trị thưởng:</br> PERCENTAGE: Phần trăm base điểm</br> FIX: Số điểm cụ thể")
    private PlusType plusType;

    @Schema(description = "Từ ngày (dd/MM/yyyy)")
    @DateTimeValidator(required = false, pattern = DateConstant.STR_PLAN_DD_MM_YYYY_STROKE)
    private String fromDate;

    @Schema(description = "Đến ngày (dd/MM/yyyy)")
    @DateTimeValidator(required = false, pattern = DateConstant.STR_PLAN_DD_MM_YYYY_STROKE)
    private String toDate;

    @Schema(description = "Điều kiện nhận thưởng thêm")
    private String condition;
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
