package vn.com.atomi.loyalty.config.dto.output;

import io.swagger.v3.oas.annotations.media.Schema;
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

  @Schema(description = "Cảnh báo trùng")
  private String message;
}
