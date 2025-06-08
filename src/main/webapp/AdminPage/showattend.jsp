<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.yourname.volunteer.adminma.Activity" %>
<%@ include file="/hadmin.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <title>Manage Activities</title>
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <script>
        function generateQRCode(idactive) {
            $.ajax({
                url: 'QRCodeGenerator',
                method: 'GET',
                data: { idactive: idactive },  // Gửi ID của hoạt động
                success: function(response) {
                    // Tạo vùng chứa QR code và hiển thị nó
                    $('#qrCode_' + idactive).html('<img src="data:image/png;base64,' + response + '" alt="QR Code" />');
                },
                error: function() {
                    alert('Có lỗi xảy ra khi tạo mã QR!');
                }
            });
        }
    </script>
</head>
<body>
<h2>Activity List</h2>
<table border="1">
    <tr>
        <th>ID</th>
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
        <th>Điểm danh</th>
    </tr>

    <%
        List<Activity> activities = (List<Activity>) request.getAttribute("activities");
        if (activities != null) {
            for (Activity a : activities) {
                // Kiểm tra nếu trạng thái là "Đang diễn ra"
                if ("Đang diễn ra".equals(a.getStatus())) {
                    String idactive = String.valueOf(a.getId());  // Lấy ID hoạt động để tạo URL
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
            <button onclick="generateQRCode(<%= idactive %>)">Tạo mã QR</button>
            <div id="qrCode_<%= idactive %>"></div>  <!-- Vùng chứa mã QR sẽ được hiển thị ở đây -->
        </td>
    </tr>
    <%
                }
            }
        }
    %>
</table>
</body>
</html>
