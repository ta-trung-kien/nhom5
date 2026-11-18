package com.nhom5.model;

/** Loại phòng học nhóm. Thêm loại mới bằng cách bổ sung enum và class phòng tương ứng. */
public enum RoomType {
    NORMAL("Phòng thường"),
    PROJECTOR("Phòng có máy chiếu"),
    SEMINAR("Phòng họp seminar");

    private final String label;

    RoomType(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public static RoomType fromCode(String code) {
        if (code == null || code.isBlank()) {
            throw new IllegalArgumentException("Thiếu loại phòng.");
        }
        return RoomType.valueOf(code.trim().toUpperCase());
    }
}
