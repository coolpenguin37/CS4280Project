/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package user;

import java.sql.*;
import java.util.ArrayList;
import database.*;

/**
 *
 * @author Lin Jianxiong
 */

public class User implements MySQLInit, UserType {
    int userID;
    String username;
    String password;
    String name;
    String email;
    String tel;
    int isSubscribed;
    int userType;

    public int getUserID() {
        return userID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public int getIsSubscribed() {
        return isSubscribed;
    }

    public void setIsSubscribed(int isSubscribed) {
        this.isSubscribed = isSubscribed;
    }

    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }

    public User(String username, String password, String name)
    {
        this.username = username;
        this.password = password;
        this.name = name;
        this.email = "";
        this.tel = "";
        this.isSubscribed = 1;
        this.userType = COMMONUSER;
    }

    public User(int userID, String username, String password, String name,
        String email, String tel, int isSubscribed, int userType) {
        this.userID = userID;
        this.username = username;
        this.password = password;
        this.name = name;
        this.email = email;
        this.tel = tel;
        this.isSubscribed = isSubscribed;
        this.userType = userType;
    }

    public static boolean userExist(String username) {
        boolean founded = false;

        try {
            Class.forName(SQLDriver);
            Connection conn = DriverManager.getConnection(SQLHost, SQLUser, SQLPassword);
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM [User] WHERE [Username] = ?");
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                founded = true;
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
        if (User.userExist(this.getUsername())) {
            return false;
        }

        try {
            Class.forName(SQLDriver);
            Connection conn = DriverManager.getConnection(SQLHost, SQLUser, SQLPassword);
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO [User] "
                + "([Username], [Password], [Name], [Email], [Tel], [IsSubscribed], [UserType]) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)");
            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.setString(3, name);
            stmt.setString(4, email);
            stmt.setString(5, tel);
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

    public static User getUserByUsername(String username)
    {
        User temp = null;
        try {
            Class.forName(SQLDriver);
            Connection conn = DriverManager.getConnection(SQLHost, SQLUser, SQLPassword);
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM [User] WHERE [Username] = ?");
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                temp = new User(rs.getInt("UserID"), rs.getString("Username"), 
                    rs.getString("Password"), rs.getString("Name"), rs.getString("Email"),
                    rs.getString("Tel"), rs.getInt("IsSubscribed"), rs.getInt("UserType"));
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

        return temp;
    }

    public static ArrayList<User> getAllUser() {
        ArrayList<User> userList = new ArrayList<User>();

        try {
            Class.forName(SQLDriver);
            Connection conn = DriverManager.getConnection(SQLHost, SQLUser, SQLPassword);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM [User]");
            while (rs.next()) {
                User temp = new User(rs.getInt("UserID"), rs.getString("Username"), 
                    rs.getString("Password"), rs.getString("Name"), rs.getString("Email"),
                    rs.getString("Tel"), rs.getInt("IsSubscribed"), rs.getInt("UserType"));
                userList.add(temp);
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

        return userList;
    }

    public static boolean updateProfile(User u)
    {
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

    public static ArrayList<User> getAllSubscribedUser() {
        ArrayList<User> userList = new ArrayList<User>();

        try {
            Class.forName(SQLDriver);
            Connection conn = DriverManager.getConnection(SQLHost, SQLUser, SQLPassword);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM [User] WHERE [IsSubscribed] = 'Yes'");
            while (rs.next()) {
                User temp = new User(rs.getInt("UserID"), rs.getString("Username"), 
                    rs.getString("Password"), rs.getString("Name"), rs.getString("Email"),
                    rs.getString("Tel"), rs.getInt("IsSubscribed"), rs.getInt("UserType"));
                userList.add(temp);
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

        return userList;
    }

    public boolean changeSubscriptionStatus(String username, int newStatus)
    {
        User u = getUserByUsername(username);
        if (u != null) {
            u.setIsSubscribed(newStatus);
            return updateProfile(u);
        } else {
            return false;
        }
    }

    public boolean subscriptionStatus()
    {
        return (isSubscribed == 1);
    }
}
