<%-- 
    Document   : login
    Created on : Apr 20, 2016, 6:11:16 PM
    Author     : yduan7
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <h1>Login As Chief Manager</h1>
        <form method="POST" action="j_security_check">
            <label>User Name:</label>
            <input type="text" name="j_username">
            <br>
            <label>Password:</label>
            <input type="password" name="j_password">
            <input type="submit">
        </form>
    </body>
</html>
