package com.nhom5.validator;

import com.nhom5.exception.BusinessException;

/** Số người tham gia không được vượt sức chứa. */
public class CapacityValidator implements BookingValidator {

    @Override
    public void validate(BookingContext context) {
        int participants = context.getRequest().getParticipants();
        if (participants <= 0) {
            throw new BusinessException("Số lượng người tham gia phải lớn hơn 0.");
        }
        if (context.getRoom() == null) {
            return;
        }
        int capacity = context.getRoom().getCapacity();
        if (participants > capacity) {
            throw new BusinessException(
                    "Số lượng người tham gia (" + participants
                            + ") vượt quá sức chứa phòng (" + capacity + ")."
            );
        }
    }
}
