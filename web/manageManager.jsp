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
        <link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css">
        <link rel =" stylesheet" href =" css/all.css">
        <link rel =" stylesheet" href ="css/nav.css">
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Hypnos</title>
    </head>
    <body>
        <div class="title_bar_home">
            <h1 id = "title" >Hypnos</h1>
            <p id = "intro" >Your One Stop Solution for High Quality Rest During Your Trip</p>
            <nav class = "menu">
            <ul>
                <li><a href="index.jsp">Home</a></li>
                <li><a href="manageManager.jsp">Manage Manager</a></li>
                <li><a href="logout.jsp">Log Out</a>
            </ul>
            </nav>
            
        </div>
            <form method="GET" action="ManageManagerServlet" class="content">
                <ul>
                    <li>
                    <label for="idOrName">Search by UserID or Username:
                    <input type="text" id="idOrName" name="idOrName">
                    </li>
                </ul>
                <input type="submit" value ="Search">
            </form>   
    </body>
</html>
