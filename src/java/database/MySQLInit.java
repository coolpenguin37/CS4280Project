/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database;

/**
 *
 * @author Jianxiong Lin
 */
public interface MySQLInit {
    static String SQLDriver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
    static String SQLHost = "jdbc:sqlserver://w2ksa.cs.cityu.edu.hk:1433;databaseName=aiad045_db";                            
    static String SQLUser = "aiad045";
    static String SQLPassword = "aiad045";
}