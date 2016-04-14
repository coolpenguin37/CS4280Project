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
            if (request.getParameter("indexAtArrayListManager")!=null){
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
