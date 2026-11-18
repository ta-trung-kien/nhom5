package com.nhom5.repository;

import com.nhom5.model.Booking;
import com.nhom5.model.Room;
import com.nhom5.model.RoomFactory;
import com.nhom5.model.RoomStatus;
import com.nhom5.model.RoomType;
import com.nhom5.model.Student;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Đọc file CSV mẫu và ghi lại toàn bộ dữ liệu ra file sau mỗi thay đổi.
 */
public class CsvFileStore {

    private final Path dataDirectory;

    public CsvFileStore(Path dataDirectory) {
        this.dataDirectory = dataDirectory;
    }

    public List<Student> readStudents(Path file) {
        List<Student> students = new ArrayList<>();
        for (List<String> row : readRows(file)) {
            students.add(new Student(row.get(0), row.get(1), row.get(2), row.get(3), row.get(4)));
        }
        return students;
    }

    public List<Room> readRooms(Path file) {
        List<Room> rooms = new ArrayList<>();
        for (List<String> row : readRows(file)) {
            rooms.add(RoomFactory.create(
                    row.get(0),
                    row.get(1),
                    Integer.parseInt(row.get(2)),
                    Integer.parseInt(row.get(3)),
                    RoomType.fromCode(row.get(4)),
                    RoomStatus.fromCode(row.get(5))
            ));
        }
        return rooms;
    }

    public void exportAll(List<Student> students, List<Room> rooms, List<Booking> bookings) {
        try {
            Path exportDir = dataDirectory.resolve("export");
            Files.createDirectories(exportDir);
            writeStudents(exportDir.resolve("students.csv"), students);
            writeRooms(exportDir.resolve("rooms.csv"), rooms);
            writeBookings(exportDir.resolve("bookings.csv"), bookings);
        } catch (IOException ex) {
            throw new IllegalStateException("Không ghi được file dữ liệu: " + ex.getMessage(), ex);
        }
    }

    public void appendLog(String message) {
        try {
            Files.createDirectories(dataDirectory);
            Path logFile = dataDirectory.resolve("booking-log.txt");
            String line = LocalDateTime.now().withNano(0) + " " + message;
            Files.writeString(
                    logFile,
                    line + System.lineSeparator(),
                    StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.APPEND
            );
        } catch (IOException ex) {
            throw new IllegalStateException("Không ghi được nhật ký: " + ex.getMessage(), ex);
        }
    }

    private void writeStudents(Path file, List<Student> students) throws IOException {
        List<String> lines = new ArrayList<>();
        lines.add("student_id,full_name,phone,class_name,email");
        for (Student student : students) {
            lines.add(String.join(",",
                    csv(student.getStudentId()),
                    csv(student.getFullName()),
                    csv(student.getPhone()),
                    csv(student.getClassName()),
                    csv(student.getEmail())
            ));
        }
        Files.write(file, lines, StandardCharsets.UTF_8);
    }

    private void writeRooms(Path file, List<Room> rooms) throws IOException {
        List<String> lines = new ArrayList<>();
        lines.add("room_id,room_name,floor,capacity,room_type,status");
        for (Room room : rooms) {
            lines.add(String.join(",",
                    csv(room.getRoomId()),
                    csv(room.getRoomName()),
                    csv(Integer.toString(room.getFloor())),
                    csv(Integer.toString(room.getCapacity())),
                    csv(room.getRoomType().name()),
                    csv(room.getStatus().name())
            ));
        }
        Files.write(file, lines, StandardCharsets.UTF_8);
    }

    private void writeBookings(Path file, List<Booking> bookings) throws IOException {
        List<String> lines = new ArrayList<>();
        lines.add("booking_id,student_id,room_id,date,start_time,end_time,participants,status,fee,created_at");
        for (Booking booking : bookings) {
            lines.add(String.join(",",
                    csv(booking.getBookingId()),
                    csv(booking.getStudentId()),
                    csv(booking.getRoomId()),
                    csv(booking.getTimeSlot().getDate().toString()),
                    csv(booking.getTimeSlot().getStartTime().toString()),
                    csv(booking.getTimeSlot().getEndTime().toString()),
                    csv(Integer.toString(booking.getParticipantCount())),
                    csv(booking.getStatus().name()),
                    csv(Long.toString(booking.getFee())),
                    csv(booking.getCreatedAt().toString())
            ));
        }
        Files.write(file, lines, StandardCharsets.UTF_8);
    }

    private List<List<String>> readRows(Path file) {
        try {
            if (!Files.exists(file)) {
                throw new IllegalStateException("Không thấy file dữ liệu: " + file.toAbsolutePath());
            }
            List<String> lines = new ArrayList<>(Files.readAllLines(file, StandardCharsets.UTF_8));
            if (!lines.isEmpty() && lines.get(0).startsWith("\uFEFF")) {
                lines.set(0, lines.get(0).substring(1));
            }
            List<List<String>> rows = new ArrayList<>();
            boolean header = true;
            for (String line : lines) {
                if (line.isBlank()) {
                    continue;
                }
                if (header) {
                    header = false;
                    continue;
                }
                rows.add(parseLine(line));
            }
            return rows;
        } catch (IOException ex) {
            throw new IllegalStateException("Không đọc được file: " + file.toAbsolutePath(), ex);
        }
    }

    static List<String> parseLine(String line) {
        List<String> cells = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        boolean inQuotes = false;
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (inQuotes) {
                if (c == '"') {
                    if (i + 1 < line.length() && line.charAt(i + 1) == '"') {
                        current.append('"');
                        i++;
                    } else {
                        inQuotes = false;
                    }
                } else {
                    current.append(c);
                }
            } else if (c == '"') {
                inQuotes = true;
            } else if (c == ',') {
                cells.add(current.toString().trim());
                current.setLength(0);
            } else {
                current.append(c);
            }
        }
        cells.add(current.toString().trim());
        return cells;
    }

    private static String csv(String value) {
        String safe = value == null ? "" : value;
        return "\"" + safe.replace("\"", "\"\"") + "\"";
    }
}
