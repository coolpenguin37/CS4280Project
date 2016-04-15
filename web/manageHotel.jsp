<%-- 
    Document   : manageHotel
    Created on : Mar 27, 2016, 3:30:55 PM
    Author     : yanlind
--%>

<%@page import="java.lang.String, user.*,hotel.*,manager.*,java.util.ArrayList"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
  <link href="bootstrap_switch/docs/css/highlight.css" rel="stylesheet">
  <link href="bootstrap_switch/dist/css/bootstrap3/bootstrap-switch.css" rel="stylesheet">
  <script src="bootstrap_switch/dist/js/bootstrap-switch.js"></script>
  <link href="http://getbootstrap.com/assets/css/docs.min.css" rel="stylesheet">
  <link href="bootstrap_switch/docs/css/main.css" rel="stylesheet">
   <script>
    function retrieveData(o,i){
        alert("hello")
        $.ajax({
            type:"POST",
            url:"ManageHotelServlet",
            data:{"indexAtArrayListManager":i},
            dataType:"json",
            
            success: function(data){
                alert(data)
//                for hotelInfo
                $.each(data.hotelInfo,function(key,value){
                    $("#"+key).html(value)
                    
                    $('#'+key).editable({
                        type: 'text',
                        pk: key,
                        success: function(response,newValue){
                            $("#"+key).html(newValue)
                            alert($("#"+key).text())
                        }
                    });
                })
                //for roomInfo
                $.each(data.roomInfo,function(key,value){
                    var roomID=value.roomType
                    var hotelID=value.hotelID
                    var roomName="<h3><a href='#' class='roomName' data-pk=roomName_"+hotelID+"_"+roomID+">"+value.roomName+"</a></h3>"
                    var roomSize="<h4> Room Size: <a href='#' class='roomSize' data-pk=roomSize_"+hotelID+"_"+roomID+">"+value.roomSize+"</a> ft.</h4>"
                    var standardRate="<h4> Standard Rate:$ <a href='#' class='rate' data-pk=rate_"+hotelID+"_"+roomID+">"+value.standardRate+"</a></h4>"
                    var rate=value.standardRate
                    var numOfRoom="<h4> Total Number of Rooms: <a href='#' class='numOfRoom' data-pk=numOfRoom_"+hotelID+"_"+roomID+">"+value.numOfRoom+"</a></h4>"
                    var priceTable="<div class='container'><table class='table table-hover'>\n\
            <thead>\n\
            <th>User Type</th>\n\
            <th>Discount</th>\n\
            <th>Price</th>\n\
            </thead>\n\
            <tbody>\n\ "
                    $.each(data.discountList,function(key,value){
                        if (key.toString().toUpperCase().search("USER")!=-1) {
                            priceTable+="<td>"+key.toString().substr(0,1).toUpperCase()+key.toString().substr(1,key.toString().length-5)+" User"+"</td>"
                            priceTable+="<td><a href='#' class='discount' data-pk=discount_"+hotelID+"_"+roomID+"_"+key.toString()+">"+value+"</a>%"+"</td>"
                            priceTable+="<td class='price'>"+rate*value/100+"</td></tr>"
                        }
                    })
                    priceTable+="</tbody></table></div>"
                    var img=$("<img></img")
                    var newDiv=($("<div class='roomInfo'>").append(roomName).append(roomSize).append(standardRate).append(numOfRoom).append(priceTable).append(img))
                    $("#room-information").append(newDiv) 
                    $('.discount').editable({
                        type: 'text',
                        validate: function(value) {
                            if($.trim(value) == '') {
                                return 'This field is required';
                            }
                            if(!$.isNumeric(value)){
                                return 'Please input a number!'
                            }
                            else if (value<=0 || value>100){
                                return "The value must greater than 0 and less or equal to 100!"
                            }
                        },
                        success: function(response,newValue){
                            $(this).parent().next().html(Math.round($(this).closest('.roomInfo').find('.rate').text()*newValue/100))
                            
                        }
                    });
                    $('.roomName').editable({
                        type: 'text',
                        validate: function(value) {
                            if($.trim(value) == '') {
                                return 'This field is required';
                            }
                        }
                    });
                    
                    $('.numOfRoom,.roomSize').editable({
                        type: 'text',
                        validate: function(value) {
                            if($.trim(value) == '') {
                                return 'This field is required';
                            }
                            if(!$.isNumeric(value)){
                                return 'Please input a number!'
                            }
                        }
                    });
                    
                    $('.rate').editable({
                        type: 'text',
                        validate: function(value) {
                            if($.trim(value) == '') {
                                return 'This field is required';
                            }
                            if(!$.isNumeric(value)){
                                return 'Please input a number!'
                            }
                        },
                        success: function(response,newValue){
                            $.each($(this).closest('.roomInfo').find('.price'),function(index,value){
                                $(value).text($(value).closest('tr').find('.discount').text()*newValue/100)
                            })
                        }
                    });
                    
                    
                })
                
        
            },
            error: function(xhr,ajaxOptions,thrownError){
                alert(xhr.status+"\n"+thrownError);
            }
        })
        }
//                
                
