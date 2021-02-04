/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author og218
 */
public class DaoPet extends Dao{
     private FileInputStream fis;
     
     public boolean register(Pet pet) {
        Connection conn = getConnection();
        PreparedStatement ps = null;
        String query = "INSERT INTO `pets` (`name`, `information`, `picture`, `type`, `age`, `sex`,"
                + " `owner`) VALUES (?,?,?,?,?,?,?)";
        try {
            ps = conn.prepareStatement(query);
            ps.setString(1, pet.getName());
            ps.setString(2, pet.getInformation());
            fis = new FileInputStream(pet.getPicture());
            ps.setBinaryStream(3, (InputStream) fis, (int) pet.getPicture().length());
            ps.setString(4, pet.getType());
            ps.setInt(5, pet.getAge());
            ps.setInt(6, pet.getSex());
            ps.setInt(7, pet.getOwner());
            ps.execute();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
     
    public ObservableList<Pet> getPets(int owner){
        ObservableList<Pet> petList = FXCollections.observableArrayList();
        Connection conn = getConnection();
        String query = "SELECT name FROM `pets` WHERE owner = ?";
        PreparedStatement ps;
        ResultSet rs;
        try {
           ps = conn.prepareStatement(query);
           ps.setInt(1, owner);
           rs = ps.executeQuery();
           Pet pet;
           while(rs.next()){
               pet = new Pet();
               pet.setName(rs.getString("name"));
               petList.add(pet);        
           }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return petList;
    }
    
    //Esta permite llenar el chooser de los request
    public ObservableList<String> getPetsName(int owner){
        ObservableList<String> petList = FXCollections.observableArrayList();
        Connection conn = getConnection();
        String query = "SELECT name FROM `pets` WHERE owner = ?";
        PreparedStatement ps;
        ResultSet rs;
        try {
           ps = conn.prepareStatement(query);
           ps.setInt(1, owner);
           rs = ps.executeQuery();
           Pet pet;
           while(rs.next()){
               petList.add(rs.getString("name"));        
           }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return petList;
    }
    
    
    public boolean getPetByName(int owner, Pet petRow){
        Connection conn = getConnection();
        String query  = "SELECT * FROM `pets` WHERE name = ? AND owner = ?";
        PreparedStatement ps;
        ResultSet rs;
        try {
            ps = conn.prepareStatement(query);
            ps.setString(1, petRow.getName());
            ps.setInt(2, owner);
            rs = ps.executeQuery();
            while(rs.next()){
                petRow.setAge(rs.getInt("age"));
                petRow.setId(rs.getInt("id"));
                petRow.setType(rs.getString("type"));
                petRow.setSex(rs.getInt("sex"));
                petRow.setInformation(rs.getString("information"));
                //Permite traer la imagen de la base de datos, como es blob, es necesario retormarla como output stream y luego eecribir el contenido byte a byte en un nuevo archivo desde una matriz de bytes
                InputStream is = rs.getBinaryStream("picture");
                File file = new File("photo.jpg");
                OutputStream os = new FileOutputStream(file);
                byte[] content = new byte[1024];
                int size = 0;
                while ((size = is.read(content)) != -1) {
                    os.write(content, 0, size);
                }
                petRow.setPicture(file);
                os.close();
                is.close();
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public boolean uploadPetPicture(Pet pet){
        Connection conn = getConnection();
        PreparedStatement ps;
        String query = "UPDATE `pets` SET name = ?, information = ?, age = ?, type = ?, sex = ?, picture = ? WHERE id = ?";
        try {
            ps = conn.prepareStatement(query);
            ps.setString(1, pet.getName());
            ps.setString(2, pet.getInformation());
            ps.setInt(3, pet.getAge());
            ps.setString(4, pet.getType());
            ps.setInt(5, pet.getSex());
            fis = new FileInputStream(pet.getPicture());
            ps.setBinaryStream(6, (InputStream) fis, (int) pet.getPicture().length());
            ps.setInt(7, pet.getId());
            ps.execute();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public boolean uploadPetNoPicture(Pet pet){
        Connection conn = getConnection();
        PreparedStatement ps;
        String query = "UPDATE `pets` SET name = ?, information = ?, age = ?, type = ?, sex = ? WHERE id = ?";
        try {
            ps = conn.prepareStatement(query);
            ps.setString(1, pet.getName());
            ps.setString(2, pet.getInformation());
            ps.setInt(3, pet.getAge());
            ps.setString(4, pet.getType());
            ps.setInt(5, pet.getSex());
            ps.setInt(6, pet.getId());
            ps.execute();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public boolean deletePet(int id){
        Connection conn = getConnection();
        PreparedStatement ps;
        String query = "DELETE FROM `pets` WHERE id = ?";
        try {
            ps = conn.prepareStatement(query);
            ps.setInt(1, id);
            ps.execute();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;   
    }
    
    public String getNameById(int id){
        Connection conn = getConnection();
        PreparedStatement ps;
        ResultSet rs;
        String query = " SELECT name FROM `pets` WHERE id = ?";
        try {
            ps = conn.prepareStatement(query);
            ps.setInt(1, id);
            rs = ps.executeQuery();
            while(rs.next()){
                return rs.getString("name");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "Not Foiund";
    }
    
     
}
