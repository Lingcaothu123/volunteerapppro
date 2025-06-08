<%-- 
    Document   : reports
    Created on : 5 thg 6, 2025
    Author     : leduyduong
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/hadmin.jsp" %>
<!DOCTYPE html>
<html>
<head><title>Reports</title></head>
<body>
<h2>Reports & Statistics</h2>

<!-- Danh sách báo cáo -->
<table border="1">
    <tr><th>Report Type</th><th>View</th></tr>
    <tr>
        <td>Total Activities by Category</td>
        <td><a href="ReportServlet?type=activityByCategory">View</a></td>
    </tr>
    <tr>
        <td>Total Volunteers by Activity</td>
        <td><a href="ReportServlet?type=volunteersByActivity">View</a></td>
    </tr>
    <tr>
        <td>Volunteer Check-ins</td>
        <td><a href="ReportServlet?type=volunteerCheckins">View</a></td>
    </tr>
    <tr>
        <td>Average Ratings</td>
        <td><a href="ReportServlet?type=ratings">View</a></td>
    </tr>
</table>

</body>
</html>
