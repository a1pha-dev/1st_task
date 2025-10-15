package db;

import java.sql.*;
import java.util.*;

public class DatabaseUtil {
    private static final String URL = "jdbc:postgresql://localhost:5432/1st_task";
    private static final String USER = "postgres";
    private static final String PASSWORD = "secret";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static List<String> getAllTableNames() {
        List<String> tables = new ArrayList<>();
        String sql = """
                    SELECT table_name
                    FROM information_schema.tables
                    WHERE table_schema = 'public'
                    ORDER BY table_name;
                """;

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) tables.add(rs.getString("table_name"));
        } catch (SQLException e) {
            System.err.println("Ошибка при получении списка таблиц: " + e.getMessage());
        }
        return tables;
    }

    public static void printAllTables() {
        List<String> tables = getAllTableNames();
        if (tables.isEmpty()) {
            System.out.println("Таблиц нет в базе данных.");
        } else {
            System.out.println("Существующие таблицы:");
            for (int i = 0; i < tables.size(); i++) {
                System.out.printf("  %d. %s%n", i + 1, tables.get(i));
            }
        }
    }

    public static String connectOrCreateTable(Scanner scanner) {
        System.out.print("Введите имя таблицы: ");
        String tableName = sanitize(scanner.nextLine());
        List<String> existing = getAllTableNames();

        if (existing.contains(tableName)) {
            System.out.printf("\nПодключено к таблице %s\n", tableName);
            return tableName;
        }

        String sql = String.format("""
                    CREATE TABLE %s (
                        id SERIAL PRIMARY KEY,
                        original TEXT NOT NULL,
                        modified TEXT NOT NULL,
                        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
                    );
                """, tableName);

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("Таблица " + tableName + " создана и подключена.");
            return tableName;
        } catch (SQLException e) {
            System.err.println("Ошибка при создании таблицы: " + e.getMessage());
            return null;
        }
    }

    public static void insertString(String table, String original, String modified) {
        String sql = "INSERT INTO " + table + " (original, modified) VALUES (?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, original);
            ps.setString(2, modified);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Ошибка добавления строки: " + e.getMessage());
        }
    }

    public static List<Map<String, Object>> getAllRows(String table) {
        List<Map<String, Object>> rows = new ArrayList<>();
        String sql = "SELECT * FROM " + table + " ORDER BY id";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            ResultSetMetaData md = rs.getMetaData();
            int columns = md.getColumnCount();
            while (rs.next()) {
                Map<String, Object> row = new LinkedHashMap<>();
                for (int i = 1; i <= columns; i++)
                    row.put(md.getColumnName(i), rs.getObject(i));
                rows.add(row);
            }
        } catch (SQLException e) {
            System.err.println("Ошибка чтения: " + e.getMessage());
        }
        return rows;
    }

    private static String sanitize(String name) {
        return name.trim().toLowerCase().replaceAll("[^a-z0-9_]", "_");
    }
}
