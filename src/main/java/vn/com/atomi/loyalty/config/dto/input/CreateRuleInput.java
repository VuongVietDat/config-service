package vn.com.atomi.loyalty.config.dto.input;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.util.ArrayList;
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

  @Deprecated
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
          "Loại chính sách hết hạn điểm:</br> AFTER_DAY: Sau số ngày</br> AFTER_DATE: Sau ngày</br> FIRST_DATE_OF_MONTH: Ngày đầu tiên của tháng thứ N +</br>NEVER: Vĩnh viễn",
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
  private List<RuleAllocationInput> allocationInputs = new ArrayList<>();

  @Schema(description = "Quy tắc tặng thêm điểm")
  private List<RuleBonusInput> ruleBonusInputs = new ArrayList<>();

  @Schema(description = "Điều kiện áp dụng quy tắc")
  private List<RuleConditionInput> ruleConditionInputs = new ArrayList<>();
}
