package com.example.update;
import com.example.EmailUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.*;

@WebServlet("/update-user")
public class UpdateUserServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("username") == null) {
            response.sendRedirect("login.jsp");
            return;
        }
        String iduser = (String) session.getAttribute("iduser");
        String username = (String) session.getAttribute("username");
        String fullname = request.getParameter("fullname");
        String phone = request.getParameter("phone");
        String skills = request.getParameter("skills");
        String newPass = request.getParameter("pass");
        String confirmPass = request.getParameter("cfpass");

        // Kiểm tra mật khẩu khớp
        if (newPass != null && !newPass.equals(confirmPass)) {
            request.setAttribute("error", "Mật khẩu xác nhận không khớp.");
            request.getRequestDispatcher("updateinf.jsp").forward(request, response);
            return;
        }

        String token = java.util.UUID.randomUUID().toString();

        try {
          com.example.dbconnect db = new com.example.dbconnect();      
            try (Connection conn = db.getConnection()) {
                // Xóa yêu cầu cũ
                String deleteOld = "DELETE FROM pending_update WHERE iduser = ?";
                try (PreparedStatement delStmt = conn.prepareStatement(deleteOld)) {
                    delStmt.setString(1, iduser);
                    delStmt.executeUpdate();
                }

                // Chèn yêu cầu cập nhật mới
                String insertSql = "INSERT INTO pending_update (token, iduser, fullname, phone, skills, new_pass)"
                        + " VALUES (?, ?, ?, ?, ?, ?)";
                try (PreparedStatement stmt = conn.prepareStatement(insertSql)) {
                    stmt.setString(1, token);
                    stmt.setString(2, iduser);
                    stmt.setString(3, fullname);
                    stmt.setString(4, phone);
                    stmt.setString(5, skills);
                    stmt.setString(6, (newPass != null && !newPass.isEmpty()) ? newPass : null);
                    stmt.executeUpdate();
                }

                // Lấy email người dùng
                String email = null;
                try (PreparedStatement emailStmt = conn.prepareStatement("SELECT email FROM login1 WHERE iduser = ?")) {
                    emailStmt.setString(1, iduser);
                    ResultSet rs = emailStmt.executeQuery();
                    if (rs.next()) {
                        email = rs.getString("email");
                    }
                }

                if (email == null || email.isEmpty()) {
                    request.setAttribute("error", "Không tìm thấy email để gửi xác nhận.");
                    request.getRequestDispatcher("update-info.jsp").forward(request, response);
                    return;
                }

                // Gửi email xác nhận
                String baseUrl = request.getRequestURL().toString().replace("/update-user", "");
                String link = baseUrl + "/confirm-update?token=" + token;
                String subject = "Xác nhận cập nhật thông tin tài khoản";
                String content = "Bạn đã yêu cầu cập nhật thông tin tài khoản.\n\n"
                        + "Nhấn vào liên kết sau để xác nhận cập nhật:\n" + link;

                EmailUtils.sendEmail(email, subject, content);

                request.setAttribute("message", "Vui lòng kiểm tra email để xác nhận cập nhật.");
                request.getRequestDispatcher("update-info.jsp").forward(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Lỗi khi xử lý cập nhật.");
            request.getRequestDispatcher("update-info.jsp").forward(request, response);
        }
    }
}
