<%-- 
    Document   : manageManager
    Created on : Apr 4, 2016, 8:26:38 PM
    Author     : yanlind
--%>

<%@page import="java.lang.String, user.*,hotel.*,manager.*,java.util.ArrayList"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Hypnos</title>
    </head>
    <body>
        <nav class = "menu">
            <ul>
                <li><a href="index.jsp">Home</a></li>
                <li><a href="manageManager.jsp">Manage Manager</a></li>
                <li><a href="logout.jsp">Log Out</a>
            </ul>
        </nav>
        <form method="GET" action="ManageManagerServlet">
            <label for="idOrName">Search by UserID or Username:
            <input type="text" id="idOrName" name="idOrName">
            <input type="submit">
        </form>
        
    </body>
</html>
