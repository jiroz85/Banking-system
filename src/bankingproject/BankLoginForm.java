package bankingproject;

import javax.swing.*;
import java.awt.event.*;

public class BankLoginForm extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JLabel messageLabel;

    public BankLoginForm() {
        setTitle("Banking System Login");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);

        JLabel userLabel = new JLabel("Username:"); userLabel.setBounds(50, 50, 100, 25); add(userLabel);
        usernameField = new JTextField(); usernameField.setBounds(150, 50, 180, 25); add(usernameField);

        JLabel passLabel = new JLabel("Password:"); passLabel.setBounds(50, 100, 100, 25); add(passLabel);
        passwordField = new JPasswordField(); passwordField.setBounds(150, 100, 180, 25); add(passwordField);

        loginButton = new JButton("Login"); loginButton.setBounds(150, 150, 100, 30); add(loginButton);
        messageLabel = new JLabel(""); messageLabel.setBounds(50, 200, 300, 25); add(messageLabel);

        loginButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            if (Database.checkLogin(username, password)) {
                dispose();
                new Dashboard(username);
            } else {
                messageLabel.setText("Invalid username or password.");
            }
        });

        setLocationRelativeTo(null);
        setVisible(true);
    }

    public static void main(String[] args) {
        new BankLoginForm();
    }
}
