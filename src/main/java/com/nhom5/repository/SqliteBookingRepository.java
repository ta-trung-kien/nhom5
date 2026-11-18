package com.nhom5.repository;

import com.nhom5.model.Booking;
import com.nhom5.model.BookingStatus;
import com.nhom5.model.TimeSlot;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public class SqliteBookingRepository implements BookingRepository {

    private final DatabaseManager database;

    public SqliteBookingRepository(DatabaseManager database) {
        this.database = database;
    }

    @Override
    public void save(Booking booking) {
        database.update(
                """
                INSERT INTO bookings(
                    booking_id, student_id, room_id, booking_date, start_time, end_time,
                    participant_count, status, fee, created_at
                ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                """,
                booking.getBookingId(),
                booking.getStudentId(),
                booking.getRoomId(),
                booking.getTimeSlot().getDate().toString(),
                booking.getTimeSlot().getStartTime().toString(),
                booking.getTimeSlot().getEndTime().toString(),
                booking.getParticipantCount(),
                booking.getStatus().name(),
                booking.getFee(),
                booking.getCreatedAt().toString()
        );
    }

    @Override
    public void update(Booking booking) {
        database.update(
                """
                UPDATE bookings
                SET student_id = ?, room_id = ?, booking_date = ?, start_time = ?, end_time = ?,
                    participant_count = ?, status = ?, fee = ?, created_at = ?
                WHERE booking_id = ?
                """,
                booking.getStudentId(),
                booking.getRoomId(),
                booking.getTimeSlot().getDate().toString(),
                booking.getTimeSlot().getStartTime().toString(),
                booking.getTimeSlot().getEndTime().toString(),
                booking.getParticipantCount(),
                booking.getStatus().name(),
                booking.getFee(),
                booking.getCreatedAt().toString(),
                booking.getBookingId()
        );
    }

    @Override
    public Optional<Booking> findById(String bookingId) {
        return database.query("SELECT * FROM bookings WHERE booking_id = ?", this::map, bookingId)
                .stream()
                .findFirst();
    }

    @Override
    public List<Booking> findAll() {
        return database.query(
                "SELECT * FROM bookings ORDER BY booking_date DESC, start_time DESC, booking_id DESC",
                this::map
        );
    }

    @Override
    public List<Booking> findByStudent(String studentId) {
        return database.query(
                """
                SELECT * FROM bookings
                WHERE student_id = ?
                ORDER BY booking_date DESC, start_time DESC
                """,
                this::map,
                studentId
        );
    }

    @Override
    public List<Booking> findByRoomAndDate(String roomId, LocalDate date) {
        return database.query(
                """
                SELECT * FROM bookings
                WHERE room_id = ? AND booking_date = ?
                ORDER BY start_time
                """,
                this::map,
                roomId,
                date.toString()
        );
    }

    @Override
    public List<Booking> findByStudentAndDate(String studentId, LocalDate date) {
        return database.query(
                "SELECT * FROM bookings WHERE student_id = ? AND booking_date = ? ORDER BY start_time",
                this::map,
                studentId,
                date.toString()
        );
    }

    private Booking map(ResultSet resultSet) throws SQLException {
        TimeSlot timeSlot = new TimeSlot(
                LocalDate.parse(resultSet.getString("booking_date")),
                LocalTime.parse(resultSet.getString("start_time")),
                LocalTime.parse(resultSet.getString("end_time"))
        );
        return new Booking(
                resultSet.getString("booking_id"),
                resultSet.getString("student_id"),
                resultSet.getString("room_id"),
                timeSlot,
                resultSet.getInt("participant_count"),
                BookingStatus.fromCode(resultSet.getString("status")),
                resultSet.getLong("fee"),
                LocalDateTime.parse(resultSet.getString("created_at"))
        );
    }
}
