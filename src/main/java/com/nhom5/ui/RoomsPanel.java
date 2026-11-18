package com.nhom5.ui;

import com.nhom5.exception.BusinessException;
import com.nhom5.model.Room;
import com.nhom5.model.RoomSchedule;
import com.nhom5.model.RoomStatus;
import com.nhom5.model.TimeSlot;
import com.nhom5.utils.DateTimes;
import com.nhom5.utils.InputCheck;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/** Danh sách phòng, tìm kiếm và lịch trống. */
public class RoomsPanel extends JPanel {

    private final Desk desk;
    private final JTextField keyword = new JTextField(16);
    private final JComboBox<String> type = new JComboBox<>(new String[]{
            "Tất cả", "Phòng thường", "Phòng có máy chiếu", "Phòng họp seminar"
    });
    private final JTextField date = new JTextField(LocalDate.now().plusDays(1).toString(), 12);
    private final JTextArea schedule = new JTextArea(5, 40);
    private final DefaultTableModel model = new DefaultTableModel(
            new Object[]{"Mã", "Tên phòng", "Tầng", "Sức chứa", "Loại", "Trạng thái", "Phí"}, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    private final JTable table = new JTable(model);
    private final List<Room> shown = new ArrayList<>();
    private final JButton statusButton = new JButton("Đổi trạng thái");

    public RoomsPanel(Desk desk) {
        super(new BorderLayout(0, 10));
        this.desk = desk;
        setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        setBackground(new Color(0xF4EFE4));

        JPanel filters = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        filters.setOpaque(false);
        filters.add(new JLabel("Mã hoặc tên"));
        filters.add(keyword);
        filters.add(new JLabel("Loại phòng"));
        filters.add(type);
        JButton search = new JButton("Tìm phòng");
        search.addActionListener(event -> reload());
        filters.add(search);
        JButton clear = new JButton("Xem tất cả");
        clear.addActionListener(event -> {
            keyword.setText("");
            type.setSelectedIndex(0);
            reload();
        });
        filters.add(clear);
        add(filters, BorderLayout.NORTH);

        table.setRowHeight(26);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        table.setFillsViewportHeight(true);

        JPanel center = new JPanel(new BorderLayout(0, 8));
        center.setOpaque(false);
        center.add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel scheduleBar = new JPanel(new BorderLayout(8, 8));
        scheduleBar.setOpaque(false);
        JPanel scheduleActions = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        scheduleActions.setOpaque(false);
        scheduleActions.add(new JLabel("Ngày xem lịch (yyyy-MM-dd)"));
        scheduleActions.add(date);
        JButton view = new JButton("Xem lịch trống");
        view.addActionListener(event -> showSchedule());
        scheduleActions.add(view);
        JButton book = new JButton("Đặt phòng này");
        book.addActionListener(event -> bookSelected());
        scheduleActions.add(book);
        statusButton.addActionListener(event -> toggleStatus());
        scheduleActions.add(statusButton);
        schedule.setEditable(false);
        schedule.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        schedule.setText("Chọn một phòng và bấm Xem lịch trống.");
        scheduleBar.add(scheduleActions, BorderLayout.NORTH);
        scheduleBar.add(new JScrollPane(schedule), BorderLayout.CENTER);
        center.add(scheduleBar, BorderLayout.SOUTH);
        add(center, BorderLayout.CENTER);
    }

    public void reload() {
        String typeCode = switch (type.getSelectedIndex()) {
            case 1 -> "NORMAL";
            case 2 -> "PROJECTOR";
            case 3 -> "SEMINAR";
            default -> "";
        };
        shown.clear();
        model.setRowCount(0);
        try {
            for (Room room : desk.app().rooms().search(keyword.getText(), typeCode)) {
                shown.add(room);
                model.addRow(new Object[]{
                        room.getRoomId(),
                        room.getRoomName(),
                        room.getFloor(),
                        room.getCapacity(),
                        room.getRoomType().getLabel(),
                        room.getStatus().getLabel(),
                        room.getFeePolicy().describeRate()
                });
            }
            desk.setStatus("Đang hiển thị " + shown.size() + " phòng.");
        } catch (BusinessException ex) {
            desk.showError(ex.getMessage());
        }
    }

    private void showSchedule() {
        Room room = selected();
        if (room == null) {
            desk.showError("Hãy chọn một phòng trong bảng.");
            return;
        }
        try {
            RoomSchedule day = desk.app().rooms().schedule(room.getRoomId(), InputCheck.parseDate(date.getText()));
            schedule.setText(describe(day));
            desk.setStatus("Đã xem lịch " + room.getRoomId() + " ngày " + day.getDate() + ".");
        } catch (BusinessException ex) {
            desk.showError(ex.getMessage());
        }
    }

    private void bookSelected() {
        Room room = selected();
        if (room == null) {
            desk.showError("Hãy chọn một phòng trong bảng.");
            return;
        }
        desk.openBooking(room.getRoomId());
    }

    private void toggleStatus() {
        Room room = selected();
        if (room == null) {
            desk.showError("Hãy chọn một phòng trong bảng.");
            return;
        }
        String next = room.getStatus() == RoomStatus.ACTIVE ? "MAINTENANCE" : "ACTIVE";
        try {
            Room updated = desk.app().rooms().updateStatus(room.getRoomId(), next);
            reload();
            desk.setStatus("Đã cập nhật " + updated.getRoomId() + " thành " + updated.getStatus().getLabel() + ".");
        } catch (BusinessException ex) {
            desk.showError(ex.getMessage());
        }
    }

    private Room selected() {
        int row = table.getSelectedRow();
        if (row < 0 || row >= shown.size()) {
            return null;
        }
        return shown.get(row);
    }

    private static String describe(RoomSchedule day) {
        StringBuilder text = new StringBuilder();
        text.append("Phòng ").append(day.getRoomId()).append(" ngày ").append(day.getDate()).append('\n');
        text.append("Giờ mở cửa ").append(DateTimes.formatTime(DateTimes.OPENING))
                .append(" - ").append(DateTimes.formatTime(DateTimes.CLOSING)).append('\n');
        text.append("Đã đặt: ").append(ranges(day.getBooked(), "không có")).append('\n');
        text.append("Còn trống: ").append(ranges(day.getFree(), "không còn"));
        return text.toString();
    }

    private static String ranges(List<TimeSlot> slots, String empty) {
        if (slots.isEmpty()) {
            return empty;
        }
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < slots.size(); i++) {
            if (i > 0) {
                text.append(", ");
            }
            TimeSlot slot = slots.get(i);
            text.append(DateTimes.formatTime(slot.getStartTime()))
                    .append(" - ")
                    .append(DateTimes.formatTime(slot.getEndTime()));
        }
        return text.toString();
    }
}
