<%-- 
    Document   : logout
    Created on : Mar 23, 2016, 2:20:30 PM
    Author     : siruzhang2
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>logout Page</title>
    </head>
    <body>  
        <% session.invalidate();%>
            <p>You have logged out successfully!</p>
        <% response.sendRedirect("userLogin.jsp");%>
    </body>
</html>
