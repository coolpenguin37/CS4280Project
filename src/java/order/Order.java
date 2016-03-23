/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package order;

import java.sql.*;
import java.sql.Date;
import java.util.ArrayList;
import database.*;
import hotel.*;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.LocalDate;


/**
 *
 * @author Lin Jianxiong
 */
public class Order implements MySQLInit, OrderStatus {
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

    public static Order getOrderByOrderID(int orderID) {
        Order temp = null;
        try {
            Class.forName(SQLDriver);
            Connection conn = DriverManager.getConnection(SQLHost, SQLUser, SQLPassword);
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM [Order] WHERE [OrderID] = ?");
            stmt.setInt(1, orderID);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                temp = new Order(rs.getInt("OrderID"), rs.getInt("UserID"), 
                    rs.getInt("Status"), rs.getDate("CIDate"), rs.getDate("CODate"), 
                    rs.getInt("HotelID"), rs.getInt("RoomType"), rs.getInt("NumOfRoom"));
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

    public boolean insertToDatabase() {
        try {
            Class.forName(SQLDriver);
            Connection conn = DriverManager.getConnection(SQLHost, SQLUser, SQLPassword);
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO [Order] "
                + "([Status] [UserID], [CIDate], [CODate], [HotelID], [RoomType], [NumOfRoom]) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)");
            stmt.setInt(1, status);
            stmt.setInt(2, userID);
            stmt.setDate(3, CIDate);
            stmt.setDate(4, CODate);
            stmt.setInt(5, hotelID);
            stmt.setInt(6, roomType);
            stmt.setInt(7, numOfRoom);

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
        try {
            Class.forName(SQLDriver);
            Connection conn = DriverManager.getConnection(SQLHost, SQLUser, SQLPassword);
            Statement stmt = conn.Statement("SELECT MAX([OrderID]) FROM [Order]");
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int itmp = rs.getInt("OrderID");
                DateTime dtCIDate = new DateTime(o.getCIDate());
                DateTime dtCODate = new DateTime(o.getCODate());
                int duration = Days.daysBetween(new LocalDate(dtCIDate), new LocalDate(dtCODate)).getDays();
                for (int i = 0; i < duration; ++i) {
                    DateTime currentDate = dtCIDate.plusDays(i);
                    java.sql.Date sqlDate = new java.sql.Date(currentDate.toDate().getTime());
                    Chris ctmp = new Chris(itmp, hotelID, roomType, numOfRoom, sqlDate);
                    ctmp.insertToDatabase();
                }
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public static ArrayList<Order> getAllOrdersByUserID(int userID) {
        ArrayList<Order> orderList = new ArrayList<Order>();

        try {



        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return orderList;
    }

    public static int deleteByOrderID(int orderID) {
        int cnt = 0;
        try {
            Class.forName(SQLDriver);
            Connection conn = DriverManager.getConnection(SQLHost, SQLUser, SQLPassword);
            PreparedStatement stmt = conn.prepareStatement("DELETE FROM [Order] WHERE [OrderID] = ?");
            stmt.setInt(1, orderID);
            cnt = stmt.executeUpdate();

            Chris.deleteByOrderID(orderID);

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

    public static int updateStatus(int orderID, int status) {
        int cnt = 0;
        try {
            Class.forName(SQLDriver);
            Connection conn = DriverManager.getConnection(SQLHost, SQLUser, SQLPassword);
            PreparedStatement stmt = conn.prepareStatement("UPDATE [Order] SET [Status] = ? WHERE [OrderID] = ?");
            stmt.setInt(1, status);
            stmt.setInt(2, orderID);
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

    public static boolean checkAvailability(Order o) {
        boolean available = true;
        int num = HotelRoom.getNumOfRoomByID(o.getHotelID(), o.getRoomType());
        if (num == 0) {
            return false;
        }

        try {
            Class.forName(SQLDriver);
            Connection conn = DriverManager.getConnection(SQLHost, SQLUser, SQLPassword);
            String strQuery = "SELECT SUM([NumOfRoom]) FROM [Chris] "
                + "WHERE [HotelID] = ? AND [RoomType] = ? AND [Date] = ?";
            PreparedStatement stmt = conn.prepareStatement(strQuery);
            stmt.setInt(1, o.getHotelID());
            stmt.setInt(2, o.getRoomType());
            DateTime dtCIDate = new DateTime(o.getCIDate());
            DateTime dtCODate = new DateTime(o.getCODate());
            int duration = Days.daysBetween(new LocalDate(dtCIDate), new LocalDate(dtCODate)).getDays();
            for (int i = 0; i < duration; ++i) {
                DateTime currentDate = dtCIDate.plusDays(i);
                java.sql.Date sqlDate = new java.sql.Date(currentDate.toDate().getTime());
                stmt.setDate(3, sqlDate);
                ResultSet rs = stmt.executeQuery();
                if (!rs.next())
                {
                    available = false;
                    break;
                }
                int occupied = rs.getInt(1);
                if (occupied + o.getNumOfRoom() > num) {
                    available = false;
                    break;
                }

                if (rs != null) {
                    rs.close();
                }
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

        return available;
    }

    public boolean updateOrder(Order o) {
        Order t = Order.getOrderByOrderID(o.getOrderID());
        Order.deleteByOrderID(o.getOrderID());
        if (Order.checkAvailability(o)) {
            o.insertToDatabase();
            return true;
        } else {
            t.insertToDatabase();
            return false;
        }
    }


    // public static boolean CancelOrder(int OrderID) {
    //    try {
    //        Class.forName(SQLDriver);
    //        Connection conn = DriverManager.getConnection(SQLHost, SQLUser, SQLPassword);
    //    }
    // }

    // public static boolean validateUpdate(Order o) {

    // }


    // public static boolean updateOrder(Order o) {
    //     if (Order.getOrderByOrderID(o.getOrderID()) == null) {
    //         return false;
    //     }

    //     try {
    //         Class.forName(SQLDriver);
    //         Connection conn = DriverManager.getConnection(SQLHost, SQLUser, SQLPassword);

    //         if (o.getStatus() != 0)
    //         {
    //             String strQuery = "UPDATE [Order] SET [Status] = ? WHERE [OrderID] = ?";
    //             PreparedStatement stmt = conn.prepareStatement(strQuery);
    //             stmt.setInt(1, o.getStatus());
    //             stmt.setInt(2, o.getOrderID());
    //             stmt.executeUpdate();
    //             if (stmt != null) {
    //                 stmt.close();
    //             }
    //         }

    //         if (o.getCIDate() != null)
    //         {
    //             String strQuery = "UPDATE [Order] SET [CIDate] = ? WHERE [OrderID] = ?";
    //             PreparedStatement stmt = conn.prepareStatement(strQuery);
    //             stmt.setDate(1, o.getCIDate());
    //             stmt.setInt(2, o.getOrderID());
    //             stmt.executeUpdate();
    //             if (stmt != null) {
    //                 stmt.close();
    //             }
    //         }

    //         if (o.getCODate() != 0)
    //         {
    //             String strQuery = "UPDATE [Order] SET [CODate] = ? WHERE [OrderID] = ?";
    //             PreparedStatement stmt = conn.prepareStatement(strQuery);
    //             stmt.setInt(1, o.getCODate());
    //             stmt.setInt(2, o.getOrderID());
    //             stmt.executeUpdate();
    //             if (stmt != null) {
    //                 stmt.close();
    //             }
    //         }

    //         if (o.getRoomType() != 0)
    //         {
    //             String strQuery = "UPDATE [Order] SET [RoomType] = ? WHERE [OrderID] = ?";
    //             PreparedStatement stmt = conn.prepareStatement(strQuery);
    //             stmt.setInt(1, o.getRoomType());
    //             stmt.setInt(2, o.getOrderID());
    //             stmt.executeUpdate();
    //             if (stmt != null) {
    //                 stmt.close();
    //             }
    //         }

    //         if (o.getStatus() != 0)
    //         {
    //             String strQuery = "UPDATE [Order] SET [Status] = ? WHERE [OrderID] = ?";
    //             PreparedStatement stmt = conn.prepareStatement(strQuery);
    //             stmt.setInt(1, o.getStatus());
    //             stmt.setInt(2, o.getOrderID());
    //             stmt.executeUpdate();
    //             if (stmt != null) {
    //                 stmt.close();
    //             }
    //         }

    //         if (conn != null) {
    //             conn.close();
    //         }
    //     } catch (Exception e) {
    //         e.printStackTrace();
    //         return false;
    //     }

    //     return true;
    // }

}
