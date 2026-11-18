package com.nhom5.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/** Kết nối SQLite dùng chung, mọi truy vấn đều đi qua đây. */
public final class DatabaseManager implements AutoCloseable {

    private final Connection connection;

    public DatabaseManager(Path databaseFile) {
        try {
            Path parent = databaseFile.toAbsolutePath().getParent();
            if (parent != null) {
                Files.createDirectories(parent);
            }
            this.connection = DriverManager.getConnection("jdbc:sqlite:" + databaseFile.toAbsolutePath());
            try (Statement statement = connection.createStatement()) {
                statement.execute("PRAGMA foreign_keys = ON");
            }
            createSchema();
        } catch (Exception ex) {
            throw new IllegalStateException("Không mở được cơ sở dữ liệu: " + ex.getMessage(), ex);
        }
    }

    private void createSchema() throws SQLException {
        try (Statement statement = connection.createStatement()) {
            statement.execute("""
                    CREATE TABLE IF NOT EXISTS students (
                        student_id TEXT PRIMARY KEY,
                        full_name TEXT NOT NULL,
                        phone TEXT NOT NULL,
                        class_name TEXT NOT NULL,
                        email TEXT NOT NULL
                    )
                    """);
            statement.execute("""
                    CREATE TABLE IF NOT EXISTS rooms (
                        room_id TEXT PRIMARY KEY,
                        room_name TEXT NOT NULL,
                        floor INTEGER NOT NULL,
                        capacity INTEGER NOT NULL,
                        room_type TEXT NOT NULL,
                        status TEXT NOT NULL
                    )
                    """);
            statement.execute("""
                    CREATE TABLE IF NOT EXISTS bookings (
                        booking_id TEXT PRIMARY KEY,
                        student_id TEXT NOT NULL,
                        room_id TEXT NOT NULL,
                        booking_date TEXT NOT NULL,
                        start_time TEXT NOT NULL,
                        end_time TEXT NOT NULL,
                        participant_count INTEGER NOT NULL,
                        status TEXT NOT NULL,
                        fee INTEGER NOT NULL,
                        created_at TEXT NOT NULL
                    )
                    """);
        }
    }

    public synchronized int update(String sql, Object... params) {
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            bind(statement, params);
            return statement.executeUpdate();
        } catch (SQLException ex) {
            throw new IllegalStateException("Không ghi được cơ sở dữ liệu: " + ex.getMessage(), ex);
        }
    }

    public synchronized <T> List<T> query(String sql, RowMapper<T> mapper, Object... params) {
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            bind(statement, params);
            try (ResultSet resultSet = statement.executeQuery()) {
                List<T> rows = new ArrayList<>();
                while (resultSet.next()) {
                    rows.add(mapper.map(resultSet));
                }
                return rows;
            }
        } catch (SQLException ex) {
            throw new IllegalStateException("Không đọc được cơ sở dữ liệu: " + ex.getMessage(), ex);
        }
    }

    private static void bind(PreparedStatement statement, Object... params) throws SQLException {
        for (int i = 0; i < params.length; i++) {
            Object value = params[i];
            int index = i + 1;
            if (value == null) {
                statement.setString(index, null);
            } else if (value instanceof Integer number) {
                statement.setInt(index, number);
            } else if (value instanceof Long number) {
                statement.setLong(index, number);
            } else {
                statement.setString(index, value.toString());
            }
        }
    }

    @Override
    public synchronized void close() {
        try {
            connection.close();
        } catch (SQLException ex) {
            throw new IllegalStateException("Không đóng được cơ sở dữ liệu.", ex);
        }
    }

    @FunctionalInterface
    public interface RowMapper<T> {
        T map(ResultSet resultSet) throws SQLException;
    }
}
