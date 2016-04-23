/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletContext;
import manager.*;
import java.util.ArrayList;
import com.google.gson.*;
import hotel.*;
import org.json.simple.*;
import order.*;
import user.*;
/**
 *
 * @author yanlind
 */
public class ManageHotelServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        try {
            HttpSession session=request.getSession();
            if (request.getParameter("welcomeGift")!=null){
                int newValue=Integer.parseInt(request.getParameter("welcomeGift"));
                MemberBenefits mb=MemberBenefits.getMemberBenefitsByHotelID(Integer.parseInt(request.getParameter("hotelID")));
                mb.setWelcomeGift(newValue);
                if (MemberBenefits.updateMemberBenefits(mb)){
                    return;
                }
                else{
                    out.print("Cannot update welcome gift!");
                }
            }
            else if (request.getParameter("lateCheckout")!=null){
                int newValue=Integer.parseInt(request.getParameter("lateCheckout"));
                MemberBenefits mb=MemberBenefits.getMemberBenefitsByHotelID(Integer.parseInt(request.getParameter("hotelID")));
                mb.setLateCheckout(newValue);
                if (MemberBenefits.updateMemberBenefits(mb)){
                    return;
                }
                else{
                    out.print("Cannot update late checkout!");
                }
            }
            else if (request.getParameter("breakfast")!=null){
                int newValue=Integer.parseInt(request.getParameter("breakfast"));
                MemberBenefits mb=MemberBenefits.getMemberBenefitsByHotelID(Integer.parseInt(request.getParameter("hotelID")));
                mb.setBreakfast(newValue);
                if (MemberBenefits.updateMemberBenefits(mb)){
                    return;
                }
                else{
                    out.print("Cannot update breakfast!");
                }
            }
            else if (request.getParameter("freeWiFi")!=null){
                int newValue=Integer.parseInt(request.getParameter("freeWiFi"));
                MemberBenefits mb=MemberBenefits.getMemberBenefitsByHotelID(Integer.parseInt(request.getParameter("hotelID")));
                mb.setFreeWiFi(newValue);
                if (MemberBenefits.updateMemberBenefits(mb)){
                    return;
                }
                else{
                    out.print("Cannot update free wifi!");
                }
            }
            
