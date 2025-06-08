package com.yourname.volunteer.adminma;

import com.example.dbconnect;  // nhớ import đúng class dbconnect của bạn
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.*;
import java.sql.*;

@WebServlet("/ReportServlet")
public class ReportServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String type = request.getParameter("type");
        response.setContentType("text/html;charset=UTF-8");

        PrintWriter out = response.getWriter();

        com.example.dbconnect db = new com.example.dbconnect();
        try (Connection conn = db.getConnection()) {

            if ("activityByCategory".equals(type)) {
                out.println("<h3>Activities by Category</h3><table border='1'>");
                out.println("<tr><th>Category</th><th>Activity Count</th></tr>");
                String sql = "SELECT c.name, COUNT(a.id) as activity_count FROM categories c " +
                             "LEFT JOIN activities a ON a.id IN (SELECT id FROM activities WHERE a.title LIKE '%' || c.name || '%') " +
                             "GROUP BY c.name";
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    out.println("<tr><td>" + rs.getString("name") + "</td><td>" + rs.getInt("activity_count") + "</td></tr>");
                }
                out.println("</table>");

            } else if ("volunteerCheckins".equals(type)) {
                out.println("<h3>Volunteer Check-ins</h3><table border='1'>");
                out.println("<tr><th>Volunteer</th><th>Activity</th><th>Check-in Time</th></tr>");
                String sql = "SELECT u.name, a.title, c.checkin_time FROM checkins c " +
                             "JOIN volunteers v ON c.volunteer_id = v.id " +
                             "JOIN users u ON v.user_id = u.id " +
                             "JOIN activities a ON v.activity_id = a.id";
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery();
                while(rs.next()) {
                    out.println("<tr>");
                    out.println("<td>" + rs.getString("name") + "</td>");
                    out.println("<td>" + rs.getString("title") + "</td>");
                    out.println("<td>" + rs.getString("checkin_time") + "</td>");
                    out.println("</tr>");
                }
                out.println("</table>");

            } else if ("ratings".equals(type)) {
                out.println("<h3>Average Ratings</h3><table border='1'>");
                out.println("<tr><th>Activity</th><th>Average Rating</th></tr>");
                String sql = "SELECT a.title, AVG(r.rating) AS avg_rating FROM ratings r " +
                             "JOIN activities a ON r.activity_id = a.id " +
                             "GROUP BY a.title";
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery();
                while(rs.next()) {
                    out.println("<tr><td>" + rs.getString("title") + "</td><td>" + rs.getDouble("avg_rating") + "</td></tr>");
                }
                out.println("</table>");

            } else {
                out.println("<h3>Report type not found</h3>");
            }

        } catch (Exception e) {
            e.printStackTrace(out);
        }
    }
}
