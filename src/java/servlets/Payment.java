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
import hotel.*;
import org.joda.time.Days;
import org.joda.time.LocalDate;
import order.*;
import org.joda.time.DateTime;
import user.*;
/**
 *
 * @author yduan7
 */
public class Payment extends HttpServlet {

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
                out.println("<title>Payment</title>");            
                out.println("</head>");
                out.println("<body>");
                HttpSession session=request.getSession();
                Order o=(Order)session.getAttribute("orderToPay");
                DateTime dtCIDate = new DateTime(o.getCIDate());
                DateTime dtCODate = new DateTime(o.getCODate());
                int numDays = Days.daysBetween(new LocalDate(dtCIDate), new LocalDate(dtCODate)).getDays();
                HotelRoom rm = HotelRoom.getHotelRoom(o.getHotelID(),o.getRoomType());
                int standardRate=rm.getStandardRate();
                int discount=100;
                int realRate = (int) Math.ceil(standardRate * (discount / 100.0)); 
                int numRooms=o.getNumOfRoom();
                User u=User.getUserByUserID((Integer)session.getAttribute("userID"));
                
                out.println("<form action='confirm.jsp' method='POST'>");
                out.println("<fieldset>");
                out.println("<legend>Personal Information</legend>");
                out.println("<label>Title:</label>");
                out.println("<input type='text' name='clientTitle'>");
                out.println("<label>Name:</label>");
                out.println("<input type='text' name='clientName' value="+u.getName()+">");
                out.println("<label>E-mail Address:</label>");
                out.println("<input type='text' name='clientEmail' value="+u.getEmail()+")>");
                out.println("<label>Retype Your E-mail Address:</label>");
                out.println("<input type='text' name='clientRetypeEmail'>");
                out.println("<label>Phone:</label>");
                out.println("<input type='text' name='clientPhone' value="+u.getTel()+")>");
                out.println("</fieldset>");
                out.println("<h2>The total payment amount is: "+(realRate*numRooms*numDays)+"</h2>");
                out.println("<fieldset>");
                out.println("<legend>Credit Card Information</legend>");
                out.println("<label>Credit Card Number:</label>");
                out.println("<input type='text' name='cardNum'>");
                out.println("<br><label>Valid until:</label>");
                out.println("<input type='text' name='validUntil'>");
                out.println("<br><label>Cardholder Name:</label>");
                out.println("<input type='text' name='cardholderName'>");
                out.println("<br><label>CVV2:</label>");
                out.println("<input type='text' name='cvv2'>");
                out.println("<br><input type='submit' name='Submit' value='Submit'>");
                out.println("</fieldset>");
                out.println("</body>");
                out.println("</html>");
        }
        catch (Exception e){}
        finally {
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
