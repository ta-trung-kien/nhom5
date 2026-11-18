package com.nhom5.service;

import com.nhom5.exception.BusinessException;
import com.nhom5.model.Student;
import com.nhom5.repository.BookingRepository;
import com.nhom5.repository.CsvFileStore;
import com.nhom5.repository.RoomRepository;
import com.nhom5.repository.StudentRepository;
import com.nhom5.utils.InputCheck;

import java.util.List;

/** Thêm sinh viên và tra cứu danh sách. */
public class StudentService {

    private final StudentRepository studentRepository;
    private final RoomRepository roomRepository;
    private final BookingRepository bookingRepository;
    private final CsvFileStore fileStore;

    public StudentService(StudentRepository studentRepository, RoomRepository roomRepository,
                          BookingRepository bookingRepository, CsvFileStore fileStore) {
        this.studentRepository = studentRepository;
        this.roomRepository = roomRepository;
        this.bookingRepository = bookingRepository;
        this.fileStore = fileStore;
    }

    public Student create(String studentId, String fullName, String phone, String className, String email) {
        String id = InputCheck.requireStudentId(studentId);
        if (studentRepository.findById(id).isPresent()) {
            throw new BusinessException("Mã sinh viên đã tồn tại.");
        }
        Student student = new Student(
                id,
                InputCheck.requireText(fullName, "Vui lòng nhập họ tên."),
                InputCheck.requirePhone(phone),
                InputCheck.requireText(className, "Vui lòng nhập lớp."),
                InputCheck.requireEmail(email)
        );
        studentRepository.save(student);
        sync("Thêm sinh viên " + id);
        return student;
    }

    public List<Student> findAll() {
        return studentRepository.findAll();
    }

    public Student require(String studentId) {
        String id = InputCheck.normalizeCode(studentId);
        return studentRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Không tìm thấy sinh viên với mã " + id + "."));
    }

    private void sync(String message) {
        fileStore.exportAll(studentRepository.findAll(), roomRepository.findAll(), bookingRepository.findAll());
        fileStore.appendLog(message);
    }
}
