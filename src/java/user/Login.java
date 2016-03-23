/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package user;

import java.security.NoSuchAlgorithmException;

/**
 *
 * @author Jianxiong Lin, yduan7
 */
public class Login {
    private String username;
    private String password;
    
    public Login(String username, String password) {
        this.username = username;
        this.password = password; 
    }
    
    public User login() throws loginException{
        
        if (User.userExist(username)){
            User u=User.getUserByUsername(username);
            try {password=PasswordHash.hash(password);}
            catch (NoSuchAlgorithmException e) {}
            if (password!=null && u.getPassword().equals(password)){
                return u;
            }
            else {
                throw new loginException("Password Incorrect! Please check.");
            }
        }
        else {
            throw new loginException("Username does not exist! Please check.");
        }
    }
}
