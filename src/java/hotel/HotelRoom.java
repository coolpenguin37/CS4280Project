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
public class HotelRoom implements MYSQLInit {
    int hotelID;
    int roomType;
    String roomName;
    int standardRate;
    int numOfRoom;
    int size;

    public int getHotelID() {
        return hotelID;
    }

    public int getRoomType() {
        return roomType;
    }

    public int setRoomType() {
        this.roomType = roomType;
    }

    public String getRoomName() {
        return roomName;
    }

    public String setRoomName() {
        this.roomName = roomName;
    }

    public int getStandardRate() {
        return standardRate;
    }

    public int setStandardRate() {
        this.standardRate = standardRate;
    }

    public int getNumOfRoom() {
        return numOfRoom;
    }

    public int setNumOfRoom() {
        this.numOfRoom = numOfRoom;
    }

    public int getRoomSize() {
        return roomSize;
    }

    public int setRoomSize() {
        this.roomSize = roomSize;
    }
}
