/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotel;

import java.sql.*;
import java.util.ArrayList;
import database.*;
import user.UserType;

/**
 *
 * @author Lin Jianxiong
 */
public class MemberBenefits implements MySQLInit {
    int rID;
    int hotelID;
    int commonUser;
    int preferredUser;
    int goldUser;
    int plantiumUser;
    int welcomeGift;
    int lateCheckout;
    int breakfast;
    int freeWiFi;

    public MemberBenefits(int hotelID, int commonUser, int preferredUser, int goldUser,
        int plantiumUser, int welcomeGift, int lateCheckout, int breakfast, int freeWiFi) {
        this.hotelID = hotelID;
        this.commonUser = commonUser;
        this.preferredUser = preferredUser;
        this.goldUser = goldUser;
        this.plantiumUser = plantiumUser;
        this.welcomeGift = welcomeGift;
        this.lateCheckout = lateCheckout;
        this.breakfast = breakfast;
        this.freeWiFi = freeWiFi;
    }

    public MemberBenefits(int rID, int hotelID, int commonUser, int preferredUser, int goldUser,
        int plantiumUser, int welcomeGift, int lateCheckout, int breakfast, int freeWiFi) {
        this.rID = rID;
        this.hotelID = hotelID;
        this.commonUser = commonUser;
        this.preferredUser = preferredUser;
        this.goldUser = goldUser;
        this.plantiumUser = plantiumUser;
        this.welcomeGift = welcomeGift;
        this.lateCheckout = lateCheckout;
        this.breakfast = breakfast;
        this.freeWiFi = freeWiFi;
    }

    public boolean insertToDatabase() {
        try {
            Class.forName(SQLDriver);
            Connection conn = DriverManager.getConnection(SQLHost, SQLUser, SQLPassword);
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO [MemberBenefits] "
                + "([HotelID], [CommonUser], [PreferredUser], [GoldUser], [PlantiumUser], "
                + "[WelcomeGift], [LateCheckout], [Breakfast], [FreeWiFi]) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");
            stmt.setInt(1, hotelID);
            stmt.setInt(2, commonUser);
            stmt.setInt(3, preferredUser);
            stmt.setInt(4, goldUser);
            stmt.setInt(5, plantiumUser);
            stmt.setInt(6, welcomeGift);
            stmt.setInt(7, lateCheckout);
            stmt.setInt(8, breakfast);
            stmt.setInt(9, freeWiFi);

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

    public static MemberBenefits getMemberBenefitsByHotelID(int hotelID) {
        MemberBenefits temp=null;
        try {
            Class.forName(SQLDriver);
            Connection conn = DriverManager.getConnection(SQLHost, SQLUser, SQLPassword);
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM [MemberBenefits] WHERE [HotelID] = ?");
            stmt.setInt(1, hotelID);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                temp = new MemberBenefits(rs.getInt("RID"), rs.getInt("HotelID"), 
                    rs.getInt("CommonUser"), rs.getInt("PreferredUser"), rs.getInt("GoldUser"), 
                    rs.getInt("PlantiumUser"), rs.getInt("WelcomeGift"), rs.getInt("LateCheckout"), 
                    rs.getInt("Breakfast"), rs.getInt("FreeWiFi"));
            }
            if(rs != null) {
                rs.close();
            }
            
            if(stmt != null) {
                stmt.close();
            }
                
            if(conn != null) {
                conn.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return temp;
    }

    public int getDiscountByUserType(int userType) {
        int temp = 100;
        if (userType == 0) {
            return temp;
        }
        try {
            Class.forName(SQLDriver);
            Connection conn = DriverManager.getConnection(SQLHost, SQLUser, SQLPassword);
            String benefit;
            switch (userType){
                case UserType.COMMONUSER:benefit="CommonUser";break;
                case UserType.GOLDUSER:benefit="GoldUser";break;
                case UserType.PREFERREDUSER:benefit="PreferredUser";break;
                case UserType.PLATINUMUSER:benefit="PlaitiumUser";break;
                default:benefit="CommonUser";break;
            }    
            PreparedStatement stmt = conn.prepareStatement("SELECT " + benefit + " FROM [MemberBenefits] WHERE [HotelID] = ?");
            stmt.setInt(1, hotelID);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                temp = rs.getInt(userType + 1);
            }
            if(rs != null) {
                rs.close();
            }
            
            if(stmt != null) {
                stmt.close();
            }
                
            if(conn != null) {
                conn.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return 100;
        }
        return temp;
    }
}
