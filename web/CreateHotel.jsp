<%-- 
    Document   : CreateHotel
    Created on : Mar 24, 2016, 10:10:04 PM
    Author     : Lin Jianxiong
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Create New Hotel</title>
    </head>
    <body>
        <form method="POST" action="CreateHotelServlet">
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
        <input type="submit" name="Create New Hotel">
        </form>
        <% if(request.getAttribute("result") != null) { %>
        <p style="color:red"><%= request.getAttribute("result") %></p>
        <% } %>    
    </body>
</html>
