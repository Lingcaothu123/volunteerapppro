<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.yourname.volunteer.adminma.Activity" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.util.Date" %>
<%@ include file="/hadmin.jsp" %>
<!DOCTYPE html>
<%!
    public String convertDbToHtmlDateTime(Date date) {
        if (date == null) return "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
        return sdf.format(date);
    }

  public Date parseStringToDate(String datetimeStr) {
    if (datetimeStr == null || datetimeStr.trim().isEmpty()) return null;
    try {
        // Thay đổi format cho phù hợp dữ liệu "2025-05-31T18:59"
        SimpleDateFormat dbFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
        return dbFormat.parse(datetimeStr);
    } catch (Exception e) {
        return null;
    }
}

%>

<%
    String action = request.getParameter("action");
    Activity activity = (Activity) request.getAttribute("activity");
    boolean isEdit = ("edit".equals(action) && activity != null);
%>

<html>
<head>
    <title><%= isEdit ? "Edit Activity" : "Add New Activity" %></title>
    <link rel="stylesheet" href="https://unpkg.com/leaflet@1.9.3/dist/leaflet.css" />
    <style>
        #map { height: 300px; margin-top: 10px; }
    </style>
</head>
<body>
    <h2><%= isEdit ? "Edit Activity" : "Add New Activity" %></h2>

    <form method="post" action="<%= request.getContextPath() %>/ActivityServlet">
        <input type="hidden" name="action" value="<%= isEdit ? "update" : "add" %>">
        <% if (isEdit) { %>
            <input type="hidden" name="id" value="<%= activity.getId() %>">
        <% } %>

        Title: <input type="text" name="title" value="<%= isEdit ? activity.getTitle() : "" %>"><br>
        Description: <input type="text" name="description" value="<%= isEdit ? activity.getDescription() : "" %>"><br>
        Start Time:
        <input type="datetime-local" name="start_time"
               value="<%= isEdit ? convertDbToHtmlDateTime(parseStringToDate(activity.getStartTime())) : "" %>"><br>
        End Time:
        <input type="datetime-local" name="end_time"
               value="<%= isEdit ? convertDbToHtmlDateTime(parseStringToDate(activity.getEndTime())) : "" %>"><br>
        Location: <input type="text" name="location" value="<%= isEdit ? activity.getLocation() : "" %>"><br>
        Roles: <input type="text" name="roles" value="<%= isEdit ? activity.getRoles() : "" %>"><br>
        Capacity: <input type="number" name="capacity" value="<%= isEdit ? activity.getCapacity() : "" %>"><br>
        Organization: <input type="text" name="organization" value="<%= isEdit ? activity.getOrganization() : "" %>"><br>
        Contact: <input type="text" name="contact" value="<%= isEdit ? activity.getContact() : "" %>"><br>
        <% if (isEdit) { %>
    Status:
    <select name="status" required>
        <option value="Chưa diễn ra" <%= "Chưa diễn ra".equals(activity.getStatus()) ? "selected" : "" %>>Chưa diễn ra</option>
        <option value="Đang diễn ra" <%= "Đang diễn ra".equals(activity.getStatus()) ? "selected" : "" %>>Đang diễn ra</option>
        <option value="Đã diễn ra" <%= "Đã diễn ra".equals(activity.getStatus()) ? "selected" : "" %>>Đã diễn ra</option>
    </select><br><br>
<% } %>
        <!-- Hidden fields for coordinates -->
        <input type="hidden" name="latitude" id="latitude" value="<%= isEdit ? activity.getLatitude() : "" %>">
        <input type="hidden" name="longitude" id="longitude" value="<%= isEdit ? activity.getLongitude() : "" %>">

        <!-- Map UI -->
        <h3>Chọn vị trí trên bản đồ</h3>
        <div id="map"></div>

        <br>
        <input type="submit" value="<%= isEdit ? "Update" : "Add" %> Activity">
    </form>

    <a href="<%= request.getContextPath() %>/ActivityServlet">Back to List</a>

    <!-- Leaflet Scripts -->
    <script src="https://unpkg.com/leaflet@1.9.3/dist/leaflet.js"></script>
    <script>
        var defaultLat = <%= isEdit && activity.getLatitude() != 0 ? activity.getLatitude() : 21.0285 %>;
        var defaultLng = <%= isEdit && activity.getLongitude() != 0 ? activity.getLongitude() : 105.8542 %>;

        var map = L.map('map').setView([defaultLat, defaultLng], 13);
        L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
            attribution: '© OpenStreetMap contributors',
        }).addTo(map);

        var marker = L.marker([defaultLat, defaultLng]).addTo(map);

        document.getElementById("latitude").value = defaultLat;
        document.getElementById("longitude").value = defaultLng;

        map.on('click', function(e) {
            var lat = e.latlng.lat.toFixed(6);
            var lng = e.latlng.lng.toFixed(6);

            marker.setLatLng([lat, lng]);
            document.getElementById("latitude").value = lat;
            document.getElementById("longitude").value = lng;
        });
    </script>
</body>
</html>
