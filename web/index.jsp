
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
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.0/jquery.min.js"></script>
    <script src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js"></script>
    <link rel ="stylesheet" href =" css/all.css">
    <link rel ="stylesheet" href ="css/nav.css">
    <link href="css/bootstrap.icon-large.min.css" rel="stylesheet">
    <title>Hypnos</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <script>
        function getSrc(s){
           var src="https://www.google.com/maps/embed/v1/place?key=AIzaSyBFkJ_-IVPI5GQKCo-hODB_G6XDzy4Sj4k&q="
           var q=s.split(" ")
           var qs=q[0];
           for (i=1;i<q.length;i++){
               qs+="+";
               qs+=q[i];
           }
           $("iframe").attr("src",src+qs);
       }
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
        
        var getMap = function(opts) {
  var src = "http://maps.googleapis.com/maps/api/staticmap?",
      params = $.extend({
        center: 'Hong Kong',
        zoom: 14,
        size: '512x512',
        maptype: 'roadmap',
        sensor: false
      }, opts),
      query = [];

  $.each(params, function(k, v) {
    query.push(k + '=' + encodeURIComponent(v));
  });

  src += query.join('&');
  return '<img src="' + src + '" />';
}
        window.onload=init;
    </script>
</head>
<body>
    <header>
        <div id = "title_bar_home_index">
            <h1 id = "title_index" >Hypnos</h1>
            <p id = "intro_index" >Your One Stop Solution for High Quality Rest During Your Trip</p>
            <div id="menu_index">
                <% if (session.getAttribute("name") == null) { %>
                <ul>
                         
                    <li><a href="userLogin.jsp">Login</a></li>
                    <li><a href="newAccount.jsp">Sign Up</a></li>
                    <li><a href="manageOrder.jsp">Manage Order</a></li> 
                    <li><a href="index.jsp">Home</a></li>
                    
                </ul>
            <% } else { %>
                <ul>
                    <li><a href="logout.jsp">Log Out</a>
                    
                <% if (session.getAttribute("type") != null && (((Integer) session.getAttribute("type")) < 10)) { %>
                    <li><a href="updateProfile.jsp">Setting</a></li>
                    <li><a href="manageOrder.jsp">Manage Order</a></li>
                <% } else if (session.getAttribute("type") != null && ((Integer) session.getAttribute("type") >= 10)) { %> 
                    <li><a href="updateProfile.jsp">Setting</a></li>
                    <li><a href="ManageHotelServlet">Manage Hotel</a></li>

                <% } %>
                    <li><a href="index.jsp">Home</a></li>
                </ul>
            </div>
        <% } %>
