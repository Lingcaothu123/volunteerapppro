package com.example.activity;

import com.example.EmailUtils;  // Đảm bảo bạn có thư viện EmailUtils để gửi email

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.*;
import java.io.PrintWriter;
import java.util.UUID;

@WebServlet("/register-activity")
public class RegisterActivityServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Lấy tham số từ form
        String iduser = request.getParameter("iduser");  // Chuyển từ username thành iduser
        String activityId = request.getParameter("activityId");
        String token = UUID.randomUUID().toString();  // Tạo token để xác nhận đăng ký

        // Kiểm tra thông tin đăng ký có đầy đủ không
        if (iduser == null || iduser.isEmpty() || activityId == null || activityId.isEmpty()) {
            response.sendRedirect("allbooks?error=Thông+tin+đăng+ký+không+đầy+đủ");
            return;
        }

        try {
            com.example.dbconnect db = new com.example.dbconnect();
            try (Connection conn = db.getConnection()) {
                // Lấy thông tin hoạt động từ bảng activities
                String activityQuery = "SELECT title, location, organization, start_time, end_time FROM activities WHERE id = ?";
                try (PreparedStatement stmt = conn.prepareStatement(activityQuery)) {
                stmt.setString(1, activityId);
  // Câu lệnh truy vấn thông tin hoạt động
                    ResultSet rs = stmt.executeQuery();

                    if (rs.next()) {
                        // Lấy thông tin chi tiết về hoạt động
                        String tenhd = rs.getString("title");
                        String diadiem = rs.getString("location");
                        String tochuc = rs.getString("organization");
                        String thoigianbd = rs.getString("start_time");
                        String thoigiankt = rs.getString("end_time");

                        // Lưu vào bảng chờ xác nhận (pending_activity)
                        String insertPending = "INSERT INTO pending_activity (token, iduser, activity_id) VALUES (?, ?, ?)";
                        try (PreparedStatement insertStmt = conn.prepareStatement(insertPending)) {
                            insertStmt.setString(1, token);
                            insertStmt.setString(2, iduser);  // Cập nhật từ username thành iduser
                            insertStmt.setInt(3, Integer.parseInt(activityId));
                            insertStmt.executeUpdate();
                        }

                        // Lấy email người dùng từ bảng login1
                        String emailQuery = "SELECT email FROM login1 WHERE iduser = ?";  // Sửa từ username thành iduser
                        String userEmail = null;
                        try (PreparedStatement emailStmt = conn.prepareStatement(emailQuery)) {
                            emailStmt.setString(1, iduser);  // Cập nhật từ username thành iduser
                            ResultSet emailRs = emailStmt.executeQuery();
                            if (emailRs.next()) {
                                userEmail = emailRs.getString("email");
                            }
                        }

                        if (userEmail == null || userEmail.isEmpty()) {
                            response.sendRedirect("allbooks?error=Không+tìm+thấy+email+người+dùng");
                            return;
                        }

                        // Gửi email xác nhận đăng ký
                        String link = request.getRequestURL().toString().replace("/register-activity", "") +
                                "/confirm-activity?token=" + token;
                        String subject = "Xác nhận đăng ký hoạt động";
                        String content = "Bạn đã đăng ký tham gia hoạt động: " + tenhd +
                                "\nĐịa điểm: " + diadiem +
                                "\nTổ chức: " + tochuc +
                                "\nThời gian bắt đầu: " + thoigianbd +
                                "\nThời gian kết thúc: " + thoigiankt +
                                "\n\nNhấn vào liên kết sau để xác nhận tham gia:\n" + link;

                        // Gửi email (chắc chắn rằng bạn đã có thư viện EmailUtils.sendEmail)
                        try {
                            EmailUtils.sendEmail(userEmail, subject, content);
                        } catch (Exception e) {
                            e.printStackTrace();
                            response.sendRedirect("allbooks?error=Không+thể+gửi+email");
                            return;
                        }

                        // Thông báo cho người dùng
                        response.setContentType("text/html;charset=UTF-8");
                        try (PrintWriter out = response.getWriter()) {
                            out.println("<!DOCTYPE html>");
                            out.println("<html><head><title>Đăng ký thành công</title></head><body>");
                            out.println("<h2>Vui lòng kiểm tra email của bạn để xác nhận đăng ký.</h2>");
                            out.println("<p>Bạn sẽ không được tham gia nếu không xác nhận qua email.</p>");
                            out.println("<a href='showactive.jsp'>Quay lại danh sách hoạt động</a>");
                            out.println("</body></html>");
                        }
                        return;
                    } else {
                        response.sendRedirect("allbooks?error=Hoạt+động+không+tồn+tại");
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("allbooks?error=Đã+xảy+ra+lỗi");
        }
    }
}
