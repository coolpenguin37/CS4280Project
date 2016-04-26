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
    private int hotelID;
    private String hotelName;
    private String address;
    private int isRecommended=-1;
    private int starRating=-1;
    private String label;
    private String intro;

    public int getHotelID() {
        return hotelID;
    }
    
    public void setHotelID(int hotelID) {
        this.hotelID=hotelID;
    }

    public String getHotelName() {
        return hotelName;
    }

    public void setHotelName(String hotelName) {
        this.hotelName = hotelName;
    }
    
    public String getAddress() {
        return address;
    }
    
    public void setAddress(String address) {
        this.address = address;
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
    
    public String getLabel() {
        return label;
    }
    
    public void setLabel(String label) {
        this.label = label;
    }
    
    public String getIntro() {
        return intro;
    }
    
    public void setIntro(String intro) {
        this.intro = intro;
    }

    public Hotel(String hotelName, String address, int isRecommended, int starRating, String label, String intro) {
        this.hotelName = hotelName;
        this.address = address;
        this.isRecommended = isRecommended;
        this.starRating = starRating;
        this.label = label;
        this.intro = intro;
    }

    public Hotel(int hotelID, String hotelName, String address, int isRecommended, 
        int starRating, String label, String intro) {
        this.hotelID = hotelID;
        this.hotelName = hotelName;
        this.address = address;
        this.isRecommended = isRecommended;
        this.starRating = starRating;
        this.label = label;
        this.intro = intro;

    }

    public static Hotel getHotelByID(int hotelID) {
        Hotel temp = null;
        try {
            Class.forName(SQLDriver);
            Connection conn = DriverManager.getConnection(SQLHost, SQLUser, SQLPassword);
            String strQuery = "SELECT * FROM [HotelInfo] WHERE [HotelID] = ?";
            PreparedStatement stmt = conn.prepareStatement(strQuery);
            stmt.setInt(1, hotelID);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                temp = new Hotel(rs.getInt("HotelID"), rs.getString("HotelName"), rs.getString("Address"), 
                    rs.getInt("IsRecommended"), rs.getInt("StarRating"), rs.getString("Label"), rs.getString("Intro"));
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
        return temp;
    }
    
    public static Hotel getHotelByName(String hotelName, String address) {
        Hotel temp = null;
        try {
            Class.forName(SQLDriver);
            Connection conn = DriverManager.getConnection(SQLHost, SQLUser, SQLPassword);
            String strQuery = "SELECT * FROM [HotelInfo] WHERE [HotelName] = ? AND [Address] = ?";
            PreparedStatement stmt = conn.prepareStatement(strQuery);
            stmt.setString(1, hotelName);
            stmt.setString(2, address);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                temp = new Hotel(rs.getInt("HotelID"), rs.getString("HotelName"), rs.getString("Address"), 
                    rs.getInt("IsRecommended"), rs.getInt("StarRating"), rs.getString("Label"), rs.getString("Intro"));
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
        return temp;
    }
    
    public static ArrayList<Hotel> getAllHotel() {
        ArrayList<Hotel> hotelList = new ArrayList<Hotel> ();
        try {
            Class.forName(SQLDriver);
            Connection conn = DriverManager.getConnection(SQLHost, SQLUser, SQLPassword);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM [HotelInfo]");
            while (rs.next()) {
                Hotel temp = new Hotel(rs.getInt("HotelID"), rs.getString("HotelName"), rs.getString("Address"), 
                    rs.getInt("IsRecommended"), rs.getInt("StarRating"), rs.getString("Label"), rs.getString("Intro"));
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

    public static ArrayList<Hotel> searchHotel(String keyword) {
        if (keyword.equals("")) {
            return getAllHotel();
        }
 
        ArrayList<Hotel> hotelList = new ArrayList<Hotel>();
        try {
            Class.forName(SQLDriver);
            Connection conn = DriverManager.getConnection(SQLHost, SQLUser, SQLPassword);
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM [HotelInfo] WHERE ([HotelName] LIKE ?) OR ([Label] LIKE ?) OR ([LABEL] LIKE ?)");
            stmt.setString(1, "%" + keyword + "%");
            stmt.setString(2, "%:" + keyword + ";%");
            stmt.setString(3, "%" + keyword + "%");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Hotel temp = new Hotel(rs.getInt("HotelID"), rs.getString("HotelName"), rs.getString("Address"),
                    rs.getInt("IsRecommended"),rs.getInt("StarRating"), rs.getString("Label"), rs.getString("Intro"));
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

    public static boolean hotelExist(String hotelName, String address) {
        boolean founded = false;

        try {
            
            Class.forName(SQLDriver);
            Connection conn = DriverManager.getConnection(SQLHost, SQLUser, SQLPassword);
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM [HotelInfo] WHERE ([HotelName] = ?) AND ([Address] = ?)");
            stmt.setString(1, hotelName);
            stmt.setString(2, address);
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
            e.printStackTrace();
            return false;
        }

        return founded;
    }

    public boolean insertToDatabase() {
        if (Hotel.hotelExist(this.getHotelName(), this.getAddress())) {
            return false;
        }

        try {
            Class.forName(SQLDriver);
            Connection conn = DriverManager.getConnection(SQLHost, SQLUser, SQLPassword);
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO [HotelInfo] "
                + "([HotelName], [Address], [IsRecommended], [StarRating], [Label], [Intro]) "
                + "VALUES (?, ?, ?, ?, ?, ?)");
            stmt.setString(1, hotelName);
            stmt.setString(2, address);
            stmt.setInt(3, isRecommended);
            stmt.setInt(4, starRating);
            stmt.setString(5, label);
            stmt.setString(6, intro);

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
    
    public static boolean updateHotel(Hotel h){
        try {
            Class.forName(SQLDriver);
            Connection conn = DriverManager.getConnection(SQLHost, SQLUser, SQLPassword);

            if (!h.getHotelName().equals(""))
            {
                String strQuery = "UPDATE [HotelInfo] SET [hotelName] = ? WHERE [hotelID] = ?";
                PreparedStatement stmt = conn.prepareStatement(strQuery);
                stmt.setString(1, h.getHotelName());
                stmt.setInt(2, h.getHotelID());
                stmt.executeUpdate();
                if (stmt != null) {
                    stmt.close();
                }
            }

            if (!h.getAddress().equals(""))
            {
                String strQuery = "UPDATE [HotelInfo] SET [Address] = ? WHERE [hotelID] = ?";
                
                PreparedStatement stmt = conn.prepareStatement(strQuery);
                stmt.setString(1, h.getAddress());
                stmt.setInt(2, h.getHotelID());
                stmt.executeUpdate();
                if (stmt != null) {
                    stmt.close();
                }
            }

            if (h.getIsRecommended() == 0||h.getIsRecommended() == 1)
            {
                String strQuery = "UPDATE [HotelInfo] SET [IsRecommended] = ? WHERE [hotelID] = ?";
                PreparedStatement stmt = conn.prepareStatement(strQuery);
                stmt.setInt(1, h.getIsRecommended());
                stmt.setInt(2, h.getHotelID());
                stmt.executeUpdate();
                if (stmt != null) {
                    stmt.close();
                }
            }

            if (h.getStarRating() != -1)
            {
                String strQuery = "UPDATE [HotelInfo] SET [StarRating] = ? WHERE [hotelID] = ?";
                PreparedStatement stmt = conn.prepareStatement(strQuery);
                stmt.setInt(1, h.getStarRating());
                stmt.setInt(2, h.getHotelID());
                stmt.executeUpdate();
                if (stmt != null) {
                    stmt.close();
                }
            }
            
            if (!h.getLabel().equals(""))
            {
                if (h.getLabel().equals(":;")){
                    h.setLabel("");
                }
                String strQuery = "UPDATE [HotelInfo] SET [Label] = ? WHERE [hotelID] = ?";
                PreparedStatement stmt = conn.prepareStatement(strQuery);
                stmt.setString(1, h.getLabel());
                stmt.setInt(2, h.getHotelID());
                stmt.executeUpdate();
                if (stmt != null) {
                    stmt.close();
                }
            }
            
            if (!h.getIntro().equals(""))
            {
                String strQuery = "UPDATE [HotelInfo] SET [Intro] = ? WHERE [hotelID] = ?";
                PreparedStatement stmt = conn.prepareStatement(strQuery);
                stmt.setString(1, h.getIntro());
                stmt.setInt(2, h.getHotelID());
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
}
