<%-- 
    Document   : dashboard
    Created on : 2 thg 6, 2025, 20:21:02
    Author     : leduyduong
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Admin Dashboard</title>
    <link rel="stylesheet" href="../assets/css/style.css">
</head>
<body>
<h2>Welcome Admin</h2>
<ul>
    <li><a href="${pageContext.request.contextPath}/ActivityServlet">Activity Management</a></li>
    <li><a href="${pageContext.request.contextPath}/CategoryServlet">Category Management</a></li>
    <li><a href="${pageContext.request.contextPath}/VolunteerServlet">Volunteer Management</a></li>
        <li><a href="${pageContext.request.contextPath}/Attend">Điểm danh</a></li>
    <li><a href="${pageContext.request.contextPath}/ReportServlet">Reports</a></li>
</ul>
</body>
</html>