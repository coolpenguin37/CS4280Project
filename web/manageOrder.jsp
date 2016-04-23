<%-- 
    Document   : OrderList
    Created on : Mar 25, 2016, 2:00:23 PM
    Author     : Lin Jianxiong
--%>

<%@page import="order.*"%>
<%@page import="java.util.ArrayList,user.*,hotel.*,java.sql.Date,order.*,org.joda.time.Days,org.joda.time.LocalDate,org.joda.time.DateTime,java.text.SimpleDateFormat;"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%! private String getDateString(Date d){
        SimpleDateFormat s=new SimpleDateFormat("YYYY/MM/DD");
        return s.format(d);
    } %>
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
            else if (request.getParameter("modify")!=null) {
                Order o=Order.getOrderByOrderID(Integer.parseInt(request.getParameter("modify")));
        %>
               
            <p>Hotel Name: <%=Hotel.getHotelByID(o.getHotelID()).getHotelName()%></p>
            <br>
            <p>Hotel Room: <%=HotelRoom.getHotelRoom(o.getHotelID(), o.getRoomType()).getRoomName()%></p>
            <form method="POST" action="showHotelRoom.jsp?changeRoom=true">
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
                <form method="GET" action="">
                    <label>From:</label><input type="date" name="ciDate" value="<%=getDateString(o.getCIDate())%>"> <br>
                    <label>To:</label><input type="date" name="coDate" value="<%=getDateString(o.getCIDate())%>"> <br>
                    <input type="submit">
                </form>
        <%
                return;
            }
            else if (request.getParameter("ciDate") != null || request.getParameter("coDate") != null || request.getParameter("confirmChangeDate") != null){
                if (request.getParameter("confirmChangeDate")!=null){
                    Order a=(Order)session.getAttribute("a");
                    Order b=(Order)session.getAttribute("b");
                    int c=(Integer)session.getAttribute("c");
                    session.removeAttribute("a");
                    session.removeAttribute("b");
                    session.removeAttribute("c");
            if (Order.doUpdateOrder(a,b,c,(request.getParameter("confirmChangeDate").equals("Confirm"))?1:0)){ %>
                    <p>Success! The new order ID is: <%=c%> </p>
                    <%
                    }
                    else { %>
                    <p> Update order failed... </p>
                <%
                }
                }
                Order a = (Order) session.getAttribute("orderToModify");
                String e = "";
                Date CIDate, CODate;
                if (request.getParameter("ciDate") == null || request.getParameter("coDate") == null) {
                    e = "Check-in date and check-out date cannot be empty!";
                }
                else {
                    try {
                        CIDate = java.sql.Date.valueOf(request.getParameter("ciDate"));
                        CODate = java.sql.Date.valueOf(request.getParameter("coDate"));
                        int numDays = Days.daysBetween(new LocalDate(CIDate), new LocalDate(CODate)).getDays();
                        if (!validateDate(CIDate,CODate).isEmpty()){
                            e = validateDate(CIDate,CODate);
                        } else {
                            Order b = new Order(a);
                            b.setCIDate(CIDate);
                            b.setCODate(CODate);
                            int mergedOrderID = Order.tryUpdateOrder(a, b);
                            if (mergedOrderID == 0) { %>
                                <span> There is no room available for your chosen check-in/check-out date</span>
                            <%
                            } else {
                                HotelRoom room = HotelRoom.getHotelRoom(a.getHotelID(), a.getRoomType());
                                User u = User.getUserByUserID(a.getUserID());
                                MemberBenefits mb = MemberBenefits.getMemberBenefitsByHotelID(a.getHotelID());
                                int discount;
                                if (u == null || u.getUserType() < 1){
                                    discount=100;
                                } else {
                                    discount = mb.getDiscountByUserType(u.getUserType());
                                }
                                int standardRate=room.getStandardRate();
                                int realRate = (int) Math.floor(standardRate * (discount / 100.0));
                                int realPrice = realRate * a.getNumOfRoom()*numDays;
                                session.setAttribute("a",a);
                                session.setAttribute("b",b);
                                session.setAttribute("c",mergedOrderID); %>
                                <span> There is room available! Now the total price is: $ <%=realPrice%>. Do you confirm the modification?</span>
                                <form method="POST" action="">
                                <input type="submit" name="confirmChangeDate" value="Confirm">
                                <input type="submit" name="confirmChangeDate" value="Cancel">
                                </form>
                            <%    
                            }
                        }
                        
                    }
                    catch (Exception e1){
                        e="Check-in date/check-out date not valid!";
                    }
                }
                if (!e.isEmpty()) { 
        %>
                    <span> <%=e%> </span>
                    <form method="GET" action="">
                        <label>From:</label><input type="date" name="ciDate" value="<%=getDateString(a.getCIDate())%>"> <br>
                        <label>To:</label><input type="date" name="coDate" value="<%=getDateString(a.getCODate())%>"> <br>
                        <input type="submit">
                    </form>
        <%
                }
                return;
            }
            else if (request.getParameter("changNumOfRoom") != null){ 
                Order o = (Order) session.getAttribute("orderToModify");
        %>
                <form method="GET" action="">
                    <label>Number of rooms to book</label>
                    <input type="number" name="changeNumOfRoomTo" value=<%=o.getNumOfRoom()%> min="1" max="99">
                    <br>
                    <input type="submit">
                </form>
        <%
                return;
            }
            else if (request.getParameter("changeNumOfRoomTo")!=null){
                //mark
                Order a = (Order) session.getAttribute("orderToModify");
                Order b = new Order(a);
                b.setNumOfRoom(Integer.parseInt(request.getParameter("changeNumOfRoomTo")));
                int mergedOrderID = Order.tryUpdateOrder(a, b);
                if (mergedOrderID == 0) { 
        %>
                    <span> There is no room available for you to change number of rooms </span>
        <%
                } else {
                    HotelRoom room = HotelRoom.getHotelRoom(a.getHotelID(), a.getRoomType());
                    User u = User.getUserByUserID(a.getUserID());
                    MemberBenefits mb = MemberBenefits.getMemberBenefitsByHotelID(a.getHotelID());
                    int numDays = Days.daysBetween(new LocalDate(a.getCIDate()), new LocalDate(CODate)).getCODate();
                    int discount;
                    if (u == null || u.getUserType() < 1){
                        discount = 100;
                    } else {
                        discount = mb.getDiscountByUserType(u.getUserType());
                    }
                    int standardRate=room.getStandardRate();
                    int realRate = (int) Math.floor(standardRate * (discount / 100.0));
                    int realPrice = realRate * b.getNumOfRoom() * numDays;
                    session.setAttribute("a",a);
                    session.setAttribute("b",b);
                    session.setAttribute("c",mergedOrderID); %>
                    <span> 
                        There is room available! Now the total price is: $ <%=realPrice%>. Do you confirm the modification?
                    </span>
                    <form method="POST" action="">
                        <input type="submit" name="confirmChangeDate" value="Confirm">
                        <input type="submit" name="confirmChangeDate" value="Cancel">
                    </form>
        <%
                }
                return;
            }
            if (session.getAttribute("userID") == null && request.getParameter("orderID")==null) { %>               
                    <form method="POST" action="">
                        <fieldset>
                        <legend>Check My Order As a Guest</legend>
                        <label>Your Order ID:</label>
                        <input type="text" name="orderID">
                        <br>
                        <label>Your Pin:</label>
                        <input type="text" name="pin">
                        <br>
                        <input type="submit">
                        </fieldset>
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
                                if (o.getStatus()==1 || o.getStatus()==5) { %>
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
