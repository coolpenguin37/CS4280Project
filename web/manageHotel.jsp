<%-- 
    Document   : manageHotel
    Created on : Mar 27, 2016, 3:30:55 PM
    Author     : yanlind
--%>

<%@page import="java.lang.String, user.*,hotel.*,manager.*,java.util.ArrayList"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
  <title>Bootstrap Case</title>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css">
  <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.0/jquery.min.js"></script>
  <script src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js"></script>
  <link href="/bootstrap_switch/docs/css/highlight.css" rel="stylesheet">
  <link href="/bootstrap_switch/dist/css/bootstrap3/bootstrap-switch.css" rel="stylesheet">
  <link href="http://getbootstrap.com/assets/css/docs.min.css" rel="stylesheet">
  <link href="/bootstrap_switch/docs/css/main.css" rel="stylesheet">
  <link href="//cdnjs.cloudflare.com/ajax/libs/x-editable/1.5.0/bootstrap3-editable/css/bootstrap-editable.css" rel="stylesheet"/>
  <script src="//cdnjs.cloudflare.com/ajax/libs/x-editable/1.5.0/bootstrap3-editable/js/bootstrap-editable.min.js"></script>
  <script>
    $.fn.editable.defaults.mode='inline';
    $(document).ready(function() {
        $('#hotelName').editable({
            type: 'text',
            pk: hotelName,
            url: '',
        });
    });

    function init(){
        $("#isRecommended").bootstrapSwitch();
    }
    
    function retrieveData(i){
        $.ajax({
            type:"POST",
            url:"manageHotelServlet",
            data:{"indexAtArrayListManager":i},
            datatype:"json",

            success: function(data,textStatus,jqXHR){
                //for hotelInfo
                $.each(data.hotelInfo,function(key,value){
                    $("#"+key).html(value);
                });
                
                //for roomInfo
                $.each(data.roomInfo,function(key,value){
                    var roomName="<h3>"+value.roomName+"</h3>"
                    var roomSize="<h4>"+value.roomSize+"</h4>"
                    var standardRate="<h4>"+value.standardRate+"</h4>"
                    var numOfRoom="<h4>"+value.numOfRoom+"</h4>"
                    var priceTable="<table>\n\
            <thead>\n\
            <th>User Type</th>\n\
            <th>Discount</th>\n\
            <th>Price</th>\n\
            </thead>\n\
            <tbody>\n\ "
                    $.each(data.discountList,function(key,value){
                        priceTable+="<td>"+key+"</td>"
                        priceTable+="<td>"+value/100+"</td>"
                        priceTable+="<td>"+standardRate*value/100+"</td>"
                    })
                    priceTable+="</tbody></table>"
                    var img=$("<img></img")
                    var newDiv=($("<div>").append(roomName).append(roomSize).append(standardRate).append(numOfRoom).append(priceTable).append(img))
                    $("#room-information").append(newDiv)
                })
                
            },
            error: function(jqXHR,taxtStatus, errorThrown){
                console.log("Something bad happened " + textStatus);
                $("#ajaxResponse").html(jqXHR.responseText);
            },
            
            beforeSend: function(jqXHR, settings){
                $("#chooseHotel").html(${managers[i].hotel.hotelName};)
            };
        };)
    }
    
    window.onload=init;
        
  </script>
</head>
<body>

<div class="container">

  <h2>Hotel and Room Management System (HRMS)</h2>
  
  <div>
    <button id="chooseHotel" class="btn btn-primary dropdown-toggle" type="button" data-toggle="dropdown">Choose Hotel...<span class="caret"></span></button>
    <ul id="hotel-selection" class="dropdown-menu">
        <c:forEach items="${managers}" var="manager" varStatus="theCount">
            <li onclick="$('#chooseHotel').html=this.html;window.location.href='ManageHotelServlet?indexAtArrayListManager='+theCount.index"><c:out value="${manager.hotel.hotelName}" /></li>
        </c:forEach>
    </ul>
  </div>
  
  <ul class="nav nav-tabs">
    <li class="active"><a data-toggle="tab" href="#home">Hotel Information</a></li>
    <li><a data-toggle="tab" href="#menu1">Room Information</a></li>
    <li><a data-toggle="tab" href="#menu2">Manage Orders</a></li>
    <li><a data-toggle="tab" href="#menu3">Report</a></li>
  </ul>

  <div class="tab-content">
    <div id="hotel-information" class="tab-pane fade in active">
      <h2>Hotel Information</h2>
      <h3><a href="#" id="hotelName" data-pk="hotelName" data-type="text">${currentHotel.hotelName}</a></h3>
      <h4><a href="#" id="address" data-pk="address" data-type="text">${currentHotel.address}</a></h4>
      <div><a href="#" id="information" data-pk="information" data-type="text-area"></a></div>
      <img>
      <input id="isRecommended" type="checkbox" ${currentHotel.isRecommended?"checked":""} data-on-color="success">
      <button onclick="sendData(this)">Update</button>
      <button onclick="resetEverything(this)">Reset</button>
      
    </div>
    <div id="room-information" class="tab-pane fade">
      <h2>Room Information</h2>
      <button type="button" class="btn btn-default">Add <span class="glyphicon glyphicon-plus"></span></button>
      <button type="button" class="btn btn-default">Remove All</button>
    </div>
    <div id="manage-orders" class="tab-pane fade">
      <h3>Manage Orders</h3>
      <p>Sed ut perspiciatis unde omnis iste natus error sit voluptatem accusantium doloremque laudantium, totam rem aperiam.</p>
    </div>
    <div id="report" class="tab-pane fade">
      <h3>Report</h3>
      <p>Eaque ipsa quae ab illo inventore veritatis et quasi architecto beatae vitae dicta sunt explicabo.</p>
    </div>
  </div>
</div>

</body>
</html>
