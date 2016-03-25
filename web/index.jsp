<%-- 
    Document   : index
    Created on : Mar 19, 2016, 2:37:28 PM
    Author     : siruzhang2
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Home Page</title>
    </head>
    <body>
        <div>
            <div>
                
                <H1>Hello + <%=session.getAttribute("name")%></H1>

            </div>
            <div>
                        <li><a href="index.jsp"><span>Home</span></a></li> 

                        <li><a href="MemberInfo.jsp"><span>Member Info</span></a></li>

                        <li><a href="search.jsp"><span>Search</span></a></li>

                        <li><a href="logout.jsp"><span>Log out</span></a></li>

            </div>
        </div>
    </body>
</html>
