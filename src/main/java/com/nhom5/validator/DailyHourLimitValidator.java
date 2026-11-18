package com.nhom5.validator;

import com.nhom5.exception.BusinessException;
import com.nhom5.model.Booking;
import com.nhom5.utils.DateTimes;

/** Mỗi sinh viên chỉ được đặt tối đa 4 giờ trong một ngày. */
public class DailyHourLimitValidator implements BookingValidator {

    @Override
    public void validate(BookingContext context) {
        long used = 0L;
        for (Booking booking : context.getStudentBookingsOnDate()) {
            if (booking.isActive()) {
                used += booking.getTimeSlot().getMinutes();
            }
        }
        long requested = context.getRequest().getMinutes();
        if (used + requested > DateTimes.MAX_MINUTES_PER_DAY) {
            String usedText = used == 0
                    ? "Chưa có lịch nào trong ngày"
                    : "Đã đặt " + DateTimes.formatDuration(used);
            throw new BusinessException(
                    "Sinh viên không được đặt quá 4 giờ trong một ngày. "
                            + usedText + ", yêu cầu thêm "
                            + DateTimes.formatDuration(requested) + "."
            );
        }
    }
}
