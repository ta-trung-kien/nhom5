package com.nhom5.validator;

import com.nhom5.exception.BusinessException;

/** Phòng phải tồn tại. */
public class RoomExistsValidator implements BookingValidator {

    @Override
    public void validate(BookingContext context) {
        String roomId = context.getRequest().getRoomId();
        if (roomId == null || roomId.isBlank()) {
            throw new BusinessException("Vui lòng nhập mã phòng.");
        }
        if (context.getRoom() == null) {
            throw new BusinessException("Không tìm thấy phòng với mã " + roomId + ".");
        }
    }
}
