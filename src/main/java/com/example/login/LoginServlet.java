package com.example.login;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.sql.*;
@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String loginType = request.getParameter("loginType"); // 👈 Lấy loại đăng nhập

        String fullname = null;
        String iduser="" ; // 👈 Khai báo ở ngoài
        boolean isValid = false;

        // Chọn bảng phù hợp
        String tableName = "login1".equals(loginType) ? "loginad" : "login1";

        com.example.dbconnect db = new com.example.dbconnect();
        try (Connection conn = db.getConnection()) {
            String sql = "SELECT * FROM " + tableName + " WHERE username = ? AND pass = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, username);
                stmt.setString(2, password);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        isValid = true;
                        fullname = rs.getString("fullname");
                        iduser = rs.getString("iduser");
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (isValid) {
            HttpSession session = request.getSession();
            session.setAttribute("username", username);
            session.setAttribute("iduser", iduser); 
            session.setAttribute("fullname", fullname);
            session.setAttribute("role", loginType);

            String redirectURL = (String) session.getAttribute("redirectAfterLogin");
            if (redirectURL != null) {
                session.removeAttribute("redirectAfterLogin");
                response.sendRedirect(redirectURL);
            } else {
                if ("login1".equals(loginType)) {
                    response.sendRedirect("AdminPage/dashboard.jsp");
                } else {
                    response.sendRedirect("home.jsp");
                }
            }
        } else {
            request.setAttribute("errorMessage", "Invalid username or password.");
            request.getRequestDispatcher("login.jsp").forward(request, response);
        }
    }
}
