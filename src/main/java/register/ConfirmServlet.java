package register;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.sql.*;

@WebServlet("/confirm")
public class ConfirmServlet extends HttpServlet {


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String token = request.getParameter("token");

        if (token == null || token.isEmpty()) {
            response.setContentType("text/html;charset=UTF-8");
            response.getWriter().println("<html><body>");
            response.getWriter().println("<h2 style='color:red'>Token không hợp lệ.</h2>");
            response.getWriter().println("</body></html>");
            return;
        }

            try {
          com.example.dbconnect db = new com.example.dbconnect();      
            try (Connection conn = db.getConnection()) {

                String selectSql = "SELECT * FROM pending_users WHERE token = ?";
                try (PreparedStatement stmt = conn.prepareStatement(selectSql)) {
                    stmt.setString(1, token);
                    ResultSet rs = stmt.executeQuery();

                    if (!rs.next()) {
                        response.setContentType("text/html;charset=UTF-8");
                        response.getWriter().println("<html><body>");
                        response.getWriter().println("<h2 style='color:red'>Liên kết không hợp lệ hoặc đã hết hạn.</h2>");
                        response.getWriter().println("</body></html>");
                        return;
                    }

                    String username = rs.getString("username");
                    String password = rs.getString("pass");
                    String fullname = rs.getString("fullname");
                    String phone = rs.getString("phone");
                    String skills = rs.getString("skills");
                    String email = rs.getString("email");

                    String insertSql = "INSERT INTO login1 (username, pass, fullname, phone, skills, email) VALUES (?, ?, ?, ?, ?, ?)";
                    try (PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {
                        insertStmt.setString(1, username);
                        insertStmt.setString(2, password);
                        insertStmt.setString(3, fullname);
                        insertStmt.setString(4, phone);
                        insertStmt.setString(5, skills);
                        insertStmt.setString(6, email);
                        insertStmt.executeUpdate();
                    }

                    try (PreparedStatement deleteStmt = conn.prepareStatement("DELETE FROM pending_users WHERE token = ?")) {
                        deleteStmt.setString(1, token);
                        deleteStmt.executeUpdate();
                    }

                    // ✅ Hiển thị trang xác nhận thành công
                    response.setContentType("text/html;charset=UTF-8");
                    response.getWriter().println("<html><body>");
                    response.getWriter().println("<h2 style='color:green'>Xác nhận thành công!</h2>");
                    response.getWriter().println("<p>Bạn có thể <a href='login.jsp'>đăng nhập tại đây</a>.</p>");
                    response.getWriter().println("</body></html>");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.setContentType("text/html;charset=UTF-8");
            response.getWriter().println("<html><body>");
            response.getWriter().println("<h2 style='color:red'>Lỗi xác nhận tài khoản.</h2>");
            response.getWriter().println("</body></html>");
        }
    }
}
