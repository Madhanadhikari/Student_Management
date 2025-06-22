package home;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import home.*;

public class ManageMarks extends JFrame {
    private DefaultTableModel model;

    public ManageMarks(String teacherUsername) {
        setTitle("Manage Marks - " + teacherUsername);
        setSize(700, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        String[] columns = {"ID", "Student", "Subject", "Marks"};
        model = new DefaultTableModel(columns, 0);
        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);

        JButton addBtn = new JButton("Add Marks");
        JButton updateBtn = new JButton("Update Selected");
        JButton deleteBtn = new JButton("Delete Selected");
        JButton backbtn=new JButton("back");

        JPanel panel = new JPanel();
        panel.add(addBtn);
        panel.add(updateBtn);
        panel.add(deleteBtn);
        panel.add(backbtn);

        add(scrollPane, BorderLayout.CENTER);
        add(panel, BorderLayout.SOUTH);

        loadData();

        // Add Marks button opens a form
        addBtn.addActionListener(e -> openAddForm(teacherUsername));

        // Update marks
        updateBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) return;

            int id = (int) model.getValueAt(row, 0);
            String currentMarks = model.getValueAt(row, 3).toString();

            String newMarksStr = JOptionPane.showInputDialog("New Marks:", currentMarks);
            if (newMarksStr == null || newMarksStr.isEmpty()) return;

            try (Connection conn = db.DBConnection.getConnection()) {
                String sql = "UPDATE marks SET marks=? WHERE id=?";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setInt(1, Integer.parseInt(newMarksStr));
                stmt.setInt(2, id);
                stmt.executeUpdate();
                JOptionPane.showMessageDialog(this, "Updated");
                loadData();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        });

        
       
        
        deleteBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) return;

            int id = (int) model.getValueAt(row, 0);
            try (Connection conn = db.DBConnection.getConnection()) {
                String sql = "DELETE FROM marks WHERE id=?";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setInt(1, id);
                stmt.executeUpdate();
                JOptionPane.showMessageDialog(this, "Deleted");
                loadData();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        });
        backbtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new TeacherHome(teacherUsername).setVisible(true); // pass username
                dispose(); // close ManageMarks window
            }
        });
    }

    private void openAddForm(String teacherUsername) {
        JDialog dialog = new JDialog(this, "Add Marks", true);
        dialog.setSize(350, 250);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(null);

        JLabel studentLabel = new JLabel("Student Username:");
        JLabel subjectLabel = new JLabel("Subject:");
        JLabel marksLabel = new JLabel("Marks:");

        JTextField studentField = new JTextField();
        JTextField subjectField = new JTextField();
        JTextField marksField = new JTextField();

        JButton submitBtn = new JButton("Submit");

        studentLabel.setBounds(20, 20, 120, 25);
        subjectLabel.setBounds(20, 60, 120, 25);
        marksLabel.setBounds(20, 100, 120, 25);

        studentField.setBounds(150, 20, 150, 25);
        subjectField.setBounds(150, 60, 150, 25);
        marksField.setBounds(150, 100, 150, 25);
        submitBtn.setBounds(100, 150, 100, 30);

        dialog.add(studentLabel);
        dialog.add(subjectLabel);
        dialog.add(marksLabel);
        dialog.add(studentField);
        dialog.add(subjectField);
        dialog.add(marksField);
        dialog.add(submitBtn);
        

        submitBtn.addActionListener(e -> {
            String student = studentField.getText().trim();
            String subject = subjectField.getText().trim();
            String marksStr = marksField.getText().trim();

            if (student.isEmpty() || subject.isEmpty() || marksStr.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Please fill all fields");
                return;
            }

            try {
                int marks = Integer.parseInt(marksStr);
                try (Connection conn = db.DBConnection.getConnection()) {
                    String sql = "INSERT INTO marks (student_username, subject, marks, added_by) VALUES (?, ?, ?, ?)";
                    PreparedStatement stmt = conn.prepareStatement(sql);
                    stmt.setString(1, student);
                    stmt.setString(2, subject);
                    stmt.setInt(3, marks);
                    stmt.setString(4, teacherUsername);
                    stmt.executeUpdate();
                    JOptionPane.showMessageDialog(dialog, "Marks added successfully");
                    dialog.dispose();
                    loadData();
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Marks must be a number");
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(dialog, "Database Error: " + ex.getMessage());
            }
        });

        dialog.setVisible(true);
    }

    private void loadData() {
        model.setRowCount(0);
        try (Connection conn = db.DBConnection.getConnection()) {
            ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM marks");
            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("id"),
                        rs.getString("student_username"),
                        rs.getString("subject"),
                        rs.getInt("marks")
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading data: " + e.getMessage());
        }
    }
}
