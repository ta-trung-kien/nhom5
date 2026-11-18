package com.nhom5.service;

import com.nhom5.exception.BusinessException;
import com.nhom5.model.Booking;
import com.nhom5.model.BookingDetail;
import com.nhom5.model.BookingRequest;
import com.nhom5.model.BookingStatus;
import com.nhom5.model.Room;
import com.nhom5.model.Student;
import com.nhom5.model.TimeSlot;
import com.nhom5.repository.BookingRepository;
import com.nhom5.repository.CsvFileStore;
import com.nhom5.repository.RoomRepository;
import com.nhom5.repository.StudentRepository;
import com.nhom5.utils.IdGenerator;
import com.nhom5.utils.InputCheck;
import com.nhom5.utils.Money;
import com.nhom5.validator.BookingContext;
import com.nhom5.validator.BookingValidator;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/** Đặt phòng, hủy lịch và tính phí. */
public class BookingService {

    private final StudentRepository studentRepository;
    private final RoomRepository roomRepository;
    private final BookingRepository bookingRepository;
    private final CsvFileStore fileStore;
    private final BookingValidator validator;

    public BookingService(StudentRepository studentRepository, RoomRepository roomRepository,
                          BookingRepository bookingRepository, CsvFileStore fileStore,
                          BookingValidator validator) {
        this.studentRepository = studentRepository;
        this.roomRepository = roomRepository;
        this.bookingRepository = bookingRepository;
        this.fileStore = fileStore;
        this.validator = validator;
    }

    public BookingDetail book(BookingRequest request) {
        BookingContext context = loadContext(request);
        validator.validate(context);

        Room room = context.getRoom();
        Student student = context.getStudent();
        long fee = room.calculateFee(request.getMinutes());
        Booking booking = new Booking(
                "TEMP",
                student.getStudentId(),
                room.getRoomId(),
                new TimeSlot(request.getDate(), request.getStartTime(), request.getEndTime()),
                request.getParticipants(),
                BookingStatus.BOOKED,
                fee,
                LocalDateTime.now().withNano(0)
        );
        booking.setBookingId(IdGenerator.nextBookingId(bookingRepository, booking));
        bookingRepository.save(booking);
        sync("Đặt phòng " + booking.getBookingId()
                + " sinh viên " + student.getStudentId()
                + " phòng " + room.getRoomId()
                + " phí " + Money.formatVnd(fee));
        return toDetail(booking);
    }

    public BookingDetail cancel(String bookingId, String studentId) {
        String id = InputCheck.normalizeCode(bookingId);
        if (id.isBlank()) {
            throw new BusinessException("Vui lòng nhập mã đặt phòng.");
        }
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Không tìm thấy mã đặt phòng " + id + "."));

        String ownerId = InputCheck.normalizeCode(studentId);
        if (ownerId.isBlank()) {
            throw new BusinessException("Vui lòng nhập mã sinh viên.");
        }
        if (studentRepository.findById(ownerId).isEmpty()) {
            throw new BusinessException("Không tìm thấy sinh viên với mã " + ownerId + ".");
        }
        if (!booking.getStudentId().equals(ownerId)) {
            throw new BusinessException("Không thể hủy lịch đặt không thuộc về sinh viên này.");
        }
        booking.markCancelled();
        bookingRepository.update(booking);
        sync("Hủy lịch " + booking.getBookingId() + " bởi " + ownerId);
        return toDetail(booking);
    }

    public List<BookingDetail> findByStudent(String studentId) {
        String id = InputCheck.normalizeCode(studentId);
        if (studentRepository.findById(id).isEmpty()) {
            throw new BusinessException("Không tìm thấy sinh viên với mã " + id + ".");
        }
        List<BookingDetail> details = new ArrayList<>();
        for (Booking booking : bookingRepository.findByStudent(id)) {
            details.add(toDetail(booking));
        }
        return details;
    }

    /** Xem trước phí. Vẫn báo lỗi nếu phòng hoặc giờ không hợp lệ. */
    public long previewFee(BookingRequest request) {
        if (request.getRoomId() == null || request.getRoomId().isBlank()) {
            throw new BusinessException("Vui lòng nhập mã phòng.");
        }
        Room room = roomRepository.findById(request.getRoomId())
                .orElseThrow(() -> new BusinessException("Không tìm thấy phòng với mã " + request.getRoomId() + "."));
        if (request.getDate() == null || request.getStartTime() == null || request.getEndTime() == null
                || !request.getEndTime().isAfter(request.getStartTime())) {
            throw new BusinessException("Nhập giờ kết thúc sau giờ bắt đầu để xem phí.");
        }
        return room.calculateFee(request.getMinutes());
    }

    private BookingContext loadContext(BookingRequest request) {
        Student student = null;
        if (request.getStudentId() != null && !request.getStudentId().isBlank()) {
            student = studentRepository.findById(request.getStudentId()).orElse(null);
        }
        Room room = null;
        List<Booking> roomBookings = List.of();
        if (request.getRoomId() != null && !request.getRoomId().isBlank()) {
            room = roomRepository.findById(request.getRoomId()).orElse(null);
            if (room != null && request.getDate() != null) {
                roomBookings = bookingRepository.findByRoomAndDate(room.getRoomId(), request.getDate());
            }
        }
        List<Booking> dayBookings = List.of();
        if (request.getStudentId() != null && !request.getStudentId().isBlank() && request.getDate() != null) {
            dayBookings = bookingRepository.findByStudentAndDate(request.getStudentId(), request.getDate());
        }
        return new BookingContext(request, student, room, roomBookings, dayBookings);
    }

    private BookingDetail toDetail(Booking booking) {
        Student student = studentRepository.findById(booking.getStudentId()).orElse(null);
        Room room = roomRepository.findById(booking.getRoomId()).orElse(null);
        return new BookingDetail(booking, student, room);
    }

    private void sync(String message) {
        fileStore.exportAll(studentRepository.findAll(), roomRepository.findAll(), bookingRepository.findAll());
        fileStore.appendLog(message);
    }
}
