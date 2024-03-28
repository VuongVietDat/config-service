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
public class ProductTypeOutput {

  @Schema(description = "ID loại sản phẩm/dịch vụ")
  private Long id;

  @Schema(description = "Mã loại khách hàng")
  private String customerType;

  @Schema(description = "Mã loại sản phẩm/dịch vụ")
  private String typeCode;

  @Schema(description = "Tên loại sản phẩm/dịch vụ")
  private String typeName;

  @Schema(description = "Thứ tự hiển thị")
  private Long orderNo;

  @Schema(description = "Trạng thái:</br> ACTIVE: Hiệu lực</br> INACTIVE: Không hiệu lực")
  private Status status;
}
