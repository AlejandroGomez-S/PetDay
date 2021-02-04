/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Model.DaoOwner;
import Model.Hash;
import Model.Owner;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Border;
import javafx.stage.Window;

/**
 * FXML Controller class
 *
 * @author og218
 */
public class RegisterOwnerController implements Initializable {

    @FXML
    private TextField txfName;
    @FXML
    private TextField txfLastName;
    @FXML
    private TextField txfEmail;
    @FXML
    private TextField txfPhone;
    @FXML
    private TextField txfCelular;
    @FXML
    private TextField TxfUser;
    @FXML
    private TextField txfAddress;
    @FXML
    private TextField txfNit;
    @FXML
    private PasswordField txfPassword;
    @FXML
    private PasswordField txfRePassword;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    } 
    
    public void clear(){
        TxfUser.clear();
        txfName.clear();
        txfLastName.clear();
        txfEmail.clear();
        txfPhone.clear();
        txfAddress.clear();
        txfNit.clear();
        txfPassword.clear();
        txfRePassword.clear();
        txfCelular.clear(); 
    }
    
    public boolean registerOwner(){
        DaoOwner dao = new DaoOwner();
        Owner owner = new Owner();
        Date date  = new Date();
        DateFormat fechaHora = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Window window = txfAddress.getScene().getWindow();
        // Serie de llamados que muestran un error si se intenta registrar y no se tiene correctamente diligenciado alguno de los campos
        if(!testNameNLastNUser(txfName.getText())){
            showAlert(Alert.AlertType.ERROR, window, "Name must have between 4 and 25 Characteres, no numbers or special"
                    + "characters", "Incorrect Name", "Error");
            return false;
        }
        if(!testNameNLastNUser(txfLastName.getText())){
            showAlert(Alert.AlertType.ERROR, window, "Last Name must have between 4 and 25 Characteres, no numbers or special"
                    + "characters", "Incorrect Last Name", "Error");
            return false;
        }
        if(!testEmail(txfEmail.getText())){
            showAlert(Alert.AlertType.ERROR, window, "Email must have less than 45 Characteres, "
                    + "must have '@ ' and '.something' ", "Incorrect Email", "Error");
            return false;
        }
        if(!testPhone(txfPhone.getText())){
            showAlert(Alert.AlertType.ERROR, window, "Phone mut have 7 numbers ", "Incorrect Phone Number", "Error");
            return false;
        }
        if(!testCelular(txfCelular.getText())){
            showAlert(Alert.AlertType.ERROR, window, "Celular Phone mut have 10 numbers ", "Incorrect CelPhone Number", "Error");
            return false;
        }
        if(!testNameNLastNUser(TxfUser.getText())){
            showAlert(Alert.AlertType.ERROR, window, "User name must have between 4 and 25 Characteres, no numbers or special"
                    + "characters", "Incorrect User Name", "Error");
            return false;
        }
        if(!testAddress(txfAddress.getText())){
            showAlert(Alert.AlertType.ERROR, window, "Address must have between 10 and 40 Characteres, no special "
                    + "characters", "Incorrect Address", "Error");
            return false;
        }
        if(!testCelular(txfNit.getText())){
            showAlert(Alert.AlertType.ERROR, window, "Nit must have 10 Numbers, no special character nor letters"
                    , "Incorrect Nit", "Error");
            return false;
        }
        if(!testPassword(txfPassword.getText())){
            showAlert(Alert.AlertType.ERROR, window, "Password must have between 6 and 15 characteres "
                    , "Incorrect Password", "Error");
            return false;
        }
        if(!txfPassword.getText().equals(txfRePassword.getText())){
            showAlert(Alert.AlertType.ERROR, window, "Passwords must be the same"
                    , "Diferent passwords", "Error");
            return false;
        }
        
        // Funciones que comprueban que no se repita información en la base de datos
        if(dao.existNit(txfNit.getText())){
            showAlert(Alert.AlertType.ERROR, window, "Nit has been already register"
                    , "Wrong Nit", "Error");
            return false;
        }
        if(dao.existEmail(txfEmail.getText())){
           showAlert(Alert.AlertType.ERROR, window, "Email has been already register"
                    , "Wrong Email", "Error");
            return false;
        }
        if(dao.existUser(TxfUser.getText())){
            showAlert(Alert.AlertType.ERROR, window, "Username has been already taken, choose another one"
                    , "Wrong Username ", "Error");
            return false;
        }
        //Set los atributos a un owner y eso pasarlo al DadOwner
        owner.setName(txfName.getText());
        owner.setLastName(txfLastName.getText());
        owner.setEmail(txfEmail.getText());
        owner.setPhoneF(Integer.parseInt(txfPhone.getText()));
        owner.setPhone(Long.parseLong(txfCelular.getText()));
        owner.setUser(TxfUser.getText());
        owner.setAddres(txfAddress.getText());
        owner.setNit(Long.parseLong(txfNit.getText()));
        owner.setPassword(Hash.sha1(txfPassword.getText()));
        owner.setLastSeason(fechaHora.format(date));
        return dao.register(owner);
    }

    public static void showAlert(Alert.AlertType alertype, Window owner, String message, String headerText, String title){
        Alert alert = new Alert(alertype);
        alert.setHeaderText(headerText);
        alert.setContentText(message);
        alert.setTitle(title);
        alert.initOwner(owner);
        alert.showAndWait();
    }

    //Serie de funciones que comprueban que se cumplan los constrains de cada uno de los campos
    
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
    
    // Serie de funciones que hacen que cambie el color de cada cuadro hasta que este lleno apropiedamente
    @FXML
    private void actionNameType(KeyEvent event) {
        if(testNameNLastNUser(txfName.getText())){
            txfName.setStyle("-fx-border-color: green");
        }else{
            txfName.setStyle("-fx-border-color:  #eb4d4b");
        }
    }

    @FXML
    private void ationLastNameTyped(KeyEvent event) {
        if(testNameNLastNUser(txfLastName.getText())){
            txfLastName.setStyle("-fx-border-color: green");
        }else{
            txfLastName.setStyle("-fx-border-color:  #eb4d4b");
        }
    }

    @FXML
    private void actionEmailTyped(KeyEvent event) {
        if(testEmail(txfEmail.getText())){
            txfEmail.setStyle("-fx-border-color: green");
        }else{
            txfEmail.setStyle("-fx-border-color: #eb4d4b");
        } 
    }

    @FXML
    private void actionPhoneTyped(KeyEvent event) {
        if(testPhone(txfPhone.getText())){
            txfPhone.setStyle("-fx-border-color: green");
        }else{
            txfPhone.setStyle("-fx-border-color: #eb4d4b");
        }
    }

    @FXML
    private void actionCellPhoneTyped(KeyEvent event) {
        if(testCelular(txfCelular.getText())){
            txfCelular.setStyle("-fx-border-color: green");
        }else{
            txfCelular.setStyle("-fx-border-color: #eb4d4b");
        }
    }

    @FXML
    private void actionUserNameTyped(KeyEvent event) {
        if(testNameNLastNUser(TxfUser.getText())){
            TxfUser.setStyle("-fx-border-color: green");
        }else{
            TxfUser.setStyle("-fx-border-color:  #eb4d4b");
        }
    }

    @FXML
    private void actionAddresTyped(KeyEvent event) {
        if(testAddress(txfAddress.getText())){
            txfAddress.setStyle("-fx-border-color: green");
        }else{
            txfAddress.setStyle("-fx-border-color:  #eb4d4b");
        }
    }

    @FXML
    private void actionNitTyped(KeyEvent event) {
        if(testCelular(txfNit.getText())){
            txfNit.setStyle("-fx-border-color: green");
        }else{
            txfNit.setStyle("-fx-border-color: #eb4d4b");
        }
    }

    @FXML
    private void actionPasswordTyped(KeyEvent event) {
        if(testPassword(txfPassword.getText())){
            txfPassword.setStyle("-fx-border-color: green");
        }else{
            txfPassword.setStyle("-fx-border-color: #eb4d4b");
        }
    }

    @FXML
    private void actionRepasswordTyped(KeyEvent event) {
         if(testPassword(txfRePassword.getText())){
            txfRePassword.setStyle("-fx-border-color: green");
        }else{
            txfRePassword.setStyle("-fx-border-color: #eb4d4b");
        }
    }
 
}

