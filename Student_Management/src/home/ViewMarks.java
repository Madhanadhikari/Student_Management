package home;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class ViewMarks extends JFrame {
    public ViewMarks(String username) {
        setTitle("View Marks - " + username);
        setSize(500, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        String[] columns = {"Subject", "Marks"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);

        JButton backBtn = new JButton("Back");

        // Bottom panel for button
        JPanel bottomPanel = new JPanel();
        bottomPanel.add(backBtn);

        add(scrollPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        // Load marks
        try (Connection conn = db.DBConnection.getConnection()) {
            String sql = "SELECT subject, marks FROM marks WHERE student_username=?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getString("subject"),
                        rs.getInt("marks")
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading marks: " + e.getMessage());
        }

        // Back button action
        backBtn.addActionListener(e -> {
            new StudentHome(username).setVisible(true); // assuming StudentHome(username) exists
            dispose();
        });
    }
}
