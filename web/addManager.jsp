<%-- 
    Document   : addManager
    Created on : Apr 22, 2016, 3:28:03 PM
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
        <title>Add Manager</title>
    </head>
    <body>
        <script>
            var confirmToAdd;
            if (${null==managerToAdd}){
                window.location.href="manageManager.jsp";
                
            }
            else{
                confirmToAdd=confirm("User with userName ${managerToAdd} is not a manager yet. Do you want to add this user as a manager?")
                if (confirmToAdd){
                    window.location.href="ManageManagerServlet?add=${managerToAdd}";}
                else {
                    window.location.href="manageManager.jsp";
                }
            }
        </script>
    </body>
</html>
