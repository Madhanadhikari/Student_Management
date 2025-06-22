package login;

import javax.swing.*;
import java.sql.*;
import db.DBConnection;

public class RegisterPage extends JFrame {
    private JTextField nameField, usernameField;
    private JPasswordField passwordField;
    private JComboBox<String> roleBox;

    public RegisterPage() {
        setTitle("Register");
        setSize(350, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);

        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setBounds(30, 30, 100, 25);
        add(nameLabel);

        nameField = new JTextField();
        nameField.setBounds(130, 30, 150, 25);
        add(nameField);

        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setBounds(30, 70, 100, 25);
        add(usernameLabel);

        usernameField = new JTextField();
        usernameField.setBounds(130, 70, 150, 25);
        add(usernameField);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(30, 110, 100, 25);
        add(passwordLabel);

        passwordField = new JPasswordField();
        passwordField.setBounds(130, 110, 150, 25);
        add(passwordField);

        JLabel roleLabel = new JLabel("Role:");
        roleLabel.setBounds(30, 150, 100, 25);
        add(roleLabel);

        String[] roles = {"Teacher", "Student"};
        roleBox = new JComboBox<>(roles);
        roleBox.setBounds(130, 150, 150, 25);
        add(roleBox);

        JButton registerButton = new JButton("Register");
        registerButton.setBounds(50, 200, 100, 30);
        add(registerButton);

        JButton loginButton = new JButton("Back to Login");
        loginButton.setBounds(170, 200, 130, 30);
        add(loginButton);

        registerButton.addActionListener(e -> registerUser());
        loginButton.addActionListener(e -> {
            dispose();
            new LoginPage().setVisible(true);
        });
    }

    private void registerUser() {
        String name = nameField.getText();
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        String role = roleBox.getSelectedItem().toString();

        if (name.isEmpty() || username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required.");
            return;
        }

        try (Connection conn = DBConnection.getConnection()) {
            String sql = "INSERT INTO user (name, username, password, role) VALUES (?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, name);
            stmt.setString(2, username);
            stmt.setString(3, password);
            stmt.setString(4, role);
            stmt.executeUpdate();

            JOptionPane.showMessageDialog(this, "Registration successful!");
            clearFields();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void clearFields() {
        nameField.setText("");
        usernameField.setText("");
        passwordField.setText("");
        roleBox.setSelectedIndex(0);
    }

    public static void main(String[] args) {
        new RegisterPage().setVisible(true);
    }
}
