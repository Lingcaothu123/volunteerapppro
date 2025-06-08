<%@ page import="java.sql.*" %>
<%@ page import="com.example.dbconnect" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page session="true" %>
  <%@ include file="navbar.jsp" %> 
<%
    String iduser = (String) session.getAttribute("iduser");
        String username = (String) session.getAttribute("username");
    if (iduser == null) {
        response.sendRedirect("login.jsp");
        return;
    }

    // Biến lưu thông tin người dùng
    String fullname = "", phone = "", skills = "", email = "";

    try {
        com.example.dbconnect db = new com.example.dbconnect();
        try (Connection conn = db.getConnection()) {
            String sql = "SELECT fullname, phone, skills, email FROM login1 WHERE iduser = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, iduser);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                fullname = rs.getString("fullname");
                phone = rs.getString("phone");
                skills = rs.getString("skills");
                email = rs.getString("email");
            }

            rs.close();
            ps.close();
        }
    } catch (Exception e) {
        e.printStackTrace();
    }

    String message = (String) request.getAttribute("message");
    String error = (String) request.getAttribute("error");
%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Cập nhật thông tin</title>
    <style>
        input[type=text], input[type=password] {
            width: 300px; padding: 8px; margin-bottom: 10px;
        }
        button {
            padding: 8px 16px;
            background-color: #4CAF50;
            color: white;
            border: none; border-radius: 4px;
        }
        .msg { color: green; font-weight: bold; }
        .error { color: red; font-weight: bold; }
    </style>
</head>
<body>
    <h2>Xin chào, <%= username %>!</h2>

    <% if (message != null) { %>
        <p class="msg"><%= message %></p>
    <% } %>

    <% if (error != null) { %>
        <p class="error"><%= error %></p>
    <% } %>

    <form method="post" action="update-user" onsubmit="return validateForm()">
        <label>Họ và tên:</label><br>
        <input type="text" name="fullname" value="<%= fullname %>" required><br>

        <label>Số điện thoại:</label><br>
        <input type="text" name="phone" value="<%= phone %>" required><br>

        <label>Kĩ năng:</label><br>
        <input type="text" name="skills" value="<%= skills %>" required><br>

        <!-- <label>Email:</label><br>
        <input type="text" name="email" value="<%= email %>" readonly><br> -->

        <label>Mật khẩu mới:</label><br>
        <input type="password" name="pass" id="pass" placeholder="Nhập mật khẩu mới"><br>

        <label>Xác nhận mật khẩu mới:</label><br>
        <input type="password" name="cfpass" id="cfpass" placeholder="Nhập lại mật khẩu mới"><br>

        <input type="checkbox" onclick="togglePasswords()"> Hiển thị mật khẩu<br><br>

        <button type="submit">Cập nhật</button>
    </form>

    <script>
        function togglePasswords() {
            var pass = document.getElementById("pass");
            var cfpass = document.getElementById("cfpass");
            var type = (pass.type === "password") ? "text" : "password";
            pass.type = type;
            cfpass.type = type;
        }

        function validateForm() {
            var pass = document.getElementById("pass").value;
            var cfpass = document.getElementById("cfpass").value;
            if (pass !== "" && pass !== cfpass) {
                alert("Mật khẩu xác nhận không khớp.");
                return false;
            }
            return true;
        }
    </script>
</body>
</html>
