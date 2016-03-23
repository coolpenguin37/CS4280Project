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
import user.*;
import database.*;
import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpSession;

/**
 *
 * @author siruzhang2
 */
public class LoginServlet extends HttpServlet implements MySQLInit {

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
        PrintWriter out = response.getWriter();
        
        HttpSession session = request.getSession(false);
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        
        try {
                Login log = new Login(username, password);
                User user = log.login();
                if(user!= null){
                    String userName = user.getUsername();
                    String name = user.getName();
                    String userEmail = user.getEmail();
                    String userTel = user.getTel();
                    String userPwd = user.getPassword();
                    int isSubscribed = user.getIsSubscribed();
                    int userType = user.getUserType();
                    session = request.getSession(true);
                    session.setAttribute("username", userName);
                    session.setAttribute("name", name);
                    session.setAttribute("email", userEmail);
                    session.setAttribute("tel",userTel);
                    session.setAttribute("isSubscribed", isSubscribed);
                    session.setAttribute("type", userType);
                    response.sendRedirect("index.jsp");
                }else{
                    String result = "Incorrect username or password, please try again.";
                    request.setAttribute("result", result);
                    RequestDispatcher disp1 = request.getRequestDispatcher("userLogin.jsp");
                    disp1.forward(request, response);

                }
            } catch (Exception e) {
                e.printStackTrace();
            }           
        } 
    }
