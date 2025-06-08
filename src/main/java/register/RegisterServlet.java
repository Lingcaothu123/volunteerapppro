package register;
import com.example.EmailUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.sql.*;
import java.util.UUID;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Trả lại trang register.jsp khi người dùng truy cập qua GET
        request.getRequestDispatcher("register.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");
        String fullname = request.getParameter("fullname");
        String phone = request.getParameter("phone");
        String skills = request.getParameter("skills");
        String email = request.getParameter("email");

        if (!password.equals(confirmPassword)) {
            request.setAttribute("error", "Mật khẩu không khớp.");
            request.getRequestDispatcher("register.jsp").forward(request, response);
            return;
        }

              try {
          com.example.dbconnect db = new com.example.dbconnect();      
            try (Connection conn = db.getConnection()) {

                String checkSql = "SELECT * FROM login1 WHERE username = ?";
                try (PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
                    checkStmt.setString(1, username);
                    ResultSet rs = checkStmt.executeQuery();
                    if (rs.next()) {
                        request.setAttribute("error", "Tên người dùng đã tồn tại.");
                        request.getRequestDispatcher("register.jsp").forward(request, response);
                        return;
                    }
                }

                // Tạo token xác thực
                String token = UUID.randomUUID().toString();
                String insertPendingSql = "INSERT INTO pending_users (token, username, pass, fullname, phone, skills, email) VALUES (?, ?, ?, ?, ?, ?, ?)";
                try (PreparedStatement stmt = conn.prepareStatement(insertPendingSql)) {
                    stmt.setString(1, token);
                    stmt.setString(2, username);
                    stmt.setString(3, password);
                    stmt.setString(4, fullname);
                    stmt.setString(5, phone);
                    stmt.setString(6, skills);
                    stmt.setString(7, email);
                    stmt.executeUpdate();
                }

                // Gửi email xác nhận
                String link = request.getRequestURL().toString().replace("/register", "") + "/confirm?token=" + token;
                EmailUtils.sendEmail(email, "Xác nhận đăng ký tài khoản", "Nhấn vào liên kết sau để xác nhận đăng ký:\n" + link);

                request.setAttribute("success", "Vui lòng kiểm tra email để xác nhận tài khoản.");
                request.getRequestDispatcher("register.jsp").forward(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Đã xảy ra lỗi khi xử lý đăng ký.");
            request.getRequestDispatcher("register.jsp").forward(request, response);
        }
    }
}
