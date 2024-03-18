package vn.com.atomi.loyalty.config.dto.output;

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

  private Long id;

  private String code;

  private String categoryId;

  private String categoryCode;

  private String name;

  private Status status;
}
