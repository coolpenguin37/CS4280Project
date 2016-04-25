/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import user.*;
import javax.servlet.RequestDispatcher;

/**
 *
 * @author yanlind
 */
public class ResetPasswordServlet extends HttpServlet {

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
        HttpSession session=request.getSession(true);
        String oldPwd=(String) session.getAttribute("password");
        String input_oldPwd,input_newPwd;
        try{
            input_oldPwd=PasswordHash.hash(request.getParameter("oldPwd"));
            input_newPwd=PasswordHash.hash(request.getParameter("newPwd"));
        }
        catch (Exception e){
            input_oldPwd=request.getParameter("oldPwd");
            input_newPwd=request.getParameter("newPwd");
        }
        if (oldPwd.equals(input_oldPwd)){
            User u=User.getUserByUserID((Integer)session.getAttribute("userID"));
            if (!User.validatePassword(input_newPwd)){
                request.setAttribute("errorMessage",User.PASSWORD_ERROR);
            }
            u.setPassword(input_newPwd);
            if (!User.updateProfile(u)){
                request.setAttribute("errorMessage","Reset password failed....");
            }
            else{
                session.setAttribute("password", input_newPwd);
            }
        }
        else {
            request.setAttribute("errorMessage","Old password is wrong!");
           
        }
        RequestDispatcher rd = request.getRequestDispatcher("updateProfile.jsp");
        rd.forward(request, response);
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
