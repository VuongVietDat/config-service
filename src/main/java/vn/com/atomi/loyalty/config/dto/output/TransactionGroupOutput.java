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
public class TransactionGroupOutput {

  private Long id;

  private String customerType;

  private String groupCode;

  private String groupName;

  private Long orderNo;

  private Status status;
}
