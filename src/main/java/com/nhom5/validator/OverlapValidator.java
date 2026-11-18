package com.nhom5.validator;

import com.nhom5.exception.BusinessException;
import com.nhom5.model.Booking;
import com.nhom5.model.BookingRequest;

/** Không đặt trùng lịch đã có của cùng phòng. */
public class OverlapValidator implements BookingValidator {

    @Override
    public void validate(BookingContext context) {
        BookingRequest request = context.getRequest();
        for (Booking existing : context.getRoomBookings()) {
            if (!existing.isActive()) {
                continue;
            }
            if (existing.getTimeSlot().overlaps(request.toTimeSlot())) {
                throw new BusinessException(
                        "Phòng bị trùng lịch với mã " + existing.getBookingId()
                                + " (" + existing.getTimeSlot().getStartTime()
                                + " - " + existing.getTimeSlot().getEndTime() + ")."
                );
            }
        }
    }
}
