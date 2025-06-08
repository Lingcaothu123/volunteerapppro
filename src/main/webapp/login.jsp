<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Login</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
    <h1>Volunteer</h1>
    <form action="login" method="post">
        <label>Username:</label><br>
        <input type="text" name="username" required><br>

        <label>Password:</label><br>
        <input type="password" name="password" required><br>
        <label>Check Login:</label><br>
        <select name="loginType" required>         
            <option value="login1">Admin</option>
              <option value="login2">User</option>
        </select><br><br>

        <button type="submit">Login</button>
    </form>
    
    <p style="color:red">${errorMessage}</p>
    <p>Don't have an account? <a href="register.jsp">Register here</a></p>
</body>
</html>