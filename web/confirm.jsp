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
        <link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css">
        <link rel =" stylesheet" href =" css/all.css">
        <link rel =" stylesheet" href ="css/nav.css">
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Confirmation</title>
    </head>
    <body>
        <header>
        <div id = "title_bar_home">
            <h1 id = "title" >Hypnos</h1>
            <p id = "intro" >Your One Stop Solution for High Quality Rest During Your Trip</p>
            <jsp:include page="nav.jsp"></jsp:include>
        </div>
        </header>
        <% if (request.getHeader("referer").indexOf("Payment")==-1){%>
        <div class = "prompt">Sorry...The confirmation failed.You did not come from the payment page.</div> 
        <% 
        }
        else{
            Order o=(Order)session.getAttribute("orderToPay");
            o.setName((request.getParameter("clientName")==null||request.getParameter("clientName").isEmpty())?
                    User.getUserByUserID(o.getUserID()).getName():request.getParameter("clientName"));
            o.setEmail((request.getParameter("clientEmail")==null||request.getParameter("clientEmail").isEmpty())?
                    User.getUserByUserID(o.getUserID()).getEmail():request.getParameter("clientEmail"));
            o.setPhone((request.getParameter("clientPhone")==null||request.getParameter("clientPhone").isEmpty())?
                    User.getUserByUserID(o.getUserID()).getEmail():request.getParameter("clientPhone"));
            if (o==null || o.getOrderID()==0){ %>
            <h1>Sorry...The confirmation failed. We cannot find your order information.</h1>
            <%
            }
            else { %>
            <div class = "prompt">The payment is successful! Your order (Order ID: <%=o.getOrderID()%>)is confirmed.</div>
            
            <%  
                //get request params, put into order (got from session), and update order
                //mark
                Order.updateStatus(o.getOrderID(),OrderStatus.ONGOING);
                session.removeAttribute("orderToPay");
            } 
        }%>
    <div class ="footer">
                <p>All the web pages are only for assignment usages for Course CS4280 in City University of Hong Kong</p>
            </div>
    </body>
</html>
