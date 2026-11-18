package com.nhom5.main;

import com.nhom5.model.Room;
import com.nhom5.model.Student;
import com.nhom5.repository.BookingRepository;
import com.nhom5.repository.CsvFileStore;
import com.nhom5.repository.DatabaseManager;
import com.nhom5.repository.RoomRepository;
import com.nhom5.repository.SqliteBookingRepository;
import com.nhom5.repository.SqliteRoomRepository;
import com.nhom5.repository.SqliteStudentRepository;
import com.nhom5.repository.StudentRepository;
import com.nhom5.service.BookingService;
import com.nhom5.service.RoomService;
import com.nhom5.service.StudentService;
import com.nhom5.validator.BookingValidator;
import com.nhom5.validator.CapacityValidator;
import com.nhom5.validator.CompositeBookingValidator;
import com.nhom5.validator.DailyHourLimitValidator;
import com.nhom5.validator.OverlapValidator;
import com.nhom5.validator.RoomActiveValidator;
import com.nhom5.validator.RoomExistsValidator;
import com.nhom5.validator.StudentExistsValidator;
import com.nhom5.validator.TimeRangeValidator;

import java.nio.file.Path;
import java.util.List;

/** Khởi tạo database, file và các service dùng chung. */
public final class AppContext implements AutoCloseable {

    private final DatabaseManager database;
    private final CsvFileStore fileStore;
    private final StudentRepository studentRepository;
    private final RoomRepository roomRepository;
    private final BookingRepository bookingRepository;
    private final StudentService studentService;
    private final RoomService roomService;
    private final BookingService bookingService;

    private AppContext(Path databaseFile, Path dataDirectory) {
        this.database = new DatabaseManager(databaseFile);
        this.fileStore = new CsvFileStore(dataDirectory);
        this.studentRepository = new SqliteStudentRepository(database);
        this.roomRepository = new SqliteRoomRepository(database);
        this.bookingRepository = new SqliteBookingRepository(database);
        this.studentService = new StudentService(studentRepository, roomRepository, bookingRepository, fileStore);
        this.roomService = new RoomService(studentRepository, roomRepository, bookingRepository, fileStore);
        this.bookingService = new BookingService(
                studentRepository,
                roomRepository,
                bookingRepository,
                fileStore,
                defaultValidator()
        );
    }

    public static AppContext open(Path databaseFile, Path dataDirectory, boolean seedIfEmpty) {
        AppContext context = new AppContext(databaseFile, dataDirectory);
        if (seedIfEmpty) {
            context.seed(dataDirectory);
        }
        context.fileStore.exportAll(
                context.studentRepository.findAll(),
                context.roomRepository.findAll(),
                context.bookingRepository.findAll()
        );
        return context;
    }

    private void seed(Path dataDirectory) {
        if (studentRepository.findAll().isEmpty()) {
            List<Student> students = fileStore.readStudents(dataDirectory.resolve("seed").resolve("students.csv"));
            for (Student student : students) {
                studentRepository.save(student);
            }
            fileStore.appendLog("Nhập " + students.size() + " sinh viên mẫu từ file CSV.");
        }
        if (roomRepository.findAll().isEmpty()) {
            List<Room> rooms = fileStore.readRooms(dataDirectory.resolve("seed").resolve("rooms.csv"));
            for (Room room : rooms) {
                roomRepository.save(room);
            }
            fileStore.appendLog("Nhập " + rooms.size() + " phòng mẫu từ file CSV.");
        }
    }

    public static BookingValidator defaultValidator() {
        return new CompositeBookingValidator(List.of(
                new StudentExistsValidator(),
                new RoomExistsValidator(),
                new RoomActiveValidator(),
                new TimeRangeValidator(),
                new OverlapValidator(),
                new CapacityValidator(),
                new DailyHourLimitValidator()
        ));
    }

    public StudentService students() {
        return studentService;
    }

    public RoomService rooms() {
        return roomService;
    }

    public BookingService bookings() {
        return bookingService;
    }

    @Override
    public void close() {
        database.close();
    }
}
