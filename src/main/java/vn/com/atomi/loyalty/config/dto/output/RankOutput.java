package vn.com.atomi.loyalty.config.dto.output;

import lombok.Getter;
import lombok.Setter;
import vn.com.atomi.loyalty.config.enums.Status;

/**
 * @author haidv
 * @version 1.0
 */
@Setter
@Getter
public class RankOutput {

  private Long id;

  private String code;

  private String name;

  private int orderNo;

  private Status status;
}