            else if (request.getParameter("checkInOrder")!=null){
                int orderID=Integer.parseInt(request.getParameter("checkInOrder"));
                Order.updateStatus(orderID,OrderStatus.STAYING);
                return;
            }
            else if (request.getParameter("checkOutOrder")!=null){
                int orderID=Integer.parseInt(request.getParameter("checkOutOrder"));
                Order.updateStatus(orderID,OrderStatus.COMPLETED);
                //upgrade user
                ArrayList<Order> allOrdersOfThisUser=Order.getAllOrdersByUserID(Order.getOrderByOrderID(orderID).getUserID());
                int completedOrder=0;
                String msg="";
                User u=User.getUserByUserID(Order.getOrderByOrderID(orderID).getUserID());
                for (Order thisOrder:allOrdersOfThisUser){
                    if (thisOrder.getStatus()==OrderStatus.COMPLETED){completedOrder++;}
                }
                if (completedOrder>=2 && completedOrder<5){
                    
                    if (u.getUserType()==2){return;} 
                    u.setUserType(2);
                    if (User.updateProfile(u)){
                        msg="Congratulations! Now you have become a preferred user.";
                    }
                }
                else if(completedOrder>=5 && completedOrder<10){
                    
                    if (u.getUserType()==3){return;} 
                    u.setUserType(3);
                    if (User.updateProfile(u)){
                        msg="Congratulations! Now you have become a gold user. Enjoy!";
                    }
                }
                else if(completedOrder>=10){
                    if (u.getUserType()==4){return;} 
                    u.setUserType(4);
                    if (User.updateProfile(u)){
                        msg="Congratulations! Now you have become our most prestigious plantium user. Enjoy!";
                    }
                }
                ServletContext application = getServletConfig().getServletContext();
                if (!msg.isEmpty()){application.setAttribute(u.getUsername(), msg);}
                return;
            }
            else if (request.getParameter("orderIDToManage")!=null){
                int orderID=Integer.parseInt(request.getParameter("orderIDToManage"));                
                JSONObject obj=new JSONObject();
                Order o=Order.getOrderByOrderID(orderID);
                if (o==null){
                    obj.put("status", "error");
                    obj.put("msg", "Order with this ID cannot be found!");
                    out.print(obj);
                    return;
                }
                int userID=(Integer)session.getAttribute("userID");
                
                boolean isManager=false;
                for (Manager m:Manager.getManagerByUserID(userID)){
                    if (m.getHotelID()==o.getHotelID()){
                        isManager=true;
                    }
                }                
                
                if (!isManager){
                    obj.put("status", "error");
                    obj.put("msg", "This order is not under the hotel you manage!");
                    out.print(obj); 
                    return;
                }
                else {
                    Gson g=new Gson();
                    String orderInfo=g.toJson(o);
                    out.print(orderInfo);
                    return;
                }
            }
            else if (request.getParameter("indexAtArrayListManager")!=null){
                int indexAtArrayListManager=Integer.parseInt(request.getParameter("indexAtArrayListManager"));
                Manager m=Manager.getManagerByUserID((Integer)session.getAttribute("userID")).get(indexAtArrayListManager);
                Hotel h=Hotel.getHotelByID(m.getHotelID());
                Gson g=new Gson();
                String hotelInfo=g.toJson(h);
                ArrayList<HotelRoom> hrs=HotelRoom.getAllRoomsByHotelID(h.getHotelID());
                String discountList=g.toJson(MemberBenefits.getMemberBenefitsByHotelID(h.getHotelID()));
                String roomInfo=g.toJson(hrs,ArrayList.class);
                response.setContentType("application/json");
                String finalString="{\"hotelInfo\":"+hotelInfo+",\"roomInfo\":"+roomInfo+",\"discountList\":"+discountList+"}";
                out.print(finalString);
            }
            else if(request.getParameter("pk")!=null){
                int hotelID, roomType;
                if (request.getParameter("pk").indexOf("_")!=-1){
                    hotelID=Integer.parseInt(request.getParameter("pk").split("_")[0]);
                    roomType=Integer.parseInt(request.getParameter("pk").split("_")[1]);
                    JSONObject obj=new JSONObject();
                    
                    if (request.getParameter("name")==null){
                        obj.put("status", "error");
                        obj.put("msg", "name must be supplied!");
                        out.print(obj);
                    }
                    else {
                        
                        String command=request.getParameter("name");
                        String result=request.getParameter("value");
                        HotelRoom r;
                        if (roomType==0){
                            //TODO: how to insert a new room?
                            r=new HotelRoom(hotelID,roomType,"");
                        }
                        else {
                           r=HotelRoom.getHotelRoom(hotelID,roomType);
                        }
                        
                        if (command.indexOf("Name")!=-1){
                            r.setRoomName(result);   
                        }
                        else if(command.indexOf("Size")!=-1){
                            r.setRoomSize(Integer.parseInt(result));   
                        }
                        
                        else if(command.indexOf("Rate")!=-1){
                            r.setStandardRate(Integer.parseInt(result));
                            
                        }
                        else if(command.indexOf("numOfRoom")!=-1){
                           
                            r.setNumOfRoom(Integer.parseInt(result));
                            //check if valid
                        }
                        else if(command.indexOf("discount")!=-1){
                            MemberBenefits mb = MemberBenefits.getMemberBenefitsByHotelID(hotelID);  
                            MemberBenefits newMB;
                            if (command.indexOf("commonUser")!=-1){
                                newMB=new MemberBenefits(mb.getHotelID(),Integer.parseInt(result),-1,-1,-1,-1,-1,-1,-1);
                            }
                            else if (command.indexOf("preferredUser")!=-1){
                                newMB=new MemberBenefits(mb.getHotelID(),-1,Integer.parseInt(result),-1,-1,-1,-1,-1,-1);
                            }
                            else if (command.indexOf("goldUser")!=-1){
                                newMB=new MemberBenefits(mb.getHotelID(),-1,-1,Integer.parseInt(result),-1,-1,-1,-1,-1);
                            }
                            else {
                                newMB=new MemberBenefits(mb.getHotelID(),-1,-1,-1,Integer.parseInt(result),-1,-1,-1,-1);
                            }
                            if (!MemberBenefits.updateMemberBenefits(newMB)){
                                obj.put("status", "error");
                                obj.put("msg", "Cannot update member benefit!");
                            }
                        }
                        if (roomType==0){
                           if (!r.insertToDatabase()){
                              
                              obj.put("status", "error");
                              obj.put("msg", "Cannot update hotel room!");
                           }
                           else{
                              obj.put("status", "correct");
                              obj.put("msg", "Nice");
                              
                           }
                        }
                        else{
                            if (!HotelRoom.updateRoom(r)){
                               obj.put("status", "error");
                               obj.put("msg", "Cannot update hotel room!");
                               
                            }
                            else{
                               obj.put("status", "correct");
                               obj.put("msg", "Nice");
                            }
                        }  
                        out.print(obj);
                    }
                }
                else {
                    hotelID=Integer.parseInt(request.getParameter("pk"));
                
                    if (request.getParameter("name")==null){
                        JSONObject obj=new JSONObject();
                        obj.put("status", "error");
                        obj.put("msg", "name must be supplied!");
                        out.print(obj);
                    }
                    else {
                        String command=request.getParameter("name");
                        Hotel h=Hotel.getHotelByID(hotelID);
                        JSONObject obj=new JSONObject();
                        if (command.indexOf("Name")!=-1){
                            h.setHotelName(request.getParameter("value"));
                        }
                        else if (command.indexOf("address")!=-1){
                            h.setAddress(request.getParameter("value"));
                        }
                        else if(command.indexOf("intro")!=-1){
                            h.setIntro(request.getParameter("value"));
                        }
                        else if (command.indexOf("isRecommended")!=-1){
                            if (request.getParameter("value").equals("true")){
                                h.setIsRecommended(1);
                            }
                            else{
                                h.setIsRecommended(0);
                            }
                        }
                        if (!Hotel.updateHotel(h)){
                            obj.put("status", "error");
                            obj.put("msg", "Cannot update hotel!");
                            out.print(obj);
                        }
                        else{
                        obj.put("status", "correct");
                        obj.put("msg", "Nice");
                        out.print(obj);
                        }
                    }
                }
            }
            else{
                session.setAttribute("managers",Manager.getManagerByUserID((Integer)session.getAttribute("userID")));
                response.sendRedirect("manageHotel.jsp");
            }
        } finally {
            out.close();
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
