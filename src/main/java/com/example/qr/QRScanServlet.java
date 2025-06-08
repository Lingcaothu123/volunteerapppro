package com.example.qr;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.sql.*;

@WebServlet("/QRScanServlet")
public class QRScanServlet extends HttpServlet {
    private final String DB_URL = "jdbc:sqlserver://localhost:1433;databaseName=volunteer;"
            + "user=sa;password=1423;trustServerCertificate=true;";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Thiết lập encoding để tránh lỗi tiếng Việt
        response.setContentType("text/html; charset=UTF-8");
        response.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession();
        if (session.getAttribute("username") == null) {
            String currentURL = request.getRequestURI() + (request.getQueryString() != null ? "?" + request.getQueryString() : "");
            session.setAttribute("redirectAfterLogin", currentURL);
            response.sendRedirect("login.jsp");
            return;
        }

        String username = (String) session.getAttribute("username");
         String iduser = (String) session.getAttribute("iduser");
                 String idactive = request.getParameter("idactive");
        System.out.println(">>> Username từ session: " + username);

        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            Connection conn = DriverManager.getConnection(DB_URL);

            String updateSql = "UPDATE attend SET diemdanh = N'có mặt' WHERE iduser = ? and idactive=?";
            PreparedStatement stmt = conn.prepareStatement(updateSql);
            stmt.setString(1, iduser);
            stmt.setString(2, idactive);

            int rows = stmt.executeUpdate();
            System.out.println(">>> Số dòng cập nhật: " + rows);

            stmt.close();
            conn.close();

            if (rows > 0) {
                response.getWriter().println("✅ Điểm danh thành công cho " + iduser);
                    session.invalidate();
                
            } else {
                response.getWriter().println("⚠️ Không tìm thấy dữ liệu phù hợp để điểm danh."+iduser);
                 session.invalidate();
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().println("❌ Lỗi khi điểm danh."+iduser);
              session.invalidate();
        }
    }
}
