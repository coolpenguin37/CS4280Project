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
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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
            if (username=="" || username==null || !validateUsername(username)) {
                String result = "Username is not valid. It should contain only uppercase, lowercase or number. The length should be at least 6 characters";
                request.setAttribute("result", result);
                RequestDispatcher disp1 = request.getRequestDispatcher("newAccount.jsp");
               disp1.forward(request, response);
               return;
            }
            String password=request.getParameter("password");
            if (password=="" || password==null || !validatePassword(password)) {
                String result = "Password is not valid. It should contain at least one uppercase, at least one lowercase and at least one number. "
                        + "The lengths should be at least 6 characters. Please check";
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
            }
            else{
                if (validateTel(request.getParameter("telephone"))) 
                    {telephone=request.getParameter("telephone");}
                else {
                    String result = "Telephone is not valid. It should contain only numbers. Please check";
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
                
                if (validateEmail(request.getParameter("email"))) 
                    {email=request.getParameter("email");}
                else {
                    String result = "Email is not valid. Please check";
                    request.setAttribute("result", result);
                    RequestDispatcher disp1 = request.getRequestDispatcher("newAccount.jsp");
                    disp1.forward(request, response);
                    return;
                }
            }
            
            if (request.getParameter("subscribe")==null){
                isSubscribed=0;
            }
            else {
                isSubscribed=1;
            }
            
            try {
                password=PasswordHash.hash(password);
            }
            catch (NoSuchAlgorithmException e) {
            }
            
            User u=new User(username,password,name,email,telephone,isSubscribed,1);
            if (u.insertToDatabase()) {
            out.println("<p>Success!</p>");
            out.println("<a href=" + request.getContextPath()+">Return back to previous page</a>");}
            else{
                out.println("<p>Failed...</p>");
                out.println("<a href=" + request.getContextPath()+">Return back to previous page</a>");
            }
            out.println("</body>");
            out.println("</html>");
        } finally {
            out.close();
        }
    }
    
    private boolean validateEmail(String email) {
        
        String email_pattern = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        System.out.println(email);
        Pattern pattern = Pattern.compile(email_pattern);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
         
    }
    //Validate password. The password must contains at least one capital letter, one lowercase letter and one number.
    //It should be at least 6 characters
    private boolean validatePassword(String password) {
        String password_pattern = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{5,}$";
        Pattern pattern = Pattern.compile(password_pattern);
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }
    
    //username can only contain one or more uppercase, lowercase and number.
    //it should be at least 6 characters
    private boolean validateUsername(String username) {
        String username_pattern = "^[a-zA-Z0-9].{5,}$";
        Pattern pattern = Pattern.compile(username_pattern);
        Matcher matcher = pattern.matcher(username);
        return matcher.matches();
    }
    
    //telephone must be one or more numbers
    private boolean validateTel(String telephone) {
        String telephone_pattern = "^[0-9]+$";
        Pattern pattern = Pattern.compile(telephone_pattern);
        Matcher matcher = pattern.matcher(telephone);
        return matcher.matches();    
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
