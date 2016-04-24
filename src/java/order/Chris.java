/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package order;

import java.sql.*;
import java.sql.Date;
import database.*;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.LocalDate;
/**
 *
 * @author Lin Jianxiong
 */
public class Chris implements MySQLInit {
    int orderID;
    int hotelID;
    int roomType;
    int numOfRoom;
    Date date;
    
    public int getOrderID() {
        return orderID;
    }

    public Date getDate() {
        return date;
    }

    public int getHotelID() {
        return hotelID;
    }

    public int getRoomType() {
        return roomType;
    }
    
    public int getNumOfRoom() {
        return numOfRoom;
    }
    
    public Chris(int orderID, Date date, int hotelID, int roomType, int numOfRoom) {
        this.orderID = orderID;
        this.date = date;
        this.hotelID = hotelID;
        this.roomType = roomType;
        this.numOfRoom = numOfRoom;
    }

    public boolean insertToDatabase() {
        try {
            Class.forName(SQLDriver);
            Connection conn = DriverManager.getConnection(SQLHost, SQLUser, SQLPassword);
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO [Chris] "
                + "([OrderID], [Date], [HotelID], [RoomType], [NumOfRoom]) "
                + "VALUES (?, ?, ?, ?, ?)");
            stmt.setInt(1, orderID);
            stmt.setDate(2, date);
            stmt.setInt(3, hotelID);
            stmt.setInt(4, roomType);
            stmt.setInt(5, numOfRoom);

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

    public static int deleteByOrderID(int orderID) {
        int cnt = 0;
        try {
            Class.forName(SQLDriver);
            Connection conn = DriverManager.getConnection(SQLHost, SQLUser, SQLPassword);
            PreparedStatement stmt = conn.prepareStatement("DELETE FROM [Chris] WHERE [OrderID] = ?");
            stmt.setInt(1, orderID);
            cnt = stmt.executeUpdate();
            if (stmt != null) {
                stmt.close();
            }

            if (conn != null) {
                conn.close();
            }
        } catch (Exception e) {
            return 0;
        }
        return cnt;
    }
    
    public static boolean insertByOrderID(int orderID) {
        try {
            Class.forName(SQLDriver);
            Connection conn = DriverManager.getConnection(SQLHost, SQLUser, SQLPassword);
            Order o = Order.getOrderByOrderID(orderID);
            DateTime dtCIDate = new DateTime(o.getCIDate());
            DateTime dtCODate = new DateTime(o.getCODate());
            int duration = Days.daysBetween(new LocalDate(dtCIDate), new LocalDate(dtCODate)).getDays();
            for (int i = 0; i < duration; ++i) {
                DateTime currentDate = dtCIDate.plusDays(i);
                java.sql.Date sqlDate = new java.sql.Date(currentDate.toDate().getTime());
                Chris ctmp = new Chris(o.getOrderID(), sqlDate, o.getHotelID(), o.getRoomType(), o.getNumOfRoom());
                ctmp.insertToDatabase();
            }
        } catch (Exception e) {
            return false;
        }   
        return true;
    }

    // Delete if [Date] <= date
    public static int deleteByDate(Date date) {
        int cnt = 0;
        try {
            Class.forName(SQLDriver);
            Connection conn = DriverManager.getConnection(SQLHost, SQLUser, SQLPassword);
            PreparedStatement stmt = conn.prepareStatement("DELETE FROM [Chris] WHERE [Date] <= ?");
            stmt.setDate(1, date);
            cnt = stmt.executeUpdate();
            if (stmt != null) {
                stmt.close();
            }

            if (conn != null) {
                conn.close();
            }
        } catch (Exception e) {
            return 0;
        }
        return cnt;
    }
}
