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
import hotel.*;
import javax.servlet.RequestDispatcher;

/**
 *
 * @author Lin Jianxiong
 */
public class CreateHotelServlet extends HttpServlet {

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
            out.println("<title>Servlet CreateHotelServlet</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet CreateHotelServlet at " + request.getContextPath() + "</h1>");

            String hotelName = request.getParameter("hotelName");
            String address = request.getParameter("address");
            int isRecommended;
            if (request.getParameter("isRecommended")==null) {
                isRecommended = 0;
            }
            else {
                isRecommended = 1;
            }
            int starRating = Integer.parseInt(request.getParameter("starRating"));
            String label = request.getParameter("label");
            String intro = request.getParameter("intro");

            if (hotelName == null || hotelName.isEmpty()) {
                String result = "Invalid hotel name.";
                request.setAttribute("result", result);
                RequestDispatcher disp = request.getRequestDispatcher("CreateHotel.jsp");
                disp.forward(request, response);
                return;
            }

            if (address == null || address.isEmpty()) {
                String result = "Invalid hotel address.";
                request.setAttribute("result", result);
                RequestDispatcher disp = request.getRequestDispatcher("CreateHotel.jsp");
                disp.forward(request, response);
                return;
            }

            if (!validateInput(hotelName, address)) {
                String result = "The hotel already exists";
                request.setAttribute("result", result);
                RequestDispatcher disp = request.getRequestDispatcher("CreateHotel.jsp");
                disp.forward(request, response);
                return;                
            }
            
            Hotel h = new Hotel(hotelName, address, isRecommended, starRating, processLabel(label), intro);
            if (h.insertToDatabase()) {
                out.println("<p>Yes!<p/>");
            } else {
                out.println("<p>No!<p/>");
            }
            out.println("</html>");
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

    private String processLabel(String label) {
        return label;
    }

    private boolean validateInput(String hotelName, String address) {
        if (Hotel.hotelExist(hotelName, address)) {
            return false;
        }
        return true;
    }
}
