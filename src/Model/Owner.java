
package Model;

/**
 *
 * @author Oscar Alejandro Gómez Suarez
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
