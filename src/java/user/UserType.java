/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package user;

/**
 *
 * @author Lin Jianxiong
 */
public interface UserType {
    static final int GUEST = 0;
    static final int COMMONUSER = 1;
    static final int PREFERREDUSER = 2;
    static final int GOLDUSER = 3;
    static final int PLATINUMUSER = 4;
    static final int MANAGER = 10;
    static final int CHEIFMANAGER = 100;
}