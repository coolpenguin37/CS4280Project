
<%-- 
    Document   : index
    Created on : Mar 19, 2016, 2:37:28 PM
    Author     : siruzhang2,yduan7
--%>

<%@page import="java.util.ArrayList,hotel.*,user.*,java.sql.Date,order.*,org.joda.time.Days,org.joda.time.LocalDate,org.joda.time.DateTime,javax.servlet.ServletContext"%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>

<html>
<head>
    <link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css">
    <link rel ="stylesheet" href =" css/all.css">
    <link rel ="stylesheet" href ="css/nav.css">
    <title>Hypnos</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <script>
        function getDateString(today){
            var year=today.getFullYear();
            var month=today.getMonth()+1;
            if (today.getMonth()+1<10){
                month="0"+(today.getMonth()+1);
            }
            var day=today.getDate();
            if (today.getDate()<10){
                day="0"+today.getDate();
            }
            return year+"-"+month+"-"+day;
        }    
        function init(){
                var today=new Date();
                var three_days_later=new Date(today.getTime());
                three_days_later.setDate(three_days_later.getDate()+3);
                document.querySelector('input[name="ciDate"]').value=getDateString(today);
                document.querySelector('input[name="coDate"]').value=getDateString(three_days_later);
                document.querySelector('#numRooms').value=1;
            //check if there is a message
            <% ServletContext sc = request.getSession().getServletContext();
               if (session.getAttribute("username")==null) { %>return;<%}
               else if (sc.getAttribute((String)session.getAttribute("username"))!=null){ 
                   String msg=(String)sc.getAttribute((String)session.getAttribute("username"));
                   sc.removeAttribute((String)session.getAttribute("username"));
            %>
                   alert("<%=msg%>");
                   return;
            <%
                }
            %>
        }
        window.onload=init;
    </script>
