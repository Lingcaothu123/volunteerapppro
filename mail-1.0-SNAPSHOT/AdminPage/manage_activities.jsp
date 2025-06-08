<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.yourname.volunteer.adminma.Activity" %>
<%@ include file="/hadmin.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <title>Manage Activities</title>
</head>
<body>
<h2>Activity List</h2>

<a href="ActivityServlet?action=add">Add New Activity</a>


<table border="1">
    <tr>
        <th>id</th>
        <th>Title</th>
        <th>Description</th>
        <th>Start Time</th>
        <th>End Time</th>
        <th>Location</th>
        <th>Maps</th>
        <th>Roles</th>
        <th>Capacity</th>
        <th>Organization</th>
        <th>Contact</th>
        <th>Status</th>
        <th>Actions</th>
    </tr>

    <%
        List<Activity> activities = (List<Activity>) request.getAttribute("activities");
        if (activities != null) {
            for (Activity a : activities) {
    %>
    <tr>
         <td><%= a.getId() %></td>
        <td><%= a.getTitle() %></td>
        <td><%= a.getDescription() %></td>
        <td><%= a.getStartTime() %></td>
        <td><%= a.getEndTime() %></td>
        <td><%= a.getLocation() %></td>
        <td>
            <a href="<%= a.getMapsLink() %>" target="_blank">View Map</a>
        </td>
        <td><%= a.getRoles() %></td>
        <td><%= a.getCapacity() %></td>
        <td><%= a.getOrganization() %></td>
        <td><%= a.getContact() %></td>
        <td><%= a.getStatus() %></td>
        <td>
            <a href="ActivityServlet?action=edit&id=<%= a.getId() %>">Edit</a> |
            <a href="ActivityServlet?action=delete&id=<%= a.getId() %>" onclick="return confirm('Delete this activity?');">Delete</a>
        </td>
    </tr>
    <%
            }
        }
    %>
</table>
</body>
</html>
