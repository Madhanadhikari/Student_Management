package home;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class ViewAttendance extends JFrame {
    public ViewAttendance(String username) {
        setTitle("View Attendance - " + username);
        setSize(500, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        String[] columns = {"Subject", "Date", "Status"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);

        JButton backBtn = new JButton("Back");
        JPanel bottomPanel = new JPanel();
        bottomPanel.add(backBtn);

        add(scrollPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        try (Connection conn = db.DBConnection.getConnection()) {
            String sql = "SELECT subject, date, status FROM attendance WHERE student_username=?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getString("subject"),
                        rs.getDate("date"),
                        rs.getString("status")
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading attendance: " + e.getMessage());
        }

        // Back button action
        backBtn.addActionListener(e -> {
            new StudentHome(username).setVisible(true);  // Replace with your actual student home class
            dispose();
        });
    }
}
