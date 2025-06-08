package com.example.activity;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@WebServlet("/confirm-activity")
public class ConfirmActivityServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");

        String token = request.getParameter("token");

        if (token == null || token.trim().isEmpty()) {
            response.getWriter().println("Liên kết xác nhận không hợp lệ.");
            return;
        }

        try {
            com.example.dbconnect db = new com.example.dbconnect();
            try (Connection conn = db.getConnection()) {
                String query = "SELECT iduser, activity_id FROM pending_activity WHERE token = ?";
                try (PreparedStatement stmt = conn.prepareStatement(query)) {
                    stmt.setString(1, token);
                    ResultSet rs = stmt.executeQuery();

                    if (rs.next()) {
                        String iduser = rs.getString("iduser");
                        String activityId = rs.getString("activity_id");

                        String checkConfirmSql = "SELECT COUNT(*) FROM trangthai WHERE iduser = ? AND idactive = ?";
                        try (PreparedStatement checkStmt = conn.prepareStatement(checkConfirmSql)) {
                            checkStmt.setString(1, iduser);
                            checkStmt.setString(2, activityId);
                            ResultSet checkRs = checkStmt.executeQuery();

                            if (checkRs.next() && checkRs.getInt(1) > 0) {
                                response.getWriter().println("Bạn đã xác nhận đăng ký hoạt động này trước đó.");
                                return;
                            }
                        }

                        // Chèn vào bảng trạng thái
                        String insertSql = "INSERT INTO trangthai (iduser, idactive, trangthaidangky) VALUES (?, ?, N'Đang chờ xác nhận')";
                        try (PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {
                            insertStmt.setString(1, iduser);
                            insertStmt.setString(2, activityId);
                            insertStmt.executeUpdate();
                        }

                        // Ghi lịch sử
                        LocalDateTime now = LocalDateTime.now();
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                        String formattedNow = now.format(formatter);

                        String insertSql2 = "INSERT INTO history (ngaygiothaotac, tt, iduser, id) VALUES (?, N'đang chờ xác nhận', ?, ?)";
                        try (PreparedStatement historyStmt = conn.prepareStatement(insertSql2)) {
                            historyStmt.setString(1, formattedNow);
                            historyStmt.setString(2, iduser);
                            historyStmt.setString(3, activityId);
                            historyStmt.executeUpdate();
                        }

                        // Xoá bản ghi tạm
                        String deleteSql = "DELETE FROM pending_activity WHERE token = ?";
                        try (PreparedStatement deleteStmt = conn.prepareStatement(deleteSql)) {
                            deleteStmt.setString(1, token);
                            deleteStmt.executeUpdate();
                        }

                        response.getWriter().println("Xác nhận thành công! Bạn đã đăng ký hoạt động.");
                    } else {
                        response.getWriter().println("Liên kết không hợp lệ hoặc đã hết hạn.");
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().println("Đã xảy ra lỗi trong quá trình xác nhận.");
        }
    }
}
