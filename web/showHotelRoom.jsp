<%--
    Document   : showHotelRoom
    Created on : Mar 25, 2016, 11:54:19 PM
    Author     : yanlind
--%>
<%@page import="java.util.ArrayList,hotel.*,user.*,order.*,java.sql.Date"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Hypnos-Your One Stop Solution for High Quality Rest During Your Trip</title>
</head>
<body>

    <h1>Hypnos-Your One Stop Solution for High Quality Rest During Your Trip</h1>
    <% if (session.getAttribute("name") != null) { %>
        <h2>Hello <%=session.getAttribute("name")%> </h2>
    <% } %>
    
    <nav>
        <% if (session.getAttribute("name")!=null) { %>
            <a href="newAccount.jsp"><div><span>Create New Account</span></div></a>
            <a href="userLogin.jsp"><div><span>Login</span></div></a>
        <% }
        else { %>
            <a href="index.jsp">
                <div><span>Home</span></div>
            </a>
            <a href="memberInfo.jsp">
                <div><span>Settings</span></div>
            </a>
            <a href="logout.jsp">
                <div><span>Log Out</span></div>
            </a>
        <% } %>
    </nav>
    
    <% 
        if (session.getAttribute("hotel")!=null) {
            Hotel currentHotel = (Hotel) session.getAttribute("hotel");
            int hotelID = currentHotel.getHotelID();
            String ciDate = request.getParameter("ciDate");
            String coDate = request.getParameter("coDate");
            Date CIDate = java.sql.Date.valueOf(ciDate);
            Date CODate = java.sql.Date.valueOf(coDate);
            //TODO: a method for hotel instance that with a ciDate and coDate, return a list of rooms that are avilable during that time (both dates included).
            ArrayList<HotelRoom> rooms = HotelRoom.getAllRoomsByHotelID(hotelID);
            for (int i = 0; i < rooms.size(); ++i) {

                HotelRoom room = rooms.get(i);
                //TODO: class method required to map (hotelID, roomType, username) --> rate
                //The reason we need to pass username is that for registered user, the rate should be lower
                //We do it in server side to allow personalized discount/special discount
                int standardRate = room.getStandardRate();
                String username = (String) session.getAttribute("username");
                User u = User.getUserByUsername(username);
                MemberBenefits mb = MemberBenefits.getMemberBenefitsByHotelID(hotelID);
                int discount = mb.getDiscountByUserType(u.getUserType());
                int realRate = (int) Math.ceil(standardRate * (discount / 100.0));
                Order o = new Order(CIDate, CODate, hotelID, room.getRoomType());
                int remained = Order.getRemainedRoom(o);
    %>
            
            <div>
                <h3> <%= room.getRoomName() %> </h3>
                <h4> <%= room.getStandardRate() %> </h4>
                <h4> <%= realRate %> </h4>
                <h4> <%= remained %> </h4>
                <div> 
                    <span>Size: </span> 
                    <span> <%= room.getRoomSize() %> Square Feet.</span>
                </div>
                <% if (remained > 30) { %>
                    <div> <span>Plenty rooms available.</span></div>
                <% } else if (remained <= 30 && remained>10) { %>
                    <div> <span>Limited rooms available!</span></div>
                <% } else { %>
                        <div> <span>Only <%= remained %> Room(s) available now! Act Fast!</span></div>
                <% } %>
                <button>Book!</button>
            </div>
        <% } %>
    <%  } %>    
</body>
</html>
