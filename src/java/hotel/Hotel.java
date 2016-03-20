/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotel;

import java.sql.*;
import java.util.ArrayList;
import database.*;

/**
 *
 * @author Lin Jianxiong
 */
public class Hotel implements MySQLInit{
    int hotelID;
    String hotelName;
    String location;
    int isRecommended;
    int starRating;

    public int getHotelID() {
        return hotelID;
    }

    public String getHotelName() {
        return hotelName;
    }

    public void setHotelName() {
        this.hotelName = hotelName;
    }

    public int getIsRecommended() {
        return isRecommended;
    }

    public void setIsRecommended(int isRecommended) {
        this.isRecommended = isRecommended;
    }

    public int getStarRating() {
        return starRating;
    }

    public void setStarRating(int starRating) {
        this.starRating = starRating;
    }

    public static boolean hotelExist(String hotelName) {
        boolean founded = false;

        try {
            Class.forName(SQLDriver);
            Connection conn = DriverManager.getConnection(SQLHost, SQLUser, SQLPassword);
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM [HotelInfo] WHERE [HotelName] = ?");
            stmt.setString(1, hotelName);
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
        if (Hotel.hotelExist(this.getHotelName())) {
            return false;
        }

        try {
            Class.forName(SQLDriver);
            Connection conn = DriverManager.getConnection(SQLHost, SQLUser, SQLPassword);
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO [HotelInfo] "
                + "([HotelName], [Location], [IsRecommended], [StarRating]) "
                + "VALUES (?, ?, ?, ?)");
            stmt.setString(1, hotelName);
            stmt.setString(2, location);
            stmt.setInt(3, isRecommended);
            stmt.setInt(4, starRating);

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
