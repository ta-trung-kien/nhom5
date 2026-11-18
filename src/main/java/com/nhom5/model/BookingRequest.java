package com.nhom5.model;

import java.time.LocalDate;
import java.time.LocalTime;

/** Dữ liệu đầu vào khi sinh viên gửi yêu cầu đặt phòng. */
public class BookingRequest {

    private String studentId;
    private String roomId;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    private int participants;

    public BookingRequest(String studentId, String roomId, LocalDate date,
                          LocalTime startTime, LocalTime endTime, int participants) {
        this.studentId = studentId;
        this.roomId = roomId;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.participants = participants;
    }

    public TimeSlot toTimeSlot() {
        return new TimeSlot(date, startTime, endTime);
    }

    public long getMinutes() {
        return toTimeSlot().getMinutes();
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
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

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public int getParticipants() {
        return participants;
    }

    public void setParticipants(int participants) {
        this.participants = participants;
    }
}
