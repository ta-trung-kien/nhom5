package com.nhom5.ui;

import com.nhom5.exception.BusinessException;
import com.nhom5.model.BookingDetail;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;

/** Lịch đặt của sinh viên và hủy theo mã. */
public class BookingsPanel extends JPanel {

    private final Desk desk;
    private final JTextField bookingId = new JTextField(16);
    private final JTextField studentId = new JTextField(12);
    private final DefaultTableModel model = new DefaultTableModel(
            new Object[]{"Mã", "Phòng", "Thời gian", "Số người", "Phí", "Trạng thái"}, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    private final JTable table = new JTable(model);
    private final List<BookingDetail> shown = new ArrayList<>();

    public BookingsPanel(Desk desk) {
        super(new BorderLayout(0, 10));
        this.desk = desk;
        setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        table.setRowHeight(26);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        table.setFillsViewportHeight(true);
        table.getSelectionModel().addListSelectionListener(event -> {
            int row = table.getSelectedRow();
            if (!event.getValueIsAdjusting() && row >= 0 && row < shown.size()) {
                bookingId.setText(shown.get(row).getBooking().getBookingId());
            }
        });
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        actions.add(new JLabel("Mã đặt phòng"));
        actions.add(bookingId);
        actions.add(new JLabel("Mã sinh viên thực hiện"));
        actions.add(studentId);
        JButton cancel = new JButton("Hủy lịch");
        cancel.addActionListener(event -> cancelSelected());
        actions.add(cancel);
        add(actions, BorderLayout.SOUTH);
    }

    public void useStudent(String id) {
        studentId.setText(id == null ? "" : id);
    }

    public void reload() {
        shown.clear();
        model.setRowCount(0);
        String id = desk.currentStudentId();
        if (id == null || id.isBlank()) {
            return;
        }
        useStudent(id);
        try {
            for (BookingDetail detail : desk.app().bookings().findByStudent(id)) {
                shown.add(detail);
                model.addRow(new Object[]{
                        detail.getBooking().getBookingId(),
                        detail.getBooking().getRoomId() + " · " + (detail.getRoom() == null ? "" : detail.getRoom().getRoomName()),
                        detail.getBooking().getTimeSlot().getDate()
                                + "  " + detail.getBooking().getTimeSlot().getStartTime()
                                + " - " + detail.getBooking().getTimeSlot().getEndTime()
                                + " (" + detail.getDurationText() + ")",
                        detail.getBooking().getParticipantCount(),
                        detail.getFeeText(),
                        detail.getBooking().getStatus().getLabel()
                });
            }
        } catch (BusinessException ex) {
            desk.showError(ex.getMessage());
        }
    }

    private void cancelSelected() {
        try {
            BookingDetail detail = desk.app().bookings().cancel(bookingId.getText(), studentId.getText());
            desk.setStatus("Đã hủy lịch " + detail.getBooking().getBookingId() + ".");
            reload();
        } catch (BusinessException ex) {
            desk.showError(ex.getMessage());
        }
    }
}
