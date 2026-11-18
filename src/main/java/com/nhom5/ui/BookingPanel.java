package com.nhom5.ui;

import com.nhom5.exception.BusinessException;
import com.nhom5.model.BookingDetail;
import com.nhom5.model.BookingRequest;
import com.nhom5.model.Room;
import com.nhom5.model.RoomStatus;
import com.nhom5.utils.DateTimes;
import com.nhom5.utils.InputCheck;
import com.nhom5.utils.Money;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.time.LocalDate;

/** Form đặt phòng và xem trước phí. */
public class BookingPanel extends JPanel {

    private final Desk desk;
    private final JTextField studentId = new JTextField(16);
    private final JTextField roomId = new JTextField(16);
    private final JTextField date = new JTextField(LocalDate.now().plusDays(1).toString(), 16);
    private final JTextField start = new JTextField("08:00", 8);
    private final JTextField end = new JTextField("10:00", 8);
    private final JTextField participants = new JTextField("4", 8);
    private final JLabel fee = new JLabel("Nhập đủ thông tin để xem phí.");

    public BookingPanel(Desk desk) {
        super(new GridBagLayout());
        this.desk = desk;
        setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

        int row = 0;
        addRow(row++, "Mã sinh viên", studentId);
        addRow(row++, "Mã phòng", roomId);
        addRow(row++, "Ngày (yyyy-MM-dd)", date);
        addRow(row++, "Giờ bắt đầu (HH:mm)", start);
        addRow(row++, "Giờ kết thúc (HH:mm)", end);
        addRow(row++, "Số người tham gia", participants);

        GridBagConstraints note = constraints(0, row++, 2);
        note.fill = GridBagConstraints.HORIZONTAL;
        JLabel rules = new JLabel("<html>Giờ mở cửa 07:00–21:00. Tối đa 4 giờ mỗi ngày. "
                + "Phòng thường miễn phí, máy chiếu 20.000đ/giờ, seminar 50.000đ/giờ.</html>");
        add(rules, note);

        GridBagConstraints feeCell = constraints(0, row++, 2);
        fee.setFont(fee.getFont().deriveFont(14f));
        add(fee, feeCell);

        JButton book = new JButton("Đặt phòng");
        book.addActionListener(event -> submit());
        add(book, constraints(0, row, 1));

        DocumentListener listener = new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent event) {
                preview();
            }

            @Override
            public void removeUpdate(DocumentEvent event) {
                preview();
            }

            @Override
            public void changedUpdate(DocumentEvent event) {
                preview();
            }
        };
        roomId.getDocument().addDocumentListener(listener);
        date.getDocument().addDocumentListener(listener);
        start.getDocument().addDocumentListener(listener);
        end.getDocument().addDocumentListener(listener);
    }

    public void useStudent(String id) {
        studentId.setText(id == null ? "" : id);
    }

    public void useRoom(String id) {
        roomId.setText(id == null ? "" : id);
        preview();
    }

    private void submit() {
        try {
            BookingDetail detail = desk.app().bookings().book(readRequest());
            desk.setStatus("Đặt phòng thành công. Mã " + detail.getBooking().getBookingId()
                    + ", phí " + detail.getFeeText() + ".");
            desk.reloadBookings();
        } catch (BusinessException ex) {
            desk.showError(ex.getMessage());
        }
    }

    private void preview() {
        try {
            BookingRequest request = readRequest();
            Room room = desk.app().rooms().require(request.getRoomId());
            long amount = room.calculateFee(request.getMinutes());
            String warning = room.getStatus() == RoomStatus.MAINTENANCE ? " · Phòng đang bảo trì, không thể đặt." : "";
            fee.setText(DateTimes.formatDuration(request.getMinutes())
                    + " · " + room.getFeePolicy().describeRate()
                    + " · Phí dự kiến " + Money.formatVnd(amount) + warning);
        } catch (BusinessException ex) {
            fee.setText(ex.getMessage());
        } catch (RuntimeException ex) {
            fee.setText("Nhập đủ thông tin để xem phí.");
        }
    }

    private BookingRequest readRequest() {
        return new BookingRequest(
                InputCheck.normalizeCode(studentId.getText()),
                InputCheck.normalizeCode(roomId.getText()),
                InputCheck.parseDate(date.getText()),
                InputCheck.parseTime(start.getText(), "giờ bắt đầu"),
                InputCheck.parseTime(end.getText(), "giờ kết thúc"),
                InputCheck.parseParticipants(participants.getText())
        );
    }

    private void addRow(int row, String label, JTextField field) {
        add(new JLabel(label), constraints(0, row, 1));
        GridBagConstraints fieldCell = constraints(1, row, 1);
        fieldCell.weightx = 1;
        add(field, fieldCell);
    }

    private static GridBagConstraints constraints(int x, int y, int width) {
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = x;
        constraints.gridy = y;
        constraints.gridwidth = width;
        constraints.insets = new Insets(6, 6, 6, 6);
        constraints.anchor = GridBagConstraints.WEST;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        return constraints;
    }
}
