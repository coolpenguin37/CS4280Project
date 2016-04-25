/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package comment;

import database.*;
import java.sql.Date;
import java.sql.*;
import java.util.ArrayList;

/**
 *
 * @author Lin Jianxiong
 */
public class Comment implements MySQLInit {
    int commentID;
    int orderID;
    String content;
    int score;
    Date date;
    
    public int getCommentID() {
        return commentID;
    }
    
    public void setCommentID(int commentID) {
        this.commentID = commentID;
    }

    public int getOrderID() {
        return orderID;
    }
    
    public void setOrderID(int orderID) {
        this.orderID = orderID;
    }

    public String getContent() {
        return content;
    }
    
    public void setContent(String content) {
        this.content = content;
    }

    public int getScore() {
        return score;
    }
    
    public void setScore(int score) {
        this.score = score;
    }
    
    public Date getDate() {
        return date;
    }
    
    public void setDate(Date date) {
        this.date = date;
    }
    
    public Comment(int orderID, int score, Date date) {
        this.orderID = orderID;
        this.content = "";
        this.score = score;
        this.date = date;
    }

    public Comment(int orderID, String content, int score, Date date) {
        this.orderID = orderID;
        this.content = content;
        this.score = score;
        this.date = date;        
    }
    
    public Comment(int commentID, int orderID, String content, int score, Date date) {
        this.commentID = commentID;
        this.orderID = orderID;
        this.content = content;
        this.score = score;
        this.date = date;
        
    }
    
    public boolean insertToDatabase() {
        try {
            Class.forName(SQLDriver);
            Connection conn = DriverManager.getConnection(SQLHost, SQLUser, SQLPassword);
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO [Comments] "
                + "([OrderID], [Content], [Score], [Date]) VALUES (?, ?, ?, ?)");

            
            stmt.setInt(1, orderID);
            stmt.setString(2, content);
            stmt.setInt(3, score);
            stmt.setDate(4, date);
            
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

    public static ArrayList<Comment> getCommentByHotelName(String hotelName) {
        ArrayList<Comment> commentList = new ArrayList<Comment>();
        try {
            Class.forName(SQLDriver);
            Connection conn = DriverManager.getConnection(SQLHost, SQLUser, SQLPassword);
            String strQuery = "SELECT Orders.OrderID, CommentID, Content, Score, [Date]"
                + "FROM Orders INNER JOIN Comments ON Orders.OrderID = Comments.OrderID "
                + "WHERE Orders.HotelID IN (SELECT HotelInfo.HotelID From [HotelInfo] "
                + "WHERE [HotelName] = ?)";
            PreparedStatement stmt = conn.prepareStatement(strQuery);
            stmt.setString(1, hotelName);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Comment temp = new Comment(rs.getInt("CommentID"), rs.getInt("OrderID"),
                    rs.getString("Content"), rs.getInt("Score"), rs.getDate("Date"));
                commentList.add(temp);
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
        return commentList;
    }

    public static double getScoreByHotelName(String hotelName) {
        ArrayList<Comment> commentList = getCommentByHotelName(hotelName);
        if (commentList == null) {
            return 0.0;
        }
        int sum = 0;
        for (int i = 0; i < commentList.size(); ++i) {
            Comment temp = commentList.get(i);
            sum = sum + temp.getScore();
        }
        double avg = sum * 1.0 / (commentList.size() + 1.0);
        avg = avg * 10;
        avg = Math.round(avg);
        avg = avg / 10;
        return avg;
    }

    public static ArrayList<Comment> getCommentByUsername(String username) {
        ArrayList<Comment> commentList = new ArrayList<Comment>();
        try {
            Class.forName(SQLDriver);
            Connection conn = DriverManager.getConnection(SQLHost, SQLUser, SQLPassword);
            String strQuery = "SELECT Orders.OrderID, CommentID, Content, Score, [Date]"
                + "FROM Orders INNER JOIN Comments ON Orders.OrderID = Comments.OrderID "
                + "WHERE Orders.UserID IN (SELECT [User].UserID From [User] "
                + "WHERE [Username] = ?";
            PreparedStatement stmt = conn.prepareStatement(strQuery);
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Comment temp = new Comment(rs.getInt("CommentID"), rs.getInt("OrderID"),
                    rs.getString("Content"), rs.getInt("Score"), rs.getDate("Date"));
                commentList.add(temp);
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
        return commentList;
    }
}
