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
        <script>
            alert("You have logged out successfully!")
            window.location.href="index.jsp"
        </script>
    </head>
    <body>  
        <%session.invalidate();%>
        
    </body>
</html>
