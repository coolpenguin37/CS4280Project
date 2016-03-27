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
            return false;
        }
        return managerList;
    }

    public boolean insertToDatabase() {

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
