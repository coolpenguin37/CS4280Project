/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package user;

import java.sql.*;
import database.*;
/**
 *
 * @author Jianxiong Lin
 */
public class Login implements MySQLInit {
    private String userID;
    private String password;
    
    //TODO: HASH THE PASSWORD
    public Login(String userID, String password) {
        this.userID = userID;
        this.password = password; 
    }
    
    public int login() {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        try {
            conn = DriverManager.getConnection(SQLHost, SQLID, SQLPassword);
            stmt = conn.createStatement();
            String stmp = new String("SELECT * FROM User WHERE USERID='" + userID + "'");
            rs = stmt.executeQuery(stmp);
            
            while (rs != null && rs.next() != false) {
                String name = rs.getString("Name");
                String email = rs.getString("Email");
                //System.out.println(name + "\n" + email + "\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
}
