/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableListBase;
/**
 *
 * @author og218
 */
public class DaoRequest extends Dao{
    
    public boolean register(Request request){
        Connection conn = getConnection();
        PreparedStatement ps;
        String query = "INSERT INTO `request` (`dateini`, `datefin`, `owner`, `pet`) VALUES (?, ?, ?, ?)";
        try {
            ps = conn.prepareStatement(query);
            ps.setString(1, request.getDateIni());
            ps.setString(2, request.getDateFin());
            ps.setInt(3, request.getOwner());
            ps.setInt(4, request.getPet());
            ps.execute();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public boolean delete(int request){
        Connection conn = getConnection();
        PreparedStatement ps;
        String query= "DELETE FROM `request` WHERE id = ?";
        try {
            ps = conn.prepareStatement(query);
            ps.setInt(1, request);
            ps.execute();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public ArrayList<Request> getRequests(int owner){
        Connection conn = getConnection();
        ArrayList<Request> requestList = new ArrayList();
        PreparedStatement ps;
        ResultSet rs;
        String query = "SELECT * FROM `request` WHERE owner = ? AND datefin > NOW()";
        try {
            ps = conn.prepareStatement(query);
            ps.setInt(1, owner);
            rs = ps.executeQuery();
            while(rs.next()){
                Request re = new Request();
                re.setId(rs.getInt("id"));
                re.setDateIni(rs.getString("dateini"));
                re.setDateFin(rs.getString("datefin"));
                re.setPet(rs.getInt("pet"));
                re.setKeeper(rs.getInt("keeper"));
                requestList.add(re);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return requestList;
    }
    
    public ObservableList<Request> getTotalRequest(){
        ObservableList<Request> requests = FXCollections.observableArrayList();
        Connection conn = getConnection();
        Statement st;
        ResultSet rs;
        String query = "SELECT * FROM `request` WHERE keeper IS NULL AND datefin > NOW()";
        try {
            st = conn.createStatement();
            rs = st.executeQuery(query);
            while(rs.next()){
                Request request = new Request();
                request.setId(rs.getInt("id"));
                request.setDateIni(rs.getString("dateini"));
                request.setDateFin(rs.getString("datefin"));
                request.setPet(rs.getInt("pet"));
                request.setOwner(rs.getInt("owner"));
                requests.add(request);
            }
            return requests;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public boolean getInfoPetOwner(int request, Pet pet, Owner owner){
        Connection conn = getConnection();
        PreparedStatement ps;
        ResultSet rs;
        // Esto se puede hacer solo con el request.id y NATURAL JOIN, si se definen pk y fk de manera correcta
        String query = "SELECT owner.name, owner.address, owner.movilphone, pets.name, pets.information, pets.picture, pets.sex, " +
                        "owner.lastname, owner.phone, owner.email, pets.age, pets.type " +
                        "FROM request JOIN owner JOIN pets " +
                        "WHERE request.id  = ? AND owner.id = ? AND pets.id = ?";
        try {
            ps = conn.prepareStatement(query);
            ps.setInt(1, request);
            ps.setInt(2, owner.getId());
            ps.setInt(3, pet.getId());
            rs = ps.executeQuery();
            while(rs.next()){
                // Aqui se esta pasando la información es por el número de la columna
                owner.setName(rs.getString(1));
                owner.setAddres(rs.getString(2));
                owner.setPhone(rs.getLong(3));
                pet.setName(rs.getString(4));
                pet.setInformation(rs.getString(5));
                //Permite recuperar la imagen de la base de datos, convierte el blob en file
                InputStream is = rs.getBinaryStream(6);
                File file = new File("photo.jpg");
                OutputStream os =  new FileOutputStream(file);
                byte[] content = new byte[1024];
                int size = 0;
                while ((size = is.read(content)) != -1) {
                    os.write(content, 0, size);
                }
                pet.setPicture(file);
                // Fin de la lectura de la imagen
                pet.setSex(rs.getInt(7));
                // Desde aqui comienza la parte para los aceptados
                owner.setLastName(rs.getString(8));
                owner.setPhoneF(rs.getInt(9));
                owner.setEmail(rs.getString(10));
                pet.setAge(rs.getInt(11));
                pet.setType(rs.getString(12));
                os.close();
                is.close();
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public boolean assingKeeper(int request, int keeper){
        Connection conn = getConnection();
        PreparedStatement ps;
        String query = "UPDATE `request` SET keeper = ? WHERE id = ?";
        try {
            ps = conn.prepareStatement(query);
            ps.setInt(1, keeper);
            ps.setInt(2, request);
            ps.execute();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public ObservableList<Request> getAcceptedRequest(int keeper){
        ObservableList<Request> requests = FXCollections.observableArrayList();
        Connection conn = getConnection();
        Statement st;
        ResultSet rs;
        String query = "SELECT * FROM `request` WHERE keeper = '"+keeper+"'";
        try {
            st = conn.createStatement();
            rs = st.executeQuery(query);
            while(rs.next()){
                Request request = new Request();
                request.setId(rs.getInt("id"));
                request.setDateIni(rs.getString("dateini"));
                request.setDateFin(rs.getString("datefin"));
                request.setPet(rs.getInt("pet"));
                request.setOwner(rs.getInt("owner"));
                requests.add(request);
            }
            return requests;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public boolean declineRequest(int request){
        Connection conn = getConnection();
        PreparedStatement ps;
        String query = "UPDATE `request` SET keeper = NULL WHERE id = ?";
        try {
            ps =conn.prepareStatement(query);
            ps.setInt(1, request);
            ps.execute();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    
    
}
