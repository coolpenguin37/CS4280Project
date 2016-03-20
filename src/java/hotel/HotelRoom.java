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
public class HotelRoom implements MySQLInit {
    int hotelID;
    int roomType;
    String roomName;
    int standardRate;
    int numOfRoom;
    int roomSize;

    public int getHotelID() {
        return hotelID;
    }

    public int getRoomType() {
        return roomType;
    }

    public void setRoomType() {
        this.roomType = roomType;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName() {
        this.roomName = roomName;
    }

    public int getStandardRate() {
        return standardRate;
    }

    public void setStandardRate() {
        this.standardRate = standardRate;
    }

    public int getNumOfRoom() {
        return numOfRoom;
    }

    public void setNumOfRoom() {
        this.numOfRoom = numOfRoom;
    }

    public int getRoomSize() {
        return roomSize;
    }

    public void setRoomSize() {
        this.roomSize = roomSize;
    }

    public HotelRoom (int hotelID, int roomType, String roomName, 
        int standardRate, int numOfRoom, int roomSize) {
        this.hotelID = hotelID;
        this.roomType = roomType;
        this.roomName = roomName;
        this.standardRate = standardRate;
        this.numOfRoom = numOfRoom;
        this.roomSize = roomSize;
    }

    public HotelRoom (int hotelID, int roomType, String roomName) {
        this.hotelID = hotelID;
        this.roomType = roomType;
        this.roomName = roomName;
        this.standardRate = 0;
        this.numOfRoom = 0;
        this.roomSize = 0;
    }

    public static ArrayList<HotelRoom> getAllRoom() {
        ArrayList<HotelRoom> roomList = new ArrayList<HotelRoom>();
        try {
            Class.forName(SQLDriver);
            Connection conn = DriverManager.getConnection(SQLHost, SQLUser, SQLPassword);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM [HotelRoom]");
            while (rs.next()) {
                HotelRoom temp = new HotelRoom(rs.getInt("HotelID"), rs.getInt("roomType"), rs.getString("RoomName"), 
                    rs.getInt("standardRate"), rs.getInt("numOfRoom"), rs.getInt("RoomSize"));
                roomList.add(temp);
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

        return roomList;
    }

    public static boolean updateRoom(HotelRoom r)
    {
        try {
            Class.forName(SQLDriver);
            Connection conn = DriverManager.getConnection(SQLHost, SQLUser, SQLPassword);

            if (!r.getRoomName().equals(""))
            {
                String strQuery = "UPDATE [HotelRoom] SET [RoomName] = ? WHERE [HotelID] = ? AND [RoomType] = ?";
                PreparedStatement stmt = conn.prepareStatement(strQuery);
                stmt.setString(1, r.getRoomName());
                stmt.setInt(2, r.getHotelID());
                stmt.setInt(3, r.getRoomType());
                stmt.executeUpdate();
                if (stmt != null) {
                    stmt.close();
                }
            }

            if (r.getStandardRate() != 0)
            {
                String strQuery = "UPDATE [HotelRoom] SET [StandardRate] = ? WHERE [HotelID] = ? AND [RoomType] = ?";
                PreparedStatement stmt = conn.prepareStatement(strQuery);
                stmt.setInt(1, r.getStandardRate());
                stmt.setInt(2, r.getHotelID());
                stmt.setInt(3, r.getRoomType());
                stmt.executeUpdate();
                if (stmt != null) {
                    stmt.close();
                }
            }

            if (r.getNumOfRoom() != 0)
            {
                String strQuery = "UPDATE [HotelRoom] SET [NumOfRoom] = ? WHERE [HotelID] = ? AND [RoomType] = ?";
                PreparedStatement stmt = conn.prepareStatement(strQuery);
                stmt.setInt(1, r.getNumOfRoom());
                stmt.setInt(2, r.getHotelID());
                stmt.setInt(3, r.getRoomType());
                stmt.executeUpdate();
                if (stmt != null) {
                    stmt.close();
                }
            }

            if (r.getRoomSize() != 0)
            {
                String strQuery = "UPDATE [HotelRoom] SET [RoomSize] = ? WHERE [HotelID] = ? AND [RoomType] = ?";
                PreparedStatement stmt = conn.prepareStatement(strQuery);
                stmt.setInt(1, r.getRoomSize());
                stmt.setInt(2, r.getHotelID());
                stmt.setInt(3, r.getRoomType());
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
