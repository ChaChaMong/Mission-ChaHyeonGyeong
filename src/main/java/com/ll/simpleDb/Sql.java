package com.ll.simpleDb;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Sql {
    private Connection connection;
    private StringBuilder sql;
    private List<Object> params;

    public Sql(Connection connection) {
        this.connection = connection;
        this.sql = new StringBuilder();
        this.params = new ArrayList<>();
    }

    public Sql append(String sqlPart, Object... params) {
        this.sql.append(sqlPart).append(" ");
        this.params.addAll(Arrays.asList(params));
        return this;
    }

    public long insert() {
        try {
            PreparedStatement pstmt = connection.prepareStatement(sql.toString(), Statement.RETURN_GENERATED_KEYS);
            for (int i = 0; i < params.size(); i++) {
                pstmt.setObject(i + 1, params.get(i));
            }
            pstmt.executeUpdate();

            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                return rs.getLong(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error executing insert operation: " + e.getMessage());
        }
        return -1; // return -1 or throw an exception, if insert operation failed
    }

    public int update() {
        try {
            PreparedStatement pstmt = connection.prepareStatement(sql.toString());
            for (int i = 0; i < params.size(); i++) {
                pstmt.setObject(i + 1, params.get(i));
            }
            return pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error executing update operation: " + e.getMessage());
        }
    }

    public int delete() {
        try {
            PreparedStatement pstmt = connection.prepareStatement(sql.toString());
            for (int i = 0; i < params.size(); i++) {
                pstmt.setObject(i + 1, params.get(i));
            }
            return pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error executing delete operation: " + e.getMessage());
        }
    }

    public LocalDateTime selectDatetime() {
        try {
            PreparedStatement pstmt = connection.prepareStatement(sql.toString());
            for (int i = 0; i < params.size(); i++) {
                pstmt.setObject(i + 1, params.get(i));
            }
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getTimestamp(1).toLocalDateTime();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error executing selectDatetime operation: " + e.getMessage());
        }
        return null;
    }

    public Long selectLong() {
        try {
            PreparedStatement pstmt = connection.prepareStatement(sql.toString());
            for (int i = 0; i < params.size(); i++) {
                pstmt.setObject(i + 1, params.get(i));
            }
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getLong(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error executing selectLong operation: " + e.getMessage());
        }
        return null;
    }

    public String selectString() {
        try {
            PreparedStatement pstmt = connection.prepareStatement(sql.toString());
            for (int i = 0; i < params.size(); i++) {
                pstmt.setObject(i + 1, params.get(i));
            }
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getString(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error executing selectString operation: " + e.getMessage());
        }
        return null;
    }

    public Map<String, Object> selectRow() {
        try {
            PreparedStatement pstmt = connection.prepareStatement(sql.toString());
            for (int i = 0; i < params.size(); i++) {
                pstmt.setObject(i + 1, params.get(i));
            }
            ResultSet rs = pstmt.executeQuery();
            ResultSetMetaData rsmd = rs.getMetaData();
            if (rs.next()) {
                Map<String, Object> row = new LinkedHashMap<>();
                for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                    String columnName = rsmd.getColumnName(i);
                    Object columnValue = rs.getObject(i);
                    row.put(columnName, columnValue);
                }
                return row;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error executing selectRow operation: " + e.getMessage());
        }
        return null;
    }

    public <T> T selectRow(Class<T> clazz) {
        try {
            PreparedStatement pstmt = connection.prepareStatement(sql.toString());
            for (int i = 0; i < params.size(); i++) {
                pstmt.setObject(i + 1, params.get(i));
            }
            ResultSet rs = pstmt.executeQuery();
            ResultSetMetaData rsmd = rs.getMetaData();
            if (rs.next()) {
                T row = clazz.getDeclaredConstructor().newInstance();
                for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                    String columnName = rsmd.getColumnName(i);
                    Object columnValue = rs.getObject(i);
                    Field field = clazz.getDeclaredField(columnName);
                    field.setAccessible(true);
                    field.set(row, columnValue);
                }
                return row;
            }
        } catch (SQLException | NoSuchFieldException | InstantiationException | IllegalAccessException |
                 InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException("Error executing selectRow operation: " + e.getMessage());
        }
        return null;
    }

    public <T> List<T> selectRows(Class<T> clazz) {
        try {
            PreparedStatement pstmt = connection.prepareStatement(sql.toString());
            for (int i = 0; i < params.size(); i++) {
                pstmt.setObject(i + 1, params.get(i));
            }
            ResultSet rs = pstmt.executeQuery();
            ResultSetMetaData rsmd = rs.getMetaData();
            List<T> rows = new ArrayList<>();
            while (rs.next()) {
                T row = clazz.getDeclaredConstructor().newInstance();
                for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                    String columnName = rsmd.getColumnName(i);
                    Object columnValue = rs.getObject(i);
                    Field field = clazz.getDeclaredField(columnName);
                    field.setAccessible(true);
                    field.set(row, columnValue);
                }
                rows.add(row);
            }
            return rows;
        } catch (SQLException | NoSuchFieldException | InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException("Error executing selectRows operation: " + e.getMessage());
        }
    }

    public Sql appendIn(String condition, Collection<?> values) {
        sql.append(" ").append(condition.replace("?", generatePlaceholders(values.size())));
        params.addAll(values);
        return this;
    }

    private String generatePlaceholders(int count) {
        return IntStream.range(0, count)
                .mapToObj(i -> "?")
                .collect(Collectors.joining(", "));
    }

    public List<Long> selectLongs() {
        try {
            PreparedStatement pstmt = connection.prepareStatement(sql.toString());
            for (int i = 0; i < params.size(); i++) {
                pstmt.setObject(i + 1, params.get(i));
            }
            ResultSet rs = pstmt.executeQuery();
            List<Long> longs = new ArrayList<>();
            while (rs.next()) {
                longs.add(rs.getLong(1));
            }
            return longs;
        } catch (SQLException e) {
            throw new RuntimeException("Error executing selectLongs operation: " + e.getMessage());
        }
    }
}