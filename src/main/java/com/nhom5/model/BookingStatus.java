package com.nhom5.model;

/** Trạng thái của một lịch đặt phòng. */
public enum BookingStatus {
    BOOKED("Đã đặt"),
    CANCELLED("Đã hủy");

    private final String label;

    BookingStatus(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public static BookingStatus fromCode(String code) {
        return BookingStatus.valueOf(code.trim().toUpperCase());
    }
}
