package com.example.activity;

import java.io.*;
import java.sql.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.net.URLEncoder;

@WebServlet("/allbooks")
public class showactive extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        StringBuilder data = new StringBuilder();

        String keyword = request.getParameter("keyword");
        String[] columns = request.getParameterValues("columns");
        String iduser = (String) request.getSession().getAttribute("iduser");

        try {
            com.example.dbconnect db = new com.example.dbconnect();
            try (Connection connection = db.getConnection()) {

                StringBuilder query = new StringBuilder("SELECT * FROM activities");
                boolean useFilter = (keyword != null && !keyword.trim().isEmpty()) && (columns != null && columns.length > 0);

                if (useFilter) {
                    query.append(" WHERE ");
                    for (int i = 0; i < columns.length; i++) {
                        if (i > 0) query.append(" OR ");
                        query.append(columns[i]).append(" LIKE ?");
                    }
                }

                try (PreparedStatement stmt = connection.prepareStatement(query.toString())) {
                    if (useFilter) {
                        for (int i = 0; i < columns.length; i++) {
                            stmt.setString(i + 1, "%" + keyword + "%");
                        }
                    }

                    try (ResultSet rs = stmt.executeQuery()) {
                        int counter = 1;
                        data.append("<div style='display: flex; align-items: flex-start;'>");
                        data.append("<table border='1'>");
                        data.append("<tr>")
                                .append("<th>STT</th>")
                                .append("<th>Tên hoạt động</th>")
                                .append("<th>Địa điểm</th>")
                                .append("<th>Maps</th>")
                                .append("<th>Tổ chức</th>")
                                .append("<th>Thời gian kết thúc</th>")
                                .append("<th>Thời gian bắt đầu</th>")
                                .append("<th>Trạng thái</th>")
                                .append("<th>Ghi chú</th>")
                                .append("<th>Đăng ký</th>")
                                .append("</tr>");

                        while (rs.next()) {
                            String id = rs.getString("id");
                            String tenhd = rs.getString("title");
                            String diadiem = rs.getString("location");
                            double latitude = rs.getDouble("latitude");
                            double longitude = rs.getDouble("longitude");
                            String tochuc = rs.getString("organization");
                            String trangThai = rs.getString("status");
                            String startTimeStr = rs.getString("start_time");
                            String endTimeStr = rs.getString("end_time");
                            String ghichu = rs.getString("description");

                            String mapsLink;
                            if (latitude != 0 && longitude != 0) {
                                mapsLink = "https://www.google.com/maps?q=" + latitude + "," + longitude;
                            } else {
                                String diadiemEncoded = URLEncoder.encode(diadiem, "UTF-8");
                                mapsLink = "https://www.google.com/maps/search/?api=1&query=" + diadiemEncoded;
                            }

                            data.append("<tr>")
                                .append("<td>").append(counter++).append("</td>")
                                .append("<td>").append(tenhd).append("</td>")
                                .append("<td>").append(diadiem).append("</td>")
                                .append("<td><a href='").append(mapsLink).append("' target='_blank'>Xem bản đồ</a></td>")
                                .append("<td>").append(tochuc).append("</td>")
                                .append("<td>").append(endTimeStr).append("</td>")
                                .append("<td>").append(startTimeStr).append("</td>")
                                .append("<td>").append(trangThai).append("</td>")
                                .append("<td>").append(ghichu).append("</td>")
                                .append("<td>");

                            boolean duocDangKy = false;

                            if (iduser != null && !iduser.isEmpty()) {
                                String checkQuery = "SELECT 1 FROM trangthai WHERE idactive = ? AND iduser = ?";
                                try (PreparedStatement checkStmt = connection.prepareStatement(checkQuery)) {
                                    checkStmt.setString(1, id);
                                    checkStmt.setString(2, iduser);

                                    try (ResultSet checkRs = checkStmt.executeQuery()) {
                                        boolean daDangKy = checkRs.next();
                                        if (!daDangKy && "Chưa diễn ra".equals(trangThai)) {
                                            duocDangKy = true;
                                        }
                                    }
                                }
                            }

                            if (duocDangKy) {
                                data.append("<form action='register-activity' method='post'>")
                                    .append("<input type='hidden' name='activityId' value='").append(id).append("' />")
                                    .append("<input type='hidden' name='iduser' value='").append(iduser).append("' />")
                                    .append("<button type='submit'>Đăng ký</button>")
                                    .append("</form>");
                            } else {
                                data.append("<button disabled>Không thể đăng ký</button>");
                            }
                            data.append("</td></tr>");
                        }

                        data.append("</table></div>");
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            data.append("<p>Lỗi: ").append(e.getMessage()).append("</p>");
        }

        try (PrintWriter out = response.getWriter()) {
            out.println(data.toString());
        }
    }
}
