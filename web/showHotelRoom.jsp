<%--
    Document   : showHotelRoom
    Created on : Mar 25, 2016, 11:54:19 PM
    Author     : yanlind
--%>
<%@page import="java.util.ArrayList,hotel.*,user.*,order.*,comment.*,java.sql.Date,org.joda.time.DateTime,org.joda.time.Days,org.joda.time.LocalDate,java.security.SecureRandom,java.util.*,org.quartz.*,org.quartz.impl.StdSchedulerFactory, java.util.*,org.quartz.ee.servlet.QuartzInitializerServlet"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="static org.quartz.JobBuilder.newJob"%>
<%@ page import="static org.quartz.TriggerBuilder.newTrigger"%>
<%@ page import="static org.quartz.SimpleScheduleBuilder.simpleSchedule"%>
<%@ page import="static org.quartz.DateBuilder.nextGivenSecondDate"%>
<!DOCTYPE html>
<html>
<head>
    <link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css">
        <link rel =" stylesheet" href =" css/all.css">
        <link rel =" stylesheet" href ="css/nav.css">
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.0/jquery.min.js"></script>
        
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Show Hotel Room</title>
    <script>
     
    function modifyOrderStatus(command,orderID){
        var values={}
        values[command]=orderID
        $.ajax({
            type:"POST",
            url:"ManageHotelServlet",
            data:values,
            dataType:"text",
            
            success:function(data){
                $(".col-md-12").append("<div class='alert alert-success fade in'><a href='#' class='close' data-dismiss='alert' aria-label='close'>&times;</a><strong>Success!</strong> The order has been updated successfully. </div>")
            },
            
            error: function(xhr,ajaxOptions,thrownError){
                alert(xhr.status+"\n"+thrownError);
            }
        })
     }
    </script>
