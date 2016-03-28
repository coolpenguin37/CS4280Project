/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package manager;

import java.sql.*;
import java.util.ArrayList;
import database.*;

/**
 *
 * @author Lin Jianxiong
 */
public class Manager implements MySQLInit {
    int rID;
    int userID;
    int hotelID;

    public int getRID() {
        return rID;
    }

    public int getUserID() {
        return userID;
    }

    public int getHotelID() {
        return hotelID;
    }

    public Manager(int rID, int userID, int hotelID) {
        this.rID = rID;
        this.userID = userID;
        this.hotelID = hotelID;
    }

    public ArrayList<Manager> getManagerByHotelID(int hotelID) {
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

    public ArrayList<Manager> getHotelByUserID(int userID) {
        ArrayList<Manager> hotelList = new ArrayList<Manager>();
        try {
            Class.forName(SQLDriver);
            Connection conn = DriverManager.getConnection(SQLHost, SQLUser, SQLPassword);
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM [Manager] WHERE [UserID] = ?");
            stmt.setInt(1, userID);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Manager temp = new Manager(rs.getInt("RID"), rs.getInt("UserID"), rs.getInt("HotelID"));
                hotelList.add(temp);
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
        return hotelList;
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
