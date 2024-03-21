package vn.com.atomi.loyalty.config.dto.output;

import lombok.*;
import vn.com.atomi.loyalty.config.enums.Status;

/**
 * @author haidv
 * @version 1.0
 */
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductCategoryOutput {

  private Long id;

  private String code;

  private String name;

  private Status status;
}
