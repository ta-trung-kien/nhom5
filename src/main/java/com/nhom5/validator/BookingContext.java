package com.nhom5.validator;

import com.nhom5.model.Booking;
import com.nhom5.model.BookingRequest;
import com.nhom5.model.Room;
import com.nhom5.model.Student;

import java.util.List;

/** Dữ liệu đã tra cứu, đưa vào từng bước kiểm tra điều kiện đặt phòng. */
public class BookingContext {

    private final BookingRequest request;
    private final Student student;
    private final Room room;
    private final List<Booking> roomBookings;
    private final List<Booking> studentBookingsOnDate;

    public BookingContext(BookingRequest request, Student student, Room room,
                          List<Booking> roomBookings, List<Booking> studentBookingsOnDate) {
        this.request = request;
        this.student = student;
        this.room = room;
        this.roomBookings = roomBookings;
        this.studentBookingsOnDate = studentBookingsOnDate;
    }

    public BookingRequest getRequest() {
        return request;
    }

    public Student getStudent() {
        return student;
    }

    public Room getRoom() {
        return room;
    }

    public List<Booking> getRoomBookings() {
        return roomBookings;
    }

    public List<Booking> getStudentBookingsOnDate() {
        return studentBookingsOnDate;
    }
}
