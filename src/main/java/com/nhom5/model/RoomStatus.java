package com.nhom5.model;

/** Trạng thái hoạt động của phòng. */
public enum RoomStatus {
    ACTIVE("Đang hoạt động"),
    MAINTENANCE("Đang bảo trì");

    private final String label;

    RoomStatus(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public static RoomStatus fromCode(String code) {
        if (code == null || code.isBlank()) {
            throw new IllegalArgumentException("Thiếu trạng thái phòng.");
        }
        return RoomStatus.valueOf(code.trim().toUpperCase());
    }
}
