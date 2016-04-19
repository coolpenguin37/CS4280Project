
<%-- 
    Document   : UserLogin
    Created on : Mar 23, 2016, 1:05:08 PM
    Author     : siruzhang2
--%>


<%@ page contentType="text/html;charset=utf-8" pageEncoding="utf-8" %>
<!DOCTYPE html>
<html>
<head>
    <link rel =" stylesheet" href =" css/all.css">
    <link rel =" stylesheet" href ="css/nav.css">
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <div id = "title_bar_home">
        <title>User Login</title>
        <h1 id = "title" >Hypnos</h1>
        <p id = "intro" >Your One Stop Solution for High Quality Rest During Your Trip</p>
        <jsp:include page="nav.jsp"></jsp:include>
    </div>
</head>
<body>
    
    <div>

            <form method="POST" action="LoginServlet">
                <fieldset>  
                    <legend>Member login</legend>
                    <p><label><b>Username: </b></label><input type="text" name="username"> </p>
                    <p><label><b>Password: </b></label><input type="password" name="password"></p>
                    <p>Click <a href="newAccount.jsp">here</a> for new member registration!</p>
                    <p><input type="submit" value="Login"></p>   
                    <% if(request.getAttribute("result") != null) { %>
                        <p style="color:red"><%= request.getAttribute("result") %></p>
                    <% } %>
                </fieldset>
            </form>

    </div>
</body>
</html>