//            },
//            error: function(jqXHR,taxtStatus, errorThrown){
//                console.log("Something bad happened " + textStatus);
//                $("#ajaxResponse").html(jqXHR.responseText);
//            },
//            
//            beforeSend: function(jqXHR, settings){
//                
//            };
//        };)
    function init(){
        $("#isRecommended").bootstrapSwitch();
    }
    window.onload=init;
    
    function refreshCSS(){
        var i,a,s;a=document.getElementsByTagName('link');for(i=0;i<a.length;i++){s=a[i];if(s.rel.toLowerCase().indexOf('stylesheet')>=0&&s.href) {var h=s.href.replace(/(&|%5C?)forceReload=\d+/,'');s.href=h+(h.indexOf('?')>=0?'&':'?')+'forceReload='+(new Date().valueOf())}}
    };
    
    function addNewRoom(){
        var div=$('div[class="roomInfo"]:last')
        var newDiv=$(div).clone()
        var allALink=$(newDiv).find('a[data-pk]')
        $.each(allALink,function(index,value){
            
            dataPKList=$(this).attr("data-pk").split("_")
            if (dataPKList[0]=="discount"){
                $(this).attr("data-pk",dataPKList[0]+"_"+dataPKList[1]+"_0_"+dataPKList[3])
            }
            else {
                $(this).attr("data-pk",dataPKList[0]+"_"+dataPKList[1]+"_0")
            }
        })
        $("#room-information").append(newDiv)
        $('.discount').editable({
                        type: 'text',
                        validate: function(value) {
                            if($.trim(value) == '') {
                                return 'This field is required';
                            }
                            if(!$.isNumeric(value)){
                                return 'Please input a number!'
                            }
                            else if (value<=0 || value>100){
                                return "The value must greater than 0 and less or equal to 100!"
                            }
                        },
                        success: function(response,newValue){
                            $(this).parent().next().html(Math.round($(this).closest('.roomInfo').find('.rate').text()*newValue/100))
                            
                        }
                    });
                    $('.roomName').editable({
                        type: 'text',
                        validate: function(value) {
                            if($.trim(value) == '') {
                                return 'This field is required';
                            }
                        }
                    });
                    
                    $('.numOfRoom,.roomSize').editable({
                        type: 'text',
                        validate: function(value) {
                            if($.trim(value) == '') {
                                return 'This field is required';
                            }
                            if(!$.isNumeric(value)){
                                return 'Please input a number!'
                            }
                        }
                    });
                    
                    $('.rate').editable({
                        type: 'text',
                        validate: function(value) {
                            if($.trim(value) == '') {
                                return 'This field is required';
                            }
                            if(!$.isNumeric(value)){
                                return 'Please input a number!'
                            }
                        },
                        success: function(response,newValue){
                            $.each($(this).closest('.roomInfo').find('.price'),function(index,value){
                                $(value).text($(value).closest('tr').find('.discount').text()*newValue/100)
                            })
                        }
                    });
        $('html, body').animate({
            scrollTop: $(newDiv).offset().top
        }, 1000);
    }
        
        
        
        

  </script>

   
</head>
<body>

<div class="container">

  <h2>Hotel and Room Management System (HRMS)</h2>
  
  <div class="dropdown">
    <button id="chooseHotel" class="btn btn-primary dropdown-toggle" type="button" data-toggle="dropdown">Choose Hotel...<span class="caret"></span></button>
    <ul id="hotel-selection" class="dropdown-menu">
        <c:forEach items="${managers}" var="manager" varStatus="theCount">
            <li onclick='retrieveData(this,${theCount.index})'><a href="#"><c:out value="${manager.hotel.hotelName}" /></a></li>
        </c:forEach>
    </ul>
  </div>
  
  <ul class="nav nav-tabs">
    <li class="active"><a data-toggle="tab" href="#hotel-information">Hotel Information</a></li>
    <li><a data-toggle="tab" href="#room-information">Room Information</a></li>
    <li><a data-toggle="tab" href="#manage-orders">Manage Orders</a></li>
    <li><a data-toggle="tab" href="#report">Report</a></li>
  </ul>

  <div class="tab-content">
    <div id="hotel-information" class="tab-pane fade in active">
      <h2>Hotel Information</h2>
      <h3><a href="#" id="hotelName"></a></h3>
      <h4><a href="#" id="address"></a></h4>
      <div><a href="#" id="information" data-type="text-area"></a></div>
      <img>
      <input id="isRecommended" type="checkbox" ${currentHotel.isRecommended?"checked":""} data-on-color="success">
      <button onclick="sendData(this)">Update</button>
      <button onclick="resetEverything(this)">Reset</button>
      
    </div>
    <div id="room-information" class="tab-pane fade">
      <h2>Room Information</h2>
      <button type="button" class="btn btn-default" onclick="addNewRoom()">Add <span class="glyphicon glyphicon-plus"></span></button>
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
  <link href="//cdnjs.cloudflare.com/ajax/libs/x-editable/1.5.0/bootstrap3-editable/css/bootstrap-editable.css" rel="stylesheet"/>
  <script src="//cdnjs.cloudflare.com/ajax/libs/x-editable/1.5.0/bootstrap3-editable/js/bootstrap-editable.min.js"></script>
  <script>
    $.fn.editable.defaults.mode='inline';
    
    </script>
 
</body>
 
</html>
