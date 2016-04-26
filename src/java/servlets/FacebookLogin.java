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
import facebook4j.*;
import facebook4j.auth.AccessToken;
import java.net.HttpURLConnection;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;
import com.google.gson.*;
import java.io.InputStream;
import java.io.InputStreamReader;
import user.*;
import javax.servlet.http.HttpSession;


/**
 *
 * @author yanlind
 */
public class FacebookLogin extends HttpServlet {

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
        HttpSession session = request.getSession(true);
        String code=request.getParameter("code");
        String appID="622571284564354";
        String appSecret="424fe43aed12780cb476cd5a41c04f5f";
        String url = "https://graph.facebook.com/v2.3/oauth/access_token?client_id="+appID+"&redirect_uri=http://localhost:52180/CS4280Project/FacebookLogin&client_secret="+appSecret+"&code="+code;
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        // optional default is GET
        con.setRequestMethod("GET");
        
        JsonParser jp = new JsonParser(); //from gson
        JsonElement root = jp.parse(new InputStreamReader(con.getInputStream())); //Convert the input stream to a json element
        JsonObject rootobj = root.getAsJsonObject(); //May be an array, may be an object. 
        String token=rootobj.get("access_token").getAsString();
        Facebook facebook = new FacebookFactory().getInstance();
        facebook.setOAuthAppId("", "");
        facebook.setOAuthAccessToken(new AccessToken(token, null));
        
        String id="";
        String fbName="";
        try{
            
            id=facebook.getId();
            fbName=facebook.getName();
        }
        catch (Exception e){
        }
        user.User u;
        if (user.User.usernameExist(id)){
            u=user.User.getUserByUsername(id);
        }
        else {
            u=new user.User(id, id, fbName,"", "", 0, 1);
            if (u.insertToDatabase()){
                u=user.User.getUserByUsername(id);
            }
        }
        if (u!=null){
            int userID = u.getUserID();
            String userName = u.getUsername();
            String name = u.getName();
            String userEmail = u.getEmail();
            String userTel = u.getTel();
            int isSubscribed = u.getIsSubscribed();
            int userType = u.getUserType();
            session.setAttribute("password", id);
            session.setAttribute("userID", userID);
            session.setAttribute("username", userName);
            if (name.isEmpty()) {
                session.setAttribute("name",userName);
            }
            else {
                session.setAttribute("name", name);
            }
            session.setAttribute("userEmail", userEmail);
            session.setAttribute("userTel",userTel);
            session.setAttribute("isSubscribed", isSubscribed);
            session.setAttribute("type", userType);
        }
        response.sendRedirect((String)session.getAttribute("previousPage"));
        
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
