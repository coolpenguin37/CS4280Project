/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package comment;

import database.*;
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

    public Comment(int orderID, String content, int score) {
        this.orderID = orderID;
        this.content = content;
        this.score = score;
    }
    
    public Comment(int commentID, int orderID, String content, int score) {
        this.commentID = commentID;
        this.orderID = orderID;
        this.content = content;
        this.score = score;
    }
    
    public boolean insertToDatabase() {
        try {
            Class.forName(SQLDriver);
            Connection conn = DriverManager.getConnection(SQLHost, SQLUser, SQLPassword);
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO [Comments] "
                + "([CommentID], [OrderID], [Content], [Score]) VALUES (?, ?, ?, ?)");

            stmt.setInt(1, commentID);
            stmt.setInt(2, orderID);
            stmt.setString(3, content);
            stmt.setInt(4, score);

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

    public ArrayList<Comment> getCommentByHotelName(String name) {
        ArrayList<Comment> commentList = new ArrayList<Comment>();
        try {
            Class.forName(SQLDriver);
            Connection conn = DriverManager.getConnection(SQLHost, SQLUser, SQLPassword);
            String strQuery = "SELECT Orders.OrderID, CommentID, Content, Score "
                + "FROM Orders INNER JOIN Comments ON Orders.OrderID = Comments.OrderID "
                + "WHERE Orders.HotelID IN (SELECT HotelInfo.HotelID From [HotelInfo] "
                + "WHERE [Name] = ?";
            PreparedStatement stmt = conn.prepareStatement(strQuery);
            stmt.setString(1, name);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Comment temp = new Comment(rs.getInt("CommentID"), rs.getInt("OrderID"),
                    rs.getString("Content"), rs.getInt("Score"));
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

    public ArrayList<Comment> getCommentByUsername(String username) {
        ArrayList<Comment> commentList = new ArrayList<Comment>();
        try {
            Class.forName(SQLDriver);
            Connection conn = DriverManager.getConnection(SQLHost, SQLUser, SQLPassword);
            String strQuery = "SELECT Orders.OrderID, CommentID, Content, Score "
                + "FROM Orders INNER JOIN Comments ON Orders.OrderID = Comments.OrderID "
                + "WHERE Orders.UserID IN (SELECT [User].UserID From [User] "
                + "WHERE [Username] = ?";
            PreparedStatement stmt = conn.prepareStatement(strQuery);
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Comment temp = new Comment(rs.getInt("CommentID"), rs.getInt("OrderID"),
                    rs.getString("Content"), rs.getInt("Score"));
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
