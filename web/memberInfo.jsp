<%-- 
    Document   : memberInfo
    Created on : Mar 26, 2016, 1:35:03 AM
    Author     : yanlind
--%>

<%@page import="java.util.ArrayList,hotel.*,user.*"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>

<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>Hypnos-Your One Stop Solution for High Quality Rest During Your Trip</title>
</head>
<body>
        <%! int isSuccess=0;
            String name;
            String userEmail;
            String userTel;
            int isSubscribed;
            String errorMessage;%>
	<% if(session.getAttribute("username")==null) { %>
		<h1>You have not logged in yet! Click <a href="userLogin.jsp">here</a> to log in.</h1>
	<% } else { 
            if (request.getParameter("name")!=null ||
                request.getParameter("userEmail")!=null ||
                request.getParameter("userTel")!=null ||
                request.getParameter("isSubscribed")!=null){
                name=request.getParameter("name");
                userEmail=request.getParameter("userEmail");
                userTel=request.getParameter("userTel");
                isSubscribed=Integer.parseInt(request.getParameter("isSubscribed"));
                User u=new User((String)session.getAttribute("username"),"",name,
                userEmail,userTel,isSubscribed,(Integer)session.getAttribute("type"));
                if (!User.validateName(name)) {errorMessage=User.NAME_ERROR;}
                if (!User.validateEmail(userEmail)) {errorMessage=User.EMAIL_ERROR;}
                if (!User.validateTel(userTel)) {errorMessage=User.TEL_ERROR;}
                if (!User.validateIsSubscribed(isSubscribed)) {errorMessage=User.SUBSCRIBE_ERROR;}
                isSuccess=(User.updateProfile(u))?1:0;}
            else {
                name=(String)session.getAttribute("name");
                userEmail=(String)session.getAttribute("userEmail");
                userTel=(String)session.getAttribute("userTel");
                isSubscribed=(Integer)session.getAttribute("isSubscribed");
            } %>
                
                <h1>Hypnos-Your One Stop Solution for High Quality Rest During Your Trip</h1>
		
		<h2>Hello <%=name%></h2>
                <nav>
		<% if (name!=null) { %>
			<a href="newAccount.jsp"><div><span>Create New Account</span></div></a>
			<a href="userLogin.jsp"><div><span>Login</span></div></a>
		<% }
		else { %>
			<a href="index.jsp">
				<div><span>Home</span></div>
			</a>
			<a href="memberInfo.jsp">
				<div><span>Settings</span></div>
			</a>
			<a href="logout.jsp">
				<div><span>Log Out</span></div>
			</a>
		<% } %>
                </nav>
        <form action="GET" target="_self">
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
                    <input type="checkbox" name="isSubscribed" value="1" <%= (isSubscribed==1)?"checked":"" %>>
                    <input type="checkbox" name="isSubscribed" value="0" <%= (isSubscribed==1)?"":"checked" %>>
                </td>
            </tr> 
            <tr>
                <td>
                    <input type="submit" name="Update"> </td>
                <td>
                    <button name="resetPassword">Reset Password</button>
                </td>
            </tr>
            </tbody>
            </table>
                <% if (isSuccess==1) { %>
                <span> Your profile has been updated successfully! </span>
                <% } else if (isSuccess==-1) { %>
                <span> Your profile cannot be updated... </span>
                <span><%=errorMessage%></span>
                <% } %>
            </form>
	<% } %>
    </body>
</html>

