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
import manager.*;
import java.util.ArrayList;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import hotel.*;
import java.util.Set;
import org.json.simple.*;
import order.*;
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
            if (request.getParameter("orderIDToManage")!=null){
                int orderID=Integer.parseInt(request.getParameter("orderIDToManage"));
                JSONObject obj=new JSONObject();
                Order o=Order.getOrderByOrderID(orderID);
                if (o==null){
                    obj.put("status", "error");
                    obj.put("msg", "Order with this ID cannot be found!");
                    out.print(obj);
                }
                else {
                    obj.put("status", "error");
                    obj.put("msg", "Order with this ID cannot be found!");
                    out.print(obj);
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
//                        else if(command.indexOf("discount")!=-1){
//                            MemberBenefits mb=MemberBenefits.getMemberBenefitsByHotelID(hotelID);
//                            //update discount
//                        }
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
                        if (command.indexOf("Name")!=-1){
                            h.setHotelName(request.getParameter("value"));
                        }
                        else if (command.indexOf("address")!=-1){
                            h.setAddress(request.getParameter("value"));
                        }
    //                    else if(command.indexOf("infomration")!=-1){
    //                        h.setInformation(request.getParameter("value"));
    //                    }
                        else if (command.indexOf("isRecommended")!=-1){
                            if (request.getParameter("value").equals("true")){
                                h.setIsRecommended(1);
                            }
                            else{
                                h.setIsRecommended(0);
                            }
                        }
                        if (!Hotel.updateHotel(h)){
                            JSONObject obj=new JSONObject();
                            obj.put("status", "error");
                            obj.put("msg", "Cannot update hotel!");
                            out.print(obj);
                        }
                        else{
                        JSONObject obj=new JSONObject();
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
