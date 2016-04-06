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
import user.*;
import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpSession;

/**
 *
 * @author siruzhang2
 */
public class LoginServlet extends HttpServlet{

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
        
        HttpSession session = request.getSession(true);
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        
        try {
                
                Login log = new Login(username, password);
                User user = log.login();
                if(user!= null){
                    int userID = user.getUserID();
                    String userName = user.getUsername();
                    String name = user.getName();
                    String userEmail = user.getEmail();
                    String userTel = user.getTel();
                    String userPwd = user.getPassword();
                    int isSubscribed = user.getIsSubscribed();
                    int userType = user.getUserType();
                    session = request.getSession(true);
                    session.setAttribute("userID", userID);
                    session.setAttribute("username", userName);
                    session.setAttribute("name", name);
                    session.setAttribute("userEmail", userEmail);
                    session.setAttribute("userTel",userTel);
                    session.setAttribute("isSubscribed", isSubscribed);
                    session.setAttribute("type", userType);
                    response.sendRedirect("index.jsp");
                }
                
            } 
        
        catch (LoginException e) {
                String result = e.getMessage();
                request.setAttribute("result", result);
                RequestDispatcher disp1 = request.getRequestDispatcher("userLogin.jsp");
                disp1.forward(request, response);
            } 
        catch (IOException e) {           
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
