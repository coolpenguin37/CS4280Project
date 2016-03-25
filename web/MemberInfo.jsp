<%-- 
    Document   : MemberInfo
    Created on : Mar 25, 2016, 2:15:09 PM
    Author     : Lin Jianxiong
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>User Profile</title>
    </head>
    <body>
        <h1>Hello World!</h1>
        <h2> Name: <%=session.getAttribute("name")%> </h2>
        <h2> Tel: <%=session.getAttribute("tel")%> </h2>
        <h2> Email: <%=session.getAttribute("email")%> </h2>
        <h2> Subscription: <%=session.getAttribute("isSubscribed")%> </h2>
        <form method="POST" action="UpdateProfileServlet">
            <input type="hidden" name="username" value='<%=session.getAttribute("name")%>'>
            <label> Old password: </label>
            <input type="password" name="oldPassword">
            <br>
            <label> New password: </label>
            <input type="password" name="newPassword">
            <br>
            <label> Name: </label>
            <input type="text" name="name">
            <br>
            <label> Tel: </label>
            <input type="text" name="tel">
            <br>
            <label> Email: </label>
            <input type="text" name="email">
            <br>
            <label> Subscribe our newsletter </label>
            <input type="checkbox" name="isSubscribed" value="yes" checked>
            <input type="submit" name="Update Your Profile">
        </form>
        <% if(request.getAttribute("result") != null) { %>
        <p style="color:red"><%= request.getAttribute("result") %></p>
        <% } %>
    </body>
</html>
