<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
  <%@ include file="navbar.jsp" %> 
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Danh sách hoạt động</title>
</head>
<body onload="loadhistory()">
    <div class="main-content">
        <h2>Danh sách các hoạt động</h2>
        <!-- Dữ liệu sẽ được hiển thị ở đây -->
        <div id="booksContainer">Đang tải dữ liệu...</div>
    </div>

    <script>
        function loadhistory() {
            fetch('showhistory')
                .then(response => response.text())
                .then(data => {
                    document.getElementById('booksContainer').innerHTML = data;
                });
        }

       function huyNhieuHoatDong() {
        const checkboxes = document.querySelectorAll("input[name='register']:checked");

        if (checkboxes.length === 0) {
            alert("Vui lòng chọn ít nhất một hoạt động để hủy.");
            return;
        }

        const selectedIds = Array.from(checkboxes).map(cb => cb.value);

        fetch('huydangky', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded'
            },
            body: new URLSearchParams(selectedIds.map(id => ['ids[]', id]))
        })
        .then(response => {
            if (response.ok) {
                alert("Đã hủy đăng ký các hoạt động thành công!");
                loadhistory(); // reload lại bảng sau khi cập nhật
            } else {
                alert("Có lỗi xảy ra khi hủy đăng ký.");
            }
        });
    }
    </script>
</body>
</html>
