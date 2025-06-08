<%-- 
    Document   : manage_categories
    Created on : 2 thg 6, 2025, 20:26:55
    Author     : leduyduong
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.yourname.volunteer.adminma.Category" %>
<%@ include file="/hadmin.jsp" %>
<!DOCTYPE html>
<html>
<head><title>Manage Categories</title></head>
<body>
<h2>Activity Categories</h2>

<!-- Form thêm danh mục -->
<form method="post" action="CategoryServlet">
    Add Category: <input type="text" name="category">
    <input type="hidden" name="action" value="add">
    <input type="submit" value="Add">
</form>

<!-- Bảng hiển thị danh mục -->
<table border="1">
<tr><th>Category</th><th>Actions</th></tr>

<%
    List<Category> categories = (List<Category>) request.getAttribute("categories");
    if (categories != null) {
        for (Category cat : categories) {
%>
<tr>
    <td><%= cat.getName() %></td>
    <td>
        <a href="CategoryServlet?action=edit&id=<%= cat.getId() %>">Edit</a> |
        <a href="CategoryServlet?action=delete&id=<%= cat.getId() %>">Delete</a>
    </td>
</tr>
<%
        }
    }
%>

</table>
</body>
</html>
