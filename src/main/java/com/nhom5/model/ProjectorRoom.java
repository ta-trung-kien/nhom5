package com.nhom5.model;

import com.nhom5.policy.ProjectorRoomFeePolicy;
import com.nhom5.policy.RoomFeePolicy;

/** Phòng có máy chiếu, tính 20.000đ mỗi giờ. */
public class ProjectorRoom extends Room {

    public ProjectorRoom(String roomId, String roomName, int floor, int capacity, RoomStatus status) {
        super(roomId, roomName, floor, capacity, status);
    }

    @Override
    public RoomType getRoomType() {
        return RoomType.PROJECTOR;
    }

    @Override
    public RoomFeePolicy getFeePolicy() {
        return new ProjectorRoomFeePolicy();
    }

    @Override
    public String getDisplayDescription() {
        return "Có máy chiếu, phí 20.000đ mỗi giờ.";
    }
}
