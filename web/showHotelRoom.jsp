<%--
    Document   : showHotelRoom
    Created on : Mar 25, 2016, 11:54:19 PM
    Author     : yanlind
--%>
<%@page import="java.util.ArrayList,hotel.*,user.*,order.*,java.sql.Date,java.sql.Date,org.joda.time.DateTime,org.joda.time.Days,org.joda.time.LocalDate,java.security.SecureRandom;"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
     <link rel =" stylesheet" href =" css/all.css">
    <link rel =" stylesheet" href ="css/nav.css">
    <div id = "title_bar_home">
         
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Show Hotel Room</title>
        <h1 id = "title" >Hypnos</h1>
        <p id = "intro" >Your One Stop Solution for High Quality Rest During Your Trip</p>
        <jsp:include page="nav.jsp"></jsp:include>
    </div>
</head>
<body>
    
    <fieldset>
        <legend>Hotel Room</legend>
   
    <% if (session.getAttribute("name") != null) { %>
        <p class = "info">Hello <%=session.getAttribute("name")%> </p>
    <% } %>       
    <% 
        if (request.getParameter("currentHotel")!=null) {
            Hotel currentHotel = Hotel.getHotelByID(Integer.parseInt(request.getParameter("currentHotel")));
            int hotelID = currentHotel.getHotelID();
            session.setAttribute("hotelID",hotelID);
            
            //TODO: a method for hotel instance that with a ciDate and coDate, return a list of rooms that are avilable during that time (both dates included).
            ArrayList<HotelRoom> rooms = HotelRoom.getAllRoomsByHotelID(hotelID);
            session.setAttribute("rooms",rooms);
            Date CIDate = (Date)session.getAttribute("ciDate");
            Date CODate = (Date)session.getAttribute("coDate");
            int numDays = Days.daysBetween(new LocalDate(CIDate), new LocalDate(CODate)).getDays();
                for (int i = 0; i < rooms.size(); ++i) {
                HotelRoom room = rooms.get(i);
                //TODO: class method required to map (hotelID, roomType, username) --> rate
                //The reason we need to pass username is that for registered user, the rate should be lower
                //We do it in server side to allow personalized discount/special discount
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
            
            <div>
                <h3> <%= room.getRoomName() %> </h3>
                <h4> Standard Rate: $ 
                    <span style="text-decoration:line-through;"><%= room.getStandardRate() %></span>
                </h4>
                <h4> You only need to pay: $ 
                    <span style="color: red;"><%= realRate %> for each room</span>
                </h4>
                <h4> Total: $
                    <span style="color: red; font-weight: bold;"><%=realRate*numRooms*numDays%></span> 
                    in total 
                </h4>
                <div> 
                    <span>Size: </span> 
                    <span> <%= room.getRoomSize() %> Square Feet.</span>
                </div>
                <% if (remained > 30) { %>
                    <div> <span>Plenty rooms available.</span></div>
                <% } else if (remained <= 30 && remained>10) { %>
                    <div> <span>Limited rooms available!</span></div>
                <% } else if (remained<=0){ %>
                    <div> <span>Sold out...</span></div>
                <% } else { %>
                    <div> <span>Only <%= remained %> Room(s) available now! Act Fast!</span></div>
                <% } %>
                <% if (remained>0){ %>
                    <form method="POST" action="">
                        <button type="submit" name="bookroom" value="<%=i%>">Book!</button>
                    </form>
                <% } %>
            </div>
        <% } %>
    <%  } %>
    <%  boolean hasLoggedIn=false;
        int random_password=0;
        int randomNum=0;
        if (request.getParameter("bookroom")!=null && !(request.getParameter("bookroom").isEmpty())){
            ArrayList<HotelRoom> rooms=(ArrayList<HotelRoom>)session.getAttribute("rooms");
            HotelRoom room=rooms.get(Integer.parseInt(request.getParameter("bookroom")));
            int orderID;
            if (session.getAttribute("orderToModify")!=null){
                Order o=(Order)session.getAttribute("orderToModify");
                o.setRoomType(room.getRoomType());
                if (o.updateOrder(o)){ %>
                    <p> Your room type has been successfully modified! </p>
                    <form method="POST" action="Payment">
                        <button type="submit">Pay now</button>
                    </form>
                    <a href="index.jsp">Go back to main page</a>
                    
                <%
                    return;
                }
                else { %>
                    <span> Failed to order the room...</span>
                    <a href="index.jsp">Go back to main page</a>
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
    </fieldset>
</body>
</html>
