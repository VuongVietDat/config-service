package vn.com.atomi.loyalty.config.dto.input;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import vn.com.atomi.loyalty.config.enums.Frequency;

/**
 * @author haidv
 * @version 1.0
 */
@Setter
@Getter
public class RuleAllocationInput {
  @Schema(
      description = "true = Quy đổi theo giá trị giao dịch / false = Quy đổi theo số lần giao dịch")
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
