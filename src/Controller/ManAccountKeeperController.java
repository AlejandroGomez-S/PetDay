/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import static Controller.RegisterKeeperController.showAlert;
import Model.DaoKeeper;
import Model.Hash;
import Model.Keeper;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.regex.Pattern;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;

/**
 * FXML Controller class
 *
 * @author og218
 */
public class ManAccountKeeperController implements Initializable {

    private Keeper keeper;
    @FXML
    private TextField txfName;
    @FXML
    private TextField txfLastName;
    @FXML
    private TextField txfPhone;
    @FXML
    private TextField txfEmail;
    @FXML
    private TextArea txfDescription;
    @FXML
    private Button btnUploadPicture;
    @FXML
    private Label lbPathPicture;
    @FXML
    private PasswordField txfLastPassword;
    @FXML
    private PasswordField txfNewPassword;
    @FXML
    private PasswordField txfRePassword;
    private DaoKeeper daoKeeper;
    
    private Image image;
    private FileChooser fileChooser; 
    private File file ;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        file = null;
        fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif")
        );
        
    }    
    
    public void setKeeper(Keeper keeper){
        this.keeper = keeper;
        daoKeeper = new DaoKeeper();
        showInfoKeeper();
        
    }

    @FXML
    private void actionUploadPicture(ActionEvent event) {
        Stage stage = new Stage();
        stage = (Stage)btnUploadPicture.getScene().getWindow();
        file = fileChooser.showOpenDialog(stage);
        if(file != null){
            try {
                lbPathPicture.setText(file.getAbsolutePath());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    
    @FXML
    private void actionUpdate(ActionEvent event) throws IOException {
        Window win  = btnUploadPicture.getScene().getWindow();
        
        if(!testNameNLastNUser(txfName.getText())){
            showAlert(Alert.AlertType.ERROR, win, "Name must have between 4 and 25 Characteres, no numbers or special"
                    + "characters", "Incorrect Name", "Error");
            return;
        }
        if(!testNameNLastNUser(txfLastName.getText())){
            showAlert(Alert.AlertType.ERROR, win, "Last Name must have between 4 and 25 Characteres, no numbers or special"
                    + "characters", "Incorrect Last Name", "Error");
            return;
        }
        if(!testEmail(txfEmail.getText())){
            showAlert(Alert.AlertType.ERROR, win, "Email must have less than 45 Characteres, "
                    + "must have '@ ' and '.something' ", "Incorrect Email", "Error");
            return;
        }
        if(!testCelular(txfPhone.getText())){
            showAlert(Alert.AlertType.ERROR, win, "Celular Phone mut have 10 numbers ", "Incorrect CelPhone Number", "Error");
            return;
        }
        if(!testDescription(txfDescription.getText())){
            showAlert(Alert.AlertType.ERROR, win, "Description must have between 5 and 100 Characteres, no numbers or special"
                    + " characters", "Incorrect Description", "Error");
            return;
        }
        
        if (!txfLastPassword.getText().isEmpty() || !txfNewPassword.getText().isEmpty() || !txfRePassword.getText().isEmpty()) {
            if(!Hash.sha1(txfLastPassword.getText()).equals(this.keeper.getPassword())){
                showAlert(Alert.AlertType.ERROR, win, "The Last password is incorrect, how did you entered?",
                         "Incorrect last Password", "Error");
                return;
            }
            if (!testPassword(txfNewPassword.getText())) {
                showAlert(Alert.AlertType.ERROR, win, "Password must have between 6 and 15 characteres ",
                         "Incorrect Password", "Error");
                return;
            }
            if (!txfNewPassword.getText().equals(txfRePassword.getText())) {
                showAlert(Alert.AlertType.ERROR, win, "Passwords must be the same",
                         "Diferent passwords", "Error");
                return;
            }
        }
        // Fin de las restriccionesm y comienzo como tal de la función
        
        Keeper updatekeeper  = new Keeper();
        updatekeeper.setId(this.keeper.getId());
        updatekeeper.setName(txfName.getText());
        updatekeeper.setLastName(txfLastName.getText());
        updatekeeper.setEmail(txfEmail.getText());
        updatekeeper.setPhone(Long.parseLong(txfPhone.getText()));
        updatekeeper.setDescription(txfDescription.getText());
        updatekeeper.setPicture(file);
        if(!txfNewPassword.getText().isEmpty() || !txfRePassword.getText().isEmpty() || !txfLastPassword.getText().isEmpty()){
           updatekeeper.setPassword(Hash.sha1(txfNewPassword.getText()));
        }else{
            updatekeeper.setPassword(this.keeper.getPassword());
        }
        if(daoKeeper.update(updatekeeper)){
            showAlert(Alert.AlertType.INFORMATION, win, "Your information has been correctily updated", "Update made", "Correct");
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/View/HomeKeeper.fxml"));
            Parent root = fxmlLoader.load();
            HomeKeeperController homeKeeperController = (HomeKeeperController)fxmlLoader.getController();
            homeKeeperController.setKeeper(this.keeper);
            openNewStage(root, "Welcome Keeper");
        }else{
            showAlert(Alert.AlertType.ERROR, win, "There was a mistake", "Please try again", "Error");
        }
        
    }

    @FXML
    private void actionDelete(ActionEvent event) throws IOException {
        Window win = btnUploadPicture.getScene().getWindow();
        Alert alert = new Alert(Alert.AlertType.WARNING, "Are you sure, this is a good change to make some money \n "
             + "This will delete your infomation and the request you have accepted", ButtonType.OK, ButtonType.CANCEL);
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.setAlwaysOnTop(true);
        stage.toFront(); 
        Optional<ButtonType> result = alert.showAndWait();
        if(result.get() == ButtonType.OK){
            if(daoKeeper.delete(this.keeper.getId())){
                showAlert(Alert.AlertType.INFORMATION, win, "Your Account has been deleted", "", ":`(");
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/View/Login.fxml"));
                Parent root = fxmlLoader.load();
                openNewStage(root, "Welcome to PetDay");
            }else{
                showAlert(Alert.AlertType.ERROR, win, "There was a mistake", "Please try again", "Error");
            }
        }
    }

    @FXML
    private void actionCancel(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/View/HomeKeeper.fxml"));
        Parent root = fxmlLoader.load();
        HomeKeeperController homeKeeperController = (HomeKeeperController)fxmlLoader.getController();
        homeKeeperController.setKeeper(this.keeper);
        openNewStage(root, "Welcome Keeper");
    }
    
    private void showInfoKeeper(){
        Keeper rKeeper = new Keeper();
        rKeeper.setId(this.keeper.getId());
        if(daoKeeper.getKeeperInfo(rKeeper)){
           txfName.setText(rKeeper.getName());
           txfLastName.setText(rKeeper.getLastName());
           txfPhone.setText(rKeeper.getPhone()+"");
           txfEmail.setText(rKeeper.getEmail());
           txfDescription.setText(rKeeper.getDescription());
           file = rKeeper.getPicture();
        }else{
            System.out.println("there was a mistake, it shouldn't have happeeeend");
        }   
    }
    
    ////// / / / / / Constrains
    private boolean testNameNLastNUser(String text){
        String regex = "[a-zA-Z]{4,25}";
        return Pattern.matches(regex, text);
    }
    
    private boolean testEmail(String text){
        String regex = "^([A-Za-z0-9]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[a-z]{2,3})$";
        return Pattern.matches(regex, text) && text.length() < 45;
    }
    
    private boolean testCelular(String text){
        String regex = "[0-9]{10,10}";
        return Pattern.matches(regex, text);
    }
    
    private boolean testPassword(String text){
        String regex = "^([a-zA-Z]+)*[a-zA-Z0-9.!#$%&]{6,15}";
        return Pattern.matches(regex, text);
    }
    
    private boolean testDescription(String text){
        String regex = "[a-zA-Z\\s,.]{5,100}";
        return Pattern.matches(regex, text);
    }
    
    public static void showAlert(Alert.AlertType alertype, Window owner, String message, String headerText, String title){
        Alert alert = new Alert(alertype);
        alert.setHeaderText(headerText);
        alert.setContentText(message);
        alert.setTitle(title);
        alert.initOwner(owner);
        alert.showAndWait();
    }
    
    
    private void openNewStage(Parent root, String text) throws IOException{
        Stage stage = new Stage();
        stage.setTitle(text);
        stage.setScene(new Scene(root));
        Stage actual = (Stage)btnUploadPicture.getScene().getWindow();
        actual.close();
        stage.show();
    }
}
