package db;

import models.StringBuffer;

import java.sql.*;
import java.util.List;
import java.util.Map;

interface Repository {

    Connection getConnection() throws SQLException;
    List<String> getAllTableNames() throws SQLException;
    String connectOrCreateTable() throws SQLException;
    void insert(StringBuffer data) throws SQLException;
    List<Map<String, Object>> fetchAll() throws SQLException;
}
