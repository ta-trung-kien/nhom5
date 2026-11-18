package com.nhom5.model;

/** Sinh viên đặt phòng học nhóm. */
public class Student extends User {

    private String className;

    public Student(String studentId, String fullName, String phone, String className, String email) {
        super(studentId, fullName, phone, email);
        this.className = className;
    }

    public String getStudentId() {
        return getId();
    }

    public void setStudentId(String studentId) {
        setId(studentId);
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }
}
