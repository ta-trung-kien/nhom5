package com.nhom5.utils;

import com.nhom5.exception.BusinessException;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;

/** Kiểm tra và chuẩn hóa dữ liệu nhập. */
public final class InputCheck {

    private InputCheck() {
    }

    public static String normalizeCode(String raw) {
        if (raw == null) {
            return "";
        }
        return raw.trim().toUpperCase();
    }

    public static String requireStudentId(String raw) {
        String id = normalizeCode(raw);
        if (!id.matches("[A-Z0-9]{3,15}")) {
            throw new BusinessException("Mã sinh viên phải từ 3 đến 15 ký tự chữ hoặc số.");
        }
        return id;
    }

    public static String requireText(String raw, String message) {
        if (raw == null || raw.trim().isEmpty()) {
            throw new BusinessException(message);
        }
        return raw.trim();
    }

    public static String requirePhone(String raw) {
        String phone = raw == null ? "" : raw.trim();
        if (!phone.matches("0\\d{8,10}")) {
            throw new BusinessException("Số điện thoại không hợp lệ. Nhập 9 đến 11 số, bắt đầu bằng 0.");
        }
        return phone;
    }

    public static String requireEmail(String raw) {
        String email = raw == null ? "" : raw.trim().toLowerCase();
        if (!email.matches("[^@\\s]+@[^@\\s]+\\.[^@\\s]+")) {
            throw new BusinessException("Email không hợp lệ.");
        }
        return email;
    }

    public static LocalDate parseDate(String raw) {
        if (raw == null || raw.isBlank()) {
            throw new BusinessException("Vui lòng chọn ngày đặt phòng.");
        }
        try {
            return LocalDate.parse(raw.trim());
        } catch (DateTimeParseException ex) {
            throw new BusinessException("Ngày đặt phòng không đúng định dạng yyyy-MM-dd.");
        }
    }

    public static LocalTime parseTime(String raw, String label) {
        if (raw == null || raw.isBlank()) {
            throw new BusinessException("Vui lòng nhập " + label + ".");
        }
        try {
            return LocalTime.parse(raw.trim());
        } catch (DateTimeParseException ex) {
            throw new BusinessException(label + " không đúng định dạng HH:mm.");
        }
    }

    public static int parseParticipants(String raw) {
        if (raw == null || raw.isBlank()) {
            throw new BusinessException("Vui lòng nhập số lượng người tham gia.");
        }
        try {
            int value = Integer.parseInt(raw.trim());
            if (value <= 0) {
                throw new BusinessException("Số lượng người tham gia phải lớn hơn 0.");
            }
            if (value > 500) {
                throw new BusinessException("Số lượng người tham gia không hợp lệ.");
            }
            return value;
        } catch (NumberFormatException ex) {
            throw new BusinessException("Số lượng người tham gia phải là số nguyên.");
        }
    }
}
