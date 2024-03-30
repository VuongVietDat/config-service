package vn.com.atomi.loyalty.config.dto.output;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.*;

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
public class WarringOverlapActiveTimeOutput {

  @Schema(description = "Đã tồn tại hay chưa")
  private boolean existed;

  @Schema(description = "Danh sách quy tắc trùng")
  private List<RuleOverlapActiveTime> ruleOverlapActiveTimes;

  @Builder
  @Getter
  @Setter
  @ToString
  @AllArgsConstructor
  @NoArgsConstructor
  public static class RuleOverlapActiveTime {

    @Schema(description = "ID bản ghi")
    private Long id;

    @Schema(description = "Mã quy tắc sinh điểm")
    private String code;
  }
}
