
package Model;

import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
/**
 *
 * @author Oscar Alejandro Gómez Suarez
 */
public class DaoOwner extends Dao{
    
    public boolean register(Owner owner){
        Connection conn = getConnection();
        PreparedStatement ps = null;
        String query = "INSERT INTO `owner` (`name`, `lastname`, `email`, `phone`, `movilphone`, `user`,"
                + " `address`, `nit`, `password`, `lastSeason`) VALUES (?,?,?,?,?,?,?,?,?,?)";
        try {
            ps = conn.prepareStatement(query);
            ps.setString(1, owner.getName());
            ps.setString(2, owner.getLastName());
            ps.setString(3, owner.getEmail());
            ps.setInt(4, owner.getPhoneF());
            ps.setLong(5, owner.getPhone());
            ps.setString(6, owner.getUser());
            ps.setString(7, owner.getAddres());
            ps.setLong(8, owner.getNit());
            ps.setString(9, owner.getPassword());
            ps.setString(10, owner.getLastSeason());
            ps.execute();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public boolean existNit(String nit){
        Connection conn = getConnection();
        Statement st;
        ResultSet rs;
        String query = "SELECT id FROM `owner` WHERE nit ='"+nit+"'";
        try {
            st = conn.createStatement();
            rs = st.executeQuery(query);
            if(rs.next()){
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public boolean existEmail(String email){
        Connection conn = getConnection();
        Statement st;
        ResultSet rs;
        String query = "SELECT id FROM `owner` WHERE email ='"+email+"'";
        try {
            st = conn.createStatement();
            rs = st.executeQuery(query);
            if(rs.next()){
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public boolean existUser(String user){
        Connection conn = getConnection();
        Statement st;
        ResultSet rs;
        String query = "SELECT id FROM `owner` WHERE user ='"+user+"'";
        try {
            st = conn.createStatement();
            rs = st.executeQuery(query);
            if(rs.next()){
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public boolean signIn(Owner owner){
        Connection conn = getConnection();
        PreparedStatement ps;
        ResultSet rs;
        String query = "SELECT id, name, lastname, password, lastSeason FROM `owner` WHERE user = ?";
        try {
            ps = conn.prepareStatement(query);
            ps.setString(1, owner.getUser());
            rs = ps.executeQuery();
            if(rs.next()){
                if(owner.getPassword().equals(rs.getString(4))){
                    owner.setId(rs.getInt(1));
                    owner.setName(rs.getString(2));
                    owner.setLastName(rs.getString(3));
                    // Primero se guarla la fecha que esta en la base de datos. para despues setearla al usuario
                    String lastTime = rs.getString(5);
                    //Ahora se actualizar con la que esta entrando el usuario
                    String queryDate = "UPDATE `owner` SET lastSeason = ? WHERE id = ?";
                    ps = conn.prepareStatement(queryDate);
                    ps.setString(1, owner.getLastSeason());
                    ps.setInt(2, owner.getId());
                    ps.execute();
                    // Ahora se siguen seteando los atributos para devolverlos a la nueva vista que se creara
                    owner.setLastSeason(lastTime);
                    return true;
                }
            }else{
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }
    
    public boolean update(Owner owner){
        Connection conn = getConnection();
        PreparedStatement ps;
        String queryPass = "UPDATE `owner` SET name = ?, lastname = ?, email = ?, phone = ?, movilphone = ?, address = ?, password = ? "
                + "WHERE id = ?";
        String queryNoPass = "UPDATE `owner` SET name = ?, lastname = ?, email = ?, phone = ?, movilphone = ?, address = ? "
                + "WHERE id = ?";
        if(owner.getPassword().equals("")){
            try {
                ps = conn.prepareStatement(queryNoPass);
                ps.setString(1, owner.getName());
                ps.setString(2, owner.getLastName());
                ps.setString(3, owner.getEmail());
                ps.setInt(4, owner.getPhoneF());
                ps.setLong(5, owner.getPhone());
                ps.setString(6, owner.getAddres());
                ps.setInt(7, owner.getId());
                ps.execute();
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else{
            try {
                ps = conn.prepareStatement(queryPass);
                ps.setString(1, owner.getName());
                ps.setString(2, owner.getLastName());
                ps.setString(3, owner.getEmail());
                ps.setInt(4, owner.getPhoneF());
                ps.setLong(5, owner.getPhone());
                ps.setString(6, owner.getAddres());
                ps.setString(7, owner.getPassword());
                ps.setInt(8, owner.getId());
                ps.execute();
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }
    
    public Owner getInformation(int owner) {
        Connection conn = getConnection();
        Statement st;
        ResultSet rs;
        Owner o = new Owner();
        String query = "SELECT * FROM `owner` WHERE id = '" + owner + "'";
        try {
            st = conn.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {

                o.setName(rs.getString("name"));
                o.setLastName(rs.getString("lastname"));
                o.setEmail(rs.getString("email"));
                o.setPhoneF(rs.getInt("phone"));
                o.setPhone(rs.getLong("movilphone"));
                o.setAddres(rs.getString("address"));
            }
            return o;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public boolean delete(int owner){
        Connection conn = getConnection();
        Statement st;
        String query = "DELETE FROM `owner` WHERE id = '"+owner+"'";
        try {
            st = conn.createStatement();
            st.execute(query);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    
    
}
