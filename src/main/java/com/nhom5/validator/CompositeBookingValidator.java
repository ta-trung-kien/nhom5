package com.nhom5.validator;

import java.util.List;

/** Chạy lần lượt các điều kiện. Gặp lỗi đầu tiên thì dừng. */
public class CompositeBookingValidator implements BookingValidator {

    private final List<BookingValidator> validators;

    public CompositeBookingValidator(List<BookingValidator> validators) {
        this.validators = List.copyOf(validators);
    }

    @Override
    public void validate(BookingContext context) {
        for (BookingValidator validator : validators) {
            validator.validate(context);
        }
    }
}
