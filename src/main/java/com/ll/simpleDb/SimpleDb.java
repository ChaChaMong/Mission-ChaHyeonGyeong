package com.ll.simpleDb;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SimpleDb {
    private final String host;
    private final String user;
    private final String password;
    private final String dbName;
    private boolean devMode;

    public SimpleDb(String host, String user, String password, String dbName) {
        this.host = host;
        this.user = user;
        this.password = password;
        this.dbName = dbName;
    }

    public void setDevMode(boolean devMode) {
        this.devMode = devMode;
    }

    public Sql genSql() {
        try {
            Connection connection = DriverManager.getConnection(
                    "jdbc:mysql://" + this.host + "/" + this.dbName,
                    this.user,
                    this.password
            );
            return new Sql(connection);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void run(String sql, Object... params) {
        Sql s = this.genSql();
        s.append(sql, params);
        try {
            s.insert();
        } catch (Exception e) {
            if (this.devMode) {
                e.printStackTrace();
            }
            throw new RuntimeException("Error executing SQL operation: " + e.getMessage());
        }
    }
}