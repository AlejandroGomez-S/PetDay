/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.sql.Connection;
import java.sql.DriverManager;


/**
 *
 * @author og218
 */
public class Dao {
    
    private final String BASE = "petday";
    private final String USER = "root";
    private final String PASSWORD = "edureka";
    private final String FIXUBICATION = "?serverTimezone=UTC";
    private final String URL = "jdbc:mysql://localhost:3306/"+BASE+FIXUBICATION;
    
    public Connection getConnection(){
        try {
            Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            return conn;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
}
