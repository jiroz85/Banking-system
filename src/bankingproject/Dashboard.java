package bankingproject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Dashboard extends JFrame {

    private JLabel welcomeLabel;
    private JButton checkBalanceBtn, depositBtn, withdrawBtn, transferBtn, transactionHistoryBtn, logoutBtn;
    private String username;

    public Dashboard(String username) {
        this.username = username;

        setTitle("Banking System - Dashboard");
        setSize(500, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        setResizable(false);

        welcomeLabel = new JLabel("Welcome, " + username + "!");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 18));
        welcomeLabel.setBounds(150, 20, 300, 30);
        add(welcomeLabel);

        checkBalanceBtn = new JButton("Check Balance"); checkBalanceBtn.setBounds(150, 70, 200, 30); add(checkBalanceBtn);
        depositBtn = new JButton("Deposit Money"); depositBtn.setBounds(150, 110, 200, 30); add(depositBtn);
        withdrawBtn = new JButton("Withdraw Money"); withdrawBtn.setBounds(150, 150, 200, 30); add(withdrawBtn);
        transferBtn = new JButton("Transfer Money"); transferBtn.setBounds(150, 190, 200, 30); add(transferBtn);
        transactionHistoryBtn = new JButton("Transaction History"); transactionHistoryBtn.setBounds(150, 230, 200, 30); add(transactionHistoryBtn);
        logoutBtn = new JButton("Logout"); logoutBtn.setBounds(150, 270, 200, 30); add(logoutBtn);

        // Actions
        checkBalanceBtn.addActionListener(e -> {
            double balance = Database.getBalance(username);
            JOptionPane.showMessageDialog(this, "Your current balance: $" + balance);
        });

        depositBtn.addActionListener(e -> {
            String input = JOptionPane.showInputDialog(this, "Enter deposit amount:");
            if (input != null && !input.isEmpty()) {
                try {
                    double amount = Double.parseDouble(input);
                    if (Database.deposit(username, amount)) {
                        JOptionPane.showMessageDialog(this, "Deposit successful!");
                    } else {
                        JOptionPane.showMessageDialog(this, "Deposit failed! Check your account.");
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Invalid number entered.");
                }
            }
        });

        withdrawBtn.addActionListener(e -> {
            String input = JOptionPane.showInputDialog(this, "Enter withdraw amount:");
            if (input != null && !input.isEmpty()) {
                try {
                    double amount = Double.parseDouble(input);
                    if (Database.withdraw(username, amount)) {
                        JOptionPane.showMessageDialog(this, "Withdrawal successful!");
                    } else {
                        JOptionPane.showMessageDialog(this, "Insufficient balance or account error.");
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Invalid number entered.");
                }
            }
        });

        transferBtn.addActionListener(e -> {
            String toUser = JOptionPane.showInputDialog(this, "Enter recipient username:");
            String amountStr = JOptionPane.showInputDialog(this, "Enter transfer amount:");
            if (toUser != null && !toUser.isEmpty() && amountStr != null && !amountStr.isEmpty()) {
                try {
                    double amount = Double.parseDouble(amountStr);
                    if (Database.transfer(username, toUser, amount)) {
                        JOptionPane.showMessageDialog(this, "Transfer successful!");
                    } else {
                        JOptionPane.showMessageDialog(this, "Transfer failed. Check balance or recipient.");
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Invalid number entered.");
                }
            }
        });

       transactionHistoryBtn.addActionListener(e -> {
    try (Connection conn = Database.getConnection()) {
        String sql = "SELECT * FROM transactions WHERE username=? ORDER BY timestamp DESC";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                StringBuilder history = new StringBuilder();
                while (rs.next()) {
                    history.append(rs.getTimestamp("timestamp")).append(" - ")
                           .append(rs.getString("type")).append(" - $")
                           .append(rs.getDouble("amount")).append(" - ")
                           .append(rs.getString("recipient")).append("\n");
                }
                if (history.length() == 0) history.append("No transactions yet.");
                JOptionPane.showMessageDialog(this, history.toString(), "Transaction History", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    } catch (Exception ex) {
        JOptionPane.showMessageDialog(this, "Error fetching transactions: " + ex.getMessage());
    }
});

        logoutBtn.addActionListener(e -> {
            dispose();
            new BankLoginForm();
        });

        setLocationRelativeTo(null);
        setVisible(true);
    }
}
