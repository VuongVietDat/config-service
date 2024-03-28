package vn.com.atomi.loyalty.config.dto.output;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import vn.com.atomi.loyalty.config.enums.Status;

/**
 * @author haidv
 * @version 1.0
 */
@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProductLineOutput {

  @Schema(description = "ID loại sản phẩm/dịch vụ")
  private Long id;

  @Schema(description = "Mã loại sản phẩm/dịch vụ")
  private String productType;

  @Schema(description = "Mã dòng sản phẩm dịch vụ")
  private String lineCode;

  @Schema(description = "Tên dòng sản phẩm dịch vụ")
  private String lineName;

  @Schema(description = "Thứ tự hiển thị")
  private Long orderNo;

  @Schema(description = "Trạng thái:</br> ACTIVE: Hiệu lực</br> INACTIVE: Không hiệu lực")
  private Status status;
}
