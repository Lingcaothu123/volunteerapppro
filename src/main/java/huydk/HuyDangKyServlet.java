package huydk;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.*;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@WebServlet("/huydangky")
public class HuyDangKyServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json;charset=UTF-8");
        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("username") == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"error\": \"Unauthorized\"}");
            return;
        }

        String iduser = (String) session.getAttribute("iduser");
        String[] idArray = request.getParameterValues("ids[]");

        if (idArray == null || idArray.length == 0) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"error\": \"Missing or invalid 'ids' parameter\"}");
            return;
        }

        try {
            com.example.dbconnect db = new com.example.dbconnect();      
            try (Connection conn = db.getConnection()) {
                String deleteSql = "DELETE FROM trangthai WHERE idactive = ? AND iduser = ?";
                String insertSql = "INSERT INTO history ( ngaygiothaotac, tt, iduser, id) "
                        + "VALUES ( ?, N'Bạn đã hủy', ?, ?)";

                try (
                    PreparedStatement deleteStmt = conn.prepareStatement(deleteSql);
                    PreparedStatement historyStmt = conn.prepareStatement(insertSql)
                ) {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                    String formattedNow = LocalDateTime.now().format(formatter);

                    for (String idStr : idArray) {
                        int id = Integer.parseInt(idStr);

                        // Xóa dữ liệu trong bảng trangthai
                        deleteStmt.setInt(1, id);
                        deleteStmt.setString(2, iduser);
                        deleteStmt.executeUpdate();

                        // Ghi lịch sử vào bảng history
                        historyStmt.setString(1, formattedNow);
                        historyStmt.setString(2, iduser);
                        historyStmt.setInt(3, id);
                        historyStmt.executeUpdate();
                    }
                }

                response.getWriter().write("{\"success\": true}");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\": \"Server error occurred\"}");
        }
    } // <-- đóng method doPost

} // <-- đóng class HuyDangKyServlet
