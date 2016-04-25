<%-- 
    Document   : OrderList
    Created on : Mar 25, 2016, 2:00:23 PM
    Author     : Lin Jianxiong, siri
--%>

<%@page import="order.*"%>
<%@page import="java.util.ArrayList,user.*,hotel.*,java.sql.Date,order.*,org.joda.time.Days,org.joda.time.LocalDate,org.joda.time.DateTime,java.text.SimpleDateFormat,java.util.*;"%>
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
        <link rel =" stylesheet" href =" css/all.css">
        <link rel =" stylesheet" href ="css/nav.css">
        <!-- Latest compiled and minified CSS -->
        <link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css">
        <!-- jQuery library -->
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.0/jquery.min.js"></script>
        <!-- Latest compiled JavaScript -->
        <script src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js"></script>
        <script>
            function processInput(o){
                
                var orderID=$(o).prev("input").val();
                var values={}
                values["comment"]=$("#comment"+orderID).val();
                values["rating"]=$(".glyphicon-star").length
                values["orderID"]=orderID
                $.ajax({
                    type:"POST",
                    url:"CommentServlet",
                    data:values,
                    dataType:"text",

                    success:function(data){
                        
                        if (data==null || data==""){
                            alert("Comment submit successfully!")
                            $("button[data-target='#"+orderID+"']").click();
                            $("button[data-target='#"+orderID+"']").css("background","grey");
                            $("button[data-target='#"+orderID+"']").css("border","grey");
                            $("button[data-target='#"+orderID+"']").prop("disabled","true");
                        }
                        else {
                            alert(data)
                        }
                    },

                    error: function(xhr,ajaxOptions,thrownError){
                        alert(xhr.status+"\n"+thrownError);
                    }
                })
            }
            function init(){
                    $(".rating-star").hover(function(){
                            if ($(this).hasClass("glyphicon-star-empty")){
                                $(this).removeClass("glyphicon-star-empty");
                                $(this).addClass("glyphicon-star");
                                $(this).addClass("fake-star");
                                $(this).css("color","yellow");
                            }
                            var previousSiblings=$(this).prevAll(".glyphicon-star-empty")
                            
                            var nextSiblings=$(this).nextAll(".glyphicon-star")
                            $(previousSiblings).css("color","yellow");
                            $.each($(previousSiblings),function(){
                                if ($(this).hasClass("glyphicon-star-empty")){
                                    $(this).removeClass("glyphicon-star-empty")
                                    $(this).addClass("glyphicon-star");
                                    $(this).addClass("fake-star");
                                }
                            })
                            $(nextSiblings).css("color","");
                            $.each($(nextSiblings),function(){
                                if ($(this).hasClass("glyphicon-star")){
                                    $(this).addClass("fake-empty");
                                    $(this).addClass("glyphicon-star-empty")
                                    $(this).removeClass("glyphicon-star")
                                }
                            })
                        },
                        function(){
                            $(".fake-star").css("color","");
                            $(".fake-star").addClass("glyphicon-star-empty")
                            $(".fake-star").removeClass("glyphicon-star")
                            $(".fake-star").removeClass("fake-star")
                            $(".fake-empty").css("color","yellow");
                            $(".fake-empty").addClass("glyphicon-star")
                            $(".fake-empty").removeClass("glyphicon-star-empty")
                            $(".fake-empty").removeClass("fake-empty")
                        })
                
                    $(".rating-star").click(function(){
                        if($(".fake-star").length!=0 || $(".fake-empty").length!=0){
                            $(".fake-star").removeClass("fake-star")
                            $(".fake-empty").removeClass("fake-empty")
                        }
                        else{
                            $(".glyphicon-star").addClass("glyphicon-star-empty");
                            $(".glyphicon-star").css("color","");
                            $(".glyphicon-star").removeClass("glyphicon-star");
                        }
                    })
                  
            }
            window.onload=init;
        </script>
    </head>
    <body class = "body">
        <div id = "title_bar_home">
        <h1 id = "title" >Hypnos</h1>
        <p id = "intro" >Your One Stop Solution for High Quality Rest During Your Trip</p>
        <jsp:include page="nav.jsp"></jsp:include>
        </div>
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
        <fieldset class = "fieldset">
            <legend>Modify Order</legend>
            <p id = "order_ID">Hotel Name: <%=Hotel.getHotelByID(o.getHotelID()).getHotelName()%></p>
            <br>
            <p>Hotel Room: <%=HotelRoom.getHotelRoom(o.getHotelID(), o.getRoomType()).getRoomName()%></p>
            <form method="POST" action="showHotelRoom.jsp?changeRoom=true">
                <% session.setAttribute("orderToModify",o);%>
                <input type="submit" name="Change Hotel Room" value = "Change Room">
            </form>
            <br>
            <p>Check-in Date: <%=o.getCIDate().toString()%></p>
            <p>Check-date Date: <%=o.getCODate().toString()%></p>
            <form method="POST" action="<%=request.getRequestURI()%>?changeDate=true">
                <% session.setAttribute("orderToModify",o);%>
                <input type="submit" name="Change Check In/Check Out Date" value = "Change Date">
            </form>
            
            <br>
            <p>Number of rooms: <%=o.getNumOfRoom()%></p>
            <form method="POST" action="<%=request.getRequestURI()%>?changNumOfRoom=true">
                <% session.setAttribute("orderToModify",o);%>
                <input type="submit" name="Change Number of Rooms" value = "Change Room Number">
                <br>
            </form>
        </fieldset>        
        <%    
                return;
            }
            else if (request.getParameter("changeType")!=null){
                RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/showHotelRoom.jsp");
                dispatcher.forward(request,response);
            }
            else if (request.getParameter("bookroom")!=null){
                Order a=(Order)session.getAttribute("orderToModify");
                Order b=new Order(a);    
                int newRoomType=((HashMap<String,Order>)session.getAttribute("orderMap")).get(request.getParameter("bookroom")).getRoomType();
                b.setRoomType(newRoomType);
                int c=Order.tryUpdateOrder(a, b);
                int newOrderID=-1;
                if (c!=0){
                    newOrderID=Order.doUpdateOrder(a, b, c, 1);
                }
                if (newOrderID!=-1){ %>
                    <div class = "info">Success! The new order ID is: <%=newOrderID%> 
                            
                    </div>
                <%}
                else{ %>
                <div class ="info"> Update order failed... 
                            
                        </div>
            <%  }   
            } 
            else if (request.getParameter("changeDate")!=null){ 
                Order o=(Order)session.getAttribute("orderToModify");
        %>
            <fieldset class ="fieldset">
                <legend>Change Date</legend>
                <form method="GET" action="" class = "content">
                    <label>From:</label><input type="date" name="ciDate" <%="value="+o.getCIDate()%>> <br>
                    <label>To:</label><input type="date" name="coDate" <%="value="+o.getCODate()%>> <br>
                    <input type="submit" value = "Update">
                </form>
            </fieldset>
        <%
                return;
            }
            else if (request.getParameter("ciDate") != null || request.getParameter("coDate") != null || request.getParameter("confirmChangeDate") != null){
                if (request.getParameter("confirmChangeDate")!=null) {
                    Order a=(Order)session.getAttribute("a");
                    Order b=(Order)session.getAttribute("b");
                    int c=(Integer)session.getAttribute("c");
                    session.removeAttribute("a");
                    session.removeAttribute("b");
                    session.removeAttribute("c");
                    int newOrderID=Order.doUpdateOrder(a,b,c,(request.getParameter("confirmChangeDate").equals("Confirm"))?1:0);
                    if (newOrderID!=-1){ %>
                        <div class = "info">Success! The new order ID is: <%=newOrderID%> 
                            
                        </div>
        <%
                    }
                    else { %>
                        <div class ="info"> Update order failed... 
                            
                        </div>
        <%
                    }
                } else {
                    Order a = (Order) session.getAttribute("orderToModify");
                    String e = "";
                    Date CIDate, CODate;
                    if (request.getParameter("ciDate") == null || request.getParameter("coDate") == null) {
                        e = "Check-in date and check-out date cannot be empty!";
                    } else {
                        try {
                            CIDate = java.sql.Date.valueOf(request.getParameter("ciDate"));
                            CODate = java.sql.Date.valueOf(request.getParameter("coDate"));
                            int numDays = Days.daysBetween(new LocalDate(CIDate), new LocalDate(CODate)).getDays();
                            if (!validateDate(CIDate,CODate).isEmpty()) {
                                e = validateDate(CIDate,CODate);
                            } else {
                                Order b = new Order(a);
                                if (a == b) {
                                    out.print("<p> APTX </p>");
                                }
                                b.setCIDate(CIDate);
                                b.setCODate(CODate);
                                int mergedOrderID = Order.tryUpdateOrder(a, b);
                                if (mergedOrderID == 0) { %>
                                    
                                <div class = "prompt"> There is no room available for your chosen check-in/check-out date<br>
                                        <button value ="<%=request.getHeader("referer")+((request.getQueryString()==null)?"":"?"+request.getQueryString())%>" >Back</button>
                                    
                                    </div>
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
                                    b.setPrice(realPrice);
                                    session.setAttribute("a",a);
                                    session.setAttribute("b",b);
                                    session.setAttribute("c",mergedOrderID); %>
                                    <div class = "prompt"> There is room available! Now the total price is: $ <%=realPrice%>. Do you confirm the modification?
                                    <form method="POST" action="">
                                    <input type="submit" name="confirmChangeDate" value="Confirm">
                                    <input type="submit" name="confirmChangeDate" value="Cancel">
                                    </div>
                                    </form>
                                <%    
                                }
                            }
                        
                        } catch (Exception e1){
                            e="Check-in date/check-out date not valid!";
                        }
                    }
                    if (!e.isEmpty()) { 
        %>
                        <fieldset class ="fieldset">
                            <legend>Change Date</legend>
                            <span class = "info"> <%=e%> </span>
                            <form method="GET" action="" class = "content">
                            <label>From:</label><input type="date" name="ciDate" value="<%=getDateString(a.getCIDate())%>"> <br>
                            <label>To:</label><input type="date" name="coDate" value="<%=getDateString(a.getCODate())%>"> <br>
                            <input type="submit">
                        </form>
                        </fieldset>
        <%
                    }
                    return;
                }
            }               
                
            else if (request.getParameter("changNumOfRoom") != null){ 
                Order o = (Order) session.getAttribute("orderToModify");
        %>
                <fieldset class =" fieldset">
                    <legend>Change Number of Rooms</legend>
                    <form method="GET" action="" class = "content">
                        <label>Number of rooms to book</label>
                        <input type="number" name="changeNumOfRoomTo" value=<%=o.getNumOfRoom()%> min="1" max="99">
                        <br>
                        <input type="submit">
                </form>
                </fieldset>
                    
        <%
                return;
            }
            else if (request.getParameter("confirmChangeNum")!=null){
                Order a=(Order)session.getAttribute("a");
                Order b=(Order)session.getAttribute("b");
                int c=(Integer)session.getAttribute("c");
                session.removeAttribute("a");
                session.removeAttribute("b");
                session.removeAttribute("c");
                int newOrderID=Order.doUpdateOrder(a,b,c,(request.getParameter("confirmChangeNum").equals("Confirm"))?1:0);
                if (newOrderID!=-1){ %>
                <div class = "info">    
                    <p>Success! The new order ID is: <%=newOrderID%> </p>
                    
                </div>
                    
    <%
                }
                else { 
    %>
                    <div class ="info"> 
                        
                    </div>
    <%
                }
            }
            else if (request.getParameter("changeNumOfRoomTo") != null){
                Order a = (Order) session.getAttribute("orderToModify");
                Order b = new Order(a);
                b.setNumOfRoom(Integer.parseInt(request.getParameter("changeNumOfRoomTo")));
                int mergedOrderID = Order.tryUpdateOrder(a, b);
                if (mergedOrderID == 0) { 
        %>
                <div class ="prompt">    
                    <p> There is no room available for you to change number of rooms </p>
                    <button onclick='window.location.href="<%=request.getHeader("referer")+((request.getQueryString()==null)?"":"?"+request.getQueryString())%>"' >Back</button>
                </div>
        <%
                } else {
                    HotelRoom room = HotelRoom.getHotelRoom(a.getHotelID(), a.getRoomType());
                    User u = User.getUserByUserID(a.getUserID());
                    MemberBenefits mb = MemberBenefits.getMemberBenefitsByHotelID(a.getHotelID());
                    int numDays = Days.daysBetween(new LocalDate(a.getCIDate()), new LocalDate(a.getCODate())).getDays();
                    int discount;
                    if (u == null || u.getUserType() < 1){
                        discount = 100;
                    } else {
                        discount = mb.getDiscountByUserType(u.getUserType());
                    }
                    int standardRate=room.getStandardRate();
                    int realRate = (int) Math.floor(standardRate * (discount / 100.0));
                    int realPrice = realRate * b.getNumOfRoom() * numDays;
                    b.setPrice(realPrice);
                    session.setAttribute("a", a);
                    session.setAttribute("b", b);
                    session.setAttribute("c", mergedOrderID); 
        %>
                    <div class = "prompt"> 
                        There is room available! Now the total price is: $ <%=realPrice%>. Do you confirm the modification?
                    <form method="POST" action="">
                        <input type="submit" name="confirmChangeNum" value="Confirm">
                        <input type="submit" name="confirmChangeNum" value="Cancel">
                    </form>
                    </div>
        <%
                }
                return;
            }
            if (session.getAttribute("userID") == null && request.getParameter("orderID")==null) { %>               
            <fieldset class = "fieldset">   
                <legend>Check My Order As a Guest</legend>
                <form method="POST" action="" class = "content">
                    <ul>
                        <li>
                        <label>Your Order ID:</label>
                        <input type="text" name="orderID">
                        </li>
                        <li>
                        <label>Your Pin:</label>
                        <input type="text" name="pin">
                        </li>
                    </ul>              
                    </form>
                    <input type="submit" value = "Check Order">
            </fieldset>
            <% 
            } else {
                int userID=0;
                if (session.getAttribute("userID")!=null) { 
                    userID = (Integer) session.getAttribute("userID");
                }
                else if (request.getParameter("orderID")!=null){
                    String username =request.getParameter("orderID");
                    if (!User.usernameExist(username)){ %>
                        <div class = "prompt">Cannot find order with this ID!
                            <button value ="<%=request.getHeader("referer")+((request.getQueryString()==null)?"":"?"+request.getQueryString())%>" >Back</button>
                            
                        </div>
                    <%
                        return;
                    }
                    else {
                        if (!User.getUserByUsername(username).getPassword().equals(PasswordHash.hash(request.getParameter("pin")))) { %>
                            <div class = "prompt">Pin does not match! Please check.
                                <button value ="<%=request.getHeader("referer")+((request.getQueryString()==null)?"":"?"+request.getQueryString())%>" >Back</button>
                            </div>
                        <%
                            return;
                        }
                    }
                    userID=User.getUserByUsername(username).getUserID();
                }
                ArrayList<Order> orderList = Order.getAllOrdersByUserID(userID);
                boolean allCanceled=true;
                for (int i = 0; i < orderList.size(); ++i) {
                    Order o = orderList.get(i);
                    if (o.getStatus()!=6){allCanceled=false;break;}

                }
                    if (orderList.size() == 0 || allCanceled) {
                %>
                        <p class = "prompt"> You don&#39;t have any order. </p>
                <%
                    } else {
                        for (int i = 0; i < orderList.size(); ++i) {
                            Order o = orderList.get(i);
                            if (o.getStatus()==6){continue;}
                %>
                            <div class = "for_content">
                            <p id = "order_ID"> Order ID: <%= o.getOrderID() %> </p>
                            <p> Order status: <%=getOrderStatus(o)%> </p>
                            <p> Hotel name: <%= Hotel.getHotelByID(o.getHotelID()).getHotelName() %> </p>
                            <p> Room type: <%= HotelRoom.getHotelRoom(o.getHotelID(), o.getRoomType()).getRoomName() %> </p>
                            <p> Number of rooms: <%= o.getNumOfRoom() %> </p>
                            <p> Check-in date: <%= o.getCIDate() %> </p>
                            <p> Check-out date: <%= o.getCODate() %> </p>
                            <p> Total price: <%=o.getPrice() %> </p>
                            <form method="GET" action="OrderPDFServlet" target="_blank">
                                <input type="hidden" name="OrderID" value="<%= o.getOrderID() %>" />
                                <button type="submit" name="download"> Download </button>
                            </form>
                            
        <%                  
                            if (o.getStatus()==1){ 
        %>
                                <form method="GET" action="">
                                    <button type="submit" name="pay" value="<%=o.getOrderID()%>">Pay now</button>
                                </form>
        <%                  }
                            if (o.getStatus()==1 || o.getStatus()==5) { 
        %>
                                <form method="GET" action="">
                                    <button type="submit" name="modify" value="<%=o.getOrderID()%>">Modify order</button>
                                </form>
        <%                  }
                            if (o.getStatus()==1 || o.getStatus()==2 || o.getStatus()==5) { 
        %>
                                <form method="GET" action="">
                                    <button type="submit" name="cancel" value="<%=o.getOrderID()%>">Cancel</button>
                                </form>
                            
        <%
                            }
                            if (o.getStatus() == 4) {
        %>
                                <button data-toggle="collapse" data-target="#<%=o.getOrderID()%>">Comment</button>
                                <div class="collapse" <%="id="+o.getOrderID()%> >
                                    <div class="form-group">
                                        <label for="comment">Comment:</label>
                                        <textarea class="form-control" rows="5" <%="id=comment"+o.getOrderID()%>></textarea>
                                        <label>Rating:</label><span class="glyphicon glyphicon-star-empty rating-star"></span><span class="rating-star glyphicon glyphicon-star-empty"></span><span class="rating-star glyphicon glyphicon-star-empty"></span><span class="rating-star glyphicon glyphicon-star-empty"></span><span class="rating-star glyphicon glyphicon-star-empty"></span>
                                        <input type="hidden" name="orderID" value="<%=o.getOrderID()%>">
                                        <button onclick="processInput(this)">Submit Comment</button>
                                    </div>  
                                </div>
        <%
                            }
        %>
                            </div>
        <%
                        }
                    }
            }
        %>
    </body>
</html>
