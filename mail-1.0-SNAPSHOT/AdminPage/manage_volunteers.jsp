
<%-- 
    Document   : manage_volunteers
    Created on : 5 thg 6, 2025
    Author     : leduyduong
--%>

<%@page import="com.yourname.volunteer.adminma.Volunteer"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.yourname.volunteer.adminma.Volunteer" %>
<%@ include file="/hadmin.jsp" %>
<!DOCTYPE html>

<html>
<head><title>Manage Volunteers</title></head>
<body>
<h2>Volunteers Management</h2>

<!-- Bảng hiển thị danh sách volunteer -->
<table border="1">
<tr>
    <th>ID</th>
    <th>User ID</th>
    <th>Activity ID</th>
    <th>Status</th>
    <th>Actions</th>
</tr>

<%
    List<Volunteer> volunteers = (List<Volunteer>) request.getAttribute("volunteers");
    if (volunteers != null) {
        for (Volunteer v : volunteers) {
%>
<tr>
    <td><%= v.getId() %></td>
    <td><%= v.getUserId() %></td>
    <td><%= v.getActivityId() %></td>
    <td><%= v.getStatus() %></td>
    <td>
        <a href="VolunteerServlet?action=edit&id=<%= v.getId() %>">Edit</a> 
    </td>
</tr>
<%
        }
    }
%>

</table>
</body>
</html>
