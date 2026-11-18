package com.nhom5.service;

import com.nhom5.exception.BusinessException;
import com.nhom5.model.Booking;
import com.nhom5.model.Room;
import com.nhom5.model.RoomSchedule;
import com.nhom5.model.RoomStatus;
import com.nhom5.model.RoomType;
import com.nhom5.model.TimeSlot;
import com.nhom5.repository.BookingRepository;
import com.nhom5.repository.CsvFileStore;
import com.nhom5.repository.RoomRepository;
import com.nhom5.repository.StudentRepository;
import com.nhom5.utils.DateTimes;
import com.nhom5.utils.InputCheck;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/** Tìm phòng, xem lịch trống và đổi trạng thái bảo trì. */
public class RoomService {

    private final StudentRepository studentRepository;
    private final RoomRepository roomRepository;
    private final BookingRepository bookingRepository;
    private final CsvFileStore fileStore;

    public RoomService(StudentRepository studentRepository, RoomRepository roomRepository,
                       BookingRepository bookingRepository, CsvFileStore fileStore) {
        this.studentRepository = studentRepository;
        this.roomRepository = roomRepository;
        this.bookingRepository = bookingRepository;
        this.fileStore = fileStore;
    }

    public List<Room> search(String keyword, String roomTypeCode) {
        String text = keyword == null ? "" : keyword.trim().toLowerCase();
        RoomType type = null;
        if (roomTypeCode != null && !roomTypeCode.isBlank()) {
            try {
                type = RoomType.fromCode(roomTypeCode);
            } catch (IllegalArgumentException ex) {
                throw new BusinessException("Loại phòng không hợp lệ.");
            }
        }
        List<Room> result = new ArrayList<>();
        for (Room room : roomRepository.findAll()) {
            boolean matchText = text.isEmpty()
                    || room.getRoomId().toLowerCase().contains(text)
                    || room.getRoomName().toLowerCase().contains(text);
            boolean matchType = type == null || room.getRoomType() == type;
            if (matchText && matchType) {
                result.add(room);
            }
        }
        return result;
    }

    public Room require(String roomId) {
        String id = InputCheck.normalizeCode(roomId);
        if (id.isBlank()) {
            throw new BusinessException("Vui lòng nhập mã phòng.");
        }
        return roomRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Không tìm thấy phòng với mã " + id + "."));
    }

    public Room updateStatus(String roomId, String statusCode) {
        Room room = require(roomId);
        RoomStatus status;
        try {
            status = RoomStatus.fromCode(statusCode);
        } catch (IllegalArgumentException ex) {
            throw new BusinessException("Trạng thái phòng không hợp lệ.");
        }
        room.setStatus(status);
        roomRepository.update(room);
        sync("Đổi trạng thái " + room.getRoomId() + " thành " + status.getLabel());
        return room;
    }

    public RoomSchedule schedule(String roomId, LocalDate date) {
        Room room = require(roomId);
        if (date == null) {
            throw new BusinessException("Vui lòng chọn ngày cần xem lịch.");
        }
        RoomSchedule schedule = new RoomSchedule(room.getRoomId(), date);
        List<TimeSlot> booked = new ArrayList<>();
        for (Booking booking : bookingRepository.findByRoomAndDate(room.getRoomId(), date)) {
            if (booking.isActive()) {
                booked.add(booking.getTimeSlot());
            }
        }
        booked.sort(Comparator.comparing(TimeSlot::getStartTime));
        schedule.setBooked(booked);
        schedule.setFree(freeSlots(booked));
        return schedule;
    }

    private List<TimeSlot> freeSlots(List<TimeSlot> booked) {
        List<TimeSlot> free = new ArrayList<>();
        LocalTime cursor = DateTimes.OPENING;
        for (TimeSlot slot : booked) {
            LocalTime start = slot.getStartTime().isBefore(DateTimes.OPENING)
                    ? DateTimes.OPENING
                    : slot.getStartTime();
            LocalTime end = slot.getEndTime().isAfter(DateTimes.CLOSING)
                    ? DateTimes.CLOSING
                    : slot.getEndTime();
            if (start.isAfter(cursor)) {
                free.add(new TimeSlot(slot.getDate(), cursor, start));
            }
            if (end.isAfter(cursor)) {
                cursor = end;
            }
        }
        if (cursor.isBefore(DateTimes.CLOSING)) {
            LocalDate date = booked.isEmpty() ? null : booked.get(0).getDate();
            free.add(new TimeSlot(date, cursor, DateTimes.CLOSING));
        }
        return free;
    }

    private void sync(String message) {
        fileStore.exportAll(studentRepository.findAll(), roomRepository.findAll(), bookingRepository.findAll());
        fileStore.appendLog(message);
    }
}
