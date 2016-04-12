<%-- 
    Document   : OrderList
    Created on : Mar 25, 2016, 2:00:23 PM
    Author     : Lin Jianxiong
--%>

<%@page import="order.*"%>
<%@page import="java.util.ArrayList,user.*,hotel.*,java.sql.Date,order.*,org.joda.time.Days,org.joda.time.LocalDate,org.joda.time.DateTime;"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Previous Order</title>
    </head>
    <body>
        <jsp:include page="nav.jsp"></jsp:include>
        <%! private String getOrderStatus(Order o){
                switch (o.getStatus()){
                    case 1: return "Not paid";
                    case 2: return "Paid and confirmed";
                    case 3: return "Checked-in";
                    case 4: return "Checked-out and finished";
                    case 5: return "Modifying";
                    case 6: return "Cancelled";
                    default: return "Unknown";
                } 
            } 
            private String validateDate(Date CIDate, Date CODate){
                String e="";
                if (CIDate.after(CODate)){
                    e="Check-in date cannot be after check-out date!";
                }
                else if (CIDate.before(DateTime.now().withTime(0, 0, 0, 0).toDate())){
                    e="Check-in date cannot be before today!";
                }
                else if (CODate.after(DateTime.now().withTime(0, 0, 0, 0).plusDays(90).toDate())){
                    e="Check-out date cannot exceed 90 days later!";
                }
                return e;
            }
        %>
        <%  
            if (request.getParameter("pay")!=null){
                session.setAttribute("orderToPay", Order.getOrderByOrderID(Integer.parseInt(request.getParameter("pay"))));
                response.sendRedirect("Payment");
                return;
            }
            else if (request.getParameter("cancel")!=null){
                
                %>
                <script>
                if(confirm("Do you really want to cancel this order?")){
                <% Order.updateStatus(Integer.parseInt(request.getParameter("cancel")), OrderStatus.ABORTED); %>
                }
                </script>
            <%
            }
            else if (request.getParameter("modify")!=null){
                Order o=Order.getOrderByOrderID(Integer.parseInt(request.getParameter("modify")));%>
               
                    <p>Hotel Name: <%=Hotel.getHotelByID(o.getHotelID()).getHotelName()%></p>
                    <br>
                    <p>Hotel Room: <%=HotelRoom.getHotelRoom(o.getHotelID(), o.getRoomType()).getRoomName()%></p>
                    <form method="POST" action="showHotelRoom.jsp?currentHotel=<%=o.getHotelID()%>">
                        <% session.setAttribute("orderToModify",o);%>
                        <input type="submit" name="Change Hotel Room">
                    </form>
                    <br>
                    <p>Check-in Date: <%=o.getCIDate().toString()%></p>
                    <p>Check-date Date: <%=o.getCODate().toString()%></p>
                    <form method="POST" action="<%=request.getRequestURI()%>?changeDate=true">
                        <% session.setAttribute("orderToModify",o);%>
                        <input type="submit" name="Change Check In/Check Out Date">
                    </form>
                    <br>
                    <p>Number of rooms: <%=o.getNumOfRoom()%></p>
                    <form method="POST" action="<%=request.getRequestURI()%>?changNumOfRoom=true">
                        <% session.setAttribute("orderToModify",o);%>
                        <input type="submit" name="Change Number of Rooms">
                    </form>
                    
            <%    
                    return;
            }
            else if (request.getParameter("changeDate")!=null){ 
                Order o=(Order)session.getAttribute("orderToModify");
            %>
                <form method="GET" action=""
                    <label>From:</label><input type="date" name="ciDate" value="<%=o.getCIDate()%>"> <br>
                    <label>To:</label><input type="date" name="coDate" value="<%=o.getCODate()%>"> <br>
                    <input type="submit">
                </form>
                <%
                return;
            }
            else if (request.getParameter("ciDate")!=null || request.getParameter("coDate")!=null){
                Order o=(Order)session.getAttribute("orderToModify");
                String e="";
                Date CIDate,CODate;
                if (request.getParameter("ciDate")==null || request.getParameter("coDate")==null){
                    e="Check-in date and check-out date cannot be empty!";
                }
                else {
                    try {
                        CIDate= java.sql.Date.valueOf(request.getParameter("ciDate"));
                        CODate = java.sql.Date.valueOf(request.getParameter("coDate"));
                        if (!validateDate(CIDate,CODate).isEmpty()){
                            e=validateDate(CIDate,CODate);
                        }
                        else {
                            o.setCIDate(CIDate);
                            o.setCODate(CODate);
                            if (o.updateOrder(o)){ %>
                            <span> Updated Successfully!</span>
                            <%
                            }
                            else { %>
                            <p> Updated failed...there are no more available room in your selection.</p>
                            <p> Rest assured. Your previous order is not canceled. </p>
                            <%
                            }
                        }
                        
                    }
                    catch (Exception e1){
                        e="Check-in date/check-out date not valid!";
                    }
                }
                if (!e.isEmpty()) { %>
                <span> <%=e%> </span>
                <form method="GET" action=""
                    <label>From:</label><input type="date" name="ciDate" value="<%=o.getCIDate()%>"> <br>
                    <label>To:</label><input type="date" name="coDate" value="<%=o.getCODate()%>"> <br>
                    <input type="submit">
                </form>
            <%
                }
                return;
            }
            else if (request.getParameter("changNumOfRoom")!=null){ 
                Order o=(Order)session.getAttribute("orderToModify");
            %>
                <form method="GET" action="">
                    <label>Number of rooms to book</label>
                    <input type="number" name="changeNumOfRoomTo" value="<%=o.getNumOfRoom()%>" min="1" max="99">
                    <br>
                    <input type="submit">
                </form>
                <%
                return;
            }
            else if (request.getParameter("changeNumOfRoomTo")!=null){
                Order o=(Order)session.getAttribute("orderToModify");
                o.setNumOfRoom(Integer.parseInt(request.getParameter("changeNumOfRoomTo")));
                if (o.updateOrder(o)){ %>
                    <span> Updated Successfully!</span>
                <%
                }
                else { %>
                    <p> Updated failed...there are no more available room in your selection.</p>
                    <p> Rest assured. Your previous order is not canceled. </p>
                <%
                }
                return;
            }
            if (session.getAttribute("userID") == null && request.getParameter("orderID")==null) { %>               
                    <form method="POST" action="">
                        <legend>Check My Order As a Guest</legend>
                        <label>Your Order ID:</label>
                        <input type="text" name="orderID">
                        <br>
                        <label>Your Pin:</label>
                        <input type="text" name="pin">
                        <br>
                        <input type="submit">
                    </form>
            <% 
            } else {
                int userID=0;
                if (session.getAttribute("userID")!=null) { 
                    userID = (Integer) session.getAttribute("userID");
                }
                else if (request.getParameter("orderID")!=null){
                    String username =request.getParameter("orderID");
                    if (!User.usernameExist(username)){ %>
                        <span>Cannot find order with this ID!</span>
                    <%
                        return;
                    }
                    else {
                        if (!User.getUserByUsername(username).getPassword().equals(PasswordHash.hash(request.getParameter("pin")))) { %>
                            <span>Pin does not match! Please check.</span>
                        <%
                            return;
                        }
                    }
                    userID=User.getUserByUsername(username).getUserID();
                }
                ArrayList<Order> orderList = Order.getAllOrdersByUserID(userID);
                    if (orderList.size() == 0) {
                %>
                        <p> You don&#39;t have any order. </p>
                <%
                    } else {
                        for (int i = 0; i < orderList.size(); ++i) {
                            Order o = orderList.get(i);
                %>
                            <div>
                            <p> ------------------------------------</p>
                            <p> Order ID: <%= o.getOrderID() %> </p>
                            <p> Order status: <%=getOrderStatus(o)%> </p>
                            <p> Hotel name: <%= Hotel.getHotelByID(o.getHotelID()).getHotelName() %> </p>
                            <p> Room type: <%= HotelRoom.getHotelRoom(o.getHotelID(), o.getRoomType()).getRoomName() %> </p>
                            <p> Number of rooms: <%= o.getNumOfRoom() %> </p>
                            <p> Check-in date: <%= o.getCIDate() %> </p>
                            <p> Check-out date: <%= o.getCODate() %> </p>
                            <% if (o.getStatus()==1){ %>
                                    <form method="POST" action="">
                                        <button type="submit" name="pay" value="<%=o.getOrderID()%>">Pay now</button>
                                    </form>
                            <% }
                                if (o.getStatus()==1) { %>
                                    <form method="POST" action="">
                                        <button type="submit" name="modify" value="<%=o.getOrderID()%>">Modify order</button>
                                    </form>
                            <%  }
                                if (o.getStatus()==1 || o.getStatus()==2 || o.getStatus()==5) { %>
                                    <form method="POST" action="">
                                        <button type="submit" name="cancel" value="<%=o.getOrderID()%>">Cancel</button>
                                    </form>
                            </div>
                            <%  }
                            
                        }
                    }
            }
                %>
    </body>
</html>
