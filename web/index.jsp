
<%-- 
    Document   : index
    Created on : Mar 19, 2016, 2:37:28 PM
    Author     : siruzhang2,yduan7
--%>

<%@page import="java.util.ArrayList,hotel.*,user.*"%>
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
        <h2>Hello <%=session.getAttribute("name")%></h2>
    <% } %>
    
    <nav>
        <% if (session.getAttribute("name") == null) { %>
            <a href="newAccount.jsp"><div><span>Create New Account</span></div></a>
            <a href="userLogin.jsp"><div><span>Login</span></div></a>
        <% } else { %>
            <a href="index.jsp">
                <div><span>Home</span></div>
            </a>
            <% if (session.getAttribute("type") != null && (((Integer) session.getAttribute("type")) < 10)) { %>
                <a href="memberInfo.jsp">
                    <div><span>Settings</span></div>
                </a>
                <a href="logout.jsp">
                    <div><span>Log Out</span></div>
                </a>
            <% } else if (session.getAttribute("type") != null && ((Integer) session.getAttribute("type") >= 10)) { %> 
                <a href="manageHotel.jsp">
                    <div><span>Manage Hotel</span></div>
                </a>
                <a href="logout.jsp">
                    <div><span>Log Out</span></div>
                </a>
            <% } %>
        <% } %>
        <h2> <%= session.getAttribute("type") %> </h2>
        <h2> <%= ((session.getAttribute("type") != null) && (((Integer) session.getAttribute("type")) == 1)) %>  </h2>
    </nav>
    
    <% if (session.getAttribute("hotelList") != null) {
        ArrayList<Hotel> hotelList= (ArrayList<Hotel>) session.getAttribute("hotelList");
        for (int i = 0;i < hotelList.size(); ++i) { 
            Hotel h=hotelList.get(i);
    %>
            <div <%= (h.getIsRecommended()==1)?"class='recommended'":"" %> >
                <h3> <%= h.getHotelName() %> </h3>
                <h4> <%= h.getAddress()%> </h4>
                <div> 
                    <span> Ratings: </span>
                    <span> <%= h.getStarRating() %> Star</span>
                </div>
                <img src="" alt="">
                <button> Check Room Availability </button>
            </div>
        <% } %>
    <% } %>


    <form>
            <label for="location">Where are you going?</label> <br>
            <input id="location" type="text" name="location"> <br>
            <label>When do you plan to travel</label> <br>
            <label>From:</label><input type="date" name="ciDate"> <br>
            <label>To:</label><input type="date" name="coDate"> <br>
            <label for="numRooms">How many rooms do you want to book?</label> <br>
            <input id="numRooms" type="text" name="numRooms"> <br>
            <label for="hotelName">Do you have a hotel in mind already?</label> <br>
            <input id="hotelName" type="text" name="hotelName">
    </form>
    </body>
</html>

