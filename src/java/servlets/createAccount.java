package servlets;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */



import java.io.IOException;
import java.io.PrintWriter;
import java.lang.Object;
import java.security.NoSuchAlgorithmException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import user.*;

/**
 *
 * @author yduan7
 */
public class createAccount extends HttpServlet {

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
            String password=request.getParameter("password");
            String name, telephone, email;
            int isSubscribed;
            if (request.getParameter("name")==null){
                name="";
            }
            else{
                name=request.getParameter("name");
            }
            if (request.getParameter("telephone")==null){
                telephone="";
            }
            else{
                telephone=request.getParameter("telephone");
            }
            if (request.getParameter("email")==null){
                email="";
            }
            else{
                email=request.getParameter("email");
            }
            
            if (request.getParameter("subscribe")==null){
                isSubscribed=0;
            }
            else {
                isSubscribed=1;
            }
            
            try {
                password=PasswordHash.hash(password);}
            catch (NoSuchAlgorithmException e) {}
            
            User u=new User(username,password,name,email,telephone,isSubscribed,1);
            if (u.insertToDatabase()) {
            out.println("<p>Success!</p>");
            out.println("<a href=" + request.getContextPath()+">Return back to previous page</a>");}
            else{
            out.println("<p>Failed...</p>");
            out.println("<a href=" + request.getContextPath()+">Return back to previous page</a>");} 
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
