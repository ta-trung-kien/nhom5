package com.nhom5.validator;

import com.nhom5.exception.BusinessException;

/** Sinh viên phải tồn tại trong hệ thống. */
public class StudentExistsValidator implements BookingValidator {

    @Override
    public void validate(BookingContext context) {
        String studentId = context.getRequest().getStudentId();
        if (studentId == null || studentId.isBlank()) {
            throw new BusinessException("Vui lòng nhập mã sinh viên.");
        }
        if (context.getStudent() == null) {
            throw new BusinessException("Không tìm thấy sinh viên với mã " + studentId + ".");
        }
    }
}
