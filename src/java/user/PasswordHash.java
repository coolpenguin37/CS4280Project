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
        byte[] bytes= md.digest();
        StringBuilder sb = new StringBuilder(2 * bytes.length);
        for (byte b : bytes) {
            sb.append("0123456789ABCDEF".charAt((b & 0xF0) >> 4));
            sb.append("0123456789ABCDEF".charAt((b & 0x0F)));
        }
        String hex = sb.toString();
        return hex;
    }
}