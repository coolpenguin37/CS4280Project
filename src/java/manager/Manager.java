/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package manager;

import java.sql.*;
import java.util.ArrayList;
import database.*;
import user.User;
import hotel.*;

/**
 *
 * @author Lin Jianxiong
 */
public class Manager implements MySQLInit {
    
    private int rID;
    private int userID;
    private int hotelID;
    
    public String getName(){
        return User.getUserByUserID(userID).getName();
    }

    public int getRID() {
        return rID;
    }

    public int getUserID() {
        return userID;
    }

    public int getHotelID() {
        return hotelID;
    }
    
    public Hotel getHotel(){
        return Hotel.getHotelByID(hotelID);
    }
    
    public Manager(){
        
    }

    public Manager(int userID, int hotelID) {
        this.userID = userID;
        this.hotelID = hotelID;
    }

    public Manager(int rID, int userID, int hotelID) {
        this.rID = rID;
        this.userID = userID;
        this.hotelID = hotelID;
    }

    public static ArrayList<Manager> getManagerByHotelID(int hotelID) {
        ArrayList<Manager> managerList = new ArrayList<Manager>();
        try {
            Class.forName(SQLDriver);
            Connection conn = DriverManager.getConnection(SQLHost, SQLUser, SQLPassword);
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM [Manager] WHERE [HotelID] = ?");
            stmt.setInt(1, hotelID);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Manager temp = new Manager(rs.getInt("RID"), rs.getInt("UserID"), rs.getInt("HotelID"));
                managerList.add(temp);
            }

            if (rs != null) {
                rs.close();
            }

            if (stmt != null) {
                stmt.close();
            }

            if (conn != null) {
                conn.close();
            }
        } catch (Exception e) {
            return null;
        }
        return managerList;
    }

    public static boolean removeManager(int userID, int hotelID) {
        boolean flag = false;
        try {
             Class.forName(SQLDriver);
            Connection conn = DriverManager.getConnection(SQLHost, SQLUser, SQLPassword);
            String strQuery = "DELETE FROM [Manager] WHERE [UserID] = ? AND [HotelID] = ?";
            PreparedStatement stmt = conn.prepareStatement(strQuery);
            stmt.setInt(1, userID);
            stmt.setInt(2, hotelID);
            int cnt = stmt.executeUpdate();
            if (cnt > 0) {
                flag = true;
            }

            if (stmt != null) {
                stmt.close();
            }

            if (conn != null) {
                conn.close();
            }
        } catch (Exception e) {
            return flag;
        }
        return flag;
    }

    public static ArrayList<Manager> getManagerByUsername(String username) {
        User u = User.getUserByUsername(username);
        ArrayList<Manager> managerList = new ArrayList<Manager>();
        try {
            Class.forName(SQLDriver);
            Connection conn = DriverManager.getConnection(SQLHost, SQLUser, SQLPassword);
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM [Manager] WHERE [UserID] = ?");
            stmt.setInt(1, u.getUserID());
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Manager temp = new Manager(rs.getInt("RID"), rs.getInt("UserID"), rs.getInt("HotelID"));
                managerList.add(temp);
            }

            if (rs != null) {
                rs.close();
            }

            if (stmt != null) {
                stmt.close();
            }

            if (conn != null) {
                conn.close();
            }
        } catch (Exception e) {
            return null;
        }
        return managerList;
    }
    
    public static ArrayList<Manager> getAllManagers(){
        ArrayList<Manager> managerList = new ArrayList<Manager>();
        try {
            Class.forName(SQLDriver);
            Connection conn = DriverManager.getConnection(SQLHost, SQLUser, SQLPassword);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM [Manager]");

            while (rs.next()) {
                Manager temp = new Manager(rs.getInt("RID"), rs.getInt("UserID"), rs.getInt("HotelID"));
                managerList.add(temp);
            }

            if (rs != null) {
                rs.close();
            }

            if (stmt != null) {
                stmt.close();
            }

            if (conn != null) {
                conn.close();
            }
        } catch (Exception e) {
            return null;
        }
        return managerList;
    }

    public static ArrayList<Manager> getManagerByUserID(int userID) {
        ArrayList<Manager> managerList = new ArrayList<Manager>();
        try {
            Class.forName(SQLDriver);
            Connection conn = DriverManager.getConnection(SQLHost, SQLUser, SQLPassword);
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM [Manager] WHERE [UserID] = ?");
            stmt.setInt(1, userID);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Manager temp = new Manager(rs.getInt("RID"), rs.getInt("UserID"), rs.getInt("HotelID"));
                managerList.add(temp);
            }

            if (rs != null) {
                rs.close();
            }

            if (stmt != null) {
                stmt.close();
            }

            if (conn != null) {
                conn.close();
            }
        } catch (Exception e) {
            return null;
        }
        return managerList;
    }

    public static boolean managerExist(int userID, int hotelID) {
        boolean founded = false;

        try {
            Class.forName(SQLDriver);
            Connection conn = DriverManager.getConnection(SQLHost, SQLUser, SQLPassword);            
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM [Manager] WHERE [UserID] = ? AND [HotelID] = ?");
            stmt.setInt(1, userID);
            stmt.setInt(2, hotelID);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                founded = true;
            }

            if (rs != null) {
                rs.close();
            }

            if (stmt != null) {
                stmt.close();
            }

            if (conn != null) {
                conn.close();
            }
        } catch (Exception e) {
            return false;
        }

        return founded;
    }

    public boolean insertToDatabase() {
        if (Manager.managerExist(this.getUserID(), this.getHotelID())) {
            return false;
        }

        try {
            Class.forName(SQLDriver);
            Connection conn = DriverManager.getConnection(SQLHost, SQLUser, SQLPassword);
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO [Manager] "
                + "([UserID], [HotelID]) "
                + "VALUES (?, ?)");
            stmt.setInt(1, userID);
            stmt.setInt(2, hotelID);

            stmt.executeUpdate();
            if (stmt != null) {
                stmt.close();
            }

            if (conn != null) {
                conn.close();
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}
