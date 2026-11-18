package com.nhom5.validator;

/** Một điều kiện đặt phòng. Các điều kiện được ghép lại và chạy lần lượt. */
public interface BookingValidator {

    void validate(BookingContext context);
}
