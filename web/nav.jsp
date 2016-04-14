<%-- 
    Document   : nav
    Created on : Apr 12, 2016, 4:01:57 PM
    Author     : yduan7
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="hotel.*,user.*,order.*"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title></title>
    </head>
    <body>
        <nav>
        <% if (session.getAttribute("name") == null) { %>
            <ul>
                <li><a href="index.jsp">Home</a></li>
                <li><a href="newAccount.jsp">Create New Account</a></li>
                <li><a href="userLogin.jsp">Login</a></li>
                <li><a href="manageOrder.jsp">Manage your order</a></li>
            </ul>
        <% } else { %>
            <ul>
                <li><a href="index.jsp">Home</a></li>
            <% if (session.getAttribute("type") != null && (((Integer) session.getAttribute("type")) < 10)) { %>
                <li><a href="updateProfile.jsp">Settings</a></li>
                <li><a href="manageOrder.jsp">Manage your order</a></li>
            <% } else if (session.getAttribute("type") != null && ((Integer) session.getAttribute("type") >= 10)) { %> 
                <li><a href="ManageHotelServlet">Manage Hotel</a></li>
                
            <% } %>
                <li><a href="logout.jsp">Log Out</a>
            </ul>
        <% } %>
<!--        <h2> <%= session.getAttribute("type") %> </h2>
        <h2> <%= ((session.getAttribute("type") != null) && (((Integer) session.getAttribute("type")) == 1)) %>  </h2>-->
    </nav>
    </body>
</html>
