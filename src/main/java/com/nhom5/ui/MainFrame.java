package com.nhom5.ui;

import com.nhom5.main.AppContext;
import com.nhom5.model.Student;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.WindowConstants;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/** Cửa sổ Swing của hệ thống đặt phòng. */
public class MainFrame extends JFrame implements Desk {

    private final AppContext app;
    private final JComboBox<StudentChoice> students = new JComboBox<>();
    private final JLabel status = new JLabel("Sẵn sàng.");
    private final JTabbedPane tabs = new JTabbedPane();
    private final RoomsPanel roomsPanel;
    private final BookingPanel bookingPanel;
    private final BookingsPanel bookingsPanel;
    private final StudentsPanel studentsPanel;
    private boolean fillingStudents;

    public MainFrame(AppContext app) {
        super("Đặt phòng học nhóm");
        this.app = app;
        this.roomsPanel = new RoomsPanel(this);
        this.bookingPanel = new BookingPanel(this);
        this.bookingsPanel = new BookingsPanel(this);
        this.studentsPanel = new StudentsPanel(this);

        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setMinimumSize(new Dimension(980, 640));
        setSize(1100, 720);
        setLocationRelativeTo(null);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(new Color(0xF4EFE4));
        root.add(header(), BorderLayout.NORTH);
        tabs.setFont(font(14));
        tabs.addTab("Phòng học", roomsPanel);
        tabs.addTab("Đặt phòng", bookingPanel);
        tabs.addTab("Lịch của tôi", bookingsPanel);
        tabs.addTab("Sinh viên", studentsPanel);
        tabs.addChangeListener(event -> {
            if (tabs.getSelectedComponent() == bookingsPanel) {
                bookingsPanel.reload();
            }
        });
        root.add(tabs, BorderLayout.CENTER);
        status.setFont(font(14));
        status.setBorder(BorderFactory.createEmptyBorder(8, 14, 10, 14));
        root.add(status, BorderLayout.SOUTH);
        setContentPane(root);

        students.addActionListener(event -> {
            if (!fillingStudents) {
                bookingPanel.useStudent(currentStudentId());
                bookingsPanel.reload();
            }
        });
        reloadStudents(null);
        roomsPanel.reload();

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent event) {
                app.close();
            }
        });
    }

    private JPanel header() {
        JPanel header = new JPanel(new BorderLayout(16, 0));
        header.setBackground(new Color(0x143528));
        header.setBorder(BorderFactory.createEmptyBorder(14, 16, 14, 16));

        JLabel title = new JLabel("Đặt phòng học nhóm");
        title.setForeground(new Color(0xF6F1E6));
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        header.add(title, BorderLayout.WEST);

        JLabel who = new JLabel("Sinh viên đang dùng");
        who.setForeground(new Color(0xD7C7A4));
        who.setFont(font(12));
        students.setFont(font(14));
        JPanel picker = new JPanel(new BorderLayout(0, 4));
        picker.setOpaque(false);
        picker.add(who, BorderLayout.NORTH);
        picker.add(students, BorderLayout.CENTER);
        header.add(picker, BorderLayout.EAST);
        return header;
    }

    @Override
    public AppContext app() {
        return app;
    }

    @Override
    public String currentStudentId() {
        StudentChoice choice = (StudentChoice) students.getSelectedItem();
        if (choice == null) {
            return "";
        }
        return choice.getStudent().getStudentId();
    }

    @Override
    public void setStatus(String message) {
        status.setForeground(new Color(0x1D6B45));
        status.setText(message);
    }

    @Override
    public void showError(String message) {
        status.setForeground(new Color(0x8D2F2A));
        status.setText(message);
        JOptionPane.showMessageDialog(this, message, "Không thực hiện được", JOptionPane.WARNING_MESSAGE);
    }

    @Override
    public void openBooking(String roomId) {
        bookingPanel.useRoom(roomId);
        tabs.setSelectedComponent(bookingPanel);
    }

    @Override
    public void reloadStudents(String preferredStudentId) {
        String keep = preferredStudentId == null || preferredStudentId.isBlank()
                ? currentStudentId()
                : preferredStudentId;
        fillingStudents = true;
        students.removeAllItems();
        for (Student student : app.students().findAll()) {
            students.addItem(new StudentChoice(student));
        }
        for (int i = 0; i < students.getItemCount(); i++) {
            if (students.getItemAt(i).getStudent().getStudentId().equalsIgnoreCase(keep)) {
                students.setSelectedIndex(i);
                break;
            }
        }
        fillingStudents = false;
        bookingPanel.useStudent(currentStudentId());
        bookingsPanel.reload();
        studentsPanel.reload();
    }

    @Override
    public void reloadBookings() {
        bookingsPanel.reload();
    }

    @Override
    public void reloadRooms() {
        roomsPanel.reload();
    }

    private static Font font(int size) {
        return new Font("Segoe UI", Font.PLAIN, size);
    }
}
