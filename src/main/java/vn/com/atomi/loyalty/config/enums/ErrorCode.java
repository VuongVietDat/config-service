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
      "Thời gian hiệu lực của quy tắc phải nằm trong thời gian hiệu lực của chiến dịch.",
      HttpStatus.BAD_REQUEST),
  RULE_ENDDATE_LESS_THAN_CAMPAIGN_ENDDATE(
      1006,
      "Thời gian hiệu lực của quy tắc phải nằm trong thời gian hiệu lực của chiến dịch.",
      HttpStatus.BAD_REQUEST),
  RULE_TYPE_NOT_EXISTED(1007, "Không tồn tại loại quy tắc sinh điểm.", HttpStatus.NOT_FOUND),
  RULE_BONUS_TYPE_NOT_EXISTED(1008, "Không tồn tại loại thưởng thêm.", HttpStatus.NOT_FOUND),
  CUSTOMER_GROUP_NOT_EXISTED(1009, "Không tồn tại nhóm khách hàng.", HttpStatus.NOT_FOUND),
  EXISTED_CAMPAIGN_USE_CUSTOMER_GROUP(
      1010, "Nhóm khách hàng này đang được sử dụng ở chiến dịch.", HttpStatus.BAD_REQUEST),
  RULE_CONDITION_NOT_EXISTED(
      1011, "Điều kiện áp dụng quy tắc không tồn tại.", HttpStatus.NOT_FOUND),
  OVERLAP_ACTIVE_TIME(
      1012,
      "Tập khách hàng quy định trong thời gian hiệu lực đã tồn tại quy tắc [%s]. Bạn có muốn tiếp tục!",
      HttpStatus.BAD_REQUEST),
  EXISTED_UPDATE_RULE_WAITING(
      1013, "Quy tắc đang có bản ghi cập nhật chờ duyệt.", HttpStatus.NOT_FOUND),
  PRODUCT_TYPE_NOT_EXISTED(1014, "Loại sản phẩm/dịch vụ không tồn tại.", HttpStatus.NOT_FOUND),
  PRODUCT_LINE_NOT_EXISTED(1015, "Dòng sản phẩm dịch vụ không tồn tại.", HttpStatus.NOT_FOUND),
  NOT_FILED_CHANGE_VALUE(1016, "Không có thông tin nào được cập nhật.", HttpStatus.BAD_REQUEST),
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
