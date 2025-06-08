package com.yourname.volunteer.adminma;

import com.example.dbconnect;  // nhớ import đúng class dbconnect của bạn
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/CategoryServlet")
public class CategoryServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // GET: Hiển thị danh sách categories
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<Category> categories = new ArrayList<>();

        com.example.dbconnect db = new com.example.dbconnect();
        try (Connection conn = db.getConnection()) {
            String sql = "SELECT id, name FROM categories";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                categories.add(new Category(rs.getInt("id"), rs.getString("name")));
            }
            System.out.println("Số lượng categories lấy được: " + categories.size());

            request.setAttribute("categories", categories);
            request.getRequestDispatcher("/AdminPage/manage_categories.jsp").forward(request, response);

        } catch (SQLException e) {
            throw new ServletException("Error loading categories", e);
        }
    }

    // POST: Thêm/Sửa/Xóa category
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        String idStr = request.getParameter("id");
        String name = request.getParameter("category");

        com.example.dbconnect db = new com.example.dbconnect();
        try (Connection conn = db.getConnection()) {
            PreparedStatement ps;

            switch (action) {
                case "add":
                    ps = conn.prepareStatement("INSERT INTO categories (name) VALUES (?)");
                    ps.setString(1, name);
                    ps.executeUpdate();
                    break;

                case "edit":
                    int editId = Integer.parseInt(idStr);
                    ps = conn.prepareStatement("UPDATE categories SET name=? WHERE id=?");
                    ps.setString(1, name);
                    ps.setInt(2, editId);
                    ps.executeUpdate();
                    break;

                case "delete":
                    int deleteId = Integer.parseInt(idStr);
                    ps = conn.prepareStatement("DELETE FROM categories WHERE id=?");
                    ps.setInt(1, deleteId);
                    ps.executeUpdate();
                    break;
            }

            response.sendRedirect("CategoryServlet");

        } catch (SQLException e) {
            throw new ServletException("Error processing category action", e);
        }
    }
}
