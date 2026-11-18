package com.nhom5.model;

/** Tạo đúng lớp phòng theo loại, để phần còn lại của hệ thống chỉ việc dùng Room. */
public final class RoomFactory {

    private RoomFactory() {
    }

    public static Room create(String roomId, String roomName, int floor, int capacity,
                               RoomType type, RoomStatus status) {
        return switch (type) {
            case NORMAL -> new NormalRoom(roomId, roomName, floor, capacity, status);
            case PROJECTOR -> new ProjectorRoom(roomId, roomName, floor, capacity, status);
            case SEMINAR -> new SeminarRoom(roomId, roomName, floor, capacity, status);
        };
    }
}
