<%-- 
    Document   : newAccount
    Created on : Mar 23, 2016, 9:59:54 PM
    Author     : yduan7
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Create New Account</title>
    </head>
    <body>
        <form method="POST" action="createAccount">
        <label>Username:</label>
        <input type="text" name="username">
        <br><label>Password:</label>
        <input type="password" name="password">
        <br><label>Name:</label>
        <input type="text" name="name">
        <br><label>Telephone:</label>
        <input type="text" name="telephone">
        <br><label>Email</label>
        <input type="text" name="email">
        <br><label>Do you want to subscribe in our distribution list?</label>
        <input type="checkbox" name="subscribe" value="yes" checked>
        <input type="submit" name="Create New Account">
        </form>
        <% if(request.getAttribute("result") != null) { %>
        <p style="color:red"><%= request.getAttribute("result") %></p>
        <% } %>    
    </body>
</html>
