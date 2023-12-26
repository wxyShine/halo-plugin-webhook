package com.wxy97.webhook.util;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/**
 * @author wxy
 * @date 2023/12/26 下午3:09
 * @email wxyrrcj@gmail.com
 */
public class DateUtil {

    private static final DateTimeFormatter formatter =
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static String format(LocalDateTime dateTime) {
        return dateTime.format(formatter);
    }

    public static String format(Instant instant) {
        LocalDateTime dateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
        return dateTime.format(formatter);
    }

    public static String formatNow() {
        return formatter.format(LocalDateTime.now());
    }
}
