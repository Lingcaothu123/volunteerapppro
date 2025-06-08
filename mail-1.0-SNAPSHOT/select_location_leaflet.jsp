<%@ page import="java.sql.*" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Chọn địa điểm (Leaflet + OSM + Tìm kiếm địa điểm)</title>
    <link rel="stylesheet" href="https://unpkg.com/leaflet@1.9.3/dist/leaflet.css" />
    <style>
        html, body {
            height: 100%;
            margin: 0;
            padding: 0;
        }
        #map {
            height: calc(100vh - 180px);
            width: 100%;
        }
        #searchBox { width: 300px; padding: 5px; }
        #searchBtn { padding: 5px 10px; }
    </style>
</head>
<body>
<h2>Click bản đồ để chọn vị trí cần lưu</h2>

<!-- Ô tìm kiếm -->
<input type="text" id="searchBox" placeholder="Nhập địa điểm cần tìm">
<button id="searchBtn" type="button">Tìm địa điểm</button>

<form method="post">
    <input type="hidden" name="lat" id="lat">
    <input type="hidden" name="lng" id="lng">
    <button type="submit">Lưu vị trí vào hệ thống</button>
</form>

<div id="map"></div>

<script src="https://unpkg.com/leaflet@1.9.3/dist/leaflet.js"></script>
<script>
    var map = L.map('map').setView([21.0285, 105.8542], 15);

    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
        attribution: '© OpenStreetMap contributors',
    }).addTo(map);

    var marker;

    map.on('click', function (e) {
        var lat = e.latlng.lat.toFixed(6);
        var lng = e.latlng.lng.toFixed(6);

        document.getElementById("lat").value = lat;
        document.getElementById("lng").value = lng;

        if (marker) {
            marker.setLatLng([lat, lng]);
        } else {
            marker = L.marker([lat, lng]).addTo(map);
        }
    });

    function searchLocation() {
        var query = document.getElementById("searchBox").value.trim();
        if (!query) {
            alert("Vui lòng nhập địa điểm cần tìm!");
            return;
        }

        var url = "https://nominatim.openstreetmap.org/search?format=json&q=" + encodeURIComponent(query);

        fetch(url)
            .then(response => response.json())
            .then(data => {
                if (data.length > 0) {
                    var place = data[0];
                    var lat = parseFloat(place.lat);
                    var lon = parseFloat(place.lon);

                    map.setView([lat, lon], 15);

                    if (marker) {
                        marker.setLatLng([lat, lon]);
                    } else {
                        marker = L.marker([lat, lon]).addTo(map);
                    }

                    document.getElementById("lat").value = lat.toFixed(6);
                    document.getElementById("lng").value = lon.toFixed(6);
                } else {
                    alert("Không tìm thấy địa điểm phù hợp!");
                }
            })
            .catch(error => {
                console.error("Lỗi tìm kiếm địa điểm:", error);
                alert("Có lỗi xảy ra khi tìm kiếm địa điểm.");
            });
    }

    document.getElementById("searchBtn").addEventListener("click", searchLocation);
</script>

<%
    String latStr = request.getParameter("lat");
    String lngStr = request.getParameter("lng");

    if (latStr != null && lngStr != null && !latStr.isEmpty() && !lngStr.isEmpty()) {
        try {
            double lat = Double.parseDouble(latStr);
            double lng = Double.parseDouble(lngStr);

            // Sử dụng class com.example.dbconnect để lấy connection
            com.example.dbconnect db = new com.example.dbconnect();
            try (Connection conn = db.getConnection()) {
                // Giả sử bạn lưu tọa độ cho id=1, bạn thay đổi theo ý bạn
                int idToUpdate = 1;

                String sql = "UPDATE activities SET latitude = ?, longitude = ? WHERE id = ?";
                try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                    stmt.setDouble(1, lat);
                    stmt.setDouble(2, lng);
                    stmt.setInt(3, idToUpdate);

                    int rowsUpdated = stmt.executeUpdate();

                    if (rowsUpdated > 0) {
                        out.println("<p style='color:green;'>Đã lưu tọa độ: (" + lat + ", " + lng + ") cho id = " + idToUpdate + "</p>");
                    } else {
                        out.println("<p style='color:red;'>Không có dòng nào được cập nhật!</p>");
                    }
                }
            }
        } catch (Exception e) {
            out.println("<p style='color:red;'>Lỗi khi lưu: " + e.getMessage() + "</p>");
        }
    }
%>

</body>
</html>
