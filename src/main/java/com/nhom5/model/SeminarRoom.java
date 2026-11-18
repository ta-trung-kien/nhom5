package com.nhom5.model;

import com.nhom5.policy.RoomFeePolicy;
import com.nhom5.policy.SeminarRoomFeePolicy;

/** Phòng họp seminar, tính 50.000đ mỗi giờ. */
public class SeminarRoom extends Room {

    public SeminarRoom(String roomId, String roomName, int floor, int capacity, RoomStatus status) {
        super(roomId, roomName, floor, capacity, status);
    }

    @Override
    public RoomType getRoomType() {
        return RoomType.SEMINAR;
    }

    @Override
    public RoomFeePolicy getFeePolicy() {
        return new SeminarRoomFeePolicy();
    }

    @Override
    public String getDisplayDescription() {
        return "Phòng họp seminar, phí 50.000đ mỗi giờ.";
    }
}
