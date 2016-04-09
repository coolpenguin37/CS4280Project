<%-- 
    Document   : confirm
    Created on : Apr 9, 2016, 7:59:17 PM
    Author     : yduan7
--%>

<%@page import="java.util.ArrayList,hotel.*,user.*,java.sql.Date,order.*"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Confirmation</title>
    </head>
    <body>
        <% if (request.getHeader("referer").indexOf("Payment")==-1){%>
        <h1>Sorry...The confirmation failed.You did not come from the payment page.</h1> 
        <% 
        }
        else{
            Order o=(Order)session.getAttribute("orderToPay");
            if (o==null || o.getOrderID()==0){ %>
            <h1>Sorry...The confirmation failed. We cannot find your order information.</h1>
            <%
            }
            else { %>
            <h1>The payment is successful! Your order (Order ID: <%=o.getOrderID()%>)is confirmed.</h1>
            <%  
                Order.updateStatus(o.getOrderID(),OrderStatus.ONGOING);
                session.removeAttribute("orderToPay");
            } 
        }%>
    </body>
</html>
