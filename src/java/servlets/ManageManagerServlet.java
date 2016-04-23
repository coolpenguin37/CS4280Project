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
        String msg="";
        processRequest(request, response);
        HttpSession session=request.getSession(true);
        if (request.getParameter("add")!=null){
            User u=User.getUserByUsername(request.getParameter("add"));
            u.setUserType(10);
            if (User.updateProfile(u)){
                session.setAttribute("managerAdded",u);
                response.sendRedirect(request.getContextPath()+"/showManager.jsp");
                msg="Please specify hotels that this user will manage.";
                return;
            }
            else{
                msg="Add manager unsuccessfully...";
            }
            
        }
        if(request.getParameter("update")!=null){
            Manager m;
            if (session.getAttribute("managerList")==null){
                m=null;
            }
            else{
                m=((ArrayList<Manager>)session.getAttribute("managerList")).get(0);
            }
            if (m==null){m=new Manager(-1,((User)session.getAttribute("managerAdded")).getUserID(),-1);}
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
                    if (Manager.managerExist(m.getUserID(),hotelID)){
                        //it is already in
                        if (request.getParameter(hotelIDInString).isEmpty()){
                            //not a manager any more
                            Manager.removeManager(m.getUserID(), hotelID);
                        }
                    }
                    else{
                        if (!request.getParameter(hotelIDInString).isEmpty() && hotelID!=-1){
                            //it is not already in, but now it is a manager-->add it to db
                            Manager newManager=new Manager(-1,m.getUserID(),hotelID);
                            newManager.insertToDatabase();
                        }
                    }
                    msg="Manager updated successfully!";
                }
            }
        }
        
        //test whether it is username or userid
        ArrayList<Manager> ms;
        User u=null;
        String idOrName="";
        if (request.getParameter("idOrName")!=null){
            idOrName=request.getParameter("idOrName");
            session.setAttribute("allHotel",Hotel.getAllHotel());
            u=getUserByUserIDorName(idOrName);
        }
        else if (session.getAttribute("managerList")!=null){
            u=User.getUserByUserID(((ArrayList<Manager>)session.getAttribute("managerList")).get(0).getUserID());
        }
        
        if (u==null){
            session.setAttribute("noSuchManager",idOrName);
            response.sendRedirect(request.getContextPath()+"/noSuchUser.jsp");
        }
        else{
            ms=Manager.getManagerByUserID(u.getUserID());
            if (ms.size()!=0){
                session.setAttribute("managerList", ms);
            }
            else{
                if (session.getAttribute("managerList")!=null){
                    ArrayList<Manager> alreadyHaveManagerList=(ArrayList<Manager>)session.getAttribute("managerList");
                    session.setAttribute("managerList", Manager.getManagerByUserID(alreadyHaveManagerList.get(0).getUserID()));
                }
                else{
                    session.setAttribute("managerToAdd",u.getUsername());
                    response.sendRedirect(request.getContextPath()+"/addManager.jsp");
                    return;
                }
            }
            if (!msg.isEmpty()){
                response.sendRedirect(request.getContextPath()+"/showManager.jsp?msg="+msg);
            }
            else{
                response.sendRedirect(request.getContextPath()+"/showManager.jsp");
            }
            
        }
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
    
    private User getUserByUserIDorName(String idOrName){
        User u;
        try{
            u=User.getUserByUserID(Integer.parseInt(idOrName));
        }
        catch (NumberFormatException e){
            //it is username not userid
            u=User.getUserByUsername(idOrName);
        }
        return u;
    }

}
