<%-- 
    Document   : manageHotel
    Created on : Mar 27, 2016, 3:30:55 PM
    Author     : yanlind
--%>

<%@page import="java.lang.String, user.*,hotel.*,manager.*,java.util.ArrayList"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
  <style>
      .member-benefit-table a{
          display:block;
          width:100%;
          height:100%;
          color:#333;
      }
 </style>
   <script>
    function updateMemberBenefit(command,value){
        var values={}
        values[command]=value
        values["hotelID"]=$(".isRecommended").attr("name")
        $.ajax({
            type:"POST",
            url:"ManageHotelServlet",
            data:values,
            dataType:"text",
            
            success:function(data){
                if (data==null || data==""){
                    $(".alert").remove()
                    $("#hotel-information").append("<div class='alert alert-success fade in'><a href='#' class='close' data-dismiss='alert' aria-label='close'>&times;</a><strong>Success!</strong> The member benefit has been updated successfully. </div>")
                    $("#"+command+" span").removeClass("fake-span");
                    $("#"+command+" .fake-empty *").remove();
                    $("#"+command+" .fake-empty").css("color","#333")
                    $("#"+command+" .fake-empty").removeClass("fake-empty");
                }
                else {
                    alert(data)
                }
            },
            
            error: function(xhr,ajaxOptions,thrownError){
                alert(xhr.status+"\n"+thrownError);
            }
        })
    }
    function modifyOrderStatus(command,orderID){
        var values={}
        values[command]=orderID
        $.ajax({
            type:"POST",
            url:"ManageHotelServlet",
            data:values,
            dataType:"text",
            
            success:function(data){
                $(".col-md-12").append("<div class='alert alert-success fade in'><a href='#' class='close' data-dismiss='alert' aria-label='close'>&times;</a><strong>Success!</strong> The order has been updated successfully. </div>")
            },
            
            error: function(xhr,ajaxOptions,thrownError){
                alert(xhr.status+"\n"+thrownError);
            }
        })
    }
    function clearErr(){
        $('.alert').remove()
        $('.fa').remove()
    }
    function checkOrder(o){
        $('.col-md-12').remove()
        $("#search_by_order_id_div").append("<i class='fa fa-spinner fa-pulse fa-fw margin-bottom'></i>")
        
            setTimeout(function(){$.ajax({
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
                $.delay(1000)
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
                if (data.statusDescription.toString().indexOf("Order paid")>-1){
                    orderInformation.append("<button type='button' class='btn btn-default' onclick='modifyOrderStatus(\"checkInOrder\","+data.orderID+")'>Check In</button>")
                }
                else if (data.statusDescription.toString().indexOf("Checked-in")>-1){
                    orderInformation.append("<button type='button' class='btn btn-default' onclick='modifyOrderStatus(\"checkOutOrder\","+data.orderID+")'>Check Out</button>")
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
        })},1000)
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
                        $(".isRecommended").bootstrapSwitch("disabled",false)
                        $("#"+key).bootstrapSwitch("state",(value==1)?true:false)
                    }
                    else {
                        $("#"+key).html(value)
                    }

                    $('#'+key).editable({
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
                $(".isRecommended").attr("name",data.hotelInfo.hotelID)
                
                $.each(data.discountList,function(key,value){
                   if (key.toString().toUpperCase().search("USER")==-1){
                       for (i=1;i<=4;i++){
                           $("#"+key+" td:nth-child("+(i+1)+") a span").remove();
                       }
                       for (i=value;i<=4;i++){
                           $("#"+key+" td:nth-child("+(i+1)+") a").html("<span class='glyphicon glyphicon-ok' aria-hidden='true'></span>")
                       }
                   }
                })
                
                $(".member-benefit-table button").attr("disabled",false);
                
                $(".member-benefit-table td").hover(function(){
//                    alert($(this).html()=="")
                    if ($(this).find("a").html()==""){
                        $(this).find("a").html("<span class='glyphicon glyphicon-ok fake-span' aria-hidden='true'></span>")
                        
                    }
                    $(this).find("a").css("color","blue")
                    $.each($(this).nextAll("td"),function(){
                        if ($(this).find("a").html()==""){
                          $(this).find("a").html("<span class='glyphicon glyphicon-ok fake-span' aria-hidden='true'></span>")  
                          
                        }
                        $(this).find("a").css("color","blue")
                    })
                    
                    $.each($(this).prevAll("td"),function(){
                        if ($(this).find("a").html()!=""){
                          $(this).find("a").addClass("fake-empty")
                          
                        }
                        $(this).find("a").css("color","transparent")
                    })
                    
                    $(this).nextAll("td")
                }, function(){
                    $.each($(this).prevAll("td"),function(){
                        if ($(this).find("a").html()!=""){
                          $(this).find("a").removeClass("fake-empty")
                          
                        }
                        $(this).find("a").css("color","#333")
                    })
                    $(this).find("a").css("color","#333")
                    $.each($(this).nextAll("td"),function(){
                        if ($(this).find("a").html()==""){
                          
                        }
                        $(this).find("a").css("color","#333")
                    })
                    $('.fake-span').remove()
                })
                $(".roomInfo").remove();
                //for roomInfo
                $.each(data.roomInfo,function(key,value){
                    var roomID=value.roomType
                    var hotelID=value.hotelID
                    var roomName="<h3><a href='#' class='roomName' data-name='roomName' data-pk="+hotelID+"_"+roomID+">"+value.roomName+"</a></h3>"
                    var roomSize="<h4> Room Size: <a href='#' class='roomSize' data-name='roomName' data-pk="+hotelID+"_"+roomID+">"+value.roomSize+"</a> ft.</h4>"
                    var standardRate="<h4> Standard Rate:$ <a href='#' class='rate' data-name='standardRate' data-pk="+hotelID+"_"+roomID+">"+value.standardRate+"</a></h4>"
                    var rate=value.standardRate
                    var numOfRoom="<h4> Total Number of Rooms: <a href='#' class='numOfRoom' data-name='numOfRoom' data-pk="+hotelID+"_"+roomID+">"+value.numOfRoom+"</a></h4>"
                    var recommendFlag='<span class="label label-default">Recommend:</span> <input class="isRecommended room" name='+roomID+' type="checkbox" data-on-color="success">'
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
                            priceTable+="<td><a href='#' class='discount' data-name='discount_"+key.toString()+"' name='discount_"+key.toString()+"' data-pk="+hotelID+"_"+roomID+"_"+">"+value+"</a>%"+"</td>"
                            priceTable+="<td class='price'>"+rate*value/100+"</td></tr>"
                        }
                    })
                    priceTable+="</tbody></table></div>"
                    var img=$("<img></img")
                    var newDiv=($("<div class='roomInfo'>").append(roomName).append(roomSize).append(standardRate).append(numOfRoom).append(recommendFlag).append(priceTable).append(img))
                    $("#room-information").append(newDiv) 
                    $(".isRecommended").bootstrapSwitch("disabled",false)
                    $(".isRecommended").on('switchChange.bootstrapSwitch', function(event, state) {
                        $.ajax({
                
                        type:"POST",
                        url:"ManageHotelServlet",
                        //duan check this
                        data:{"pk":$(this).attr("name"),"name":"isRecommended","value":state},
                        dataType:"json",

                        success:function(response){
                        },
                        error: function(xhr,ajaxOptions,thrownError){
                        }
                    })
                    })
        
        
        
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
                            else if (Math.floor(value)!=value){
                                return "The value must be an integer!"
                            }
                        },
                        success: function(response,newValue){
                            var name=$(this).attr("name")
                            var atags=$('a[name="'+name+'"]')
                            $.each(atags,function(){
                              $(this).editable('setValue',newValue)
                              $(this).parent().next().html(Math.round($(this).closest('.roomInfo').find('.rate').text()*newValue/100))  
                          })
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
        $(".isRecommended").bootstrapSwitch("disabled",true)
    }
    window.onload=init;
    
    
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
      <h3><a href="#" id="hotelName" data-type="text"></a></h3>
      <h4><a href="#" id="address" data-type="text"></a></h4>
      <div><a href="#" id="intro" class='editable editable-pre-wrapped editable-click' data-type="textarea"></a></div>
      <img>
      <h3><span class="label label-default">Recommend:</span> <input class="isRecommended hotel" type="checkbox" data-on-color="success"></h3>
      <div>
          <table class="table table-hover member-benefit-table">
    <thead>
      <tr>
        <th>Member Benefits</th>
        <th>Common User</th>
        <th>Preferred User</th>
        <th>Gold User</th>
        <th>Platinum User</th>
      </tr>
    </thead>
    <tbody>
      <tr id="welcomeGift">
        <th>Welcome Gift</th>
        <td><a onclick='updateMemberBenefit("welcomeGift",1)'></a></td>
        <td><a onclick='updateMemberBenefit("welcomeGift",2)'></a></td>
        <td><a onclick='updateMemberBenefit("welcomeGift",3)'></a></td>
        <td><a onclick='updateMemberBenefit("welcomeGift",4)'></a></td>
        <td><button type="button" class="btn btn-danger" onclick='updateMemberBenefit("welcomeGift",5)' disabled>Remove this benefit</button></td>
      </tr>
      <tr id="lateCheckout">
        <th>Late Checkout</th>
        <td><a onclick='updateMemberBenefit("lateCheckout",1)'></a></td>
        <td><a onclick='updateMemberBenefit("lateCheckout",2)'></a></td>
        <td><a onclick='updateMemberBenefit("lateCheckout",3)'></a></td>
        <td><a onclick='updateMemberBenefit("lateCheckout",4)'></a></td>
        <td><button type="button" class="btn btn-danger" onclick='updateMemberBenefit("lateCheckout",5)' disabled>Remove this benefit</button></td>
      </tr>
      <tr id="breakfast">
        <th>Free Breakfast</th>
        <td><a onclick='updateMemberBenefit("breakfast",1)'></a></td>
        <td><a onclick='updateMemberBenefit("breakfast",2)'></a></td>
        <td><a onclick='updateMemberBenefit("breakfast",3)'></a></td>
        <td><a onclick='updateMemberBenefit("breakfast",4)'></a></td>
        <td><button type="button" class="btn btn-danger" onclick='updateMemberBenefit("breakfast",5)' disabled>Remove this benefit</button></td>
      </tr>
      <tr id="freeWiFi">
        <th>Free Wifi</th>
        <td><a onclick='updateMemberBenefit("freeWiFi",1)'></a></td>
        <td><a onclick='updateMemberBenefit("freeWiFi",2)'></a></td>
        <td><a onclick='updateMemberBenefit("freeWiFi",3)'></a></td>
        <td><a onclick='updateMemberBenefit("freeWiFi",4)'></a></td>
        <td><button type="button" class="btn btn-danger" onclick='updateMemberBenefit("freeWiFi",5)' disabled>Remove this benefit</button></td>
      </tr>
    </tbody>
  </table>
      </div>
    </div>
    <div id="room-information" class="tab-pane fade">
      <h2>Room Information</h2>
      <!--<button type="button" class="btn btn-default" onclick="addNewRoom()">Add <span class="glyphicon glyphicon-plus"></span></button>-->
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
     $(".isRecommended").on('switchChange.bootstrapSwitch', function(event, state) {
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
