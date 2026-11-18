package com.nhom5.ui;

import com.nhom5.model.Student;

/** Mục trong danh sách chọn sinh viên trên giao diện. */
public class StudentChoice {

    private final Student student;

    public StudentChoice(Student student) {
        this.student = student;
    }

    public Student getStudent() {
        return student;
    }

    @Override
    public String toString() {
        return student.getStudentId() + " · " + student.getFullName() + " · " + student.getClassName();
    }
}
