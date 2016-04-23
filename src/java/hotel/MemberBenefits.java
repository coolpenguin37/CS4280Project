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
    int rID = -1;
    int hotelID = -1;
    int commonUser = -1;
    int preferredUser = -1;
    int goldUser = -1;
    int plantiumUser = -1;
    int welcomeGift = -1;
    int lateCheckout = -1;
    int breakfast = -1;
    int freeWiFi = -1;
    
    public int getHotelID() {
        return hotelID;
    }
    
    public void setHotelID(int i){
        this.hotelID=i;
    }

    public int getCommonUser() {
        return commonUser;
    }
    
    public void setCommonUser(int i){
        this.commonUser=i;
    }

    public int getPreferredUser() {
        return preferredUser;
    }
    
    public void setPreferredUser(int i){
        this.preferredUser=i;
    }

    public int getGoldUser() {
        return goldUser;
    }
    
    public void setGoldUser(int i){
        this.goldUser=i;
    }

    public int getPlantiumUser() {
        return plantiumUser;
    }
    
    public void setPlantiumUser(int i){
        this.plantiumUser=i;
    }

    public int getWelcomeGift() {
        return welcomeGift;
    }
    
    public void setWelcomeGift(int i){
        this.welcomeGift=i;
    }
    

    public int getLateCheckout() {
        return lateCheckout;
    }
     
    public void setLateCheckout(int i){
        this.lateCheckout=i;
    }
    
    public int getBreakfast() {
        return breakfast;
    }
    
    public void setBreakfast(int i){
        this.breakfast=i;
    }

    public int getFreeWiFi() {
        return freeWiFi;
    }
    public void setFreeWiFi(int i){
        this.freeWiFi=i;
    }
    
    

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

    public static boolean updateMemberBenefits(MemberBenefits mb)
    {
        try {
            Class.forName(SQLDriver);
            Connection conn = DriverManager.getConnection(SQLHost, SQLUser, SQLPassword);

            if (mb.getCommonUser() != -1)
            {
                String strQuery = "UPDATE [MemberBenefits] SET [CommonUser] = ? WHERE [HotelID] = ?";
                PreparedStatement stmt = conn.prepareStatement(strQuery);
                stmt.setInt(1, mb.getCommonUser());
                stmt.setInt(2, mb.getHotelID());
                stmt.executeUpdate();
                if (stmt != null) {
                    stmt.close();
                }
            }

            if (mb.getPreferredUser() != -1)
            {
                String strQuery = "UPDATE [MemberBenefits] SET [PreferredUser] = ? WHERE [HotelID] = ?";
                PreparedStatement stmt = conn.prepareStatement(strQuery);
                stmt.setInt(1, mb.getPreferredUser());
                stmt.setInt(2, mb.getHotelID());
                stmt.executeUpdate();
                if (stmt != null) {
                    stmt.close();
                }
            }

            if (mb.getGoldUser() != -1)
            {
                String strQuery = "UPDATE [MemberBenefits] SET [GoldUser] = ? WHERE [HotelID] = ?";
                PreparedStatement stmt = conn.prepareStatement(strQuery);
                stmt.setInt(1, mb.getGoldUser());
                stmt.setInt(2, mb.getHotelID());
                stmt.executeUpdate();
                if (stmt != null) {
                    stmt.close();
                }
            }

            if (mb.getPlantiumUser() != -1)
            {
                String strQuery = "UPDATE [MemberBenefits] SET [PlantiumUser] = ? WHERE [HotelID] = ?";
                PreparedStatement stmt = conn.prepareStatement(strQuery);
                stmt.setInt(1, mb.getPlantiumUser());
                stmt.setInt(2, mb.getHotelID());
                stmt.executeUpdate();
                if (stmt != null) {
                    stmt.close();
                }
            }

            if (mb.getWelcomeGift() != -1)
            {
                String strQuery = "UPDATE [MemberBenefits] SET [WelcomeGift] = ? WHERE [HotelID] = ?";
                PreparedStatement stmt = conn.prepareStatement(strQuery);
                stmt.setInt(1, mb.getWelcomeGift());
                stmt.setInt(2, mb.getHotelID());
                stmt.executeUpdate();
                if (stmt != null) {
                    stmt.close();
                }
            }

            if (mb.getLateCheckout() != -1)
            {
                String strQuery = "UPDATE [MemberBenefits] SET [LateCheckout] = ? WHERE [HotelID] = ?";
                PreparedStatement stmt = conn.prepareStatement(strQuery);
                stmt.setInt(1, mb.getLateCheckout());
                stmt.setInt(2, mb.getHotelID());
                stmt.executeUpdate();
                if (stmt != null) {
                    stmt.close();
                }
            }

            if (mb.getBreakfast() != -1)
            {
                String strQuery = "UPDATE [MemberBenefits] SET [Breakfast] = ? WHERE [HotelID] = ?";
                PreparedStatement stmt = conn.prepareStatement(strQuery);
                stmt.setInt(1, mb.getBreakfast());
                stmt.setInt(2, mb.getHotelID());
                stmt.executeUpdate();
                if (stmt != null) {
                    stmt.close();
                }
            }

            if (mb.getFreeWiFi() != -1)
            {
                String strQuery = "UPDATE [MemberBenefits] SET [FreeWiFi] = ? WHERE [HotelID] = ?";
                PreparedStatement stmt = conn.prepareStatement(strQuery);
                stmt.setInt(1, mb.getFreeWiFi());
                stmt.setInt(2, mb.getHotelID());
                stmt.executeUpdate();
                if (stmt != null) {
                    stmt.close();
                }
            }

            if (conn != null) {
                conn.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
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
                case UserType.PLATINUMUSER:benefit="PlantiumUser";break;
                default:benefit="CommonUser";break;
            }    
            PreparedStatement stmt = conn.prepareStatement("SELECT " + benefit + " FROM [MemberBenefits] WHERE [HotelID] = ?");
            stmt.setInt(1, hotelID);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                temp = rs.getInt(1);
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
