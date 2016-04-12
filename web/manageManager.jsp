<%-- 
    Document   : manageManager
    Created on : Apr 4, 2016, 8:26:38 PM
    Author     : yanlind
--%>

<%@page import="java.lang.String, user.*,hotel.*,manager.*,java.util.ArrayList"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Hypnos</title>
    </head>
    <body>
        <jsp:include page="nav.jsp"></jsp:include>
        <h1>Manage Managers</h1>
        <% 
            if (request.getParameter("action")!=null){
                if (request.getParameter("action")=="update"){
                    
                }
                else if (request.getParameter("action")=="change"){
                    User u=User.getUserByUserID(Integer.parseInt(request.getParameter("uid")));
                    if (u==null){ %>
                        <p><span>Change manager failed. Cannot find the manager with its ID.</span></p>
                    <%
                    }
                    else {
                        u.setUserType(((u.getUserType()==10)?100:10));
                        if (User.updateProfile(u)){ %>
                        <p><span>Succeeded!</span></p>
                        <%
                        }
                    }
                }
                else if (request.getParameter("action")=="delete"){
                    User u=User.getUserByUserID(Integer.parseInt(request.getParameter("uid")));
                    if (u==null){ %>
                        <p><span>Delete manager failed. Cannot find the manager with its ID.</span></p>
                    <%
                    }
                    else {
                        u.setUserType(1);
                        if (User.updateProfile(u) && Manager.removeManager(Integer.parseInt(request.getParameter("uid")),Integer.parseInt(request.getParameter("hid")))){ %>
                        <p><span>Succeeded! The manager now becomes a common user.</span></p>
                        <%
                        }
                        else { %>
                        <p><span>Delete manager failed.</span></p>
                        <%
                        }
                    }
                }
                else if (request.getParameter("action")=="Search"){
                    if (request.getParameter("userID")==null && request.getParameter("userName")==null){ %>
                       <p><span>Please supply either User ID or User Name.</span></p> 
                    <%
                    }
                    else   {
                        User u1=User.getUserByUserID(Integer.parseInt(request.getParameter("userID")));
                        if (u1!=null) { %>
                            <div>
                                <h2>Search Results:</h2>
                                <p>User Name:<%=u1.getUsername()%></p>
                                <p>User ID:<%=u1.getUserID()%></p>
                                <p>Name:<%=u1.getName()%></p>
                                <button onclick="<%="window.location.href="+request.getRequestURI() + "?action=chooseHotel&uid=" + Integer.toString(u1.getUserID())%>">Choose Hotel to Manage</button>
                            </div>
                    <%
                        }
                        User u2=User.getUserByUsername(request.getParameter("username"));
                        if (u2!=null) { %>
                            <div>
                                <h2>------------</h2>
                                <p>User Name:<%=u2.getUsername()%></p>
                                <p>User ID:<%=u2.getUserID()%></p>
                                <p>Name:<%=u2.getName()%></p>
                                <button onclick="<%="window.location.href="+request.getRequestURI() + "?action=chooseHotel&uid=" + Integer.toString(u2.getUserID())%>">Choose Hotel to Manage</button>
                            </div> 
                        <%
                        }
                    }
                }
                else if (request.getParameter("action")=="chooseHotel"){ %>
                <div>
                    <h3> You are choosing hotel for setting user <%=User.getUserByUserID(Integer.parseInt(request.getParameter("uid"))).getName()%> as a manager. </h3>
                    <form action="<%=request.getRequestURI() + "?action=chooseHotel&uid=" + request.getParameter("uid")%>" method="POST">
                    <p><span>Search by hotelID: <input type="text" name="hotelID" value="<%=(request.getParameter("hotelID")==null)?"":request.getParameter("hotelID")%>"></span></p>
                    <p><span>OR</span></p>
                    <p><span>Search by keywords (location, name, etc...) <input type="text" name="location" value="<%=(request.getParameter("location")==null)?"":request.getParameter("location")%>"></span></p>
                    <input type="submit" name="chooseHotel">
                    </form>
                </div>
                    <%
                    ArrayList<Hotel> hotelList=new ArrayList<Hotel>();
                    if (request.getParameter("hotelID")==null &&  request.getParameter("location")==null){ %>
                        <span> Please supply either hotel ID or hotel keywords!</span>
                    <%
                    }
                    else {
                        if (request.getParameter("hotelID")!=null){
                        Hotel h=Hotel.getHotelByID(Integer.parseInt(request.getParameter("hotelID")));
                        hotelList.add(h);
                        }
                        if (request.getParameter("location")!=null){
                            String location=request.getParameter("location");
                            String[] keywords;
                            if (location.indexOf(" ")!=-1 || location.indexOf(",")!=-1 || location.indexOf(";")!=-1){
                               keywords=location.split(" |\\,|;");
                            }
                            else {
                                keywords=new String[] {location};
                            }
                            for (String keyword: keywords){
                                hotelList.removeAll(Hotel.searchHotel(keyword));
                                hotelList.addAll(Hotel.searchHotel(keyword));
                            }
                        }%>      
                        <form action="<%=request.getRequestURI() + "?action=add&uid=" + request.getParameter("uid")%>" method="POST">    
                        <%
                        for (int i = 0; i < hotelList.size(); ++i) { 
                            Hotel h = hotelList.get(i); %>
                            <div <%= (h.getIsRecommended() == 1)?"class='recommended'":"" %> >
                                <h3> <%= h.getHotelName() %> </h3>
                                <h4> <%= h.getAddress()%> </h4>
                                <div> 
                                    <span> Ratings: </span>
                                    <span> <%= h.getStarRating() %> Star</span>
                                </div>
                                <img src="" alt="">
                                <input type="checkbox" name="hotelSelection" value="<%=h.getHotelID()%>">
                            </div>
                        <%
                        } %>
                        <input type="submit" name="add" value="add">
                        </form>
                <%
                    }
                }
                else if (request.getParameter("action")=="add"){
                    User u=User.getUserByUserID(Integer.parseInt(request.getParameter("uid")));
                    if (u==null){ %>
                        <p><span>Add manager failed. Cannot find the user with its ID.</span></p>
                    <%
                    }
                    else if (request.getParameter("hotelSelection")==null){ %>
                        <p><span>Please select at least one hotel!</span></p>
                    <%
                    }
                    else {
                        String e="";
                        u.setUserType(10);
                        //checkbox multiple value should be separated with comma
                        String[] hotels=request.getParameter("hotelSelection").split(" |\\,");
                        for (String h:hotels){
                            Manager m=new Manager(Integer.parseInt(request.getParameter("uid")),Integer.parseInt(h));
                            if (!m.insertToDatabase()){
                                e=e+"Cannot add user "+u.getName()+"as a manager of "+Hotel.getHotelByID(Integer.parseInt(h)).getHotelName()+"\n";
                            }
                        }
                        if (User.updateProfile(u)){ %>
                        <p><span>Succeeded! The user now becomes a manager.</span></p>
                        <%
                        }
                        else {
                            e=e+"Cannot change user type to manager";
                        }
                        if (!e.isEmpty()){ %>
                            <p><span> <%=e%> </span></p>
                        <%
                        }
                    }
                }
            }
            ArrayList<Manager> managers=Manager.getAllManagers();
            for (Manager m:managers){
                User u=User.getUserByUserID(m.getUserID()); %>
            <div>
                <p>--------------</p>
                <p><span>Name: <%=u.getUsername()%></span> </p>
                <p><span>Hotel that is managing: <%=Hotel.getHotelByID(m.getHotelID()).getHotelName()%></span></p>
                <p><span>User Type: <%=(u.getUserType()==10)?"Manager":"Chief Manager"%></span> </p> 
                <button onclick="<%="window.location.href="+request.getRequestURI() + "?action=update&uid=" + Integer.toString(u.getUserID()) + "&hid=" + Integer.toString(m.getHotelID())%>">Update access</button>
                <button onclick="<%="window.location.href="+request.getRequestURI() + "?action=change&uid=" + Integer.toString(u.getUserID()) + "&hid=" + Integer.toString(m.getHotelID())%>">Change to <%=(u.getUserID()==10)?"Normal Manager":"Chief Manager"%></button>
                <button onclick="<%="window.location.href="+request.getRequestURI() + "?action=delete&uid=" + Integer.toString(u.getUserID()) + "&hid=" + Integer.toString(m.getHotelID())%>">Delete</button>
            </div>
        <%    
            } %>
            <div>
                <h2>Add new manager</h2>
                <form action="_self" method="GET">
                    <label>Search by User ID:</label>
                    <input type="text" name="userID">
                    <br>
                    <label>Search by Username:</label>
                    <input type="text" name="username">
                    <input type="submit" name="action" value="Search">
                </form>
            </div>
        
        
    </body>
</html>
