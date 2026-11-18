package com.nhom5.validator;

import com.nhom5.exception.BusinessException;
import com.nhom5.model.BookingRequest;
import com.nhom5.utils.DateTimes;

import java.time.LocalDateTime;

/** Thời gian phải hợp lệ, trong giờ mở cửa và không nằm trong quá khứ. */
public class TimeRangeValidator implements BookingValidator {

    @Override
    public void validate(BookingContext context) {
        BookingRequest request = context.getRequest();
        if (request.getDate() == null || request.getStartTime() == null || request.getEndTime() == null) {
            throw new BusinessException("Vui lòng nhập đủ ngày và khung giờ đặt phòng.");
        }
        if (!request.getEndTime().isAfter(request.getStartTime())) {
            throw new BusinessException("Thời gian kết thúc phải sau thời gian bắt đầu.");
        }
        if (request.getStartTime().isBefore(DateTimes.OPENING) || request.getEndTime().isAfter(DateTimes.CLOSING)) {
            throw new BusinessException("Thời gian đặt phải nằm trong khung giờ hoạt động từ 07:00 đến 21:00.");
        }
        LocalDateTime start = LocalDateTime.of(request.getDate(), request.getStartTime());
        if (!start.isAfter(LocalDateTime.now())) {
            throw new BusinessException("Không thể đặt phòng trong quá khứ.");
        }
    }
}
