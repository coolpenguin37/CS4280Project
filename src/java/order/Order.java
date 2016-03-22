/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package order;

import java.sql.*;
import java.util.*;
import database.*;

/**
 *
 * @author Lin Jianxiong
 */
public class Order {
    int orderID;
    int status;
    int userID;
    Date CIDate;
    Date CODate;
    int hotelID;
    int roomType;
    int numOfRoom;

    public int getOrderID() {
        return orderID;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public Date getCIDate() {
        return CIDate;
    }

    public void setCIDate(Date CIDate) {
        this.CIDate = CIDate;
    }

    public Date getCODate() {
        return CODate;
    }

    public void setCODate(Date CODate) {
        this.CODate = CODate;
    }

    public int getHotelID() {
        return hotelID;
    }

    public void setHotelID(int hotelID) {
        this.hotelID = hotelID;
    }

    public int getRoomType() {
        return roomType;
    }

    public void setRoomType(int roomType) {
        this.roomType = roomType;
    }

    public int getNumOfRoom() {
        return numOfRoom;
    }

    public void setNumOfRoom(int numOfRoom) {
        this.numOfRoom = numOfRoom;
    }

    public Order(int orderID, int status, int userID, Date CIDate, Date CODate, 
        int hotelID, int roomType, int numOfRoom) {
        this.orderID = orderID;
        this.status = status;
        this.userID = userID;
        this.CIDate = CIDate;
        this.CODate = CODate;
        this.hotelID = hotelID;
        this.roomType = roomType;
        this.numOfRoom = numOfRoom;
    }

    // public static boolean orderExist(int orderID) {
    //     boolean founded = false;

    //     try {
    //         Class.forName(SQLDriver);
    //         Connection conn = DriverManager.getConnection(SQLHost, SQLUser, SQLPassword);
    //         PreparedStatement stmt = conn.prepareStatement("SELECT * FROM [Order] WHERE [OrderID] = ?");
    //         stmt.setInt(1, orderID);
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

    public static boolean checkAvailability(Order o) {
        boolean available = false;
        try {
            Class.forName(SQLDriver);
            Connection conn = DriverManager.getConnection(SQLHost, SQLUser, SQLPassword);
            String strQuery = "SELECT COUNT(*) FROM [HotelInfo] WHERE ([HotelInfo.HotelID] = ?) AND "
            + "([HotelInfo.NumOfRoom] > (SELECT SUM([NumOfRoom]) FROM [Order] WHERE [Order.HotelID] = ?)";
            PreparedStatement stmt = conn.prepareStatement(strQuery);
            stmt.setInt(1, o.getHotelID());
            stmt.setInt(2, o.getHotelID());
            ResultSet rs = stmt.executeQuery();
            if (rs.next())
            {
                available = true;
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
        if (Order.checkAvailability())
        try {
            Class.forName(SQLDriver);
            Connection conn = DriverManager.getConnection(SQLHost, SQLUser, SQLPassword);
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO [Order] "
                + "([Status] [UserID], [CIDate], [CODate], [HotelID], [RoomType], [NumOfRoom]) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)");
            stmt.setInt(1, username);
            stmt.setInt(2, password);
            stmt.setDate(3, name);
            stmt.setDate(4, email);
            stmt.setInt(5, tel);
            stmt.setInt(6, isSubscribed);
            stmt.setInt(7, userType);

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



    public static boolean updateOrder(Order o) {
        if (!Order.checkAvailability(o)) {
            return false;
        }

        try {
            Class.forName(SQLDriver);
            Connection conn = DriverManager.getConnection(SQLHost, SQLUser, SQLPassword);

            if (!u.getName().equals(""))
            {
                String strQuery = "UPDATE [User] SET [Name] = ? WHERE [userID] = ?";
                PreparedStatement stmt = conn.prepareStatement(strQuery);
                stmt.setString(1, u.getName());
                stmt.setInt(2, u.getUserID());
                stmt.executeUpdate();
                if (stmt != null) {
                    stmt.close();
                }
            }

            if (!u.getEmail().equals(""))
            {
                String strQuery = "UPDATE [User] SET [Email] = ? WHERE [userID] = ?";
                PreparedStatement stmt = conn.prepareStatement(strQuery);
                stmt.setString(1, u.getEmail());
                stmt.setInt(2, u.getUserID());
                stmt.executeUpdate();
                if (stmt != null) {
                    stmt.close();
                }
            }

            if (!u.getTel().equals(""))
            {
                String strQuery = "UPDATE [User] SET [Tel] = ? WHERE [userID] = ?";
                PreparedStatement stmt = conn.prepareStatement(strQuery);
                stmt.setString(1, u.getTel());
                stmt.setInt(2, u.getUserID());
                stmt.executeUpdate();
                if (stmt != null) {
                    stmt.close();
                }
            }

            if (true)
            {
                String strQuery = "UPDATE [User] SET [IsSubscribed] = ? WHERE [userID] = ?";
                PreparedStatement stmt = conn.prepareStatement(strQuery);
                stmt.setInt(1, u.getIsSubscribed());
                stmt.setInt(2, u.getUserID());
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
