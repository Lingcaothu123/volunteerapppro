package com.example.update;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.*;

@WebServlet("/confirm-update")
public class ConfirmUpdateServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String token = request.getParameter("token");

        if (token == null || token.isEmpty()) {
            response.setContentType("text/html;charset=UTF-8");
            response.getWriter().println("<h2 style='color:red'>Token không hợp lệ.</h2>");
            return;
        }

        try {
             com.example.dbconnect db = new com.example.dbconnect();      
            try (Connection conn = db.getConnection()) {

                String selectSql = "SELECT * FROM pending_update WHERE token = ?";
                try (PreparedStatement stmt = conn.prepareStatement(selectSql)) {
                    stmt.setString(1, token);
                    ResultSet rs = stmt.executeQuery();

                    if (!rs.next()) {
                        response.setContentType("text/html;charset=UTF-8");
                        response.getWriter().println("<h2 style='color:red'>Liên kết không hợp lệ hoặc đã hết hạn.</h2>");
                        return;
                    }

                    String iduser = rs.getString("iduser");
                    String fullname = rs.getString("fullname");
                    String phone = rs.getString("phone");
                    String skills = rs.getString("skills");
                    String newPass = rs.getString("new_pass");

                    String updateSql;
                    if (newPass != null && !newPass.isEmpty()) {
                        updateSql = "UPDATE login1 SET fullname = ?, phone = ?, skills = ?, pass = ? WHERE iduser = ?";
                        try (PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {
                            updateStmt.setString(1, fullname);
                            updateStmt.setString(2, phone);
                            updateStmt.setString(3, skills);
                            updateStmt.setString(4, newPass);
                            updateStmt.setString(5, iduser);
                            updateStmt.executeUpdate();
                        }
                    } else {
                        updateSql = "UPDATE login1 SET fullname = ?, phone = ?, skills = ? WHERE iduser = ?";
                        try (PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {
                            updateStmt.setString(1, fullname);
                            updateStmt.setString(2, phone);
                            updateStmt.setString(3, skills);
                            updateStmt.setString(4, iduser);
                            updateStmt.executeUpdate();
                        }
                    }

                    try (PreparedStatement deleteStmt = conn.prepareStatement("DELETE FROM pending_update WHERE token = ?")) {
                        deleteStmt.setString(1, token);
                        deleteStmt.executeUpdate();
                    }

                    response.setContentType("text/html;charset=UTF-8");
                    response.getWriter().println("<h2 style='color:green'>Cập nhật thông tin thành công!</h2>");
                    response.getWriter().println("<p><a href='home.jsp'>Quay lại trang thông tin cá nhân</a></p>");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.setContentType("text/html;charset=UTF-8");
            response.getWriter().println("<h2 style='color:red'>Lỗi khi xác nhận cập nhật.</h2>");
        }
    }
}
