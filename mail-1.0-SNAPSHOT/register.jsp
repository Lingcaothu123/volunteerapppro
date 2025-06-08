<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Register</title>
    <link rel="stylesheet" href="css/regstyle.css">
</head>
<body>
    <h1>Register for Bookstore</h1>

    <%
        String error = (String) request.getAttribute("error");
        String success = (String) request.getAttribute("success");

        if (error == null) error = request.getParameter("error");
        if (success == null) success = request.getParameter("success");

        if (error != null) {
    %>
        <p class="error"><%= error %></p>
    <%
        } else if (success != null) {
    %>
        <p class="success"><%= success %></p>
    <%
        }
    %>

    <form action="register" method="post">
        <label>Username:</label>
        <input type="text" name="username" required>

        <label>Password:</label>
        <input type="password" name="password" required>

        <label>Confirm Password:</label>
        <input type="password" name="confirmPassword" required>

        <label>Full Name:</label>
        <input type="text" name="fullname" required>

        <label>Phone Number:</label>
        <input type="tel" name="phone" pattern="[0-9]{10,15}" required>

        <label>Skills:</label>
        <input type="text" name="skills">

        <label>Email:</label>
        <input type="email" name="email" required>

        <button type="submit">Register</button>
    </form>

    <p>Already have an account? <a href="login.jsp">Login here</a></p>
</body>
</html>
