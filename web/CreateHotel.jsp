<%-- 
    Document   : CreateHotel
    Created on : Mar 24, 2016, 10:10:04 PM
    Author     : Lin Jianxiong
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css">
        <link rel =" stylesheet" href =" css/all.css">
        <link rel =" stylesheet" href ="css/nav.css">
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Create New Hotel</title>
    </head>
    <body>
        <div id = "title_bar_home">
            <h1 id = "title" >Hypnos</h1>
            <p id = "intro" >Your One Stop Solution for High Quality Rest During Your Trip</p>
            <jsp:include page="nav.jsp"></jsp:include>
        </div>
        <form method="POST" action="CreateHotelServlet" class = "content">

            <label>Hotel Name:</label>
            <input type="text" name="hotelName">
            <br>
            <label>Address:</label>
            <input type="text" name="address"> 
            <br>
            <label>Star Rating:</label>
            <input type="radio" name="starRating" value="1" checked> 1
            <input type="radio" name="starRating" value="2"> 2
            <input type="radio" name="starRating" value="3"> 3
            <input type="radio" name="starRating" value="4"> 4
            <input type="radio" name="starRating" value="5"> 5
            <br>
            <label>Do you want to recommend this hotel?</label>
            <input type="checkbox" name="isRecommended" value="yes" checked>
            <br>
            <label>Label</label>
            <input type="text" name="label"> 
            <br>
            <label>Introduction </label>
            <input type="text" name="intro">
            <br>
            <input type="submit" name="Create New Hotel">
        </form>
        <% if(request.getAttribute("result") != null) { %>
        <p style="color:red"><%= request.getAttribute("result") %></p>
        <% } %>    
    </body>
</html>
