package com.nhom5.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/** Lịch trống và lịch đã đặt của một phòng trong một ngày. */
public class RoomSchedule {

    private String roomId;
    private LocalDate date;
    private List<TimeSlot> booked = new ArrayList<>();
    private List<TimeSlot> free = new ArrayList<>();

    public RoomSchedule(String roomId, LocalDate date) {
        this.roomId = roomId;
        this.date = date;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public List<TimeSlot> getBooked() {
        return booked;
    }

    public void setBooked(List<TimeSlot> booked) {
        this.booked = booked;
    }

    public List<TimeSlot> getFree() {
        return free;
    }

    public void setFree(List<TimeSlot> free) {
        this.free = free;
    }
}
