/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import user.*;

/**
 *
 * @author Lin Jianxiong
 */
public class UpdateProfileServlet extends HttpServlet {

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
            out.println("<title>Servlet UpdateProfileServlet</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet UpdateProfileServlet at " + request.getContextPath() + "</h1>");
            
            String username = request.getParameter("username");
            String oldPassword = request.getParameter("oldPassword");
            String newPassword = request.getParameter("newPassword");
            String tel = request.getParameter("tel");
            String email = request.getParameter("email");
            int isSubscribed = 0;
            if (request.getParameter("isSubscribed")==null) {
                isSubscribed =0 ;
            }
            else {
                isSubscribed = 1;
            }

            if (oldPassword == null || oldPassword.isEmpty()) {
                String result = "Incorrect Password";
                request.setAttribute("result", result);
                RequestDispatcher disp1 = request.getRequestDispatcher("MemberInfo.jsp");
                disp1.forward(request, response);
                return;
            }

            try {
                Login l = new Login(username, oldPassword);
                User user = l.login();
                if (user != null) {
                    if (newPassword != null && !newPassword.isEmpty() && !validatePassword(newPassword)) {
                        String result = "Invalid New Password.";
                        request.setAttribute("result", result);
                        RequestDispatcher disp1 = request.getRequestDispatcher("MemberInfo.jsp");
                        disp1.forward(request, response);
                        return;
                    }

                    if (tel != null && !tel.isEmpty() && !validateTel(tel)) {
                        String result = "Invalid Tel.";
                        request.setAttribute("result", result);
                        RequestDispatcher disp1 = request.getRequestDispatcher("MemberInfo.jsp");
                        disp1.forward(request, response);
                        return;
                    }

                    if (email != null && !email.isEmpty() && !validateEmail(email)) {
                        String result = "Invalid Email.";
                        request.setAttribute("result", result);
                        RequestDispatcher disp1 = request.getRequestDispatcher("MemberInfo.jsp");
                        disp1.forward(request, response);
                        return;
                    }

                    //TODOj
                    User temp = new User(user.getUserID(), )
                }
            } catch (LoginException e) {
                String result = e.getMessage();
                request.setAttribute("result", result);
                RequestDispatcher disp1 = request.getRequestDispatcher("MemberInfo.jsp");
                disp1.forward(request, response);
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

    private boolean validatePassword(String password) {
        String password_pattern = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{5,}$";
        Pattern pattern = Pattern.compile(password_pattern);
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }
    
    private boolean validateEmail(String email) {
        String email_pattern = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        System.out.println(email);
        Pattern pattern = Pattern.compile(email_pattern);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
         
    }

    private boolean validateTel(String telephone) {
        String telephone_pattern = "^[0-9]+$";
        Pattern pattern = Pattern.compile(telephone_pattern);
        Matcher matcher = pattern.matcher(telephone);
        return matcher.matches();    
    }    
}
