/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Model.DaoKeeper;
import Model.Keeper;
import java.io.FileInputStream;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author og218
 */
public class InfoKeeperController implements Initializable {

    @FXML
    private Label lbPhone;
    @FXML
    private Label lbSex;
    @FXML
    private Label lbEmail;
    @FXML
    private Label lbNit;
    @FXML
    private TextArea lbDescription;
    @FXML
    private Label lbName;
    @FXML
    private ImageView imgPicture;
    @FXML
    private Button btnAcept;
    private Keeper keeper;
    Image image;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
    public void setKeeper(Keeper keeper){
        this.keeper = keeper;
        showInfo();
    }

    @FXML
    private void actionClickAccept(ActionEvent event) {
        Stage stage = (Stage) btnAcept.getScene().getWindow();
        stage.close();
    }
    
    public void showInfo(){
        DaoKeeper daoKeeper = new DaoKeeper();
        if(daoKeeper.getKeeperInfo(this.keeper)){
            lbName.setText(this.keeper.getName()+ " "+this.keeper.getLastName());
            try {
                image = new Image(new FileInputStream(this.keeper.getPicture()),168, 166, true, true);
                imgPicture.setImage(image);
            } catch (Exception e) {
                e.printStackTrace();
            }
            lbPhone.setText(""+this.keeper.getPhone());
            String sex = "";
            if(this.keeper.getSex() == 0){
                sex = "Prefer not tellling";
            }else if(this.keeper.getSex() == 1){
                sex = "Female";
            }else{
                sex = "Male";
            }
            lbSex.setText(sex);
            lbEmail.setText(this.keeper.getEmail());
            lbNit.setText(""+this.keeper.getNit());
            lbDescription.setText(this.keeper.getDescription());
        }else{
            System.out.println("There was a problem");
        }
    }
    
}