<!--        <h2> <%= session.getAttribute("type") %> </h2>
        <h2> <%= ((session.getAttribute("type") != null) && (((Integer) session.getAttribute("type")) == 1)) %>  </h2>-->
            <form method="GET" action="" class = "search"> 
            <ul>
                <li>
                <label for="location">Destination</label>
                <input id="location" type="text" name="location" placeholder="Destination, Hotel"> 
                </li>
                <li>
                    <label>From:</label><input type="date" name="ciDate" value="">
                </li>
                <li>
                    <label>To:</label><input type="date" name="coDate" value=""> 
                </li>
                <li>
                <label for="numRooms">Humber</label>
                <input id="numRooms" type="number" name="numRooms" min="1" max="99">
                </li>  
                <li>
                <input type="submit" name="search" value="Search">
                </li>
            </ul>
            
            </form>
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
                            <div class="recommendedTag">Recommended!</div>
                            
                            <div class ="image">
                                <img src="image/11.jpg" class = "img">
                                <div class = "price">
                                <h3> Price: $<%=Order.getLowestRate(h.getHotelID(),CIDate,CODate)*MemberBenefits.getMemberBenefitsByHotelID(h.getHotelID()).getDiscountByUserType((session.getAttribute("type")==null)?0:(Integer)session.getAttribute("type"))/100%></h3>
                                </div>
                            </div>
                            <div class = "text">
                                <h3> <%= h.getHotelName() %> </h3>
                                <h4> <%= h.getAddress()%> </h4>
                                <%
                                if (MemberBenefits.getMemberBenefitsByHotelID(h.getHotelID()).getFreeWiFi()<=((session.getAttribute("type")==null)?0:(Integer)session.getAttribute("type"))){ %>
                                    <i class="icon-large icon-wifi-alt" title="Free wifi"></i>
                                <% }
                                if (MemberBenefits.getMemberBenefitsByHotelID(h.getHotelID()).getWelcomeGift()<=((session.getAttribute("type")==null)?0:(Integer)session.getAttribute("type"))){ %>
                                    <i class="icon-large icon-gift" title="Welcome gift"></i>
                                <% }
                                if (MemberBenefits.getMemberBenefitsByHotelID(h.getHotelID()).getLateCheckout()<=((session.getAttribute("type")==null)?0:(Integer)session.getAttribute("type"))){ %>
                                    <i class="icon-large icon-clock" title="Late checkout"></i>
                                <% } 
                                if (MemberBenefits.getMemberBenefitsByHotelID(h.getHotelID()).getBreakfast()<=((session.getAttribute("type")==null)?0:(Integer)session.getAttribute("type"))){ %>
                                    <i class="icon-large icon-cutlery" title="Breakfast available"></i>
                                <% } %>
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
                                    <button type="button" onclick="getSrc('<%=h.getHotelName()%>')" data-toggle="modal" data-target="#myModal">View in Google Map</button>
                                </form>
                            </div>
                            
                        </div>
                    <% } %>
                    
                    <%
                    for (int i = 0; i < hotelIDList.size(); ++i) { 
                        Hotel h = Hotel.getHotelByID(hotelIDList.get(i).intValue());
                        if (h.getIsRecommended()==1){continue;}
                        String q=h.getHotelName().replaceAll(" ", "+");
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
                                 <%
                                if (MemberBenefits.getMemberBenefitsByHotelID(h.getHotelID()).getFreeWiFi()<=((session.getAttribute("type")==null)?0:(Integer)session.getAttribute("type"))){ %>
                                    <i class="icon-large icon-wifi-alt" title="Free wifi"></i>
                                <% }
                                if (MemberBenefits.getMemberBenefitsByHotelID(h.getHotelID()).getWelcomeGift()<=((session.getAttribute("type")==null)?0:(Integer)session.getAttribute("type"))){ %>
                                    <i class="icon-large icon-gift" title="Welcome gift"></i>
                                <% }
                                if (MemberBenefits.getMemberBenefitsByHotelID(h.getHotelID()).getLateCheckout()<=((session.getAttribute("type")==null)?0:(Integer)session.getAttribute("type"))){ %>
                                    <i class="icon-large icon-clock" title="Late checkout"></i>
                                <% }
                                if (MemberBenefits.getMemberBenefitsByHotelID(h.getHotelID()).getBreakfast()<=((session.getAttribute("type")==null)?0:(Integer)session.getAttribute("type"))){ %>
                                    <i class="icon-large icon-cutlery" title="Breakfast available"></i>
                                <% } %>
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
                                    <button type="button" onclick="getSrc('<%=h.getHotelName()%>')" data-toggle="modal" data-target="#myModal">View in Google Map</button>
                                </form>
                                    <!-- Button trigger modal -->



                            </div>
                        </div>
                    <% } %>
                    
                    <!-- Modal -->
                    <div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
  <div class="modal-dialog" role="document">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
        <h4 class="modal-title" id="myModalLabel">Google Map</h4>
      </div>
      <div class="modal-body">
          <div class="container">
        <iframe
  width="500"
  height="450"
  frameborder="0" style="border:0"
  allowfullscreen>
</iframe>
          </div>
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-primary" data-dismiss="modal">Close</button>
      </div>
    </div>
  </div>
</div>
                    
                    
                    
                <% } %>
            <% }
                catch (Exception ex){
                    e="Check-in/Check-out date is invalid!";
                }
            }
        } %>

            <% if (e!=null && !e.isEmpty()){ %>
                <span class = "prompt"><%=e%></span>
            <% } %>
 
            <div class="pic" align="center">
                <div class="pic.img">
                <img src="image/hk.jpg" class = "img_index" title="Hong Kong">
                <p class="pic_text1">Hong Kong</p>
                </div>
                <div class="pic.img">
                <img src="image/ny.jpg" class = "img_index" title="New York">
                <p class="pic_text2">New York</p>
                </div>
                <div class="pic.img">
                <img src="image/sh.jpg" class = "img_index" title="Shang Hai">
                <p class="pic_text3">Shang Hai</p>
                </div>
                
            </div>
        
                
   
        
    </div>
            <div class ="footer">
                <p>All the web pages are only for assignment usages for Course CS4280 in City University of Hong Kong</p>
            </div>
    </body>
</html>

