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
  <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/font-awesome/4.6.1/css/font-awesome.min.css">
   <script>
    function checkInOrder(orderID){
        $.ajax({
            type:"POST",
            url:"ManageHotelServlet",
            data:{"checkInOrder":orderID},
            dataType:"json",
            
            success:function(data){
                $(".col-md-12").append("<p>Success!</p>")
            }
        })
    }
    function clearErr(){
        $('.alert').remove()
        $('.fa').remove()
    }
    function checkOrder(o){
        $("#search_by_order_id_div").append("<i class='fa fa-spinner fa-pulse fa-fw margin-bottom'></i>")

            $.ajax({
            type:"POST",
            url:"ManageHotelServlet",
            data:{"orderIDToManage":o.value},
            dataType:"json",
            
            success: function(data){
                if (data.status=="error"){
                    clearErr()
                    $("#manage-orders").append("<div><span class='alert alert-danger'><strong>Error!</strong> " + data.msg + "</span></div>")
                    return
                }
                $('.fa').remove()
                $('.col-md-12').remove()
                var orderInformation=$("<div class='col-md-12'>")
                var idNumber=$("<h4> Order ID: "+data.orderID+"</h4>")
                var status=$("<p> Status: "+data.statusDescription+"</p>")
                var CIDate=$("<p> Check-in Date: "+data.CIDate+"</p>")
                var CODate=$("<p> Check-out Date: "+data.CODate+"</p>")
                var roomName=$("<p> Room Name: "+data.roomName+"</p>")
                var numOfRoom=$("<p> Number of rooms booked: "+data.numOfRoom+"</p>")
                var name=$("<p> Client Name: "+data.name+"</p>")
                var email=$("<p> Client Email: "+data.email+"</p>")
                var phone=$("<p> Client Phone: "+data.phone+"</p>")
                var price=$("<p> Price of the offer: "+data.price+"</p>")
                orderInformation.append(idNumber).append(status).append(CIDate)
                        .append(CODate).append(roomName).append(numOfRoom).append(name)
                        .append(email).append(phone).append(price);
                if (data.statusDescription.toString().indexOf("Order unpaid")>-1){
                    orderInformation.append("<button type='button' class='btn btn-default' onclick='checkInOrder("+data.orderID+")'>Check In</button>")
                }
                
                $("#manage-orders").append(orderInformation);
            },
            
            beforeSend:function(){
                if (!($.isNumeric(o.value)) || Math.floor(o.value) != o.value){
                    clearErr()
                    $("#manage-orders").append("<div><span class='alert alert-warning'><strong>Warning!</strong> Order ID must be a number!</span></div>")
                    return false;
                }
            },
            error: function(xhr,ajaxOptions,thrownError){
                alert(xhr.status+"\n"+thrownError);
            }
        })
    }
    function retrieveData(o,i){
        $.ajax({
            type:"POST",
            url:"ManageHotelServlet",
            data:{"indexAtArrayListManager":i},
            dataType:"json",
            
            success: function(data){
                
//                for hotelInfo
                $.each(data.hotelInfo,function(key,value){
                    if (key=="isRecommended") {
                        $("#isRecommended").bootstrapSwitch("disabled",false)
                        $("#"+key).bootstrapSwitch("state",(value==1)?true:false)
                    }
                    else {
                        $("#"+key).html(value)
                    }

                    $('#'+key).editable({
                        type: 'text',
                        pk: data.hotelInfo.hotelID,
                        url: 'ManageHotelServlet',
                        success: function(response,newValue){
                            if (response.status=="error") {return response.msg}
                        },
                        error: function(){
                            alert("no")
                        }
                        
                    });
                })
                $("#isRecommended").attr("name",data.hotelInfo.hotelID)
                
                //for roomInfo
                $.each(data.roomInfo,function(key,value){
                    var roomID=value.roomType
                    var hotelID=value.hotelID
                    var roomName="<h3><a href='#' class='roomName' data-name='roomName' data-pk="+hotelID+"_"+roomID+">"+value.roomName+"</a></h3>"
                    var roomSize="<h4> Room Size: <a href='#' class='roomSize' data-name='roomName' data-pk="+hotelID+"_"+roomID+">"+value.roomSize+"</a> ft.</h4>"
                    var standardRate="<h4> Standard Rate:$ <a href='#' class='rate' data-name='standardRate' data-pk="+hotelID+"_"+roomID+">"+value.standardRate+"</a></h4>"
                    var rate=value.standardRate
                    var numOfRoom="<h4> Total Number of Rooms: <a href='#' class='numOfRoom' data-name='numOfRoom' data-pk="+hotelID+"_"+roomID+">"+value.numOfRoom+"</a></h4>"
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
                            priceTable+="<td><a href='#' class='discount' name='discount_"+key.toString()+"' data-pk="+hotelID+"_"+roomID+"_"+">"+value+"</a>%"+"</td>"
                            priceTable+="<td class='price'>"+rate*value/100+"</td></tr>"
                        }
                    })
                    priceTable+="</tbody></table></div>"
                    var img=$("<img></img")
                    var newDiv=($("<div class='roomInfo'>").append(roomName).append(roomSize).append(standardRate).append(numOfRoom).append(priceTable).append(img))
                    $("#room-information").append(newDiv) 
                    $('.discount').editable({
                        type: 'text',
                        url: 'ManageHotelServlet',
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
                        url: 'ManageHotelServlet',
                        validate: function(value) {
                            if($.trim(value) == '') {
                                return 'This field is required';
                            }
                        }
                    });
                    
                    $('.numOfRoom,.roomSize').editable({
                        type: 'text',
                        url: 'ManageHotelServlet',
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
                        url: 'ManageHotelServlet',
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
                        },
                        error: function(response){
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
        $("#isRecommended").bootstrapSwitch("disabled",true)
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
            if (dataPKList.length==1){
                $(this).attr("data-pk",dataPKList[0])
            }
            else {
                $(this).attr("data-pk",dataPKList[0]+"_0")
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
      <h3><span class="label label-default">Recommend:</span> <input id="isRecommended" type="checkbox" data-on-color="success"></h3>
    </div>
    <div id="room-information" class="tab-pane fade">
      <h2>Room Information</h2>
      <button type="button" class="btn btn-default" onclick="addNewRoom()">Add <span class="glyphicon glyphicon-plus"></span></button>
      <button type="button" class="btn btn-default">Remove All</button>
    </div>
    <div id="manage-orders" class="tab-pane fade">
      <h3>Manage Orders</h3>
      <h4><label for="input_orderID">Search by order ID</label></h4>
      <div id="search_by_order_id_div">
      <div class="col-xs-4" >
          <input type="text" class="form-control" id="input_orderID" placeholder="Type in Order ID..." onclick='clearErr()' onblur="checkOrder(this)">
      </div>
      </div>
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
     $("#isRecommended").on('switchChange.bootstrapSwitch', function(event, state) {
            $.ajax({
                
                type:"POST",
                url:"ManageHotelServlet",
                data:{"pk":$(this).attr("name"),"name":"isRecommended","value":state},
                dataType:"json",
                
                success:function(response){
                },
                error: function(xhr,ajaxOptions,thrownError){
                }
            })
        })
    </script>
 
</body>
 
</html>
