package com.nhom5.model;

import com.nhom5.policy.RoomFeePolicy;

/**
 * Phòng học nhóm. Mỗi loại phòng cụ thể tự chọn chính sách phí của mình.
 */
public abstract class Room implements Bookable {

    private String roomId;
    private String roomName;
    private int floor;
    private int capacity;
    private RoomStatus status;

    protected Room(String roomId, String roomName, int floor, int capacity, RoomStatus status) {
        this.roomId = roomId;
        this.roomName = roomName;
        this.floor = floor;
        this.capacity = capacity;
        this.status = status;
    }

    public abstract RoomType getRoomType();

    public abstract RoomFeePolicy getFeePolicy();

    /** Mô tả ngắn, mỗi lớp con diễn đạt khác nhau. */
    public abstract String getDisplayDescription();

    public long calculateFee(long minutes) {
        return getFeePolicy().calculateFee(minutes);
    }

    @Override
    public String getBookableCode() {
        return roomId;
    }

    @Override
    public boolean isAvailableForBooking() {
        return status == RoomStatus.ACTIVE && capacity > 0;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public int getFloor() {
        return floor;
    }

    public void setFloor(int floor) {
        this.floor = floor;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public RoomStatus getStatus() {
        return status;
    }

    public void setStatus(RoomStatus status) {
        this.status = status;
    }
}
