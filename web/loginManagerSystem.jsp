<%-- 
    Document   : login
    Created on : Apr 20, 2016, 6:11:16 PM
    Author     : yduan7
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css">
        <link rel =" stylesheet" href =" css/all.css">
        <link rel =" stylesheet" href ="css/nav.css">
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <header>
        <div id = "title_bar_home">
            <h1 id = "title" >Hypnos</h1>
            <p id = "intro" >Your One Stop Solution for High Quality Rest During Your Trip</p>
            <jsp:include page="nav.jsp"></jsp:include>
        </div>
        </header>
        <fieldset class =" fieldset">
            <legend>Login As Chief Manager</legend>
            <form method="POST" action="j_security_check" class="content">
                <ul>
                    <li>
                    <label>User Name:</label>
                    <input type="text" name="j_username">
                    </li>
                    <li>
                    <label>Password:</label>
                    <input type="password" name="j_password">
                    </li>
                </ul>     
                <input type="submit" value ="Login">
            </form>
        </fieldset>
    </body>
</html>
