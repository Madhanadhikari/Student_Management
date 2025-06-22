package login;

import javax.swing.*;
import java.sql.*;
import db.DBConnection;
import home.*;

public class LoginPage extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;

    public LoginPage() {
        setTitle("Login");
        setSize(300, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);

        JLabel userLabel = new JLabel("Username:");
        userLabel.setBounds(30, 40, 100, 25);
        add(userLabel);

        usernameField = new JTextField();
        usernameField.setBounds(120, 40, 130, 25);
        add(usernameField);

        JLabel passLabel = new JLabel("Password:");
        passLabel.setBounds(30, 80, 100, 25);
        add(passLabel);

        passwordField = new JPasswordField();
        passwordField.setBounds(120, 80, 130, 25);
        add(passwordField);

        JButton loginButton = new JButton("Login");
        loginButton.setBounds(50, 130, 90, 30);
        add(loginButton);

        JButton registerButton = new JButton("Register");
        registerButton.setBounds(150, 130, 100, 30);
        add(registerButton);

        loginButton.addActionListener(e -> loginUser());
        registerButton.addActionListener(e -> {
            dispose();
            new RegisterPage().setVisible(true);
        });
    }

    private void loginUser() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT * FROM user WHERE username = ? AND password = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String role = rs.getString("role");
                JOptionPane.showMessageDialog(this, "Login successful as " + role);

                dispose(); // close login page

                if ("Student".equalsIgnoreCase(role)) {
                    new StudentHome(username).setVisible(true);
                } else if ("Teacher".equalsIgnoreCase(role)) {
                    new TeacherHome(username).setVisible(true);
                }
            }
else {
                JOptionPane.showMessageDialog(this, "Invalid credentials");
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        new LoginPage().setVisible(true);
    }
}
