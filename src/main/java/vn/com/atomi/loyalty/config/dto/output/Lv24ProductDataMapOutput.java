package vn.com.atomi.loyalty.config.dto.output;

import lombok.*;
import vn.com.atomi.loyalty.config.enums.*;

/**
 * @author haidv
 * @version 1.0
 */
@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Lv24ProductDataMapOutput {

  private Long id;

  private Long productId;

  private String productCode;

  private String productName;

  private String transactionGroup;

  private Status status;
}
