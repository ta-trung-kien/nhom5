package com.nhom5.repository;

import com.nhom5.model.Student;

import java.util.List;
import java.util.Optional;

public class SqliteStudentRepository implements StudentRepository {

    private final DatabaseManager database;

    public SqliteStudentRepository(DatabaseManager database) {
        this.database = database;
    }

    @Override
    public void save(Student student) {
        database.update(
                "INSERT INTO students(student_id, full_name, phone, class_name, email) VALUES (?, ?, ?, ?, ?)",
                student.getStudentId(),
                student.getFullName(),
                student.getPhone(),
                student.getClassName(),
                student.getEmail()
        );
    }

    @Override
    public Optional<Student> findById(String studentId) {
        return database.query(
                "SELECT * FROM students WHERE student_id = ?",
                this::map,
                studentId
        ).stream().findFirst();
    }

    @Override
    public List<Student> findAll() {
        return database.query("SELECT * FROM students ORDER BY student_id", this::map);
    }

    private Student map(java.sql.ResultSet resultSet) throws java.sql.SQLException {
        return new Student(
                resultSet.getString("student_id"),
                resultSet.getString("full_name"),
                resultSet.getString("phone"),
                resultSet.getString("class_name"),
                resultSet.getString("email")
        );
    }
}
