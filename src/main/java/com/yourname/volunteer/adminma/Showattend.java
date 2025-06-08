package com.yourname.volunteer.adminma;

import com.example.dbconnect;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.net.URLEncoder;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/Attend")
public class Showattend extends HttpServlet {

    private static final DateTimeFormatter DB_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final DateTimeFormatter HTML_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        if ("delete".equals(action)) {
            int id = Integer.parseInt(request.getParameter("id"));
            com.example.dbconnect db = new com.example.dbconnect();
            try (Connection conn = db.getConnection()) {
                PreparedStatement ps = conn.prepareStatement("DELETE FROM activities WHERE id = ?");
                ps.setInt(1, id);
                ps.executeUpdate();
            } catch (SQLException e) {
                throw new ServletException("Error deleting activity", e);
            }
            response.sendRedirect("ActivityServlet");
            return;
        }

        if ("edit".equals(action)) {
            int id = Integer.parseInt(request.getParameter("id"));
            Activity act = null;

            com.example.dbconnect db = new com.example.dbconnect();
            try (Connection conn = db.getConnection()) {
                PreparedStatement ps = conn.prepareStatement("SELECT * FROM activities WHERE id = ?");
                ps.setInt(1, id);
                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    act = new Activity();
                    act.setId(rs.getInt("id"));
                    act.setTitle(rs.getString("title"));
                    act.setDescription(rs.getString("description"));
                    act.setStartTime(convertDbToHtmlDateTime(rs.getString("start_time")));
                    act.setEndTime(convertDbToHtmlDateTime(rs.getString("end_time")));
                    act.setLocation(rs.getString("location"));
                    act.setRoles(rs.getString("roles"));
                    act.setCapacity(rs.getInt("capacity"));
                    act.setOrganization(rs.getString("organization"));
                    act.setContact(rs.getString("contact"));
                    act.setStatus(rs.getString("status"));
                    act.setLatitude(rs.getDouble("latitude"));
                    act.setLongitude(rs.getDouble("longitude"));

                    // Tạo link maps
                    double latitude = act.getLatitude();
                    double longitude = act.getLongitude();
                    String mapsLink;
                    if (latitude != 0 && longitude != 0) {
                        mapsLink = "https://www.google.com/maps?q=" + latitude + "," + longitude;
                    } else {
                        String diadiemEncoded = URLEncoder.encode(act.getLocation(), "UTF-8");
                        mapsLink = "https://www.google.com/maps/search/?api=1&query=" + diadiemEncoded;
                    }
                    act.setMapsLink(mapsLink);
                }
            } catch (Exception e) {
                throw new ServletException("Error loading activity for edit", e);
            }

            request.setAttribute("activity", act);
            request.setAttribute("action", "edit");
            request.getRequestDispatcher("/AdminPage/activity_form.jsp").forward(request, response);
            return;
        }

        if ("add".equals(action)) {
            request.setAttribute("action", "add");
            request.getRequestDispatcher("/AdminPage/activity_form.jsp").forward(request, response);
            return;
        }

        // Mặc định: load danh sách hoạt động
        List<Activity> activityList = new ArrayList<>();
        com.example.dbconnect db = new com.example.dbconnect();
        try (Connection conn = db.getConnection()) {
            String sql = "SELECT * FROM activities";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Activity act = new Activity();
                act.setId(rs.getInt("id"));
                act.setTitle(rs.getString("title"));
                act.setDescription(rs.getString("description"));
                act.setStartTime(rs.getString("start_time"));
                act.setEndTime(rs.getString("end_time"));
                act.setLocation(rs.getString("location"));
                act.setRoles(rs.getString("roles"));
                act.setCapacity(rs.getInt("capacity"));

                double latitude = rs.getDouble("latitude");
                double longitude = rs.getDouble("longitude");
                act.setLatitude(latitude);
                act.setLongitude(longitude);

                String mapsLink;
                if (latitude != 0 && longitude != 0) {
                    mapsLink = "https://www.google.com/maps?q=" + latitude + "," + longitude;
                } else {
                    String diadiemEncoded = URLEncoder.encode(rs.getString("location"), "UTF-8");
                    mapsLink = "https://www.google.com/maps/search/?api=1&query=" + diadiemEncoded;
                }
                act.setMapsLink(mapsLink);

                act.setOrganization(rs.getString("organization"));
                act.setContact(rs.getString("contact"));
                act.setStatus(rs.getString("status"));

                activityList.add(act);
            }

            request.setAttribute("activities", activityList);
            request.getRequestDispatcher("/AdminPage/showattend.jsp").forward(request, response);
        } catch (Exception e) {
            throw new ServletException("Error loading activities", e);
        }
    }
    private String convertDbToHtmlDateTime(String dbDateTime) {
        if (dbDateTime == null || dbDateTime.isEmpty()) {
            return "";
        }
        try {
            LocalDateTime ldt = LocalDateTime.parse(dbDateTime, DB_FORMATTER);
            return ldt.format(HTML_FORMATTER);
        } catch (DateTimeParseException e) {
            return dbDateTime;
        }
    }

    private String convertHtmlToDbDateTime(String htmlDateTime) {
        if (htmlDateTime == null || htmlDateTime.isEmpty()) {
            return null;
        }
        try {
            LocalDateTime ldt = LocalDateTime.parse(htmlDateTime, HTML_FORMATTER);
            return ldt.format(DB_FORMATTER);
        } catch (DateTimeParseException e) {
            return null;
        }
    }
}
