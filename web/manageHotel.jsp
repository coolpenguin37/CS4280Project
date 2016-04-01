<%-- 
    Document   : manageHotel
    Created on : Mar 27, 2016, 3:30:55 PM
    Author     : yanlind
--%>

<%@page import="java.lang.String, user.*,hotel.*,manager.*"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Hypnos--Manage Hotel</title>
    </head>
    <body>
        <%! Hotel h;%>
        <% if(session.getAttribute("username")==null) { %>
		<h1>You have not logged in yet! Click <a href="userLogin.jsp">here</a> to log in.</h1>
	<% } else {
            if (request.getParameter("isRecommended")!=null){
                  if (!(session.getAttribute("hotel_to_manage")==null)){
                      h=(Hotel)session.getAttribute("hotel_to_manage");
                      if (request.getParameter("isRecommended")=="yes"){
                         h.setIsRecommended(1);
                      }
                        else if (request.getParameter("isRecommend")=="no"){
                        h.setIsRecommended(0);
                            }
                      //TODO: updateHotel with the same hotelID
                      Hotel.updateHotel(h);
                    }
            } else { %>
        
                <h1>Hypnos-Your One Stop Solution for High Quality Rest During Your Trip</h1>
                <h2>Hello <%=session.getAttribute("name")%>. Manage your hotel:</h2>
                <% String username=(String)session.getAttribute("username"); %>
                    <% //TODO: we need a static method that takes a String (username) and return the manager
                       //with that name %>
                    Manager m=new Manager.getManagerByUserID(username);
                    <% //TODO: we need another static method that takes a String (hotelID) and return the hotel
                        //with that ID %>
                    <% h= Hotel.getHotelByID(m.getHotelID());
                       session.setAttribute("hotel_to_manage", h);
                       String hotelName=h.getHotelName();
                       String address=h.getAddress();
                       int isRecommended=h.getIsRecommended();
                       int starRating=h.getStarRating(); %>

                    <div>
                        <h3> <%=hotelName%></h3>
                        <% //TODO: a popup box with a input textbox %>
                        <button>Update</button>
                        <h4> <%=address%> </h4>
                        <% //TODO: a popup box with a input textbox %>
                        <button>Update</button>
                        <form method="get" action="_self">
                        <input type="checkbox" name="isRecommended" value="yes" <%=(isRecommended==1)?"checked":""%>>
                        <input type="checkbox" name="isRecommended" value="no" <%=(isRecommended==0)?"checked":""%>>
                        <input type="submit">
                        </form>
                        <br>
                        <span>Your hotel's current star rating is: <%=starRating%> star(s).</span>
                    </div>

                    <div>
                        <span> View the room availability at </span> <input type="date" name="room_availability_date"> <button>Check</button>
                    </div>
                    
                    <button onclick="window.location.href =/CheckOutRoom">Check Out Room</button>
 
                
                <%}%>
                
            
    </body>
</html>
