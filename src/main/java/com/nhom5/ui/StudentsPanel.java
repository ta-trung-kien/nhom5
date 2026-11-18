package com.nhom5.ui;

import com.nhom5.exception.BusinessException;
import com.nhom5.model.Student;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

/** Thêm sinh viên và xem danh sách. */
public class StudentsPanel extends JPanel {

    private final Desk desk;
    private final JTextField studentId = new JTextField(16);
    private final JTextField fullName = new JTextField(16);
    private final JTextField phone = new JTextField(16);
    private final JTextField className = new JTextField(16);
    private final JTextField email = new JTextField(16);
    private final DefaultTableModel model = new DefaultTableModel(
            new Object[]{"Mã", "Họ tên", "Điện thoại", "Lớp", "Email"}, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };

    public StudentsPanel(Desk desk) {
        super(new BorderLayout(12, 0));
        this.desk = desk;
        setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(BorderFactory.createTitledBorder("Thêm sinh viên"));
        addField(form, 0, "Mã sinh viên", studentId);
        addField(form, 1, "Họ tên", fullName);
        addField(form, 2, "Số điện thoại", phone);
        addField(form, 3, "Lớp", className);
        addField(form, 4, "Email", email);
        JButton create = new JButton("Thêm sinh viên");
        create.addActionListener(event -> createStudent());
        form.add(create, constraints(1, 5));
        add(form, BorderLayout.WEST);

        JTable table = new JTable(model);
        table.setRowHeight(26);
        table.setFillsViewportHeight(true);
        add(new JScrollPane(table), BorderLayout.CENTER);
    }

    public void reload() {
        model.setRowCount(0);
        for (Student student : desk.app().students().findAll()) {
            model.addRow(new Object[]{
                    student.getStudentId(),
                    student.getFullName(),
                    student.getPhone(),
                    student.getClassName(),
                    student.getEmail()
            });
        }
    }

    private void createStudent() {
        try {
            Student student = desk.app().students().create(
                    studentId.getText(),
                    fullName.getText(),
                    phone.getText(),
                    className.getText(),
                    email.getText()
            );
            studentId.setText("");
            fullName.setText("");
            phone.setText("");
            className.setText("");
            email.setText("");
            desk.reloadStudents(student.getStudentId());
            desk.setStatus("Đã thêm sinh viên " + student.getStudentId() + ".");
        } catch (BusinessException ex) {
            desk.showError(ex.getMessage());
        }
    }

    private static void addField(JPanel form, int row, String label, JTextField field) {
        form.add(new JLabel(label), constraints(0, row));
        GridBagConstraints fieldCell = constraints(1, row);
        fieldCell.weightx = 1;
        form.add(field, fieldCell);
    }

    private static GridBagConstraints constraints(int x, int y) {
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = x;
        constraints.gridy = y;
        constraints.insets = new Insets(5, 5, 5, 5);
        constraints.anchor = GridBagConstraints.WEST;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        return constraints;
    }
}
