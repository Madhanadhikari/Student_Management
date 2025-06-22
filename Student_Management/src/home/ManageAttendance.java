package home;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class ManageAttendance extends JFrame {
    private DefaultTableModel model;

    public ManageAttendance(String teacherUsername) {
        setTitle("Manage Attendance - " + teacherUsername);
        setSize(650, 400);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        String[] columns = {"ID", "Student", "Subject", "Date", "Status"};
        model = new DefaultTableModel(columns, 0);
        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);

        JButton addBtn = new JButton("Add Attendance");
        JButton deleteBtn = new JButton("Delete Selected");
        JButton backBtn = new JButton("Back");

        JPanel bottomPanel = new JPanel();
        bottomPanel.add(addBtn);
        bottomPanel.add(deleteBtn);
        bottomPanel.add(backBtn);

        add(scrollPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        loadData();

        addBtn.addActionListener(e -> openAttendanceForm(teacherUsername));

        deleteBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) return;
            int id = (int) model.getValueAt(row, 0);
            try (Connection conn = db.DBConnection.getConnection()) {
                PreparedStatement stmt = conn.prepareStatement("DELETE FROM attendance WHERE id=?");
                stmt.setInt(1, id);
                stmt.executeUpdate();
                JOptionPane.showMessageDialog(this, "Deleted");
                loadData();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        });

        backBtn.addActionListener(e -> {
            new TeacherHome(teacherUsername).setVisible(true);
            dispose();
        });
    }

    private void openAttendanceForm(String teacherUsername) {
        JDialog dialog = new JDialog(this, "Mark Attendance", true);
        dialog.setSize(350, 250);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(null);

        JLabel studentLabel = new JLabel("Student Username:");
        JLabel subjectLabel = new JLabel("Subject:");
        JLabel statusLabel = new JLabel("Status:");

        JTextField studentField = new JTextField();
        JTextField subjectField = new JTextField();
        String[] statuses = {"Present", "Absent"};
        JComboBox<String> statusCombo = new JComboBox<>(statuses);

        JButton submitBtn = new JButton("Submit");

        studentLabel.setBounds(20, 20, 120, 25);
        subjectLabel.setBounds(20, 60, 120, 25);
        statusLabel.setBounds(20, 100, 120, 25);

        studentField.setBounds(150, 20, 150, 25);
        subjectField.setBounds(150, 60, 150, 25);
        statusCombo.setBounds(150, 100, 150, 25);
        submitBtn.setBounds(100, 150, 100, 30);

        dialog.add(studentLabel);
        dialog.add(subjectLabel);
        dialog.add(statusLabel);
        dialog.add(studentField);
        dialog.add(subjectField);
        dialog.add(statusCombo);
        dialog.add(submitBtn);

        submitBtn.addActionListener(e -> {
            String student = studentField.getText();
            String subject = subjectField.getText();
            String status = statusCombo.getSelectedItem().toString();

            if (student.isEmpty() || subject.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Please fill all fields");
                return;
            }

            try (Connection conn = db.DBConnection.getConnection()) {
                String sql = "INSERT INTO attendance (student_username, subject, date, status, marked_by) VALUES (?, ?, CURDATE(), ?, ?)";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setString(1, student);
                stmt.setString(2, subject);
                stmt.setString(3, status);
                stmt.setString(4, teacherUsername);
                stmt.executeUpdate();
                JOptionPane.showMessageDialog(dialog, "Attendance marked");
                dialog.dispose();
                loadData();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(dialog, "Error: " + ex.getMessage());
            }
        });

        dialog.setVisible(true);
    }

    private void loadData() {
        model.setRowCount(0);
        try (Connection conn = db.DBConnection.getConnection()) {
            ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM attendance");
            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("id"),
                        rs.getString("student_username"),
                        rs.getString("subject"),
                        rs.getDate("date"),
                        rs.getString("status")
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading data: " + e.getMessage());
        }
    }
}
