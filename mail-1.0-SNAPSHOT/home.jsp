<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>volunter</title>
     <link rel="stylesheet" href="css/hstyle.css">
</head>
<body>
    <div class="header">
        volunter <span style="color:white;"></span>
        <div style="float:right; font-size:16px;">
          <%
    String fullname = (String) session.getAttribute("fullname");
    if (fullname == null) {
        response.sendRedirect("login.jsp"); 
    }
%>
Welcome <%= fullname %> |<a href="logout" style="color:black;">Log out</a>
        </div>
    </div>
    <div class="container">
        <div class="sidebar">
            <a href="home.jsp">Home</a>
            <a href="showactive.jsp">Xem danh sách các hoạt động</a>
            <a href="updateinf.jsp">Sửa thông tin cá nhân</a>
            <a href="historyactive.jsp">Hoạt động của bạn</a>
            <a href="personalhistory.jsp">Lịch sử cá nhân</a>
            <a href="attend.jsp">Xem điểm danh</a>

<!--            <form action="search.jsp" method="get">
                <input type="text" name="query" />
                <button type="submit" class="search-btn">Search</button>
            </form>-->
        </div>
        <div class="main-content">
            <h1>Home</h1>
           
        </div>
    </div>
</body>
</html>
