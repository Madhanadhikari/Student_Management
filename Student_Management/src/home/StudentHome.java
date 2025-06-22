package home;

import javax.swing.*;
import db.DBConnection;

public class StudentHome extends JFrame {
    public StudentHome(String username) {
        setTitle("Student Dashboard - " + username);
        setSize(400, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JMenuBar menuBar = new JMenuBar();

        JMenu menu = new JMenu("Options");
        JMenuItem viewAttendance = new JMenuItem("View Attendance");
        JMenuItem viewMarks = new JMenuItem("View Marks");

        viewAttendance.addActionListener(e -> new ViewAttendance(username).setVisible(true));
        viewMarks.addActionListener(e -> new ViewMarks(username).setVisible(true));

        menu.add(viewAttendance);
        menu.add(viewMarks);
        menuBar.add(menu);
        setJMenuBar(menuBar);
    }
}
