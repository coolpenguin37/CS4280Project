
<%-- 
    Document   : UserLogin
    Created on : Mar 23, 2016, 1:05:08 PM
    Author     : siruzhang2
--%>


<%@ page contentType="text/html;charset=utf-8" pageEncoding="utf-8" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
        <title>User Login</title>
    </head>
    <body>
        <div>
            <div id="content">
                    <form method="POST" action="LoginServlet">
                        <fieldset>  
                                <legend>Member login</legend>
                                <p><label><b>Username: </b></label><input type="text" name="username"> </p>
                                <p><label><b>Password: </b></label><input type="password" name="password"></p>
                                <p>Click <a href="CreateAccount.jsp">here</a> for new member registration!</p>
                                <p><input type="submit" value="Login"></p>   
                                <% if(request.getAttribute("result") != null) { %>
                                    <p style="color:red"><%= request.getAttribute("result") %></p>
                                <% } %>
                            <% } %>      
                        </fieldset>
                    </form>
            </div>
        </div>
    </body>
</html>

