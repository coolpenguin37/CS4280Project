<%-- 
    Document   : showManager
    Created on : Apr 20, 2016, 6:44:57 PM
    Author     : yduan7
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css">
        <link rel =" stylesheet" href =" css/all.css">
        <link rel =" stylesheet" href ="css/nav.css">
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Show Manager</title>
    </head>
    <body>
        <jsp:include page="manageManager.jsp"></jsp:include>
        <h3 id ="subtitle">Manager Name: ${(null== managerList[0])?managerAdded.name:managerList[0].name}</h4>
        <form action="ManageManagerServlet" method="GET" class="content">
            <table>
                <thead>
                    <tr>
                        <th>Hotel Name</th>
                        <th>Is Manager</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach items="${allHotel}" var="thisHotel">
                        <tr>
                            <c:set var="shouldCheck" value=""/>
                            <td><c:out value="${thisHotel.hotelName}" /></td>
                            <td>
                                <c:forEach items="${managerList}" var="thisManager">
                                    <c:if test="${(null==thisManager) || (thisManager.hotel.hotelName==thisHotel.hotelName)}">
                                        <c:set var="shouldCheck" value="checked"/>
                                    </c:if>
                                </c:forEach>
                                <input type="checkbox" name="${thisHotel.hotelID}" value="on" ${(shouldCheck!="")?"checked":""}>
                                <input type="hidden" name="${thisHotel.hotelID}" value="">
                          </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
            <input type="submit" name="update" value="Update Manager">
        </form>
        <span>${param['msg']}</span>
    </body>
</html>
