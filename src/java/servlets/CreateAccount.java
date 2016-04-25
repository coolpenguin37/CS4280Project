package servlets;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */



import java.io.IOException;
import java.io.PrintWriter;
import java.security.NoSuchAlgorithmException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import user.*;
import javax.servlet.RequestDispatcher;

/**
 *
 * @author yduan7
 */
public class CreateAccount extends HttpServlet {

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
            out.println("<title>Servlet createAccount</title>");            
            out.println("</head>");
            out.println("<body>");
            String username=request.getParameter("username");
            if (username==null || username.isEmpty() || !User.validateUsername(username)) {
                String result = User.USERNAME_ERROR;
                request.setAttribute("result", result);
                RequestDispatcher disp1 = request.getRequestDispatcher("newAccount.jsp");
                disp1.forward(request, response);
                return;
            }
            String password=request.getParameter("password");
            if (password==null || password.isEmpty() || !User.validatePassword(password)) {
                String result = User.PASSWORD_ERROR;
                request.setAttribute("result", result);
                RequestDispatcher disp1 = request.getRequestDispatcher("newAccount.jsp");
                disp1.forward(request, response);
                return;
            }
            String name, telephone = null, email = null;
            int isSubscribed;
            if (request.getParameter("name")==null){
                name="";
            }
            else{
                name=request.getParameter("name");               
            }
            if (request.getParameter("telephone")==null){
                telephone="";
            } else{
                if (User.validateTel(request.getParameter("telephone"))) {
                    telephone=request.getParameter("telephone");
                } else {
                    String result = User.TEL_ERROR;
                    request.setAttribute("result", result);
                    RequestDispatcher disp1 = request.getRequestDispatcher("newAccount.jsp");
                    disp1.forward(request, response);
                    return;
                }
                
            }
            if (request.getParameter("email")==null){
                email="";
            }
            else{
                
                if (User.validateEmail(request.getParameter("email"))) 
                    {email=request.getParameter("email");}
                else {
                    String result =User.EMAIL_ERROR;
                    request.setAttribute("result", result);
                    RequestDispatcher disp1 = request.getRequestDispatcher("newAccount.jsp");
                    disp1.forward(request, response);
                    return;
                }
            }
            
            if (request.getParameter("subscribe")==null){
                isSubscribed=0;
            } else {
                isSubscribed=1;
            }
            
            try {
                password=PasswordHash.hash(password);
            } catch (NoSuchAlgorithmException e) {
            }
            
            User u=new User(username,password,name,email,telephone,isSubscribed,1);
            if (u.insertToDatabase()) {
                out.println("<p>Success!</p>");
                out.println("<a href=" + request.getContextPath()+">Return back to previous page</a>");
            } else {
                out.println("<p>Failed...</p>");
                out.println("<a href=" + request.getContextPath()+">Return back to previous page</a>");
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
