package vn.com.atomi.loyalty.config.enums;

import org.springframework.http.HttpStatus;
import vn.com.atomi.loyalty.base.exception.AbstractError;

/**
 * @author haidv
 * @version 1.0
 */
public enum ErrorCode implements AbstractError {
  APPROVING_RECORD_NOT_EXISTED(1000, "Không tìm thấy bản ghi chờ duyệt.", HttpStatus.NOT_FOUND),
  RULE_NOT_EXISTED(1001, "Không tồn tại quy tắc sinh điểm.", HttpStatus.NOT_FOUND),
  APPROVE_TYPE_NOT_MATCH_UPDATE(
      1002, "Chỉ so sánh khi loại phê duyệt là cập nhật.", HttpStatus.BAD_REQUEST),
  CAMPAIGN_NOT_EXISTED(1003, "Chiến dịch không tồn tại.", HttpStatus.NOT_FOUND),
  CAMPAIGN_INACTIVE(1004, "Chiến dịch đã hết thời gian hiệu lực.", HttpStatus.BAD_REQUEST),
  RULE_STARTDATE_GREAT_THAN_CAMPAIGN_STARTDATE(
      1005,
      "Ngày bắt đầu hiệu lực của quy tắc phải lớn hơn hoặc bằng ngày bắt đầu hiệu lực của chiến dịch.",
      HttpStatus.BAD_REQUEST),
  RULE_ENDDATE_LESS_THAN_CAMPAIGN_ENDDATE(
      1006,
      "Ngày kết thúc hiệu lực của quy tắc phải nhỏ hơn hoặc bằng ngày kết thúc hiệu lực của chiến dịch.",
      HttpStatus.BAD_REQUEST),
  RULE_TYPE_NOT_EXISTED(1007, "Không tồn tại loại quy tắc sinh điểm.", HttpStatus.NOT_FOUND),
  RULE_BONUS_TYPE_NOT_EXISTED(1008, "Không tồn tại loại thưởng thêm.", HttpStatus.NOT_FOUND),
  ;

  private final int code;

  private final String message;

  private final HttpStatus httpStatus;

  ErrorCode(int code, String message, HttpStatus httpStatus) {
    this.httpStatus = httpStatus;
    this.code = code;
    this.message = message;
  }

  @Override
  public int getCode() {
    return code;
  }

  @Override
  public String getMessage() {
    return message;
  }

  @Override
  public HttpStatus getHttpStatus() {
    return httpStatus;
  }
}
