package com.nhom5.repository;

import com.nhom5.model.Student;

import java.util.List;
import java.util.Optional;

public interface StudentRepository {

    void save(Student student);

    Optional<Student> findById(String studentId);

    List<Student> findAll();
}
