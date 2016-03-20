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

    public int setRoomSize() {
        this.roomSize = roomSize;
    }
}
