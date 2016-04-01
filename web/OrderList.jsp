<%-- 
    Document   : OrderList
    Created on : Mar 25, 2016, 2:00:23 PM
    Author     : Lin Jianxiong
--%>

<%@page import="order.*"%>
<%@page import="java.util.ArrayList"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Previous Order</title>
    </head>
    <body>
        <h1>Hello World!</h1>
        <% 
        if (session.getAttribute("userID") == null) { 
        %>
            <p> Sorry, your session has expired</p>
            <a href="newAccount.jsp"> 
                <div><span> Create New Account </span></div>
            </a>
            <a href="userLogin.jsp">
                <div><span>Login</span></div>
            </a>
        <% 
        } else { 
            int userID = (Integer) session.getAttribute("userID");
            ArrayList<Order> orderList = Order.getAllOrdersByUserID(userID);
            if (orderList.size() == 0) {
        %>
                <p> You don&#39;t have any order. </p>
        <%
            } else {
                for (int i = 0; i < orderList.size(); ++i) {
                    Order o = orderList.get(i);
        %>
                    <h4> <%= o.getHotelID() %> </h4>
                    <h4> <%= o.getRoomType() %> </h4>
                    <h4> <%= o.getCIDate() %> </h4>
                    <h4> <%= o.getCODate() %> </h4>
        <%
                }
            }
        } 
        %>
    </body>
</html>
