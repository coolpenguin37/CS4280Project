
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
    <%! String e="";%>
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
                <a href="updateProfile.jsp">
                    <div><span>Settings</span></div>
                </a>
                <a href="OrderList.jsp">
                    <div><span>Previous Order</span></div>
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
    
        <% if (request.getMethod()=="POST") {
            ArrayList<Hotel> hotelList=new ArrayList<Hotel>();
            //check if parameters are null. Only hotel name can be null;
            if (request.getParameter("location") == null || request.getParameter("location").isEmpty()) {
                e="Location is not specified!";
            }
            else if (request.getParameter("ciDate") == null || request.getParameter("ciDate").isEmpty()){
                e="Check-in date is not specified!";
            }
            else if (request.getParameter("coDate") == null || request.getParameter("coDate").isEmpty()){
                e="Check-out date is not specified!";
            }
            else if (request.getParameter("numRooms") == null || request.getParameter("numRooms").isEmpty()){
                e="Number of rooms to be booked is not specified!";
            }
            else{
                session.setAttribute("ciDate",request.getParameter("ciDate"));
                session.setAttribute("coDate",request.getParameter("coDate"));
                session.setAttribute("numRooms", request.getParameter("numRooms"));
                String location= request.getParameter("location");
                String[] keywords;
                //match either space or comma or semicolon
                if (location.indexOf(" ") == -1 || location.indexOf(",") == -1 || location.indexOf(";") == -1){
                    keywords = location.split(" |\\.|;");
                }
                else {
                    keywords = new String[] {location};
                }
                for (String keyword: keywords){
                    hotelList.removeAll(Hotel.searchHotel(keyword));
                    hotelList.addAll(Hotel.searchHotel(keyword));
                }           
                for (int i = 0; i < hotelList.size(); ++i) { 
                    Hotel h = hotelList.get(i);
        %>
                    <div <%= (h.getIsRecommended() == 1)?"class='recommended'":"" %> >
                        <h3> <%= h.getHotelName() %> </h3>
                        <h4> <%= h.getAddress()%> </h4>
                        <div> 
                            <span> Ratings: </span>
                            <span> <%= h.getStarRating() %> Star</span>
                        </div>
                        <img src="" alt="">
                        <button onclick="window.location.href='showHotelRoom.jsp?currentHotel=<%=h.getHotelID()%>'"> Check Room Availability </button>
                    </div>
                <% } %>
            <% } %>
        <% } %>


    <form method="POST" action="" >
        <label for="location">Where are you going?</label> <br>
        <input id="location" type="text" name="location" value="Destination, Hotel"> <br>
        <label>When do you plan to travel</label> <br>
        <label>From:</label><input type="date" name="ciDate"> <br>
        <label>To:</label><input type="date" name="coDate"> <br>
        <label for="numRooms">How many rooms do you want to book?</label> <br>
        <input id="numRooms" type="text" name="numRooms"> <br>
        <p><input type="submit" value="Search"></p>
    </form>
        <span><%=(e!=null && !e.isEmpty())?e:""%></span>
        
    </body>
</html>

