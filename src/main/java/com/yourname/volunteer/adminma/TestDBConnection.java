package com.yourname.volunteer.adminma;

import com.example.dbconnect;  // nhớ import đúng class dbconnect của bạn
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class TestDBConnection {
    public static void main(String[] args) {
        com.example.dbconnect db = new com.example.dbconnect();
        try (Connection conn = db.getConnection()) {
            System.out.println("Connected to DB!");
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM categories");
            while (rs.next()) {
                System.out.println(rs.getInt("id") + " - " + rs.getString("name"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
