package vn.com.atomi.loyalty.config.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;
import org.apache.commons.lang3.StringUtils;
import vn.com.atomi.loyalty.base.constant.DateConstant;

/**
 * @author haidv
 * @version 1.0
 */
public class Utils {

  public static DateTimeFormatter LOCAL_DATETIME_FORMATTER =
      DateTimeFormatter.ofPattern(DateConstant.STR_PLAN_DD_MM_YYYY_HH_MM_SS);
  public static DateTimeFormatter LOCAL_DATE_FORMATTER =
      DateTimeFormatter.ofPattern(DateConstant.STR_PLAN_DD_MM_YYYY);

  private Utils() {
    throw new IllegalStateException("Utility class");
  }

  public static String generateUniqueId() {
    return UUID.randomUUID().toString();
  }

  public static String makeLikeParameter(String param) {
    return "%|" + param + "|%";
  }

  public static LocalDateTime convertToLocalDateTime(String date) {
    return StringUtils.isEmpty(date) ? null : LocalDateTime.parse(date, LOCAL_DATETIME_FORMATTER);
  }

  public static LocalDate convertToLocalDate(String date) {
    return StringUtils.isEmpty(date) ? null : LocalDate.parse(date, LOCAL_DATE_FORMATTER);
  }

  public static String formatLocalDateTimeToString(LocalDateTime date) {
    return date == null ? null : LOCAL_DATETIME_FORMATTER.format(date);
  }

  public static String formatLocalDateToString(LocalDate date) {
    return date == null ? null : LOCAL_DATE_FORMATTER.format(date);
  }
}
