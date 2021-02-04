
package Model;

import java.io.File;

/**
 *
 * @author Oscar Alejandro Gómez Suarez
 */
public class Keeper extends Person{
    private int sex;
    private String description;
    private File picture;

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public File getPicture() {
        return picture;
    }

    public void setPicture(File picture) {
        this.picture = picture;
    }
    
    
}
