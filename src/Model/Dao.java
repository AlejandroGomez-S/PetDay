
package Model;

import java.sql.Connection;
import java.sql.DriverManager;


/**
 *
 * @author Oscar Alejandro Gómez Suarez
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
