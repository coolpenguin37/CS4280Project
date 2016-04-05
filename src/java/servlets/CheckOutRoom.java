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
import order.*;
import hotel.*;
import user.*;
import java.util.ArrayList;
import java.text.*;
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
            
            out.println("<label>Client's Name:</label>");
            out.println("<input type='text' name='name'>");
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
            
            out.println("<label>Room name:</label>");
            out.println("<input type='text' name='roomName'>");
            out.println("<br>");
            
            out.println("<input type='submit' name='Search' value='Search'>");
            out.println("<input type='reset' name='Clear'>");
            out.println("</form>");
            
            if (request.getMethod()!=null && !request.getMethod().isEmpty() && request.getMethod().equals("GET")) {
                if (request.getParameter("Search")!=null && !request.getParameter("Search").isEmpty() && request.getParameter("Search")=="Search"){
                
                    if (request.getParameter("orderID")!=null){
                        Order o=Order.getOrderByOrderID(Integer.parseInt(request.getParameter("orderID")));
                        out.println("<div>");
                        out.println("<span> Name:"+User.getUserByUserID(o.getUserID()).getName()+"</span>");
                        int hotelID= o.getHotelID();
                        Hotel h=Hotel.getHotelByID(hotelID);
                        out.println("<span> Hotel Name:"+h.getHotelName()+"</span>");
                        out.println("<span> Room Name:"+HotelRoom.getHotelRoom(hotelID,o.getRoomType()).getRoomName()+"</span>");
                        out.println("<span> Check In Date:"+o.getCIDate().toString()+"</span>");
                        out.println("<span> Check Out Date:"+o.getCODate().toString()+"</span>");
                        out.println("<button type='submit' name='checkout' value='checkout'"+Integer.toString(o.getOrderID())+">Check Out</button>");
                        out.println("</div>");
                    }
                    else if (request.getParameter("name")!=null && request.getParameter("hotelName")!=null){
                        boolean hasOutput=false;
                        ArrayList<Order> orders=Order.getOrderList(request.getParameter("hotelName"),request.getParameter("name"));
                        for (Order o:orders){
                            //if ciDate, coDate and roomName are supplied, use to cross-check.
                            //be careful about the format of the date after to String.
                            //use DateFormat comparison first; if not okay, do string comparison
                            if (request.getParameter("ciDate")!=null && !request.getParameter("ciDate").isEmpty()){

                                try {
                                    DateFormat df = DateFormat.getDateInstance();
                                    DateFormat df2 = DateFormat.getDateInstance();
                                    df.parse(request.getParameter("ciDate"));
                                    df2.format(o.getCIDate());
                                    if (!df2.equals(df)){
                                    continue;
                                    }
                                }
                                catch (ParseException e) {
                                    if (!o.getCIDate().toString().equals(request.getParameter("ciDate"))){
                                        continue;
                                    }
                                }


                            }
                            if (request.getParameter("coDate")!=null && !request.getParameter("coDate").isEmpty()){

                                try {
                                    DateFormat df = DateFormat.getDateInstance();
                                    DateFormat df2 = DateFormat.getDateInstance();
                                    df.parse(request.getParameter("coDate"));
                                    df2.format(o.getCODate());
                                    if (!df2.equals(df)){
                                    continue;
                                    }
                                }
                                catch (ParseException e) {
                                    if (!o.getCODate().toString().equals(request.getParameter("coDate"))){
                                        continue;
                                    }
                                }


                            }

                            if (request.getParameter("roomName")!=null && !request.getParameter("roomName").isEmpty()){
                                if (!HotelRoom.getHotelRoom(o.getHotelID(),o.getRoomType()).getRoomName().equals(request.getParameter("roomName"))){
                                    continue;
                                }
                            }
                            hasOutput=true;
                            out.println("<div>");
                            out.println("<span> Name:"+User.getUserByUserID(o.getUserID()).getName()+"</span>");
                            int hotelID= o.getHotelID();
                            Hotel h=Hotel.getHotelByID(hotelID);
                            out.println("<span> Hotel Name:"+h.getHotelName()+"</span>");
                            out.println("<span> Room Name:"+HotelRoom.getHotelRoom(hotelID,o.getRoomType()).getRoomName()+"</span>");
                            out.println("<span> Check In Date:"+o.getCIDate().toString()+"</span>");
                            out.println("<span> Check Out Date:"+o.getCODate().toString()+"</span>");
                            out.println("<button type='submit' name='checkout' value='checkout'"+Integer.toString(o.getOrderID())+">Check Out</button>");
                            out.println("</div>");
                        }
                        if (hasOutput==false) {
                            out.println("<p>No order record is found!</p>");
                        }
                    }
                    else {
                        out.println("<p>Either 1)Order ID or 2)Hotel name & Client Name must be supplied in order to search!</p>");
                    }
                }
                else if (request.getParameter("checkout")!=null && !request.getParameter("checkout").isEmpty()){
                    //get orderID;
                    int checkout_orderID=Integer.parseInt(request.getParameter("checkout").substring(8));
                    Order.updateStatus(checkout_orderID, OrderStatus.COMPLETED);
                    out.println("<span>Successfully Checkout!</span>");
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
