package com.nhom5.exception;

/**
 * Lỗi nghiệp vụ: dữ liệu không thỏa rule đặt phòng, hủy lịch hoặc nhập liệu.
 * Message được hiển thị trực tiếp cho người dùng.
 */
public class BusinessException extends RuntimeException {

    public BusinessException(String message) {
        super(message);
    }
}
