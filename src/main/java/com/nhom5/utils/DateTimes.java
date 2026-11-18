package com.nhom5.utils;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/** Định dạng ngày giờ dùng chung. */
public final class DateTimes {

    public static final LocalTime OPENING = LocalTime.of(7, 0);
    public static final LocalTime CLOSING = LocalTime.of(21, 0);
    public static final long MAX_MINUTES_PER_DAY = 4L * 60L;
    private static final DateTimeFormatter HH_MM = DateTimeFormatter.ofPattern("HH:mm");

    private DateTimes() {
    }

    public static String formatTime(LocalTime time) {
        return time == null ? "" : time.format(HH_MM);
    }

    public static String formatDuration(long minutes) {
        if (minutes <= 0) {
            return "0 phút";
        }
        long hours = minutes / 60;
        long remain = minutes % 60;
        if (hours == 0) {
            return remain + " phút";
        }
        if (remain == 0) {
            return hours + " giờ";
        }
        return hours + " giờ " + remain + " phút";
    }
}
