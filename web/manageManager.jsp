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
                        if (User.updateProfile(u)){ %>
                        <p><span>Succeeded! The manager now becomes a common user.</span></p>
                        <%
                        }
                    }
                }
                else if (request.getParameter("action")=="Search"){
                    if (request.getParameter("userID")==null && request.getParameter("userName")==null){ %>
                       <p><span>Please supply either User ID or User Name.</span></p> 
                    <%
                    }
                    else {
                        User u1=User.getUserByUserID(Integer.parseInt(request.getParameter("userID")));
                        if (u1!=null) { %>
                            <div>
                                <h2>Search Results:</h2>
                                <p>User Name:<%=u1.getUsername()%></p>
                                <p>User ID:<%=u1.getUserID()%></p>
                                <p>Name:<%=u1.getName()%></p>
                                <button onclick=<%="window.location.href="+request.getRequestURI() + "?action=add&uid=" + Integer.toString(u1.getUserID())%> >Add to manager list</button>
                            </div>
                    <%
                        }
                        User u2=User.getUserByUsername(request.getParameter("username"));
                        if (u2!=null) { %>
                            <div>
                                <h2>Search Results:</h2>
                                <p>User Name:<%=u2.getUsername()%></p>
                                <p>User ID:<%=u2.getUserID()%></p>
                                <p>Name:<%=u2.getName()%></p>
                                <button onclick=<%="window.location.href="+request.getRequestURI() + "?action=add&uid=" + Integer.toString(u2.getUserID())%> >Add to manager list</button>
                            </div> 
                        <%
                        }
                    }
                }
                else if (request.getParameter("action")=="add"){
                    User u=User.getUserByUserID(Integer.parseInt(request.getParameter("uid")));
                    if (u==null){ %>
                        <p><span>Add manager failed. Cannot find the user with its ID.</span></p>
                    <%
                    }
                    else {
                        u.setUserType(10);
                        if (User.updateProfile(u)){ %>
                        <p><span>Succeeded! The user now becomes a manager.</span></p>
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
                <p><span>User Type: <%=(u.getUserType()==10)?"Manager":"Chief Manager"%></span> </p> 
                <button onclick=<%="window.location.href="+request.getRequestURI() + "?action=update&uid=" + Integer.toString(u.getUserID())%> >Update access</button>
                <button onclick=<%="window.location.href="+request.getRequestURI() + "?action=change&uid=" + Integer.toString(u.getUserID())%> >Change to <%=(u.getUserID()==10)?"Normal Manager":"Chief Manager"%></button>
                <button onclick=<%="window.location.href="+request.getRequestURI() + "?action=delete&uid=" + Integer.toString(u.getUserID())%> >Delete</button>
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
