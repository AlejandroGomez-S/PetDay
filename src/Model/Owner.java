/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

/**
 *
 * @author og218
 */
public class Owner extends Person {
    private int phoneF;
    private String addres;

    public String getAddres() {
        return addres;
    }

    public void setAddres(String addres) {
        this.addres = addres;
    }
    
    public int getPhoneF() {
        return phoneF;
    }

    public void setPhoneF(int phoneF) {
        this.phoneF = phoneF;
    }
    
}
