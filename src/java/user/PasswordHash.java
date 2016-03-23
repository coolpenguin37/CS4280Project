/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package user;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
/**
 *
 * @author yduan7
 */
public class PasswordHash {
    public static String hash(String password) throws NoSuchAlgorithmException{
        
        MessageDigest md= MessageDigest.getInstance("SHA-256");
        md.update(password.getBytes());
        String encryptedPassword=new String(md.digest());
        return encryptedPassword;
    }
}
