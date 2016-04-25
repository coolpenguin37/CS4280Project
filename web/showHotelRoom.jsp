<%--
    Document   : showHotelRoom
    Created on : Mar 25, 2016, 11:54:19 PM
    Author     : yanlind
--%>
<%@page import="java.util.ArrayList,hotel.*,user.*,order.*,comment.*,java.sql.Date,java.sql.Date,org.joda.time.DateTime,org.joda.time.Days,org.joda.time.LocalDate,java.security.SecureRandom;"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css">
        <link rel =" stylesheet" href =" css/all.css">
        <link rel =" stylesheet" href ="css/nav.css">
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Show Hotel Room</title>
</head>
<body>
    
    <div id = "title_bar_home">
        <h1 id = "title" >Hypnos</h1>
        <p id = "intro" >Your One Stop Solution for High Quality Rest During Your Trip</p>
        <jsp:include page="nav.jsp"></jsp:include>
    </div>
    <% 
        if (session.getAttribute("name") != null) { 
    %>
            <div class = "info" >
                <h1 >Hotel Room</h1>
                <%
        }
    %>
    <% 
        if (request.getParameter("currentHotel") != null || session.getAttribute("orderToModify") != null) {
            Hotel currentHotel = null;
            Order a = null;
            if (request.getParameter("currentHotel") != null) {
                currentHotel = Hotel.getHotelByID(Integer.parseInt(request.getParameter("currentHotel")));
            } else {
                a = (Order) session.getAttribute("orderToModify");
                currentHotel = Hotel.getHotelByID(a.getHotelID());
            }
            
            int hotelID = currentHotel.getHotelID();
            session.setAttribute("hotelID",hotelID);
    %>
                <span> Score: </span>
                <span> <%= Comment.getScoreByHotelName(currentHotel.getHotelName()) %> </span>
                <%
                ArrayList<Comment> commentList = Comment.getCommentByHotelName(currentHotel.getHotelName());
            if (commentList.size() > 0) {
                for (int i = 0; i < commentList.size(); ++i) {
                    Comment tmp = commentList.get(i);
                    Order o = Order.getOrderByOrderID(tmp.getOrderID());
                    int currentUserID = Integer.parseInt((String) session.getAttribute("UserID"));
                    //ALANTODO PRINT OUT COMMENT                
                    out.print("<div id=comment"+tmp.getCommentID()+">");
                    out.print("<p>"+tmp.getContent()+" </p>");
                    out.print("<p>"+tmp.getScore()+" </p>");
                    out.print("<p>"+tmp.getDate()+" </p>");
                    if (currentUserID == o.getUserID())
                    {
                        out.println("<a onclick=delcomment(" + tmp.getCommentID() + ") >" + "Delete"+"</a>");
                    }
                    out.println("<br></br>");
                    out.println("</div>");
                }
            } else {
                out.print("<p> No Comment </p>");
                //ALANTODO: NO COMMENT YET
            }
                %>
            </div>

   
    
    
            
    <%
            
            
            //TODO: a method for hotel instance that with a ciDate and coDate, return a list of rooms that are avilable during that time (both dates included).
            ArrayList<HotelRoom> rooms = HotelRoom.getAllRoomsByHotelID(hotelID);
            session.setAttribute("rooms",rooms);
            Date CIDate = (Date)session.getAttribute("ciDate");
            Date CODate = (Date)session.getAttribute("coDate");
            int numDays = Days.daysBetween(new LocalDate(CIDate), new LocalDate(CODate)).getDays();
            for (int i = 0; i < rooms.size(); ++i) {
                HotelRoom room = rooms.get(i);
                int standardRate = room.getStandardRate();
                String username = (String) session.getAttribute("username");
                User u = User.getUserByUsername(username);
                MemberBenefits mb = MemberBenefits.getMemberBenefitsByHotelID(hotelID);
                int discount;
                //See whether is guest
                if (u==null || u.getUserType()<1){
                    discount=100;
                }
                else {
                    discount = mb.getDiscountByUserType(u.getUserType());
                }
                int realRate = (int) Math.floor(standardRate * (discount / 100.0)); 
                int numRooms=Integer.valueOf((String)session.getAttribute("numRooms"));
                Order o = new Order(hotelID, room.getRoomType(),numRooms,CIDate, CODate);
                int remained = Order.getRemainedRoom(o);
                
    %>
            
                <div class = "recommended">
                    <div class ="image">
                        <img src="image/13-2.jpg" class = "img">
                        <div class = "price">
                            <h3>
                            Price: $
                            <span style="color: red;"><%= realRate %> for each room</span>
                            </h3>
                        </div>
                    </div>
                    <div class ="text">
                    <h3> <%= room.getRoomName() %> </h3>
                    <h4> Standard Rate: $ 
                        <span style="text-decoration:line-through;"><%= room.getStandardRate() %></span>
                    </h4>
                    <h3> Total: $
                        <span style="color: red; font-weight: bold;"><%=realRate*numRooms*numDays%></span> 
                        in total 
                    </h3>
                    <div> 
                        <span>Size: </span> 
                        <span> <%= room.getRoomSize() %> Square Feet.</span>
                    </div>
                    <% if (remained > 30) { %>
                        <div> <span>Plenty rooms available.</span>
                            <br><br>
                        </div>
                    <% } else if (remained <= 30 && remained>10) { %>
                        <div> <span>Limited rooms available!</span>
                            <br><br>
                        </div>
                    <% } else if (remained<=0){ %>
                        <div> <span>Sold out...</span>
                            <br><br>
                        </div>
                    <% } else { %>
                        <div> <span>Only <%= remained %> Room(s) available now! Act Fast!</span></div>
                    <% } %>
                    <% if (remained>0){ %>
                        <form method="POST" action="">
                            <button id ="check_room" type="submit" name="bookroom" value="<%=i%>">Book!</button>
                        </form>
                    <% } %>
                    </div>
                </div>
                
        <% } %>
    <%  } %>
    <%  
        
        
        
        boolean hasLoggedIn=false;
        int random_password=0;
        int randomNum=0;
        if (request.getParameter("bookroom") != null && !(request.getParameter("bookroom").isEmpty())){
            ArrayList<HotelRoom> rooms = (ArrayList<HotelRoom>)session.getAttribute("rooms");
            HotelRoom room = rooms.get(Integer.parseInt(request.getParameter("bookroom")));
            int orderID;
            
            if (session.getAttribute("orderToModify") != null){
                Order a = (Order) session.getAttribute("orderToModify");
                if (Order.updateOrderRoomType(a, room.getRoomType()) > 0){ 
    %>
                <div class = "prompt">
                    <p> Your room type has been successfully modified! </p>
                    <form method="POST" action="Payment">
                        <button type="submit">Pay now</button>
                    </form>
                    <a href="index.jsp">Go back to main page</a>
                </div>
                    
    <%
                    return;
                }
                else { 
    %>
                    <div class ="prompt">
                    <span> Failed to order the room...</span>
                    <a href="index.jsp">Go back to main page</a>
                    </div>
                    
                <%
                    return;
                }
            }

            else {
            
            int numRooms=Integer.valueOf((String)session.getAttribute("numRooms"));
            int userID;
            if (session.getAttribute("userID")==null){
                hasLoggedIn=false;
                SecureRandom rn=new SecureRandom();
                int maximum=99999999;
                int minimum=10000000;
                int n = maximum - minimum + 1;
                randomNum =  minimum + Math.abs(rn.nextInt() % n);
                while (User.usernameExist(Integer.toString(randomNum))){
                    randomNum = minimum + Math.abs(rn.nextInt() % n);
                }
                random_password=minimum + Math.abs(rn.nextInt() % n);
                User u=new User(Integer.toString(randomNum),PasswordHash.hash(Integer.toString(random_password)),Integer.toString(randomNum));
                u.insertToDatabase();
                u=User.getUserByUsername(Integer.toString(randomNum));
                userID=u.getUserID();
//                request.setAttribute("userID",userID);
            }
            else {
                userID=(Integer)session.getAttribute("userID");
                hasLoggedIn=true;
            }
            Date ciDate= (Date)session.getAttribute("ciDate");
            Date coDate= (Date)session.getAttribute("coDate");
            Order o=new Order(OrderStatus.PROCESSING,userID,ciDate,coDate,room.getHotelID(),room.getRoomType(),numRooms);
            orderID = o.insertToDatabase();
            if (orderID > 0){ 
                o.setOrderID(orderID);
                session.setAttribute("orderToPay",o);
    %>

                <span> Your order has been submitted successfully! </span>

                <% if (!hasLoggedIn) { %>
                    <p>You are not logged in. Please remember your Order ID and Pin below in order to manage your order later.</p>
                    <span> Your Order ID is: <%=randomNum%> </span>
                    <p>Pin: <%=random_password%></p>
                <%} %>
                <form method="POST" action="Payment">
                    <button type="submit">Pay now</button>
                </form>
                <a href="index.jsp">Go back to main page</a>
            <% }
            else { %>
                <span> Failed to order the room...</span>
                <a href="index.jsp">Go back to main page</a>
            <% }  
        }
    }%>
    <div class ="footer">
                <p>All the web pages are only for assignment usages for Course CS4280 in City University of Hong Kong</p>
            </div>
</body>
</html>
