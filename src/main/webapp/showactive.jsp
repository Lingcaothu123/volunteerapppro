<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="navbar.jsp" %> 
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8" />
    <title>Danh sách hoạt động</title>
</head>
<body>
<div class="main-content">
    <h2>Danh sách các hoạt động</h2>

    <form id="searchForm" onsubmit="searchActivities(event)">
        <input type="text" id="searchInput" placeholder="Nhập từ khóa tìm kiếm..." />
        <div id="filterGroup">
            <label><input type="checkbox" value="title"> Tên hoạt động</label>
            <label><input type="checkbox" value="location"> Địa điểm</label>
            <label><input type="checkbox" value="organization"> Tổ chức</label>
        </div>
        <button type="submit">Tìm kiếm</button>
    </form>

    <div id="booksContainer">Đang tải dữ liệu...</div>
</div>

<script>
    function searchActivities(event) {
        event.preventDefault();

        const keyword = document.getElementById("searchInput").value.trim();
        const checkboxes = document.querySelectorAll("#filterGroup input[type=checkbox]");
        let selectedColumns = [];

        checkboxes.forEach(cb => {
            if (cb.checked) selectedColumns.push(cb.value);
        });

        if (selectedColumns.length === 0) {
            fetch('allbooks')
                .then(res => res.text())
                .then(data => {
                    document.getElementById("booksContainer").innerHTML = data;
                });
            return;
        }

        if (keyword === "") {
            console.log("Bạn phải nhập từ khóa để lọc khi đã chọn bộ lọc.");
            return;
        }

        const params = new URLSearchParams();
        params.append("keyword", keyword);
        selectedColumns.forEach(col => params.append("columns", col));

        fetch("allbooks?" + params.toString())
            .then(res => res.text())
            .then(data => {
                document.getElementById("booksContainer").innerHTML = data;
            });
    }

    function loadAllBooks() {
        fetch('allbooks')
            .then(res => res.text())
            .then(data => {
                document.getElementById('booksContainer').innerHTML = data;
            });
    }

    window.onload = loadAllBooks;
</script>
</body>
</html>
