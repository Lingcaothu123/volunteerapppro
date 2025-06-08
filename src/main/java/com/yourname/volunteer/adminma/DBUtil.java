///*
// * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
// * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
// */
//package com.yourname.volunteer.adminma;
//
//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.SQLException;
//
//public class DBUtil {
//  private static final String DB_URL = "jdbc:sqlite:C:/Users/Admins/Documents/NetBeansProjects/admin(project)/database.db";
//
//    public static Connection getConnection() throws SQLException {
//        try {
//            // Load driver SQLite
//            Class.forName("org.sqlite.JDBC");
//        } catch (ClassNotFoundException e) {
//            throw new SQLException("SQLite JDBC driver not found.", e);
//        }
//        return DriverManager.getConnection(DB_URL);
//    }
//}
