
<%-- 
    Document   : UserLogin
    Created on : Mar 23, 2016, 1:05:08 PM
    Author     : siruzhang2
--%>


<%@ page contentType="text/html;charset=utf-8" pageEncoding="utf-8" %>
<!DOCTYPE html>
<html>
<head>
    <link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css">
    <link rel =" stylesheet" href =" css/all.css">
    <link rel =" stylesheet" href ="css/nav.css">
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <title>User Login</title>
</head>
<body>
    <div id = "title_bar_home">
        <h1 id = "title" >Hypnos</h1>
        <p id = "intro" >Your One Stop Solution for High Quality Rest During Your Trip</p>
        <jsp:include page="nav.jsp"></jsp:include>
    </div>
    <div>
        <a href="https://www.facebook.com/dialog/oauth?client_id=622571284564354&redirect_uri=<%=request.getHeader("referer")+((request.getQueryString()==null)?"":"?"+request.getQueryString())%>&response_type=token">Login using Facebook</a>
            <form method="POST" action="LoginServlet" class = "content">
                <fieldset class = "fieldset">  
                    <legend>Member login</legend>
                <%if (request.getHeader("referer").indexOf("userLogin.jsp")==-1){
                        if (request.getHeader("referer").indexOf("newAccount")==-1){
                            session.setAttribute("previousPage",request.getHeader("referer")+((request.getQueryString()==null)?"":"?"+request.getQueryString()));
                        }
                        else{
                            session.setAttribute("previousPage","index.jsp");
                        }
                }
                %>
                    <ul>
                    <li><label><b>Username: </b></label><input type="text" name="username"> </li>
                    <li><label><b>Password: </b></label><input type="password" name="password"></li>
                    </ul>
                    <p class = "info">Click <a href="newAccount.jsp">here</a> for new member registration!</p>
                    <p class = "info"><a href="manageManager.jsp">Administrative Login</a></p>  
                    <p><input type="submit" value="Login"></p>
            </form>
               
            <% if(request.getAttribute("result") != null) { %>
                <p class = "error"><%= request.getAttribute("result") %></p>
             <% } %>
            <p><%=(request.getParameter("result")!=null)?request.getParameter("result"):""%></p>
        </fieldset>
    </div>
        <div class ="footer">
                <p>All the web pages are only for assignment usages for Course CS4280 in City University of Hong Kong</p>
            </div>
</body>
</html>

