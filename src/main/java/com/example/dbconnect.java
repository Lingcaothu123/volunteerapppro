package com.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class dbconnect {
    private final String DB_URL = "jdbc:sqlserver://localhost:1433;databaseName=volunteer;user=sa;"
            + "password=1423;trustServerCertificate=true;";
    private final String JDBC_DRIVER = "com.microsoft.sqlserver.jdbc.SQLServerDriver";

    public Connection getConnection() {
        Connection conn = null;
        try {
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(DB_URL);
        } catch (ClassNotFoundException e) {

        } catch (SQLException e) {
     
        }
        return conn;
    }
}
