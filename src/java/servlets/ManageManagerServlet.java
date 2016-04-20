/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package servlets;

import hotel.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Enumeration;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import manager.*;
import user.*;
import javax.servlet.http.HttpSession;

/**
 *
 * @author yduan7
 */
public class ManageManagerServlet extends HttpServlet {

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
        HttpSession session=request.getSession(true);
        
        if(request.getParameter("Update!")!=null){
            ArrayList<Manager> m=(ArrayList<Manager>)session.getAttribute("managerList");
            Enumeration e=request.getParameterNames();
            while(e.hasMoreElements()){
                String hotelIDInString=(String) e.nextElement();
                int hotelID=-1;
                try{
                    hotelID=Integer.parseInt(hotelIDInString);
                }
                catch (NumberFormatException e2){
                    continue;
                }
                finally{
                    if (Manager.managerExist(m.get(0).getUserID(),hotelID)){
                        //it is already in
                        if (request.getParameter(hotelIDInString).isEmpty()){
                            //not a manager any more
                            Manager.removeManager(m.get(0).getUserID(), hotelID);
                        }
                    }
                    else{
                        if (!request.getParameter(hotelIDInString).isEmpty() && hotelID!=-1){
                            //it is not already in, but now it is a manager-->add it to db
                            Manager newManager=new Manager(-1,m.get(0).getUserID(),hotelID);
                            newManager.insertToDatabase();
                        }
                    }
                }
            }
        }
        
        //test whether it is username or userid
        ArrayList<Manager> m;
        try{
            m=Manager.getManagerByUserID(Integer.parseInt(request.getParameter("idOrName")));
        }
        catch (NumberFormatException e){
            //it is username not userid
            m=Manager.getManagerByUsername(request.getParameter("idOrName"));
        }
        if (m!=null){
            session.setAttribute("managerList", m);
        }
        else{
            ArrayList<Manager> alreadyHaveManagerList=(ArrayList<Manager>)session.getAttribute("managerList");
            session.setAttribute("managerList", Manager.getManagerByUserID(alreadyHaveManagerList.get(0).getUserID()));
        }
        session.setAttribute("allHotel",Hotel.getAllHotel());
        response.sendRedirect(request.getContextPath()+"/showManager.jsp");
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
