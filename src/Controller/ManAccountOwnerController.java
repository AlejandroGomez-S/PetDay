/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import static Controller.ManageRequestOwnerController.showAlert;
import static Controller.RegisterOwnerController.showAlert;
import Model.DaoOwner;
import Model.Hash;
import Model.Owner;
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
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.Window;

/**
 * FXML Controller class
 *
 * @author og218
 */
public class ManAccountOwnerController implements Initializable {

    /**
     * Initializes the controller class.
     */
    private Owner owner;
    @FXML
    private TextField txfName;
    @FXML
    private TextField txfLastName;
    @FXML
    private TextField txfEmail;
    @FXML
    private TextField txfPhone;
    @FXML
    private TextField txfMovil;
    @FXML
    private TextField txfAddress;
    @FXML
    private PasswordField txfLastPassword;
    @FXML
    private PasswordField txfPassword;
    @FXML
    private PasswordField txfRePassword;
    @FXML
    private Button btnUpdate;
    @FXML
    private Button btnCancel;
    private DaoOwner daoOwner;
    private HomeOwnerController ownHomeOwnerController;
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
    public void setOwner(Owner owner, HomeOwnerController ownController){
        this.owner = owner;
        daoOwner = new DaoOwner();
        this.ownHomeOwnerController = ownController;
        showInformation(this.owner.getId());
    }

    @FXML
    private void actionUpdate(ActionEvent event) {
        Window window = txfAddress.getScene().getWindow();
        Owner uOwner = new Owner();
        
        if(!testNameNLastNUser(txfName.getText())){
            showAlert(Alert.AlertType.ERROR, window, "Name must have between 4 and 25 Characteres, no numbers or special"
                    + "characters", "Incorrect Name", "Error");
            return;
        }
        if(!testNameNLastNUser(txfLastName.getText())){
            showAlert(Alert.AlertType.ERROR, window, "Last Name must have between 4 and 25 Characteres, no numbers or special"
                    + "characters", "Incorrect Last Name", "Error");
            return;
        }
        if(!testEmail(txfEmail.getText())){
            showAlert(Alert.AlertType.ERROR, window, "Email must have less than 45 Characteres, "
                    + "must have '@ ' and '.something' ", "Incorrect Email", "Error");
            return;
        }
        if(!testPhone(txfPhone.getText())){
            showAlert(Alert.AlertType.ERROR, window, "Phone mut have 7 numbers ", "Incorrect Phone Number", "Error");
            return;
        }
        if(!testCelular(txfMovil.getText())){
            showAlert(Alert.AlertType.ERROR, window, "Celular Phone mut have 10 numbers ", "Incorrect CelPhone Number", "Error");
            return;
        }
        if(!testAddress(txfAddress.getText())){
            showAlert(Alert.AlertType.ERROR, window, "Address must have between 10 and 40 Characteres, no special "
                    + "characters", "Incorrect Address", "Error");
            return;
        }
        // Comprobar si se quiere actualizar la contraseña 
        if (!txfPassword.getText().isEmpty() || !txfRePassword.getText().isEmpty() || !txfLastPassword.getText().isEmpty()){
            if (!Hash.sha1(txfLastPassword.getText()).equals(this.owner.getPassword())) {
                showAlert(Alert.AlertType.ERROR, window, "The Last Password is incorrect",
                         "Incorrect old Password", "Error");
                return;
            }
            if (!testPassword(txfPassword.getText())) {
                showAlert(Alert.AlertType.ERROR, window, "Password must have between 6 and 15 characteres ",
                         "Incorrect Password", "Error");
                return;
            }
            if (!txfPassword.getText().equals(txfRePassword.getText())) {
                showAlert(Alert.AlertType.ERROR, window, "Passwords must be the same",
                         "Diferent passwords", "Error");
                return;
            }
        }
        uOwner.setId(owner.getId());
        uOwner.setName(txfName.getText());
        uOwner.setLastName(txfLastName.getText());
        uOwner.setEmail(txfEmail.getText());
        uOwner.setPhoneF(Integer.parseInt(txfPhone.getText()));
        uOwner.setPhone(Long.parseLong(txfMovil.getText()));
        uOwner.setAddres(txfAddress.getText());
        if(!txfPassword.getText().isEmpty() || !txfRePassword.getText().isEmpty() || !txfLastPassword.getText().isEmpty()){
            uOwner.setPassword(Hash.sha1(txfPassword.getText()));
        }else{
            uOwner.setPassword("");
        }
        if(daoOwner.update(uOwner)){
            showAlert(Alert.AlertType.INFORMATION, window, "Your data have been correctily updated", "Update made", "Correct");
            Stage stage = (Stage) btnCancel.getScene().getWindow();
            stage.close();
        }else{
            System.out.println("There was a problem");
        }
    }

    @FXML
    private void actionCancel(ActionEvent event) {
        Stage stage = (Stage) btnCancel.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void actionDeleteAccount(ActionEvent event) throws IOException {
        Window win = btnUpdate.getScene().getWindow();
        Alert alert = new Alert(Alert.AlertType.WARNING, "Are you sure? Do you really want to delete your account? \n "
                                 + "This will delete your pet's information", ButtonType.OK, ButtonType.CANCEL);
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.setAlwaysOnTop(true);
        stage.toFront(); 
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            if (daoOwner.delete(this.owner.getId())){
                showAlert(Alert.AlertType.INFORMATION, win, "Your account has been deleted, \n We hope you come back :(", "", "Account Deleted");
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/View/Login.fxml"));
                Parent root = fxmlLoader.load();
                Stage stage2 = new Stage();
                stage2.setTitle("Welcome to PetDay");
                stage2.setScene(new Scene(root));
                Stage actual = (Stage) btnCancel.getScene().getWindow();
                actual.close();
                stage2.show();
                ownHomeOwnerController.close();
            }
        } else {
            System.out.println("Nothing happend");
        }
    }
    
    // Constrains
    private boolean testNameNLastNUser(String text){
        String regex = "[a-zA-Z]{4,25}";
        return Pattern.matches(regex, text);
    }
    
    private boolean testAddress(String text){
        String regex = "[a-zA-z0-9#\\s]{10,40}";
        return Pattern.matches(regex, text);
    }
    
    private boolean testEmail(String text){
        String regex = "^([A-Za-z0-9]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[a-z]{2,3})$";
        return Pattern.matches(regex, text) && text.length() < 45;
    }
    
    private boolean testPhone(String text){
        String regex = "[0-9]{7,7}";
        return Pattern.matches(regex, text);
    }
    
    private boolean testCelular(String text){
        String regex = "[0-9]{10,10}";
        return Pattern.matches(regex, text);
    }
    
    private boolean testPassword(String text){
        // No debe iniciar con numeros
        String regex = "^([a-zA-Z]+)*[a-zA-Z0-9.!#$%&]{6,15}";
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
    
    private void showInformation(int owner){
        Owner ownerSet = daoOwner.getInformation(owner);
        txfName.setText(ownerSet.getName());
        txfLastName.setText(ownerSet.getLastName());
        txfEmail.setText(ownerSet.getEmail());
        txfPhone.setText(""+ownerSet.getPhoneF());
        txfMovil.setText(""+ownerSet.getPhone());
        txfAddress.setText(ownerSet.getAddres());
    }
    
}
