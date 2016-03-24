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

    public static ArrayList<Hotel> getHotelByName(String hotelName) {
        ArrayList<Hotel> hotelList = new ArrayList<Hotel>();
        try {
            Class.forName(SQLDriver);
            Connection conn = DriverManager.getConnection(SQLHost, SQLUser, SQLPassword);
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM [HotelInfo] WHERE [Name] LIKE ?");
            stmt.setString(1, hotelName + "%");
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                temp = new Hotel(rs.getString("Name"), rs.getString("Location"),
                    rs.getInt("IsRecommended"),rs.getInt("StarRating"));
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

    // public static boolean hotelExist(String hotelName) {
    //     boolean founded = false;

    //     try {
    //         Class.forName(SQLDriver);
    //         Connection conn = DriverManager.getConnection(SQLHost, SQLUser, SQLPassword);
    //         PreparedStatement stmt = conn.prepareStatement("SELECT * FROM [HotelInfo] WHERE [Name] = ?");
    //         stmt.setString(1, hotelName);
    //         ResultSet rs = stmt.executeQuery();
    //         if (rs.next()) {
    //             founded = true;
    //         }

    //         if (rs != null) {
    //             rs.close();
    //         }

    //         if (stmt != null) {
    //             stmt.close();
    //         }

    //         if (conn != null) {
    //             conn.close();
    //         }
    //     } catch (Exception e) {
    //         return false;
    //     }

    //     return founded;
    // }

    public boolean insertToDatabase() {
        // if (Hotel.hotelExist(this.getHotelName())) {
        //     return false;
        // }

        try {
            Class.forName(SQLDriver);
            Connection conn = DriverManager.getConnection(SQLHost, SQLUser, SQLPassword);
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO [HotelInfo] "
                + "([Name], [Location], [IsRecommended], [StarRating]) "
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
