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
public class TransactionTypeOutput {

  private Long id;

  private String groupCode;

  private String typeCode;

  private String typeName;

  private Long orderNo;

  private Status status;
}
