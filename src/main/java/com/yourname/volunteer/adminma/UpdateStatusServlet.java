package com.yourname.volunteer.adminma;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;

@WebServlet("/UpdateStatusServlet")
public class UpdateStatusServlet extends HttpServlet {
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        
        String idStr = request.getParameter("id");
        String status = request.getParameter("status");
        
        if (idStr == null || status == null || idStr.isEmpty() || status.isEmpty()) {
            response.sendRedirect("ActivityServlet?error=Missing+parameters");
            return;
        }
        
        int id = Integer.parseInt(idStr);
        
        com.example.dbconnect db = new com.example.dbconnect();
        
        try (Connection conn = db.getConnection()) {
            String sql = "UPDATE activities SET status = ? WHERE id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, status);
            ps.setInt(2, id);
            ps.executeUpdate();
        } catch (Exception e) {
            throw new ServletException("Error updating status", e);
        }
        
      
        response.sendRedirect("ActivityServlet");
    }
}
