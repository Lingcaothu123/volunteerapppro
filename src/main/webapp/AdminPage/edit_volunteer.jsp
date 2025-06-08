<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.yourname.volunteer.adminma.Volunteer" %>
<%
    Volunteer v = (Volunteer) request.getAttribute("volunteer");
    if (v == null) {
        out.println("Không tìm thấy volunteer để chỉnh sửa.");
        return;
    }
%>
<html>
<head><title>Edit Volunteer</title></head>
<body>
<h2>Edit Volunteer</h2>

<form action="VolunteerServlet" method="post">
    <input type="hidden" name="action" value="edit" />
    <input type="hidden" name="id" value="<%= v.getId() %>" />

    User ID: <input type="text" name="user_id" value="<%= v.getUserId() %>" /><br/>
    Activity ID: <input type="text" name="activity_id" value="<%= v.getActivityId() %>" /><br/>

    Status:
    <select name="status">
        <option value="Đang chờ xác nhận" <%= "Đang chờ xác nhận".equals(v.getStatus()) ? "selected" : "" %>>Đang chờ xác nhận</option>
        <option value="Đã xác nhận" <%= "Đã xác nhận".equals(v.getStatus()) ? "selected" : "" %>>Đã xác nhận</option>
 <option value="Hủy đăng ký" <%= "Hủy đăng ký".equals(v.getStatus()) ? "selected" : "" %>>Hủy đăng ký</option>
    </select><br/>

    <input type="submit" value="Save" />
</form>

<a href="VolunteerServlet">Back to list</a>
</body>
</html>
