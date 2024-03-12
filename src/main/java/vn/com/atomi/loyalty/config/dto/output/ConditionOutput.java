package vn.com.atomi.loyalty.config.dto.output;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.*;
import vn.com.atomi.loyalty.config.enums.ComponentType;
import vn.com.atomi.loyalty.config.enums.ConditionInputType;
import vn.com.atomi.loyalty.config.enums.Operators;
import vn.com.atomi.loyalty.config.enums.SourceType;

/**
 * @author haidv
 * @version 1.0
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class ConditionOutput {

  @Schema(description = "Mã thuộc tính")
  private String properties;

  @Schema(description = "Tên thuộc tính")
  private String propertiesName;

  private List<ConditionData> data;

  @JsonIgnore private Operators operators;

  @JsonIgnore private ConditionInputType inputType;

  @JsonIgnore private ComponentType componentType;

  @JsonIgnore private String minValue;

  @JsonIgnore private String maxValue;

  @JsonIgnore private String defaultValue;

  @JsonIgnore private String sourceValue;

  @JsonIgnore private SourceType sourceType;

  @JsonIgnore private List<ConditionSourceValue> sourceValues;

  @Setter
  @Getter
  public static class ConditionData {

    @Schema(
        description =
            "Điều kiện:</br> EQUAL: Bằng</br> LESS_THAN: Nhỏ hơn</br> LESS_THAN_EQUAL: Nhỏ hơn hoặc bằng</br> GREATER_THAN: Lớn hơn</br> GREATER_THAN_EQUAL: Lớn hơn hoặc bằng</br> CONTAIN: Chứa</br> START_WITH: Bắt đầu bằng</br> END_WITH: Kết thúc bằng</br> IN: Nằm trong</br> NOT_IN: Không nằm trong")
    private Operators operators;

    @Schema(description = "Kiểu dữ liệu truyền lên")
    private ConditionInputType inputType;

    @Schema(description = "Loại component để frontend hiển thị")
    private ComponentType componentType;

    @Schema(description = "Giá trị nhỏ nhất")
    private String minValue;

    @Schema(description = "Giá trị lớn nhất")
    private String maxValue;

    @Schema(description = "Giá trị mặc định")
    private String defaultValue;

    @Schema(description = "Dữ liệu của select box")
    private List<ConditionSourceValue> sourceValues;
  }

  @Setter
  @Getter
  @NoArgsConstructor
  @AllArgsConstructor
  public static class ConditionSourceValue {

    @Schema(description = "Giá trị")
    private String key;

    @Schema(description = "Tên hiển thị")
    private String displayName;
  }
}
