<%-- 
    Document   : updateProfile
    Created on : Mar 26, 2016, 6:05:18 AM
    Author     : yanlind, siruzhang2
--%>

<%@page import="java.util.ArrayList,hotel.*,user.*"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>

<html>
<head>
        <link rel =" stylesheet" href =" css/all.css">
        <link rel =" stylesheet" href ="css/nav.css">
        <div id = "title_bar_home">
             
            <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
            <title>Update Profile</title>
            <h1 id = "title" >Hypnos</h1>
            <p id = "intro" >Your One Stop Solution for High Quality Rest During Your Trip</p>
            <jsp:include page="nav.jsp"></jsp:include>
    </div>
</head>
<body>
        <% 
            int isSuccess=0;
            String errorMessage="";
            String name; 
            String userEmail;
            String userTel;
            int isSubscribed;
	if(session.getAttribute("username")==null) { %>
		<h1 class = "error">You have not logged in yet! Click <a href="userLogin.jsp">here</a> to log in.</h1>
	<% } 
            else { 
            if (request.getParameter("name")!=null ||
                request.getParameter("userEmail")!=null ||
                request.getParameter("userTel")!=null ||
                request.getParameter("isSubscribed")!=null){
                name=request.getParameter("name");
                userEmail=request.getParameter("userEmail");
                userTel=request.getParameter("userTel");
                if (request.getParameter("isSubscribed")==null) {
                    isSubscribed=-1;
                }
                else {
                    isSubscribed=Integer.parseInt(request.getParameter("isSubscribed"));
                }
                User u=new User((String)session.getAttribute("username"),"",name,userEmail,userTel,isSubscribed,(Integer)session.getAttribute("type"));
                if (!User.validateName(name)) {
                    errorMessage=User.NAME_ERROR;
                }
                if (!User.validateEmail(userEmail)) {
                    errorMessage=User.EMAIL_ERROR;
                }
                if (!User.validateTel(userTel)) {
                    errorMessage=User.TEL_ERROR;
                }
                if (errorMessage.isEmpty()){
                    isSuccess=(User.updateProfile(u))?1:-1;}
                else {
                    isSuccess=-1;}
                if (isSuccess==1){
                    session.setAttribute("name",u.getName());
                    session.setAttribute("userEmail",u.getEmail());
                    session.setAttribute("userTel",u.getTel());
                    if (isSubscribed!=-1){session.setAttribute("isSubscribed",u.getIsSubscribed());}
                    else {isSubscribed=(Integer)session.getAttribute("isSubscribed");}
                }
                else {
                    name=(String)session.getAttribute("name");
                    userEmail=(String)session.getAttribute("userEmail");
                    userTel=(String)session.getAttribute("userTel");
                    isSubscribed=(Integer)session.getAttribute("isSubscribed");
                }
            }
            else {
                name=(String)session.getAttribute("name");
                userEmail=(String)session.getAttribute("userEmail");
                userTel=(String)session.getAttribute("userTel");
                isSubscribed=(Integer)session.getAttribute("isSubscribed");
            } %>
                
        <fieldset>
            <legend>Update Profile</legend>
            <p class = "info">Hello <%=name%>. Update your profile here:</p>
        <jsp:include page="nav.jsp"></jsp:include>
        <form method="POST" action="">
	<table>
	<thead>
            <tr>
                <th>Information</th><th>Details</th>
            </tr>
	</thead>
        <tbody>
            <tr>
                <td>Name</td>
                <td><input type="text" name="name" value="<%= name%>" ></td>
            </tr>
            
            <tr>
                <td>Email</td>
                <td><input type="text" name="userEmail" value="<%=userEmail %>" ></td>
            </tr> 
            
            <tr>
                <td>Telephone</td>
                <td><input type="text" name="userTel" value="<%=userTel%>" ></td>
            </tr> 
            
            <tr>
                <td>Subscribe to our email list</td>
                <td>
                    <span>Yes</span>
                    <input type="checkbox" name="isSubscribed" value="1" <%= (isSubscribed==1)?"checked":"" %>>
                    <span>No</span>
                    <input type="checkbox" name="isSubscribed" value="0" <%= (isSubscribed==0)?"checked":"" %>>
                </td>
            </tr> 
            <tr>
                <td>
                    <input type="submit" name="Update">
                </td>
                <td>
                    <button name="resetPassword">Reset Password</button>
                </td>
            </tr>
            </tbody>
            </table>
                <% if (isSuccess==1) { %>
                <span> Your profile has been updated successfully! </span>
                <% } else if (isSuccess==-1){ %>
                    <span> Your profile cannot be updated... </span>
                    <span><%=errorMessage%></span>
                <% } %>
            </form>
        </fieldset>
	<% } %>
    </body>
</html>

