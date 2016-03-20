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
    int hotelID;
    String location;
    int isRecommended;
    int starRating;

    public int getHotelID() {
        return hotelID;
    }

}
