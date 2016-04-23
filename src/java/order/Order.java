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

    private int orderID;
    private int status;
    private int userID;
    private Date CIDate;
    private Date CODate;
    private int hotelID;
    private int roomType;
    private int numOfRoom;
    private String name;
    private String email;
    private String phone;
    private int price;
    private String roomName="";
    private String statusDescription="";
    private int orderIDWrapper=0;
    
    public String getStatusDescription(){
        switch (status){
            case (OrderStatus.ABORTED):statusDescription="Order cancelled";break;
            case (OrderStatus.COMPLETED):statusDescription="Checked-out and completed";break;
            case (OrderStatus.HOLDING):statusDescription="Order currently modifying";break;
            case (OrderStatus.ONGOING):statusDescription="Order paid";break;
            case (OrderStatus.PROCESSING):statusDescription="Order unpaid";break;
            case (OrderStatus.STAYING):statusDescription="Checked-in already";break;
            default:statusDescription="";
        }
        return statusDescription;
    }
    
    public void setStatusDescription(String statusDescription){
        this.statusDescription=statusDescription;
    }

    public String getRoomName(){
        HotelRoom hr=HotelRoom.getHotelRoom(hotelID, roomType);
        if (hr!=null){
            roomName=hr.getRoomName();
        }
        else {
            roomName="";
        }
        return roomName;
    }
    
    public void setRoomName(String roomName){
        this.roomName=roomName;
    }
    public int getOrderID() {
        return orderID+orderIDWrapper;
    }
    
    public void setOrderID(int orderID) {
        this.orderID=orderID-orderIDWrapper;
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
    
    public int getPrice() {
        return price;
    }
    
    public void setPrice(int price) {
        this.price = price;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getPhone() {
        return phone;
    }
    
    public void setPhone(String phone) {
        this.phone = phone;
    }
    
    
    public Order(){
        
    }
    
    // public Order(int hotelID, int roomType, Date CIDate, Date CODate) {
    //     this.hotelID = hotelID;
    //     this.roomType = roomType;
    //     this.CIDate = CIDate;
    //     this.CODate = CODate;
    // }

    public Order(int hotelID, int roomType, int numOfRoom, Date CIDate, Date CODate) {
        this.hotelID = hotelID;
        this.roomType = roomType;
        this.numOfRoom = numOfRoom;
        this.CIDate = CIDate;
        this.CODate = CODate;
        this.name = "";
        this.email = "";
        this.phone = "";
        this.price = 0;
    }

    public Order(int status, int userID, Date CIDate, Date CODate,
            int hotelID, int roomType, int numOfRoom) {
        this.status = status;
        this.userID = userID;
        this.CIDate = CIDate;
        this.CODate = CODate;
        this.hotelID = hotelID;
        this.roomType = roomType;
        this.numOfRoom = numOfRoom;
        this.name = "";
        this.email = "";
        this.phone = "";
        this.price = 0;
    }
    
    public Order(int status, int userID, Date CIDate, Date CODate,
            int hotelID, int roomType, int numOfRoom, String name, String email, String phone, int price) {
        this.status = status;
        this.userID = userID;
        this.CIDate = CIDate;
        this.CODate = CODate;
        this.hotelID = hotelID;
        this.roomType = roomType;
        this.numOfRoom = numOfRoom;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.price = price;
    }
    
    public Order(int orderID, int status, int userID, Date CIDate, Date CODate, 
        int hotelID, int roomType, int numOfRoom, String name, String email, String phone, int price) {
        this.orderID = orderID;
        this.status = status;
        this.userID = userID;
        this.CIDate = CIDate;
        this.CODate = CODate;
        this.hotelID = hotelID;
        this.roomType = roomType;
        this.numOfRoom = numOfRoom;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.price = price;
    }

    public Order(Order another) {
        this.status = another.status;
        this.userID = another.userID;
        this.CIDate = another.CIDate;
        this.CODate = another.CODate;
        this.hotelID = another.hotelID;
        this.roomType = another.roomType;
        this.numOfRoom = another.numOfRoom;
        this.name = another.name;
        this.email = another.email;
        this.phone = another.phone;
        this.price = another.price;
    }

    public static Order getOrderByOrderID(int orderID) {
        Order temp = null;
        try {
            Class.forName(SQLDriver);
            Connection conn = DriverManager.getConnection(SQLHost, SQLUser, SQLPassword);
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM [Orders] WHERE [OrderID] = ?");
            stmt.setInt(1, orderID);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                temp = new Order(rs.getInt("OrderID"),  rs.getInt("Status"), rs.getInt("UserID"),
                    rs.getDate("CIDate"), rs.getDate("CODate"), rs.getInt("HotelID"), 
                    rs.getInt("RoomType"), rs.getInt("NumOfRoom"), rs.getString("Name"), 
                    rs.getString("Email"), rs.getString("Phone"), rs.getInt("Price"));
                //set roomName field
                temp.getRoomName();
                temp.getStatusDescription();
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

    public static int getLowestRate(int hotelID, Date CIDate, Date CODate) {
        ArrayList<HotelRoom> roomList = HotelRoom.getAllRoomsByHotelID(hotelID);
        int lowestRate = Integer.MAX_VALUE;
        for (int i = 0; i < roomList.size(); ++i) {
            HotelRoom e = roomList.get(i);
            if (e.getStandardRate() >= lowestRate) {
                continue;
            }
            Order o = new Order(hotelID, e.getRoomType(), 1, CIDate, CODate);
            int remained = Order.getRemainedRoom(o);
            if (remained > 0) {
                lowestRate = e.getStandardRate();
            }
        }
        if (lowestRate == Integer.MAX_VALUE) {
            return -1;
        } else {
            return lowestRate;
        }
    }

    public int insertToDatabase() {
        int ans = 0;
        try {
            Class.forName(SQLDriver);
            Connection conn = DriverManager.getConnection(SQLHost, SQLUser, SQLPassword);
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO [Orders] "
                + "([Status], [UserID], [CIDate], [CODate], [HotelID], [RoomType], [NumOfRoom], [Name], [Email], [Phone], [Price]) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            stmt.setInt(1, status);
            stmt.setInt(2, userID);
            stmt.setDate(3, CIDate);
            stmt.setDate(4, CODate);
            stmt.setInt(5, hotelID);
            stmt.setInt(6, roomType);
            stmt.setInt(7, numOfRoom);
            stmt.setString(8, name);
            stmt.setString(9, email);
            stmt.setString(10, phone);
            stmt.setInt(11, price);

            stmt.executeUpdate();

//            if (affectedRows == 0) {
//                return 0;
//            }

            ResultSet generatedKeys = stmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                ans = generatedKeys.getInt(1);
            }
            else {
                return 0;
            }
                
        } catch (Exception e) {
            return 0;
        }
        try {
            Class.forName(SQLDriver);
            Connection conn = DriverManager.getConnection(SQLHost, SQLUser, SQLPassword);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT MAX([OrderID]) FROM [Orders]");
            if (rs.next()) {
                int itmp = rs.getInt(1);
                DateTime dtCIDate = new DateTime(CIDate);
                DateTime dtCODate = new DateTime(CODate);
                int duration = Days.daysBetween(new LocalDate(dtCIDate), new LocalDate(dtCODate)).getDays();
                for (int i = 0; i < duration; ++i) {
                    DateTime currentDate = dtCIDate.plusDays(i);
                    java.sql.Date sqlDate = new java.sql.Date(currentDate.toDate().getTime());
                    Chris ctmp = new Chris(itmp, sqlDate, hotelID, roomType, numOfRoom);
                    ctmp.insertToDatabase();
                }
            }
        } catch (Exception e) {
            return 0;
        }
        return ans;
    }
    
    public static ArrayList<Order> getOrderList(String hotelName, String name) {
        ArrayList<Order> orderList = new ArrayList<Order>();
        
        try {
            Class.forName(SQLDriver);
            Connection conn = DriverManager.getConnection(SQLHost, SQLUser, SQLPassword);
            String strQuery = "SELECT [OrderID] FROM [Orders] INNER JOIN [User] "
                + "ON Orders.UserID = [User].UserID "
                + "WHERE [User].Name = ? AND Orders.HotelID IN "
                + "(SELECT HotelInfo.HotelID FROM [HotelInfo] "
                + " WHERE [HotelName] = ?)";
            PreparedStatement stmt = conn.prepareStatement(strQuery);
            stmt.setString(1, name);
            stmt.setString(2, hotelName);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Order temp = Order.getOrderByOrderID(rs.getInt("OrderID"));
                orderList.add(temp);
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
        
        return orderList;
    }

    public static ArrayList<Order> getAllOrdersByUserID(int userID) {
        ArrayList<Order> orderList = new ArrayList<Order>();

        try {
            Class.forName(SQLDriver);
            Connection conn = DriverManager.getConnection(SQLHost, SQLUser, SQLPassword);
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM [Orders] WHERE [userID] = ?");
            stmt.setInt(1, userID);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Order temp = new Order(rs.getInt("OrderID"), rs.getInt("status"), rs.getInt("UserID"), 
                    rs.getDate("CIDate"), rs.getDate("CODate"), rs.getInt("HotelID"), 
                    rs.getInt("RoomType"), rs.getInt("NumOfRoom"), rs.getString("Name"),
                    rs.getString("Email"), rs.getString("Phone"), rs.getInt("Price"));
                orderList.add(temp);
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

        return orderList;
    }

    // public static int deleteByOrderID(int orderID) {
    //     int cnt = 0;
    //     try {
    //         Class.forName(SQLDriver);
    //         Connection conn = DriverManager.getConnection(SQLHost, SQLUser, SQLPassword);
    //         PreparedStatement stmt = conn.prepareStatement("DELETE FROM [Orders] WHERE [OrderID] = ?");
    //         stmt.setInt(1, orderID);
    //         cnt = stmt.executeUpdate();

    //         Chris.deleteByOrderID(orderID);

    //         if (stmt != null) {
    //             stmt.close();
    //         }

    //         if (conn != null) {
    //             conn.close();
    //         }
    //     } catch (Exception e) {
    //         return 0;
    //     }
    //     return cnt;
    // }

    public static int updateStatus(int orderID, int status) {
        int cnt = 0;
        try {
            Class.forName(SQLDriver);
            Connection conn = DriverManager.getConnection(SQLHost, SQLUser, SQLPassword);
            PreparedStatement stmt = conn.prepareStatement("UPDATE [Orders] SET [Status] = ? WHERE [OrderID] = ?");
            stmt.setInt(1, status);
            stmt.setInt(2, orderID);
            cnt = stmt.executeUpdate();
            if (status == COMPLETED || status == ABORTED || status == HOLDING) {
                Chris.deleteByOrderID(orderID);
            }
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

    public static int getRemainedRoom(Order o) {
        int remained = Integer.MAX_VALUE;
        int num = HotelRoom.getNumOfRoomByID(o.getHotelID(), o.getRoomType());
        if (num == 0) {
            return 0;
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
                    remained = 0;
                    break;
                }

                int occupied = rs.getInt(1);
                if (occupied >= num) {
                    remained = 0;
                    break;
                } else if (num - occupied < remained) {
                    remained = num - occupied;
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
            return 0;
        }

        return remained;
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

    public static Order mergeOrder(Order a, Order b) {
        int newHotelID = a.getHotelID();
        int newRoomType = a.getRoomType();
        Date newCIDate;
        Date newCODate;
        int newNumOfRoom;
        
        if (a.getCIDate().compareTo(b.getCIDate()) < 0) {
            newCIDate = a.getCIDate();
        } else {
            newCIDate = b.getCIDate();
        }

        if (a.getCODate().compareTo(b.getCODate()) > 0) {
            newCODate = a.getCODate();
        } else {
            newCODate = b.getCODate();
        }

        if (a.getNumOfRoom() > b.getNumOfRoom()) {
            newNumOfRoom = a.getNumOfRoom();
        } else {
            newNumOfRoom = b.getNumOfRoom();
        }

        Order o = new Order(newHotelID, newRoomType, newNumOfRoom, newCIDate, newCODate);
        if (!checkAvailability(o)) {
            return null;
        } else {
            return o;
        }
    }

    // b: new order
    // (status == PROCESSING, userID, hotelID, roomType, numOfRoom, CIDate, CODate)
    public static int tryUpdateOrder(Order a, Order b) {
        if (a.getRoomType() != b.getRoomType()) {
            if (Order.checkAvailability(b)) {
                return b.insertToDatabase();
            } else {
                return 0;
            }
        }
        Order o = Order.mergeOrder(a, b);
        if (o != null) {
            Order.updateStatus(a.getOrderID(), HOLDING);
            return o.insertToDatabase();
        } else {
            return 0;
        }
    }

    // a: old Order
    // b: new Order
    // c: merged OrderID
    // d: 0 for negative; 1 for positive
    public static boolean doUpdateOrder(Order a, Order b, int c, int d) {
        if (d == 0) {
            Order.updateStatus(a.getOrderID(), PROCESSING);
            Order.updateStatus(c, ABORTED);
            int aID = a.getOrderID();
            DateTime dtCIDate = new DateTime(a.getCIDate());
            DateTime dtCODate = new DateTime(a.getCODate());
            int duration = Days.daysBetween(new LocalDate(dtCIDate), new LocalDate(dtCODate)).getDays();
            for (int i = 0; i < duration; ++i) {
                DateTime currentDate = dtCIDate.plusDays(i);
                java.sql.Date sqlDate = new java.sql.Date(currentDate.toDate().getTime());
                Chris ctmp = new Chris(aID, sqlDate, a.getHotelID(), a.getRoomType(), a.getNumOfRoom());
                ctmp.insertToDatabase();
            }
        } else {
            b.insertToDatabase();
            Order.updateStatus(a.getOrderID(), ABORTED);
            Order.updateStatus(c, ABORTED);
        }
        return true;
    }

    public static int updateOrderRoomType(Order a, int newRoomType) {
        Order b = new Order(a);
        b.setRoomType(newRoomType);
        int newOrderID = b.insertToDatabase();
        if (newOrderID > 0) {
            a.updateStatus(a.getOrderID(), ABORTED);
            return newOrderID;
        } else {
            return 0;
        }
    }
    
    //mark placeholder
    public boolean updateOrder(Order o){
        return true;
    }

    // public boolean updateOrder(Order o) {
    //     Order t = Order.getOrderByOrderID(o.getOrderID());
    //     Order.updateStatus(o.getOrderID(), ABORTED);
    //     if (Order.checkAvailability(o)) {
    //         o.insertToDatabase();
    //         return true;
    //     } else {
    //         t.insertToDatabase();
    //         return false;
    //     }
    // }

    // public static ArrayList<Order> getOrderlist(String hotelName, String name){
    //     return new ArrayList<Order> ();
    // }

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
    //             String strQuery = "UPDATE [Orders] SET [Status] = ? WHERE [OrderID] = ?";
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
    //             String strQuery = "UPDATE [Orders] SET [CIDate] = ? WHERE [OrderID] = ?";
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
    //             String strQuery = "UPDATE [Orders] SET [CODate] = ? WHERE [OrderID] = ?";
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
    //             String strQuery = "UPDATE [Orders] SET [RoomType] = ? WHERE [OrderID] = ?";
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
    //             String strQuery = "UPDATE [Orders] SET [Status] = ? WHERE [OrderID] = ?";
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
