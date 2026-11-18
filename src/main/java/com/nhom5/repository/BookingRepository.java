package com.nhom5.repository;

import com.nhom5.model.Booking;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface BookingRepository {

    void save(Booking booking);

    void update(Booking booking);

    Optional<Booking> findById(String bookingId);

    List<Booking> findAll();

    List<Booking> findByStudent(String studentId);

    List<Booking> findByRoomAndDate(String roomId, LocalDate date);

    List<Booking> findByStudentAndDate(String studentId, LocalDate date);
}
