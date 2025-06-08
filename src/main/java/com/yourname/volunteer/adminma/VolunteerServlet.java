package com.yourname.volunteer.adminma;

import com.example.dbconnect;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/VolunteerServlet")
public class VolunteerServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // GET: Hiển thị danh sách hoặc form sửa
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        if ("edit".equals(action)) {
            String idStr = request.getParameter("id");
            if (idStr == null || idStr.isEmpty()) {
                response.sendRedirect("VolunteerServlet?error=Missing+id");
                return;
            }

            int id = Integer.parseInt(idStr);
            dbconnect db = new dbconnect();

            try (Connection conn = db.getConnection()) {
                String sql = "SELECT id, iduser, idactive, trangthaidangky FROM trangthai WHERE id=?";
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setInt(1, id);
                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    Volunteer volunteer = new Volunteer();
                    volunteer.setId(rs.getInt("id"));
                    volunteer.setUserId(rs.getInt("iduser"));
                    volunteer.setActivityId(rs.getInt("idactive"));
                    volunteer.setStatus(rs.getString("trangthaidangky"));
                    request.setAttribute("volunteer", volunteer);

                    request.getRequestDispatcher("/AdminPage/edit_volunteer.jsp").forward(request, response);
                } else {
                    response.sendRedirect("VolunteerServlet?error=Volunteer+not+found");
                }

            } catch (SQLException e) {
                throw new ServletException("Error loading volunteer for edit", e);
            }

        } else {
            // Hiển thị danh sách volunteer
            List<Volunteer> volunteers = new ArrayList<>();
            dbconnect db = new dbconnect();

            try (Connection conn = db.getConnection()) {
                String sql = "SELECT id, iduser, idactive, trangthaidangky FROM trangthai";
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery();

                while (rs.next()) {
                    Volunteer volunteer = new Volunteer();
                    volunteer.setId(rs.getInt("id"));
                    volunteer.setUserId(rs.getInt("iduser"));
                    volunteer.setActivityId(rs.getInt("idactive"));
                    volunteer.setStatus(rs.getString("trangthaidangky"));
                    volunteers.add(volunteer);
                }

                request.setAttribute("volunteers", volunteers);
                request.getRequestDispatcher("/AdminPage/manage_volunteers.jsp").forward(request, response);

            } catch (SQLException e) {
                throw new ServletException("Error loading volunteers", e);
            }
        }
    }

// POST: chỉ xử lý edit
// POST: chỉ xử lý edit
@Override
protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    String action = request.getParameter("action");
    String idStr = request.getParameter("id");

    if (!"edit".equals(action)) {
        response.sendRedirect("VolunteerServlet");
        return;
    }

    dbconnect db = new dbconnect();

    try (Connection conn = db.getConnection()) {
        int id = Integer.parseInt(idStr);
        int userId = Integer.parseInt(request.getParameter("user_id"));
        int activityId = Integer.parseInt(request.getParameter("activity_id"));
        String status = request.getParameter("status");

        // Xử lý trạng thái đặc biệt cho "Hủy đăng ký"
        String actualStatus = status.equals("Hủy đăng ký") ? "Admin hủy" : status;

        // 1. Cập nhật bảng trangthai
        String sql = "UPDATE trangthai SET iduser=?, idactive=?, trangthaidangky=? WHERE id=?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, userId);
        ps.setInt(2, activityId);
        ps.setString(3, actualStatus);
        ps.setInt(4, id);
        ps.executeUpdate();

        // 2. Nếu là "Đã xác nhận" → Thêm vào attend
        if ("Đã xác nhận".equals(status)) {
            String checkSql = "SELECT COUNT(*) FROM attend WHERE iduser=? AND idactive=?";
            PreparedStatement checkStmt = conn.prepareStatement(checkSql);
            checkStmt.setInt(1, userId);
            checkStmt.setInt(2, activityId);
            ResultSet rs = checkStmt.executeQuery();
            rs.next();
            int count = rs.getInt(1);

            if (count == 0) {
                String insertAttend = "INSERT INTO attend (iduser, idactive, diemdanh) VALUES (?, ?, ?)";
                PreparedStatement attendStmt = conn.prepareStatement(insertAttend);
                attendStmt.setInt(1, userId);
                attendStmt.setInt(2, activityId);
                attendStmt.setString(3, "Chưa điểm danh");
                attendStmt.executeUpdate();
            }
        }

        // 3. Nếu là "Hủy đăng ký" → Xóa khỏi attend
        if ("Hủy đăng ký".equals(status)) {
            String deleteAttend = "DELETE FROM attend WHERE iduser=? AND idactive=?";
            PreparedStatement deleteStmt = conn.prepareStatement(deleteAttend);
            deleteStmt.setInt(1, userId);
            deleteStmt.setInt(2, activityId);
            deleteStmt.executeUpdate();
        }

        response.sendRedirect("VolunteerServlet");

    } catch (SQLException e) {
        throw new ServletException("Database error during edit", e);
    }
}


}