</head>
<body>
    
    <div id = "title_bar_home">
        <h1 id = "title" >Hypnos</h1>
        <p id = "intro" >Your One Stop Solution for High Quality Rest During Your Trip</p>
        <jsp:include page="nav.jsp"></jsp:include>
    </div>
    <div >
    <h2 style="text-align:center; ">Hotel Room</h2>
    </div>
   
    <%  if (session.getAttribute("name") != null) { %>
        <p class = "info">Hello <%=session.getAttribute("name")%> </p>
    <%  }
        
        boolean hasLoggedIn=false;
        int random_password=0;
        int randomNum=0;
        
        //Case 1 & 2: A room is already chosen for booking/updating
        if (request.getParameter("bookroom") != null && !(request.getParameter("bookroom").isEmpty())) {
            HashMap<String,Order> orderMap=(HashMap<String,Order>) session.getAttribute("orderMap");
            String key=request.getParameter("bookroom");
            Order o=orderMap.get(key);
            HotelRoom room = HotelRoom.getHotelRoom(o.getHotelID(), o.getRoomType());
            int orderID;
            //Case 1: Updating
            if (session.getAttribute("orderToModify") != null) {
                Order a = (Order) session.getAttribute("orderToModify");
                if (Order.updateOrderRoomType(a, room.getRoomType()) > 0){ %>
                    <div class="prompt">
                    <p> Your room type has been successfully modified! </p>
                    <form method="POST" action="Payment">
                        <button type="submit">Pay now</button>
                    </form>
                    <a href="index.jsp">Go back to main page</a>
                    
    <%              return;
                }
                else { %>
                    <span> Failed to order the room...</span>
                    <a href="index.jsp">Go back to main page</a>
                    </div>
                <%  return;
                }
            }
            //Case 2: Booking
            else {
                int numRooms=Integer.valueOf((String)session.getAttribute("numRooms"));
                int userID;
                //Case 2.1: User has not logged in (Guest)
                if (session.getAttribute("userID") == null){
                    hasLoggedIn = false;
                    SecureRandom rn = new SecureRandom();
                    int maximum = 999999999;
                    int minimum = 100000000;
                    int n = maximum - minimum + 1;
                    randomNum =  minimum + Math.abs(rn.nextInt() % n);
                    while (User.usernameExist(Integer.toString(randomNum))){
                        randomNum = minimum + Math.abs(rn.nextInt() % n);
                    }
                    random_password = minimum + Math.abs(rn.nextInt() % n);
                    User u = new User(Integer.toString(randomNum),PasswordHash.hash(Integer.toString(random_password)),Integer.toString(randomNum));
                    u.insertToDatabase();
                    u = User.getUserByUsername(Integer.toString(randomNum));
                    userID = u.getUserID();
                    // request.setAttribute("userID",userID);
                } 
                //Case 2.2: Logged in user
                else {
                    userID=(Integer)session.getAttribute("userID");
                    hasLoggedIn=true;
                }
                o.setUserID(userID);
                o.setStatus(OrderStatus.PROCESSING);
                
                orderID = o.insertToDatabase();
                //successfully submit order
                if (orderID > 0) {
                    o.setOrderID(orderID);
                    session.setAttribute("orderToPay",o);
//                    SchedulerFactory sf=null;
//                    try{
//                        sf = new StdSchedulerFactory();
//                    }
//                    catch (Exception e){
//                        Exception f=e;
//                    }
//                    java.util.Date startTime = nextGivenSecondDate(null, 10);
//                    Scheduler sched = sf.getScheduler();
//                    JobDetail job1 = newJob(CancelOrder.class).withIdentity("job1", "group1").build();
//                    SimpleTrigger trigger1 = (SimpleTrigger) newTrigger().withIdentity("trigger1", "group1").startAt(startTime).build();
//                    job1.getJobDataMap().put("orderID", orderID);
//                    java.util.Date scheduleTime1 = sched.scheduleJob(job1, trigger1);
//                    sched.start();
    %>
                    <div class="prompt">
                    <span> Your order has been submitted successfully! </span>
                    <% if (!hasLoggedIn) { %>
                        <p>You are not logged in. Please remember your temporary User ID and Pin below in order to manage your order later.</p>
                        <p>Your Temporary User ID is: <%=randomNum%> </p>
                        <p>Your Pin is: <%=random_password%></p>
                    <%} %>
                    <form method="POST" action="Payment">
                        <button type="submit">Pay now</button>
                    </form>
                    <a href="index.jsp">Go back to main page</a>
    <% 
                } 
                //order failed submission
                else { 
    %>
                    <span> Failed to order the room...</span>
                    <a href="index.jsp">Go back to main page</a>
                    </div>
    <% 
                }
            }
        }
        //Case 3 & 4: Just came from index.jsp for looking up rooms/Just came from manage order for modifying rooms
        else if (request.getParameter("currentHotel") != null || session.getAttribute("orderToModify") != null) {
            Hotel currentHotel = null;
            Order a = null;
            //Case 3: get currentHotel form parameter
            if (request.getParameter("currentHotel") != null) {
                currentHotel = Hotel.getHotelByID(Integer.parseInt(request.getParameter("currentHotel")));
            }
            //Case 4: get currentHotel from session
            else { 
                a = (Order) session.getAttribute("orderToModify");
                currentHotel = Hotel.getHotelByID(a.getHotelID());
            }
            
            int hotelID = currentHotel.getHotelID();
            session.setAttribute("hotelID",hotelID);
    %>
            <div class="prompt" style="margin-top: 10px; height: 40px;">
                <span> Score: </span>
                <span> <%= Comment.getScoreByHotelName(currentHotel.getHotelName()) %> </span>
            </div>
            
    <%
            ArrayList<Comment> commentList = Comment.getCommentByHotelName(currentHotel.getHotelName());
            if (commentList!=null) {
                for (int i = 0; i < commentList.size(); ++i) {
                    Comment tmp = commentList.get(i);
                    Order o = Order.getOrderByOrderID(tmp.getOrderID());
                    int currentUserID;
                    if (session.getAttribute("userID") != null) {
                        currentUserID = (Integer) session.getAttribute("userID");
                    } else {
                        currentUserID = -1;
                    } %>
                    <fieldset class="fieldset">    
                        <legend> Comment</legend>
                        <div <%="id=comment"+tmp.getCommentID()%> class ="conmment">
                            <p> Comment: <%=tmp.getContent()%> </p>
                            <p> Score: <%=tmp.getScore()%> </p>
                            <p> Date: <%=tmp.getDate()%> </p>

                    <%  if (currentUserID == o.getUserID()) { %>
                            <a href="#" onclick='return delcomment(<%=tmp.getCommentID()%>)'>Delete</a>
                    <%  } %>
                        <br>
                        </div>
                    </fieldset>
                
            <%  }
            } 
            else { %>
            <fieldset>
                <p> No comment </p>
            </fieldset>
                    
                
            <%
            }
            
            //TODO: a method for hotel instance that with a ciDate and coDate, return a list of rooms that are avilable during that time (both dates included).
            ArrayList<HotelRoom> rooms = HotelRoom.getAllRoomsByHotelID(hotelID);
            Date CIDate = (session.getAttribute("ciDate")==null)?a.getCIDate():(Date)session.getAttribute("ciDate");
            Date CODate = (session.getAttribute("coDate")==null)?a.getCODate():(Date)session.getAttribute("coDate");
            int numDays = Days.daysBetween(new LocalDate(CIDate), new LocalDate(CODate)).getDays();
            HashMap<String, Order> orderMap=new HashMap<String, Order>();
            for (int i = 0; i < rooms.size(); ++i) {
                HotelRoom room = rooms.get(i);
                if (room.getIsRecommended()==0){continue;}
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
                
                int numRooms=(session.getAttribute("numRooms")==null)?a.getNumOfRoom():Integer.valueOf((String)session.getAttribute("numRooms"));
                Order o=new Order(room.getHotelID(), room.getRoomType(), numRooms, CIDate, CODate);
                o.setPrice(realRate*numRooms*numDays);
                orderMap.put(hotelID+"_"+room.getRoomType(), o);
                int remained = Order.getRemainedRoom(o); %>
                <div class="recommended">
                    <div class="recommendedTag">Recommended!</div>
                    <div class="image">
                        <img src="image/13-2.jpg" class = "img">
                    </div>
                    <div class="text">
                    <h3><%= room.getRoomName()%></h3> 
                    <span class="">
    <%              if (realRate != room.getStandardRate()) {           %>
                    <h4> Standard Rate: $ 
                        <span style="text-decoration:line-through;"><%= room.getStandardRate() %></span>
                    </h4>
                    <h4> You only need to pay: $ 
                        <span style="color: red;"><%= realRate %> for each room</span>
                    </h4>
    <%              } else {        %>
                    <h4> Standard Rate: $ 
                        <span> <%= room.getStandardRate() %> </span>
                    </h4>
    <%              }               %>
                    <h4> Total: $
                        <span style="color: red; font-weight: bold;"><%=realRate*numRooms*numDays%></span> 
                        in total 
                    </h4>
                    <div> 
                        <span>Size: </span> <span> <%= room.getRoomSize() %> Square Feet.</span>
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
                        <form method="POST" <%="action='"+((session.getAttribute("orderToModify")==null)?"":"manageOrder.jsp")+"'"%>>
                            <button type="submit"  name="bookroom" value="<%=hotelID+"_"+room.getRoomType()%>"><%=(session.getAttribute("orderToModify")==null)?"Book!":"Modify!"%></button>
                        </form>
                    <% } %>
                    </div>
                </div>
                
        <% }
            for (int i = 0; i < rooms.size(); ++i) {
                HotelRoom room = rooms.get(i);
                if (room.getIsRecommended()==1){continue;}
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
                
                int numRooms=(session.getAttribute("numRooms")==null)?a.getNumOfRoom():Integer.valueOf((String)session.getAttribute("numRooms"));
                Order o=new Order(room.getHotelID(), room.getRoomType(), numRooms, CIDate, CODate);
                o.setPrice(realRate*numRooms*numDays);
                orderMap.put(hotelID+"_"+room.getRoomType(), o);
                int remained = Order.getRemainedRoom(o); %>
                <div class="recommended">
                    <div class="image">
                        <img src="image/13-2.jpg" class = "img">
                    </div>
                    <div class="text">
                    <h3><%= room.getRoomName()%></h3>        
    <%              if (realRate != room.getStandardRate()) {           %>
                    <h4> Standard Rate: $ 
                        <span style="text-decoration:line-through;"><%= room.getStandardRate() %></span>
                    </h4>
                    <h4> You only need to pay: $ 
                        <span style="color: red;"><%= realRate %> for each room</span>
                    </h4>
    <%              } else {        %>
                    <h4> Standard Rate: $ 
                        <span> <%= room.getStandardRate() %> </span>
                    </h4>
    <%              }               %>
                    <h4> Total: $
                        <span style="color: red; font-weight: bold;"><%=realRate*numRooms*numDays%></span> 
                        in total 
                    </h4>
                    <div> 
                        <span>Size: </span> <span> <%= room.getRoomSize() %> Square Feet.</span>
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
                        <form method="POST" <%="action='"+((session.getAttribute("orderToModify")==null)?"":"manageOrder.jsp")+"'"%>>
                            <button type="submit" name="bookroom" value="<%=hotelID+"_"+room.getRoomType()%>"><%=(session.getAttribute("orderToModify")==null)?"Book!":"Modify!"%></button>
                        </form>
                    <% } %>
                    </div>
                </div>
                
        <% }
            session.setAttribute("orderMap",orderMap);
        } %> 
        <div class ="footer">
                <p>All the web pages are only for assignment usages for Course CS4280 in City University of Hong Kong</p>
            </div>
<script>
    function delcomment(id){
         $("#comment"+id).remove()
         $.ajax({
            type:"POST",
            url:"CommentServlet",
            data:{"deleteComment":id},
            dataType:"text",
            
            success:function(data){
                alert("Comment delete successfully!")
                window.reload;
            },
            
            error: function(xhr,ajaxOptions,thrownError){
                alert(xhr.status+"\n"+thrownError);
            }
        })
        return;
    }
</script>
</body>

</html>