</head>
<body>
    <header>
        <div id = "title_bar_home">
            <h1 id = "title" >Hypnos</h1>
            <p id = "intro" >Your One Stop Solution for High Quality Rest During Your Trip</p>
            <jsp:include page="nav.jsp"></jsp:include>
        </div>
    </header>
    <div>
        <% String e="";%>
        <% if (session.getAttribute("name") != null) { %>
            <p class = "info" >Hello <%=session.getAttribute("name")%></p>
        <% } %>
        <% if (request.getParameter("search") != null) {
            ArrayList<Integer> hotelIDList=new ArrayList<Integer>();
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
                Date CIDate,CODate;
                try{
                    CIDate= java.sql.Date.valueOf(request.getParameter("ciDate"));
                    CODate = java.sql.Date.valueOf(request.getParameter("coDate"));
                
                DateTime CIDateDT=new DateTime(CIDate);
                if (CIDate.after(CODate)){
                    e="Check-in date cannot be after check-out date!";
                }
                else if (CIDate.before(DateTime.now().withTime(0, 0, 0, 0).toDate())){
                    e="Check-in date cannot be before today!";
                }
                else if (!CODate.after(CIDateDT.withTime(0,0,0,0).toDate())){
                    e="Check-out date must be at least 1 day after check-in date!";
                }
                else if (CODate.after(DateTime.now().withTime(0, 0, 0, 0).plusDays(180).toDate())){
                    e="Check-out date cannot exceed 180 days later!";
                }
                else {
                    session.setAttribute("ciDate",CIDate);
                    session.setAttribute("coDate",CODate);
                    session.setAttribute("numRooms", request.getParameter("numRooms"));
                    String location= request.getParameter("location");
                    String[] keywords;
                    //match either space or comma or semicolon
                    if (location.indexOf(" ") == -1 || location.indexOf(",") == -1 || location.indexOf(";") == -1){
                        keywords = location.split(" |\\,|;");
                    }
                    else {
                        keywords = new String[] {location};
                    }
                    for (String keyword: keywords){
                        if (keyword.equals(" ") || keyword.equals(",") || keyword.equals(";")) {continue;}
                        ArrayList<Hotel> hotels=Hotel.searchHotel(keyword);
                        for (Hotel h: hotels){
                            hotelIDList.remove(Integer.valueOf(h.getHotelID()));
                            hotelIDList.add(Integer.valueOf(h.getHotelID()));
                        }
                    }
                    if (location.indexOf(",") == -1 || location.indexOf(";") == -1){
                        keywords = location.split("\\,|;");
                    }
                    else {
                        keywords = new String[] {location};
                    }
                    for (String keyword: keywords){
                        if (keyword.equals(" ") || keyword.equals(",") || keyword.equals(";")) {continue;}
                        ArrayList<Hotel> hotels=Hotel.searchHotel(keyword);
                        for (Hotel h: hotels){
                            hotelIDList.remove(Integer.valueOf(h.getHotelID()));
                            hotelIDList.add(Integer.valueOf(h.getHotelID()));
                        }
                    }
                    if (hotelIDList==null||hotelIDList.size()==0) { %>
                        <div class="recommended">
                            <p>Sorry...no hotel matching your criteria can be found!</p>
                        </div>
                    <%
                    }
                    for (int i = 0; i < hotelIDList.size(); ++i) { 
                        Hotel h = Hotel.getHotelByID(hotelIDList.get(i).intValue());
                        if (h.getIsRecommended()==0){continue;}
            %>
                        <div class='recommended'>
                            <div class ="image">
                                <img src="image/11.jpg" class = "img">
                                <div class = "price">
                                <h3> Price: $<%=Order.getLowestRate(h.getHotelID(),CIDate,CODate)*MemberBenefits.getMemberBenefitsByHotelID(h.getHotelID()).getDiscountByUserType((session.getAttribute("type")==null)?0:(Integer)session.getAttribute("type"))/100%></h3>
                                </div>
                            </div>
                            <div class = "text">
                                <h3> <%= h.getHotelName() %> </h3>
                                <h4> <%= h.getAddress()%> </h4>
                                <p> <%=h.getIntro()%></p>
                                <div> 
                                    <span> Ratings: </span>
                                    <% for (int p=1;p<=h.getStarRating();p++){ %>
                                        <span class='glyphicon glyphicon-star' style='color:red;'></span>
                                    <% } %>
                                    <br><br>
                                </div>
                                <form method="GET" action="showHotelRoom.jsp">
                                <button  id ="check_room" type="submit" name="currentHotel" value="<%=h.getHotelID()%>"> Check Room </button>
                                </form>
                            </div>
                            
                        </div>
                    <% } %>
                    
                    <%
                    for (int i = 0; i < hotelIDList.size(); ++i) { 
                        Hotel h = Hotel.getHotelByID(hotelIDList.get(i).intValue());
                        if (h.getIsRecommended()==1){continue;}
            %>
                        <div class='recommended'>
                            <div class="image">
                                <img src="image/10.jpg" class = "img">
                                <div class = "price">
                                <h3> Price: $<%=Order.getLowestRate(h.getHotelID(),CIDate,CODate)*MemberBenefits.getMemberBenefitsByHotelID(h.getHotelID()).getDiscountByUserType((session.getAttribute("type")==null)?0:(Integer)session.getAttribute("type"))/100%></h3>
                                </div>
                            </div>
                            <div class ="text">
                                <h3> <%= h.getHotelName() %> </h3>
                                <h4> <%= h.getAddress()%> </h4>
                                <p> <%=h.getIntro()%></p>
                                
                                <div> 
                                    <span> Ratings: </span>
                                    <% for (int p=1;p<=h.getStarRating();p++){ %>
                                        <span class='glyphicon glyphicon-star' style='color:red;'></span>
                                    <% } %>
                                    <br><br>
                                </div>
                                <form method="GET" action="showHotelRoom.jsp">
                                <button type="submit" id ="check_room" name="currentHotel" value="<%=h.getHotelID()%>"> Check Room</button>
                                </form>
                            </div>
                        </div>
                    <% } %>
                    
                    
                    
                    
                    
                <% } %>
            <% }
                catch (Exception ex){
                    e="Check-in/Check-out date is invalid!";
                }
            }
        } %>
    
        
        
        <fieldset class = "fieldset">
            <legend>Search The Hotel</legend>
            <form method="GET" action="" class = "content"> 
            <ul>
                <li>
                <label for="location">Where are you going?</label>
                <input id="location" type="text" name="location" placeholder="Destination, Hotel"> 
                </li>
                <li>
                <label style="width:400px">When do you plan to travel?</label>
                </li>
                <li>
                    <label>From:</label><input type="date" name="ciDate" value="">
                </li>
                <li>
                    <label>To:</label><input type="date" name="coDate" value=""> 
                </li>
                <li>
                <label for="numRooms">How many rooms do you want to book?</label>
                <input id="numRooms" type="number" name="numRooms" min="1" max="99">
                </li>  
            </ul>
            <p class = "submit" ><input type="submit" name="search" value="Search"></p>
            <% if (e!=null && !e.isEmpty()){ %>
                <span class = "error"><%=e%></span>
            <% } %>
            </form>
        </fieldset>
        
                
   
        
    </div>
            <div class ="footer">
                <p>All the web pages are only for assignment usages for Course CS4280 in City University of Hong Kong</p>
            </div>
    </body>
</html>

