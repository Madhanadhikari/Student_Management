package home;



import javax.swing.*;

public class TeacherHome extends JFrame {
    public TeacherHome(String username) {
        setTitle("Teacher Dashboard - " + username);
        setSize(400, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JMenuBar menuBar = new JMenuBar();

        JMenu menu = new JMenu("Manage");
        JMenuItem attendance = new JMenuItem("Mark Attendance");
        JMenuItem marks = new JMenuItem("Enter Marks");

        attendance.addActionListener(e -> new ManageAttendance(username).setVisible(true));
        marks.addActionListener(e -> new ManageMarks(username).setVisible(true));

        menu.add(attendance);
        menu.add(marks);
        menuBar.add(menu);
        setJMenuBar(menuBar);
    }
}
