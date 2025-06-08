package com.example;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.sql.*;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

@WebServlet("/personalhistory")
public class Personalhistory extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("username") == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        String username = (String) session.getAttribute("username");
        String iduser = (String) session.getAttribute("iduser");
        StringBuilder data = new StringBuilder();

        // Câu truy vấn SQL đã chỉnh sửa đúng cột
        String sql ="select * from activities a join  History h ON a.id = h.id " +
        "where h.iduser=?" +
        "order by h.ngaygiothaotac asc";

        try (Connection conn = new dbconnect().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, iduser);
            try (ResultSet rs = stmt.executeQuery()) {
                int counter = 1;
                data.append("<html><head><meta charset='UTF-8'><title>Lịch sử hoạt động</title></head><body>");
                data.append("<h2>Lịch sử hoạt động cá nhân</h2>");
                data.append("<div style='display: flex; align-items: flex-start;'>");
                data.append("<table border='1'>")
                    .append("<tr>")
                    .append("<th>STT</th>")
                    .append("<th>Tên hoạt động</th>")
                    .append("<th>Địa điểm</th>")
                    .append("<th>Định vị</th>")
                    .append("<th>Tổ chức</th>")
                    .append("<th>Thời gian bắt đầu</th>")
                    .append("<th>Thời gian kết thúc</th>")
                    .append("<th>Trạng thái sự kiện</th>")
                   .append("<th>Trạng thái xử lý</th>")
                    .append("<th>Ngày giờ thao tác</th>")                
                    .append("</tr>");

                while (rs.next()) {
                    String title = rs.getString("title");
                    String location = rs.getString("location");
                    String organization = rs.getString("organization");
                    String endTime = rs.getString("end_time");
                    String startTime = rs.getString("start_time");
                    String status = rs.getString("status");
                    String statususer = rs.getString("tt");           
                    Timestamp thaoTacTime = rs.getTimestamp("ngaygiothaotac");
                    double latitude = rs.getDouble("latitude");
                    double longitude = rs.getDouble("longitude");

                    String mapsLink;
                    if (latitude != 0 && longitude != 0) {
                        mapsLink = "https://www.google.com/maps?q=" + latitude + "," + longitude;
                    } else {
                        String locationEncoded = URLEncoder.encode(location, "UTF-8");
                        mapsLink = "https://www.google.com/maps/search/?api=1&query=" + locationEncoded;
                    }

                    data.append("<tr>")
                        .append("<td>").append(counter++).append("</td>")
                        .append("<td>").append(title).append("</td>")
                        .append("<td>").append(location).append("</td>")
                        .append("<td><a href='").append(mapsLink).append("' target='_blank'>Xem bản đồ</a></td>")
                        .append("<td>").append(organization).append("</td>")
                                  .append("<td>").append(startTime != null ? startTime : "").append("</td>")
                        .append("<td>").append(endTime != null ? endTime : "").append("</td>")                  
                        .append("<td>").append(status != null ? status : "").append("</td>")
                        .append("<td>").append(statususer != null ? statususer : "").append("</td>")
                        .append("<td>").append(thaoTacTime != null ? thaoTacTime : "").append("</td>")
                 
                        .append("</tr>");
                }

                data.append("</table></div></body></html>");
            }

        } catch (Exception e) {
            e.printStackTrace();
            data.append("<p>Lỗi khi truy vấn dữ liệu: ").append(e.getMessage()).append("</p>");
        }

        try (PrintWriter out = response.getWriter()) {
            out.println(data.toString());
        }
    }
}
