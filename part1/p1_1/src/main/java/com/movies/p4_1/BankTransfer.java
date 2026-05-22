package com.movies.p4_1;

import java.sql.*;

public class BankTransfer {

    public static void main(String[] args) throws SQLException {
        String url = "jdbc:h2:mem:bankdb;DB_CLOSE_DELAY=-1";
        try (Connection conn = DriverManager.getConnection(url, "sa", "")) {
            setup(conn);

            System.out.println("=== До переводов ===");
            printAccounts(conn);

            System.out.println("\n=== Перевод 200 от Алисы к Бобу ===");
            transfer(conn, 1, 2, 200.0);
            printAccounts(conn);

            System.out.println("\n=== Перевод 5000 от Боба к Алисе (недостаток средств) ===");
            transfer(conn, 2, 1, 5000.0);
            printAccounts(conn);
        }
    }

    static void setup(Connection conn) throws SQLException {
        try (Statement stmt = conn.createStatement()) {
            stmt.execute("DROP TABLE IF EXISTS accounts");
            stmt.execute("""
                CREATE TABLE accounts (
                    id      INT PRIMARY KEY,
                    owner   VARCHAR(100),
                    balance DECIMAL(10,2)
                )
            """);
            stmt.execute("INSERT INTO accounts VALUES (1, 'Алиса', 1000.00)");
            stmt.execute("INSERT INTO accounts VALUES (2, 'Боб', 500.00)");
            stmt.execute("INSERT INTO accounts VALUES (3, 'Карл', 750.00)");
        }
        System.out.println("Таблица accounts создана");
    }

    static void transfer(Connection conn, int fromId, int toId, double amount)
            throws SQLException {
        conn.setAutoCommit(false);
        try {
            // Проверка баланса
            double balance;
            String checkSql = "SELECT balance FROM accounts WHERE id = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(checkSql)) {
                pstmt.setInt(1, fromId);
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (!rs.next()) throw new SQLException("Счёт не найден: " + fromId);
                    balance = rs.getDouble("balance");
                }
            }

            if (balance < amount) {
                System.out.println("Недостаточно средств: баланс=" + balance + ", нужно=" + amount);
                conn.rollback();
                return;
            }

            // Списание
            String debitSql = "UPDATE accounts SET balance = balance - ? WHERE id = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(debitSql)) {
                pstmt.setDouble(1, amount);
                pstmt.setInt(2, fromId);
                pstmt.executeUpdate();
            }

            // Зачисление
            String creditSql = "UPDATE accounts SET balance = balance + ? WHERE id = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(creditSql)) {
                pstmt.setDouble(1, amount);
                pstmt.setInt(2, toId);
                pstmt.executeUpdate();
            }

            conn.commit();
            System.out.println("Перевод " + amount + " выполнен успешно");

        } catch (SQLException e) {
            conn.rollback();
            System.out.println("Ошибка, откат: " + e.getMessage());
        } finally {
            conn.setAutoCommit(true);
        }
    }

    static void printAccounts(Connection conn) throws SQLException {
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM accounts ORDER BY id")) {
            while (rs.next()) {
                System.out.printf("id=%d | %-10s | %.2f%n",
                        rs.getInt("id"), rs.getString("owner"), rs.getDouble("balance"));
            }
        }
    }
}
