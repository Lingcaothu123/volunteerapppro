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
import java.util.ArrayList;
import java.util.List;

@WebServlet("/ActivityServlet")
public class ActivityServlet extends HttpServlet {

    private static final DateTimeFormatter DB_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final DateTimeFormatter HTML_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        if ("delete".equals(action)) {
            int id = Integer.parseInt(request.getParameter("id"));
            try (Connection conn = new dbconnect().getConnection()) {
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

            try (Connection conn = new dbconnect().getConnection()) {
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

                    double lat = act.getLatitude();
                    double lng = act.getLongitude();
                    String mapsLink = (lat != 0 && lng != 0) ?
                        "https://www.google.com/maps?q=" + lat + "," + lng :
                        "https://www.google.com/maps/search/?api=1&query=" + URLEncoder.encode(act.getLocation(), "UTF-8");
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

        // Load danh sách hoạt động
        List<Activity> activityList = new ArrayList<>();
        try (Connection conn = new dbconnect().getConnection()) {
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
                act.setOrganization(rs.getString("organization"));
                act.setContact(rs.getString("contact"));
                act.setStatus(rs.getString("status"));

                double lat = rs.getDouble("latitude");
                double lng = rs.getDouble("longitude");
                act.setLatitude(lat);
                act.setLongitude(lng);

                String mapsLink = (lat != 0 && lng != 0) ?
                    "https://www.google.com/maps?q=" + lat + "," + lng :
                    "https://www.google.com/maps/search/?api=1&query=" + URLEncoder.encode(rs.getString("location"), "UTF-8");
                act.setMapsLink(mapsLink);

                activityList.add(act);
            }

            request.setAttribute("activities", activityList);
            request.getRequestDispatcher("/AdminPage/manage_activities.jsp").forward(request, response);
        } catch (Exception e) {
            throw new ServletException("Error loading activities", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        String action = request.getParameter("action");

        String title = request.getParameter("title");
        String description = request.getParameter("description");
        String startTime = convertHtmlToDbDateTime(request.getParameter("start_time"));
        String endTime = convertHtmlToDbDateTime(request.getParameter("end_time"));
        String location = request.getParameter("location");
        String roles = request.getParameter("roles");
        int capacity = Integer.parseInt(request.getParameter("capacity"));
        String organization = request.getParameter("organization");
        String contact = request.getParameter("contact");
        String status = request.getParameter("status");

        if (status == null || status.trim().isEmpty()) {
            status = "Chưa diễn ra";
        }

        Double latitude = null;
        Double longitude = null;
        try {
            latitude = Double.parseDouble(request.getParameter("latitude"));
            longitude = Double.parseDouble(request.getParameter("longitude"));
        } catch (Exception ignored) {}

        try (Connection conn = new dbconnect().getConnection()) {
            if ("add".equals(action)) {
                PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO activities (title, description, start_time, end_time, location, roles, capacity, organization, contact, status, latitude, longitude) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
                ps.setString(1, title);
                ps.setString(2, description);
                ps.setString(3, startTime);
                ps.setString(4, endTime);
                ps.setString(5, location);
                ps.setString(6, roles);
                ps.setInt(7, capacity);
                ps.setString(8, organization);
                ps.setString(9, contact);
                ps.setString(10, status);
                ps.setObject(11, latitude, Types.DOUBLE);
                ps.setObject(12, longitude, Types.DOUBLE);
                ps.executeUpdate();
            } else if ("update".equals(action)) {
                int id = Integer.parseInt(request.getParameter("id"));
                PreparedStatement ps = conn.prepareStatement(
                    "UPDATE activities SET title=?, description=?, start_time=?, end_time=?, location=?, roles=?, capacity=?, organization=?, contact=?, status=?, latitude=?, longitude=? WHERE id=?");
                ps.setString(1, title);
                ps.setString(2, description);
                ps.setString(3, startTime);
                ps.setString(4, endTime);
                ps.setString(5, location);
                ps.setString(6, roles);
                ps.setInt(7, capacity);
                ps.setString(8, organization);
                ps.setString(9, contact);
                ps.setString(10, status);
                ps.setObject(11, latitude, Types.DOUBLE);
                ps.setObject(12, longitude, Types.DOUBLE);
                ps.setInt(13, id);
                ps.executeUpdate();
            }

            response.sendRedirect("ActivityServlet");

        } catch (Exception e) {
            throw new ServletException("Error saving activity", e);
        }
    }

    private String convertDbToHtmlDateTime(String dbDateTime) {
        try {
            if (dbDateTime.contains(".")) {
                dbDateTime = dbDateTime.split("\\.")[0];
            }
            LocalDateTime dateTime = LocalDateTime.parse(dbDateTime, DB_FORMATTER);
            return dateTime.format(HTML_FORMATTER);
        } catch (Exception e) {
            return "";
        }
    }

    private String convertHtmlToDbDateTime(String htmlDateTime) {
        try {
            return LocalDateTime.parse(htmlDateTime, HTML_FORMATTER).format(DB_FORMATTER);
        } catch (Exception e) {
            return null;
        }
    }
}
