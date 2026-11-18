package com.nhom5.utils;

/** Định dạng số tiền Việt Nam, ví dụ 20000 -> 20.000đ. */
public final class Money {

    private Money() {
    }

    public static String formatVnd(long amount) {
        String raw = Long.toString(Math.abs(amount));
        StringBuilder grouped = new StringBuilder();
        int count = 0;
        for (int i = raw.length() - 1; i >= 0; i--) {
            grouped.append(raw.charAt(i));
            count++;
            if (count % 3 == 0 && i > 0) {
                grouped.append('.');
            }
        }
        String text = grouped.reverse().toString();
        if (amount < 0) {
            text = "-" + text;
        }
        return text + "đ";
    }
}
