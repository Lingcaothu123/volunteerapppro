package com.example;

import java.io.*;
import java.sql.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.*;
import jakarta.servlet.http.*;

@WebServlet("/showhistory")
public class ShowHistory extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");
        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("username") == null) {
            response.getWriter().write("<p>Vui lòng đăng nhập để xem lịch sử.</p>");
            return;
        }

        String username = (String) session.getAttribute("username");
       String iduser = (String) request.getSession().getAttribute("iduser");
        StringBuilder data = new StringBuilder();

        String sql = "SELECT active.id, active.title AS tenhd, active.location AS diadiem, active.latitude, active.longitude, " +
                     "active.organization AS tochuc, active.start_time AS timestart, active.end_time AS timeend, " +
                     "trangthai.trangthaidangky, active.status, trangthai.attend " +
                     "FROM activities active " +
                     "JOIN trangthai ON active.id = trangthai.idactive " +
                     "WHERE trangthai.iduser = ? " +
                     "ORDER BY active.start_time ASC";

        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        } catch (ClassNotFoundException e) {
            throw new ServletException("Không tìm thấy driver JDBC SQL Server", e);
        }

        // Kết nối thông qua class dbconnect
        com.example.dbconnect db = new com.example.dbconnect();
        try (Connection conn = db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, iduser);

            try (ResultSet rs = stmt.executeQuery()) {
                // Bắt đầu div flex chứa bảng và button
                data.append("<div style='display: flex; align-items: flex-start;'>");

                // Bảng dữ liệu
                data.append("<table border='1' cellpadding='5' cellspacing='0'>");
                data.append("<tr>")
                    .append("<th>ID</th>")
                    .append("<th>Tên hoạt động</th>")
                    .append("<th>Địa điểm</th>")
                    .append("<th>Định vị</th>")
                    .append("<th>Tổ chức</th>")
                    .append("<th>Thời gian bắt đầu</th>")
                    .append("<th>Thời gian kết thúc</th>")
                    .append("<th>Trạng thái đăng ký</th>")
                    .append("<th>Trạng thái hoạt động</th>")

                    .append("<th>Hủy đăng ký</th>")
                    .append("</tr>");

                while (rs.next()) {
                    int id = rs.getInt("id");
                    String tenhd = rs.getString("tenhd");
                    String diadiem = rs.getString("diadiem");
                    double latitude = rs.getDouble("latitude");
                    double longitude = rs.getDouble("longitude");
                    String tochuc = rs.getString("tochuc");
                    String timestart = rs.getString("timestart");
                    String timeend = rs.getString("timeend");
                    String trangthaidangky = rs.getString("trangthaidangky");
                    String status = rs.getString("status");
                    String mapsLink;
                    if (latitude != 0 && longitude != 0) {
                        mapsLink = "https://www.google.com/maps?q=" + latitude + "," + longitude;
                    } else {
                        mapsLink = "https://www.google.com/maps/search/?api=1&query=" +
                                   java.net.URLEncoder.encode(diadiem != null ? diadiem : "", "UTF-8");
                    }

                    data.append("<tr>")
                        .append("<td>").append(id).append("</td>")
                        .append("<td>").append(tenhd).append("</td>")
                        .append("<td>").append(diadiem).append("</td>")
                        .append("<td><a href='").append(mapsLink).append("' target='_blank'>Xem bản đồ</a></td>")
                        .append("<td>").append(tochuc).append("</td>")
                        .append("<td>").append(timestart).append("</td>")
                        .append("<td>").append(timeend).append("</td>")
                        .append("<td>").append(trangthaidangky).append("</td>")
                        .append("<td>").append(status).append("</td>")
               
.append("<td>");
if ("Đã xác nhận".equalsIgnoreCase(trangthaidangky)) {
    data.append("<input type='checkbox' name='register' value='").append(id).append("' disabled>");
} else {
    data.append("<input type='checkbox' name='register' value='").append(id).append("'>");
}
data.append("</td>")
    .append("</tr>");
                }
                data.append("</table>");

                // Button hủy, cách bảng 20px
                data.append("<div style='margin-left: 20px;'>")
                    .append("<button onclick=\"huyNhieuHoatDong()\">Hủy đăng ký các hoạt động đã chọn</button>")
                    .append("</div>");

                data.append("</div>"); // kết thúc div flex
            }

        } catch (SQLException e) {
            e.printStackTrace();
            data.append("<p>Lỗi khi truy vấn dữ liệu: ").append(e.getMessage()).append("</p>");
        }

        try (PrintWriter out = response.getWriter()) {
            out.print(data.toString());
        }
    }
}
