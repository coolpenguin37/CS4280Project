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
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import order.*;
import hotel.*;
import user.*;
import java.util.ArrayList;
/**
 *
 * @author yanlind
 */
public class CheckOutRoom extends HttpServlet {

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
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet CheckOutRoom</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Check out room</h1>");
            out.println("<form method='Get' action='self'>");
            out.println("<label>Order ID:</label>");
            out.println("<input type='text' name='orderID'>");
            out.println("<span>*</span>");
            out.println("<br>");
            
            out.println("<label>User Name:</label>");
            out.println("<input type='text' name='username'>");
            out.println("<span>**</span>");
            out.println("<br>");
            
            out.println("<label>Hotel Name:</label>");
            out.println("<input type='text' name='hotelName'>");
            out.println("<span>**</span>");
            out.println("<br>");
            
            out.println("<label>Check In Time:</label>");
            out.println("<input type='date' name='ciDate'>");
            out.println("<br>");
            
            out.println("<label>Check Out Time:</label>");
            out.println("<input type='date' name='coDate'>");
            out.println("<br>");
            
            out.println("<label>Room type:</label>");
            out.println("<input type='text' name='roomType'>");
            out.println("<br>");
            
            out.println("<input type='submit' name='Search'>");
            out.println("<input type='reset' name='Clear'>");
            out.println("</form>");
            
            if (request.getMethod()!=null && !request.getMethod().isEmpty() && request.getMethod().equals("GET")) {
                if (request.getParameter("orderID")==null && request.getParameter("username")==null){
                    out.println("<p> Either 1) supply order ID or 2) supply username!</p>");
                }
                else if (request.getParameter("orderID")!=null){
                    Order o=Order.getOrderByOrderID(Integer.parseInt(request.getParameter("orderID")));
                    out.println("<div>");
                    out.println("<span> Name:"+User.getUserByUserID(o.getUserID()).getName()+"</span>");
                    int hotelID= o.getHotelID();
                    Hotel h=Hotel.getHotelByID(hotelID);
                    out.println("<span> Hotel Name:"+h.getHotelName()+"</span>");
                    out.println("<span> Room Name:"+HotelRoom.getHotelRoom(hotelID,o.getRoomType()).getRoomName()+"</span>");
                    out.println("<span> Check In Date:"+o.getCIDate().toString()+"</span>");
                    out.println("<span> Check Out Date:"+o.getCODate().toString()+"</span>");
                    HttpSession session=request.getSession(true);
                    session.setAttribute("order",o);
                    out.println("<button type='submit' name='checkout' value='checkout'>Check Out</button>");
                    out.println("</div>");
                }
                else if (request.getParameter("username")!=null){
                    ArrayList <Order> orders=Order.getAllOrdersByUserID(Integer.parseInt(request.getParameter("username")));
                    if (orders.isEmpty()) { out.println("No order is found!");}
                    else {
                        String hotelName, ciDate, coDate, roomType;
                        if (request.getParameter("hotelName")!=null && !request.getParameter("hotelName").isEmpty()){
                                hotelName=request.getParameter("hotelName");
                        }
                        if (request.getParameter("ciDate")!=null && !request.getParameter("ciDate").isEmpty()){
                                ciDate=request.getParameter("ciDate");
                        }
                        if (request.getParameter("coDate")!=null && !request.getParameter("coDate").isEmpty()){
                                coDate=request.getParameter("coDate");
                        }
                        if (request.getParameter("roomType")!=null && !request.getParameter("roomType").isEmpty()){
                                roomType=request.getParameter("roomType");
                        }
                        //TODO: duan
                        for (Order o:orders){
                            if (hotelName!=null && !hotelName.isEmpty()) {
                                Hotel.getHotelByID(o.getHotelID()).getHotelName();
                            }
                        }
                    }
                }
            }
            out.println("</body>");
            out.println("</html>");
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
