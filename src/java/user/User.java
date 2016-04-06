/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package user;

import java.sql.*;
import java.util.ArrayList;
import database.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Lin Jianxiong
 */

public class User implements MySQLInit, UserType {
    int userID=-1;
    String username;
    String password;
    String name;
    String email;
    String tel;
    int isSubscribed=-1;
    int userType=-1;
    public static final String USERNAME_ERROR="Username is not valid. It should contain only uppercase, lowercase or number. The length should be at least 6 characters.";
    public static final String PASSWORD_ERROR="Password is not valid. It should contain at least one uppercase, at least one lowercase and at least one number. "
                        + "The lengths should be at least 6 characters. Please check";
    public static final String TEL_ERROR="Telephone is not valid. It should contain only numbers. Please check";
    public static final String EMAIL_ERROR= "Email is not valid. Please check";
    public static final String NAME_ERROR="";
    public static final String SUBSCRIBE_ERROR="";
    public static final String TYPE_ERROR="";
    
    public void setUserID(int userID) {
        this.userID=userID;
    }
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

    public User(String username, String password, String name,
        String email, String tel, int isSubscribed, int userType) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.email = email;
        this.tel = tel;
        this.isSubscribed = isSubscribed;
        this.userType = userType;
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

    public static boolean usernameExist(String username) {
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
        
        if (User.usernameExist(this.getUsername())) {
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

    public static User getUserByUserID(int userID)
    {
        User temp=null;
        try {
            Class.forName(SQLDriver);
            Connection conn = DriverManager.getConnection(SQLHost, SQLUser, SQLPassword);
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM [User] WHERE [UserID] = ?");
            stmt.setInt(1, userID);
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

    public static User getUserByUsername(String username)
    {
        User temp=null;
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
                User temp = new User(rs.getString("Username"), 
                    rs.getString("Password"), rs.getString("Name"), rs.getString("Email"),
                    rs.getString("Tel"), rs.getInt("IsSubscribed"), rs.getInt("UserType"));
                userList.add(temp);
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
            e.printStackTrace();
            return null;
        }

        return userList;
    }

    public static boolean updateProfile(User u)
    {
        try {
            if (u.getUserID()==-1) {
                if (u.getUsername()!=null && !u.getUsername().isEmpty()){
                    u.setUserID((User.getUserByUsername(u.getUsername()).getUserID()));
                }
                else{
                    return false;
                }
            }
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

            if (u.getIsSubscribed()==0 || u.getIsSubscribed()==1)
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
                User temp = new User(rs.getString("Username"), 
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
    public static boolean validateEmail(String email) {
        
        String email_pattern = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        System.out.println(email);
        Pattern pattern = Pattern.compile(email_pattern);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
         
    }
    //Validate password. The password must contains at least one capital letter, one lowercase letter and one number.
    //It should be at least 6 characters
    public static boolean validatePassword(String password) {
        String password_pattern = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{5,}$";
        Pattern pattern = Pattern.compile(password_pattern);
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }
    
    //username can only contain one or more uppercase, lowercase and number.
    //it should be at least 6 characters
    public static boolean validateUsername(String username) {
        String username_pattern = "^[a-zA-Z0-9].{5,}$";
        Pattern pattern = Pattern.compile(username_pattern);
        Matcher matcher = pattern.matcher(username);
        return matcher.matches();
    }
    
    //telephone must be one or more numbers
    public static boolean validateTel(String telephone) {
        String telephone_pattern = "^[0-9]+$";
        Pattern pattern = Pattern.compile(telephone_pattern);
        Matcher matcher = pattern.matcher(telephone);
        return matcher.matches();    
    } 
    
    public static boolean validateName(String name){
        return (name!=null && !name.isEmpty());
    }
    
    public static boolean validateIsSubscribed(int isSubscribed){
        return (isSubscribed==0 || isSubscribed==1);
    }
    
    public static boolean validateType(int type){
        return (type==0 || type==1 || type==2 || type==3);
    }
}
