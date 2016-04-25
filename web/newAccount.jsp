<%-- 
    Document   : newAccount
    Created on : Mar 23, 2016, 9:59:54 PM
    Author     : yduan7, siruzhang2
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>

<html>
    <head>
        <link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css">
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
        <fieldset class = "fieldset">
            <legend>Create Account</legend>
            <form method="POST" action="CreateAccount" class = "content">
                
                <ul>
                    <li>
                    <label>Username:</label>
                    <input type="text" name="username">
                    </li>
                    <li>
                    <label>Password:</label>
                    <input type="password" name="password">
                    </li>
                    <li>
                    <label>Name:</label>
                    <input type="text" name="name">
                    </li>
                    <li>
                    <label>Telephone:</label>
                    <input type="text" name="telephone">
                    </li>
                    <li>
                    <label>Email</label>
                    <input type="text" name="email">
                    </li>
                    <li>
                    <label>Do you want to subscribe in our distribution list?</label>
                    <input type="checkbox" name="subscribe" id = "checkbox" value="yes" checked>
                    </li>
                </ul>
                <input type="submit" value="Create">
            </form>  
            <% if(request.getAttribute("result") != null) { %>
            <p class = "error"><%= request.getAttribute("result") %></p>
            <% } %> 
        </fieldset>
        <div class ="footer">
                <p>All the web pages are only for assignment usages for Course CS4280 in City University of Hong Kong</p>
            </div>
    </body>
</html>
