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
import java.sql.*;
import database.*;

/**
 *
 * @author Jianxiong Lin
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
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet LoginServlet</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>zzServlet LoginServlet at " + request.getContextPath() + "</h1>");
            Connection conn = null;
            Statement stmt = null;
            ResultSet rs = null;

            try {
                Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            } catch (Exception e) {
                e.printStackTrace();
            }
            out.println("<h1>ttServlet LoginServlet at " + request.getContextPath() + "</h1>");

            try {
                conn = DriverManager.getConnection(SQLHost, SQLID, SQLPassword);
                stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                String stmp = new String("SELECT * FROM [User]");
                rs = stmt.executeQuery("SELECT * FROM [User]");
                out.println("<h1>ttServlet LoginServlet at " + request.getContextPath() + "</h1>");

                int numRow = 0;
                out.println("<p>Total " + numRow + " entries.</p>");
                
                if (rs != null && rs.last() != false) {                 
                    numRow = rs.getRow();
                    rs.beforeFirst();
                }
                out.println("<p>Total " + numRow + " entries.</p>");
                while (rs != null && rs.next() != false) {
                    String name = rs.getString("Name");
                    String email = rs.getString("Email");
                    out.println("<h2>" + name + " " + email +  "</h2>");
                    //System.out.println(name + "\n" + email + "\n");
                }
            } catch (Exception e) {
                e.printStackTrace();
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
