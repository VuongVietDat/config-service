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
public class ProductOutput {

	  @Schema(description = "ID sản phẩm/dịch vụ")
	  private Long id;

	  @Schema(description = "Ma Sản phẩm/dịch vụ")
	  private String productCode;

	  @Schema(description = "Tên sản phẩm dịch vụ")
	  private String productName;

	  @Schema(description = "Thứ tự hiển thị")
	  private Long orderNo;

	  @Schema(description = "Dong sản phẩm/dịch vụ")
	  private String productLine;

	  @Schema(description = "Trạng thái:</br> ACTIVE: Hiệu lực</br> INACTIVE: Không hiệu lực")
	  private Status status;
}
