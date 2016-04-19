<%-- 
    Document   : newAccount
    Created on : Mar 23, 2016, 9:59:54 PM
    Author     : yduan7, siruzhang2
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>

<html>
    <head>
        <link rel =" stylesheet" href =" css/all.css">
        <link rel =" stylesheet" href ="css/nav.css">
        <div id = "title_bar_home">
         
            <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
            <title>Create new account</title>
            <h1 id = "title" >Hypnos</h1>
            <p id = "intro" >Your One Stop Solution for High Quality Rest During Your Trip</p>
            <jsp:include page="nav.jsp"></jsp:include>
        </div>
    </head>
    <body>
        <fieldset>
            <legend>Create Account</legend>
            <form method="POST" action="CreateAccount">
                <label>Username:</label><br>
            <input type="text" name="username">
            <br><label>Password:</label><br>
            <input type="password" name="password">
            <br><label>Name:</label><br>
            <input type="text" name="name">
            <br><label>Telephone:</label><br>
            <input type="text" name="telephone">
            <br><label>Email</label><br>
            <input type="text" name="email">
            <br><label>Do you want to subscribe in our distribution list?</label>
            <input type="checkbox" name="subscribe" value="yes" checked><br>
            <input type="submit" name="Create New Account">
            </form>
            <% if(request.getAttribute("result") != null) { %>
            <p class = "error"><%= request.getAttribute("result") %></p>
            <% } %> 
        </fieldset>
    </body>
</html>
