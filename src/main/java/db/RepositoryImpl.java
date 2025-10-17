package db;

import models.StringBuffer;

import java.sql.*;
import java.util.*;

public class RepositoryImpl implements Repository {
    private final String URL;
    private final String USER;
    private final String PASSWORD;

    private String tableName = null;

    private static final RepositoryImpl INSTANCE = getInstance();

    private RepositoryImpl() {
        this.URL = "jdbc:postgresql://localhost:5432/postgres";
        this.USER = "postgres";
        this.PASSWORD = "secret";
    }

    public void setTableName(String tableName) {
        this.tableName = sanitize(tableName);
    }

    public String getTableName() {
        return tableName;
    }

    public static RepositoryImpl getInstance() {
        if (INSTANCE == null) {
            return new RepositoryImpl();
        }
        return INSTANCE;
    }

    @Override
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    @Override
    public List<String> getAllTableNames() throws SQLException {
        List<String> tables = new ArrayList<>();
        String sql = """
                    SELECT table_name
                    FROM information_schema.tables
                    WHERE table_schema = 'public'
                    ORDER BY table_name;
                """;

        Connection conn = getConnection();
        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) tables.add(rs.getString("table_name"));

        return tables;
    }

    @Override
    public String connectOrCreateTable() throws SQLException, IllegalStateException {
        requireTable();
        List<String> existing = getAllTableNames();

        tableName = sanitize(tableName);
        if (existing.contains(tableName)) {
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

        Connection conn = getConnection();
        Statement stmt = conn.createStatement();
        stmt.execute(sql);
        return tableName;
    }

    @Override
    public void insert(StringBuffer data) throws SQLException, IllegalStateException {
        requireTable();

        String sql = "INSERT INTO " + sanitize(tableName) + " (original, modified) VALUES (?, ?)";
        Connection conn = getConnection();
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, data.getOriginal());
        ps.setString(2, data.getModified());
        ps.executeUpdate();
    }

    @Override
    public List<Map<String, Object>> fetchAll() throws SQLException, IllegalStateException {
        requireTable();

        List<Map<String, Object>> rows = new ArrayList<>();
        String sql = "SELECT * FROM " + sanitize(tableName) + " ORDER BY id";

        Connection conn = getConnection();
        Statement stmt = conn.createStatement();

        ResultSet rs = stmt.executeQuery(sql);
        ResultSetMetaData md = rs.getMetaData();
        int columns = md.getColumnCount();
        while (rs.next()) {
            Map<String, Object> row = new LinkedHashMap<>();
            for (int i = 1; i <= columns; i++)
                row.put(md.getColumnName(i), rs.getObject(i));
            rows.add(row);
        }
        return rows;
    }

    private String sanitize(String name) {
        if (name == null) {
            return "";
        }
        return name.trim().toLowerCase().replaceAll("[^a-z0-9_]", "_");
    }

    private void requireTable() {
        if (tableName == null || tableName.isEmpty()) {
            throw new IllegalStateException("Table name is not set.");
        }
    }
}
