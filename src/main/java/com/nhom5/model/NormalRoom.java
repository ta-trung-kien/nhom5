package com.nhom5.model;

import com.nhom5.policy.FreeRoomFeePolicy;
import com.nhom5.policy.RoomFeePolicy;

/** Phòng học nhóm thông thường, không tính phí. */
public class NormalRoom extends Room {

    public NormalRoom(String roomId, String roomName, int floor, int capacity, RoomStatus status) {
        super(roomId, roomName, floor, capacity, status);
    }

    @Override
    public RoomType getRoomType() {
        return RoomType.NORMAL;
    }

    @Override
    public RoomFeePolicy getFeePolicy() {
        return new FreeRoomFeePolicy();
    }

    @Override
    public String getDisplayDescription() {
        return "Phòng học nhóm thông thường, không tính phí.";
    }
}
