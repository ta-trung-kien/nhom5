package com.nhom5.validator;

import com.nhom5.exception.BusinessException;
import com.nhom5.model.RoomStatus;

/** Không đặt phòng đang bảo trì. */
public class RoomActiveValidator implements BookingValidator {

    @Override
    public void validate(BookingContext context) {
        if (context.getRoom() == null) {
            return;
        }
        if (context.getRoom().getStatus() == RoomStatus.MAINTENANCE) {
            throw new BusinessException("Phòng đang bảo trì, không thể đặt.");
        }
        if (!context.getRoom().isAvailableForBooking()) {
            throw new BusinessException("Phòng hiện không thể đặt.");
        }
    }
}
