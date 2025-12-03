package bankingproject;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Database {

    private static final String URL = "jdbc:mysql://localhost:3306/banking_db";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (Exception e) {
            System.out.println("Connection error: " + e.getMessage());
            return null;
        }
    }

    public static boolean checkLogin(String username, String password) {
        try (Connection conn = getConnection()) {
            if (conn == null) return false;
            String sql = "SELECT * FROM users WHERE username=? AND password=?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, username);
                stmt.setString(2, password);
                try (ResultSet rs = stmt.executeQuery()) {
                    return rs.next();
                }
            }
        } catch (Exception e) {
            System.out.println("Login error: " + e.getMessage());
            return false;
        }
    }

    public static double getBalance(String username) {
        double balance = 0;
        try (Connection conn = getConnection()) {
            String sql = "SELECT balance FROM accounts WHERE username=?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, username);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) balance = rs.getDouble("balance");
                }
            }
        } catch (Exception e) {
            System.out.println("Get balance error: " + e.getMessage());
        }
        return balance;
    }

    public static boolean deposit(String username, double amount) {
        try (Connection conn = getConnection()) {
            String sql = "UPDATE accounts SET balance = balance + ? WHERE username=?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setDouble(1, amount);
                stmt.setString(2, username);
                int updated = stmt.executeUpdate();
                if (updated == 0) return false;
            }
            sql = "INSERT INTO transactions(username, type, amount, date, recipient, timestamp) VALUES(?, ?, ?, NOW(), ?, NOW())";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, username);
                stmt.setString(2, "Deposit");
                stmt.setDouble(3, amount);
                stmt.setString(4, "");
                stmt.executeUpdate();
            }
            return true;
        } catch (Exception e) {
            System.out.println("Deposit error: " + e.getMessage());
            return false;
        }
    }

    public static boolean withdraw(String username, double amount) {
        double balance = getBalance(username);
        if (balance < amount) return false;

        try (Connection conn = getConnection()) {
            String sql = "UPDATE accounts SET balance = balance - ? WHERE username=?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setDouble(1, amount);
                stmt.setString(2, username);
                int updated = stmt.executeUpdate();
                if (updated == 0) return false;
            }
            sql = "INSERT INTO transactions(username, type, amount, date, recipient, timestamp) VALUES(?, ?, ?, NOW(), ?, NOW())";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, username);
                stmt.setString(2, "Withdraw");
                stmt.setDouble(3, amount);
                stmt.setString(4, "");
                stmt.executeUpdate();
            }
            return true;
        } catch (Exception e) {
            System.out.println("Withdraw error: " + e.getMessage());
            return false;
        }
    }

    public static boolean transfer(String fromUser, String toUser, double amount) {
        double balance = getBalance(fromUser);
        if (balance < amount) return false;

        try (Connection conn = getConnection()) {
            // Withdraw from sender
            String sql = "UPDATE accounts SET balance = balance - ? WHERE username=?";
            int senderUpdated;
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setDouble(1, amount);
                stmt.setString(2, fromUser);
                senderUpdated = stmt.executeUpdate();
            }

            // Deposit to recipient
            sql = "UPDATE accounts SET balance = balance + ? WHERE username=?";
            int receiverUpdated;
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setDouble(1, amount);
                stmt.setString(2, toUser);
                receiverUpdated = stmt.executeUpdate();
            }

            if (senderUpdated == 0 || receiverUpdated == 0) return false;

            sql = "INSERT INTO transactions(username, type, amount, date, recipient, timestamp) VALUES(?, ?, ?, NOW(), ?, NOW())";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                // Sender
                stmt.setString(1, fromUser);
                stmt.setString(2, "Transfer Sent");
                stmt.setDouble(3, amount);
                stmt.setString(4, toUser);
                stmt.executeUpdate();

                // Recipient
                stmt.setString(1, toUser);
                stmt.setString(2, "Transfer Received");
                stmt.setDouble(3, amount);
                stmt.setString(4, fromUser);
                stmt.executeUpdate();
            }

            return true;
        } catch (Exception e) {
            System.out.println("Transfer error: " + e.getMessage());
            return false;
        }
    }
}
