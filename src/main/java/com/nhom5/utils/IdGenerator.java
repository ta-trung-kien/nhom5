package com.nhom5.utils;

import com.nhom5.model.Booking;
import com.nhom5.repository.BookingRepository;

import java.time.format.DateTimeFormatter;

/** Sinh mã đặt phòng dạng DPyyyyMMdd + số thứ tự trong ngày. */
public final class IdGenerator {

    private static final DateTimeFormatter DAY = DateTimeFormatter.BASIC_ISO_DATE;

    private IdGenerator() {
    }

    public static String nextBookingId(BookingRepository bookings, Booking booking) {
        String prefix = "DP" + booking.getTimeSlot().getDate().format(DAY);
        int sequence = 1;
        String id;
        do {
            id = prefix + String.format("%03d", sequence);
            sequence++;
        } while (bookings.findById(id).isPresent());
        return id;
    }
}
