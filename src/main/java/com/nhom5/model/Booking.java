package com.nhom5.model;

import com.nhom5.exception.BusinessException;

import java.time.LocalDateTime;

/** Một lịch đặt phòng của sinh viên. */
public class Booking {

    private String bookingId;
    private String studentId;
    private String roomId;
    private TimeSlot timeSlot;
    private int participantCount;
    private BookingStatus status;
    private long fee;
    private LocalDateTime createdAt;

    public Booking(String bookingId, String studentId, String roomId, TimeSlot timeSlot,
                   int participantCount, BookingStatus status, long fee, LocalDateTime createdAt) {
        this.bookingId = bookingId;
        this.studentId = studentId;
        this.roomId = roomId;
        this.timeSlot = timeSlot;
        this.participantCount = participantCount;
        this.status = status;
        this.fee = fee;
        this.createdAt = createdAt;
    }

    public boolean isActive() {
        return status == BookingStatus.BOOKED;
    }

    public void markCancelled() {
        if (status == BookingStatus.CANCELLED) {
            throw new BusinessException("Lịch đặt đã được hủy trước đó.");
        }
        this.status = BookingStatus.CANCELLED;
    }

    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
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

    public TimeSlot getTimeSlot() {
        return timeSlot;
    }

    public void setTimeSlot(TimeSlot timeSlot) {
        this.timeSlot = timeSlot;
    }

    public int getParticipantCount() {
        return participantCount;
    }

    public void setParticipantCount(int participantCount) {
        this.participantCount = participantCount;
    }

    public BookingStatus getStatus() {
        return status;
    }

    public void setStatus(BookingStatus status) {
        this.status = status;
    }

    public long getFee() {
        return fee;
    }

    public void setFee(long fee) {
        this.fee = fee;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
