/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Model.DaoKeeper;
import Model.Hash;
import Model.Keeper;
import java.io.File;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.regex.Pattern;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;

/**
 * FXML Controller class
 *
 * @author og218
 */
public class RegisterKeeperController implements Initializable {

    @FXML
    private TextField txfName;
    @FXML
    private TextField txfLastName;
    @FXML
    private TextField txfEmail;
    @FXML
    private ChoiceBox chSex;
    @FXML
    private TextField txfCelular;
    @FXML
    private TextField TxfUser;
    @FXML
    private TextArea txfDescription;
    @FXML
    private TextField txfNit;
    @FXML
    private PasswordField txfPassword;
    @FXML
    private PasswordField txfRePassword;
    
    private ObservableList<String> sexes =  FXCollections.observableArrayList("Male", "Female");

    //Esta sección es la que permite seleccionar una imagen
    private Image image;
    private FileChooser fileChooser; 
    private File file ;
    //     / / / / / / / / / 
    @FXML
    private ImageView imgPhoto;
    @FXML
    private Button btnUpload;
    @FXML
    private Label lbPath;
    @FXML
    private Label lbDescriptionInfo;
   
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        chSex.setValue("Prefer Not to tell");
        chSex.setItems(sexes);
        //Selección del archivo
        file = null;
        fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif")
        );
        
        // Es la función que permite que se ejecute codigo cuando alguno de los text field es seleccionado o deseleccionado
        txfDescription.focusedProperty().addListener(new ChangeListener<Boolean>(){
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if(newValue){
                    lbDescriptionInfo.setText("Tell us Somethig \n about you, \n like the things you like to play \n "
                            + "with the puppets etc...");
                }else{
                    lbDescriptionInfo.setText("");
                }
            } 
        });
        
    }    

    @FXML
    private void actionUpload(ActionEvent event) {
        Stage stage = new Stage();
        stage = (Stage)txfDescription.getScene().getWindow();
        file = fileChooser.showOpenDialog(stage);
        if(file != null){
            try {
                //desktop.open(file);    Esto es por si se desea abrir el archivo
                lbPath.setText(file.getAbsolutePath());
                image = new Image(file.toURI().toString(), 128, 130, true, true);
                imgPhoto.setImage(image);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    public void clear(){
        TxfUser.clear();
        txfName.clear();
        txfLastName.clear();
        txfEmail.clear();
        txfDescription.clear();
        txfCelular.clear();
        txfNit.clear();
        txfPassword.clear();
        txfRePassword.clear();
        chSex.setValue("Prefer not to tell");
    }
    
    public boolean registerKeeper(){
        DaoKeeper dao = new DaoKeeper();
        Keeper keeper = new Keeper();
        Window window = TxfUser.getScene().getWindow();
        Date date  = new Date();
        DateFormat fechaHora = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //Serie de funciones para comprobar constraint de cada uno de los campos 
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
        if(!testCelular(txfCelular.getText())){
            showAlert(Alert.AlertType.ERROR, window, "Celular Phone mut have 10 numbers ", "Incorrect CelPhone Number", "Error");
            return false;
        }
        if(!testNameNLastNUser(TxfUser.getText())){
            showAlert(Alert.AlertType.ERROR, window, "User name must have between 4 and 25 Characteres, no numbers or special"
                    + "characters", "Incorrect User Name", "Error");
            return false;
        }
        if(!testDescription(txfDescription.getText())){
            showAlert(Alert.AlertType.ERROR, window, "Description must have between 5 and 100 Characteres, no numbers or special"
                    + " characters", "Incorrect Description", "Error");
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
        
        if(lbPath.getText().equals("Nothing has been selected")){
            showAlert(Alert.AlertType.ERROR, window, "You must select your picture, so people can see you"
                    , "Show yourself!", "Error");
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
        
        ////// Set los atributos al keeper que se pasan al DaoKeeper
        keeper.setName(txfName.getText());
        keeper.setLastName(txfLastName.getText());
        keeper.setEmail(txfEmail.getText());
        //Sex: 0 -> No dice, 2 -> Male, 1 -> Female ////
        int sexx = 0;
        if(chSex.getValue().equals("Female")){
            sexx = 1;
        }else if(chSex.getValue().equals("Male")){
            sexx = 2;
        }
        ///////////////////////////////////////////////
        keeper.setSex(sexx);
        keeper.setPhone(Long.parseLong(txfCelular.getText()));
        keeper.setDescription(txfDescription.getText());
        keeper.setUser(TxfUser.getText());
        keeper.setNit(Long.parseLong(txfNit.getText()));
        keeper.setPassword(Hash.sha1(txfPassword.getText()));
        keeper.setPicture(file);
        keeper.setLastSeason(fechaHora.format(date));
        return dao.register(keeper);
    }
   
    //Funciones que permiten combrobar el cumplimiento de los contrains // Como para todos es lo mismo, esto pudo haber ido en una clase aparte
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
    //// / / / / / /  //
    
    @FXML
    private void actionNameTyped(KeyEvent event) {
        if(testNameNLastNUser(txfName.getText())){
            txfName.setStyle("-fx-border-color: green");
        }else{
            txfName.setStyle("-fx-border-color:  #eb4d4b");
        }
    }

    @FXML
    private void actionLastNameTyped(KeyEvent event) {
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
    private void actionCelularTyped(KeyEvent event) {
        if(testCelular(txfCelular.getText())){
            txfCelular.setStyle("-fx-border-color: green");
        }else{
            txfCelular.setStyle("-fx-border-color: #eb4d4b");
        }
    }

    @FXML
    private void actionUserTyped(KeyEvent event) {
        if(testNameNLastNUser(TxfUser.getText())){
            TxfUser.setStyle("-fx-border-color: green");
        }else{
            TxfUser.setStyle("-fx-border-color:  #eb4d4b");
        }
    }

    @FXML
    private void actionDescrptionTyped(KeyEvent event) {
        if(testDescription(txfDescription.getText())){
            txfDescription.setStyle("-fx-border-color: green");
        }else{
            txfDescription.setStyle("-fx-border-color:  #eb4d4b");
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
    private void actionRePasswordTyped(KeyEvent event) {
        if(testPassword(txfRePassword.getText())){
            txfRePassword.setStyle("-fx-border-color: green");
        }else{
            txfRePassword.setStyle("-fx-border-color: #eb4d4b");
        }
    }

    public static void showAlert(Alert.AlertType alertype, Window owner, String message, String headerText, String title){
        Alert alert = new Alert(alertype);
        alert.setHeaderText(headerText);
        alert.setContentText(message);
        alert.setTitle(title);
        alert.initOwner(owner);
        alert.showAndWait();
    }
}
