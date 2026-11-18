package com.nhom5.model;

import com.nhom5.utils.DateTimes;
import com.nhom5.utils.Money;

/**
 * Chi tiết một lịch đặt đã ghép với sinh viên và phòng, dùng khi hiển thị.
 */
public class BookingDetail {

    private Booking booking;
    private Student student;
    private Room room;

    public BookingDetail(Booking booking, Student student, Room room) {
        this.booking = booking;
        this.student = student;
        this.room = room;
    }

    public String getDurationText() {
        return DateTimes.formatDuration(booking.getTimeSlot().getMinutes());
    }

    public String getFeeText() {
        return Money.formatVnd(booking.getFee());
    }

    public Booking getBooking() {
        return booking;
    }

    public void setBooking(Booking booking) {
        this.booking = booking;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }
}
