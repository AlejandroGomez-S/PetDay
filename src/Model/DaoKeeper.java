
package Model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 *
 * @author Oscar Alejandro Gómez Suarez
 */
public class DaoKeeper extends Dao{
    // Guardar la imagen en la base de datos
    private FileInputStream fis;
    
    public boolean register(Keeper keeper){
        Connection conn = getConnection();
        PreparedStatement ps = null;
        String query = "INSERT INTO `keeper` (`name`, `lastname`, `sex`, `email`, `phone`, `nit`,"
                + " `description`, `picture`, `password`, `user`, `lastSeason`) VALUES (?,?,?,?,?,?,?,?,?,?,?)";
        try {
            ps = conn.prepareStatement(query);
            ps.setString(1, keeper.getName());
            ps.setString(2, keeper.getLastName());
            ps.setInt(3, keeper.getSex());
            ps.setString(4, keeper.getEmail());
            ps.setLong(5, keeper.getPhone());
            ps.setLong(6, keeper.getNit());
            ps.setString(7, keeper.getDescription());
            //Set la imagen del usuario al preparedStatement
            fis = new FileInputStream(keeper.getPicture());
            ps.setBinaryStream(8,(InputStream)fis, (int)keeper.getPicture().length());
            //Se continua con los otros campos
            ps.setString(9, keeper.getPassword());
            ps.setString(10, keeper.getUser());
            ps.setString(11, keeper.getLastSeason());
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
        String query = "SELECT id FROM `keeper` WHERE nit ='"+nit+"'";
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
        String query = "SELECT id FROM `keeper` WHERE email ='"+email+"'";
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
        String query = "SELECT id FROM `keeper` WHERE user ='"+user+"'";
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
    
    public boolean singIn(Keeper keeper){
        Connection conn = getConnection();
        PreparedStatement ps;
        ResultSet rs;
        String query = "SELECT id, name, lastname, password, lastSeason, picture FROM `keeper` WHERE user = ?";
        try {
            ps = conn.prepareStatement(query);
            ps.setString(1, keeper.getUser());
            rs = ps.executeQuery();
            if(rs.next()){
                if(keeper.getPassword().equals(rs.getString(4))){
                    keeper.setId(rs.getInt(1));
                    keeper.setName(rs.getString(2));
                    keeper.setLastName(rs.getString(3));
                    // Primero se guarla la fecha que esta en la base de datos. para despues setearla al usuario
                    String lastTime = rs.getString(5);
                    //Ahora se actualizar con la que esta entrando el usuario
                    String queryDate = "UPDATE `keeper` SET lastSeason = ? WHERE id = ?";
                    ps = conn.prepareStatement(queryDate);
                    ps.setString(1, keeper.getLastSeason());
                    ps.setInt(2, keeper.getId());
                    ps.execute();
                    // Ahora se siguen seteando los atributos para devolverlos a la nueva vista que se creara
                    keeper.setLastSeason(lastTime);
                    //Recuperar la imagen
                    InputStream is = rs.getBinaryStream(6);
                    File file = new File("photo.jpg");
                    OutputStream os = new FileOutputStream(file);
                    byte[] content = new byte[1024];
                    int size = 0;
                    while ((size = is.read(content)) != -1) {
                        os.write(content, 0, size);
                    }
                    keeper.setPicture(file);
                    os.close();
                    is.close();
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
    
    public String getKeeperName(int keeper){
        Connection conn = getConnection();
        PreparedStatement ps;
        ResultSet rs;
        String query = "SELECT name FROM `keeper` WHERE id = ?";
        try {
            ps = conn.prepareStatement(query);
            ps.setInt(1, keeper);
            rs = ps.executeQuery();
            while(rs.next()){
                return(rs.getString("name"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "Not Found";
    }
    
    public boolean getKeeperInfo(Keeper keeper){
        Connection conn = getConnection();
        PreparedStatement ps;
        ResultSet rs;
        String query = "SELECT * FROM `keeper` WHERE id = ?";
        try {
            ps = conn.prepareStatement(query);
            ps.setInt(1, keeper.getId());
            rs = ps.executeQuery();
            while(rs.next()){
                keeper.setName(rs.getString("name"));
                keeper.setLastName(rs.getString("lastname"));
                keeper.setSex(rs.getInt("sex"));
                keeper.setEmail(rs.getString("email"));
                keeper.setPhone(rs.getLong("phone"));
                keeper.setNit(rs.getLong("nit"));
                keeper.setDescription(rs.getString("description"));
                // Recuperar la image como inputstream
                // Luego pasarla ese is como outputstream a un file
                // Leugo set ese file al archivo de la foto del cuidador 
                InputStream is = rs.getBinaryStream("picture");
                File file = new File("photo.jpg");
                OutputStream os = new FileOutputStream(file);
                byte[] content = new byte[1024];
                int size = 0;
                while ((size = is.read(content)) != -1) {
                    os.write(content, 0, size);
                }
                keeper.setPicture(file); 
                os.close();
                is.close();
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public boolean update(Keeper keeper){
        Connection conn = getConnection();
        PreparedStatement ps;
        String query = "UPDATE `keeper` SET name = ?, lastname = ?, email = ?, phone = ?, description = ?, picture = ?, password = ?"
                + "WHERE id = ?";
        try {
            ps = conn.prepareStatement(query);
            ps.setString(1, keeper.getName());
            ps.setString(2, keeper.getLastName());
            ps.setString(3, keeper.getEmail());
            ps.setLong(4, keeper.getPhone());
            ps.setString(5, keeper.getDescription());
            fis = new FileInputStream(keeper.getPicture());
            ps.setBinaryStream(6,(InputStream)fis, (int)keeper.getPicture().length());
            ps.setString(7, keeper.getPassword());
            ps.setInt(8, keeper.getId());
            ps.execute();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public boolean delete(int keeper){
        Connection conn = getConnection();
        Statement st;
        String query = "DELETE FROM `keeper` WHERE id = '"+keeper+"'";
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
