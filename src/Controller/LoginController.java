
package Controller;

import Model.DaoKeeper;
import Model.DaoOwner;
import Model.Hash;
import Model.Keeper;
import Model.Owner;
import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.Window;
import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

/**
 * FXML Controller class
 *
 * @author Oscar Alejandro Gómez Suarez
 */
public class LoginController implements Initializable {

    @FXML
    private ChoiceBox chRole; 
    @FXML
    private Hyperlink hpRegistro;
    @FXML
    private Pane paneLogin;
    @FXML
    private TextField txfUser;
    @FXML
    private PasswordField txfPassword;
    @FXML
    private Button btnSing;
    @FXML
    private Button btnClear;
    
    @FXML
    private RadioButton rbOwner;
    @FXML
    private RadioButton rbKeeper;
    
    private ToggleGroup group; 

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        //Es la parte del selector 
        if(rbOwner.isSelected()){
            paneLogin.setVisible(true);
            paneLogin.setStyle("-fx-background-color: rgba(247,49,99,0.22)");
        }
    }

    @FXML
    private void actionSingUp(ActionEvent event) throws IOException {
        openNewStage("/View/Register.fxml", "Register");
    }
    
    //Permite mostrar mensajes de información
    public static void infoBox(String infoMessage, String headerText, String title){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText(headerText);
        alert.setContentText(infoMessage);
        alert.setTitle(title);
        alert.showAndWait();
    }
    
    //Permite Mostrar alertas
    public static void showAlert(Alert.AlertType alertype, Window owner, String message, String headerText, String title){
        Alert alert = new Alert(alertype);
        alert.setHeaderText(headerText);
        alert.setContentText(message);
        alert.setTitle(title);
        alert.initOwner(owner);
        alert.show();
    }

    @FXML
    private void actionSingIn(ActionEvent event) throws IOException {
        Window window = btnClear.getScene().getWindow();
        Date date  = new Date();
        DateFormat fechaHora = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        
        if(rbOwner.isSelected()){
            DaoOwner daoOwner = new DaoOwner();
            Owner owner = new Owner();
            owner.setUser(txfUser.getText());
            owner.setPassword(Hash.sha1(txfPassword.getText()));
            owner.setLastSeason(fechaHora.format(date));
            if(daoOwner.signIn(owner)){
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/View/HomeOwner.fxml"));
                Parent root = fxmlLoader.load();
                Stage stage = new Stage();
                stage.setTitle("Welcome Owner");
                stage.setScene(new Scene(root));
                HomeOwnerController homeOwnerController = (HomeOwnerController) fxmlLoader.getController();
                homeOwnerController.setOwner(owner, homeOwnerController);
                Stage actual = (Stage) btnSing.getScene().getWindow();
                actual.close();
                stage.show();
                showAlert(Alert.AlertType.ERROR, window, "Login Succecs", "Succes", "Rigth");
            }else{
                showAlert(Alert.AlertType.ERROR, window, "User or password mistake", "Look at data", "Error");
            }

        }else{
            DaoKeeper daoKeeper = new DaoKeeper();
            Keeper keeper = new Keeper();
            keeper.setUser(txfUser.getText());
            keeper.setPassword(Hash.sha1(txfPassword.getText()));
            keeper.setLastSeason(fechaHora.format(date));
            if(daoKeeper.singIn(keeper)){
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/View/HomeKeeper.fxml"));
                Parent root = fxmlLoader.load();
                Stage stage = new Stage();
                stage.setTitle("Welcome Keeper");
                stage.setScene(new Scene(root));
                HomeKeeperController homeKeeperController = (HomeKeeperController) fxmlLoader.getController();
                homeKeeperController.setKeeper(keeper);
                Stage actual = (Stage) btnSing.getScene().getWindow();
                actual.close();
                stage.show();
                showAlert(Alert.AlertType.ERROR, window, "Ingreso Exitoso", "Correcto", "Rigth");
            }else{
                showAlert(Alert.AlertType.ERROR, window, "Usuario o contrasena icorrecto", "Look at data", "Error");
            }
        } 
    }

    @FXML
    private void actionClear(ActionEvent event) {
        txfPassword.clear();
        txfUser.clear();
    }
    
    //Carga un nuevo FXML y cierra el anterior
    private void openNewStage(String resource, String text) throws IOException{
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(resource));
        Parent root = fxmlLoader.load();
        Stage stage = new Stage();
        stage.setTitle(text);
        stage.setScene(new Scene(root));
        Stage actual = (Stage)btnSing.getScene().getWindow();
        actual.close();
        stage.show();
    }

    @FXML
    private void actionRbOwner(ActionEvent event) throws AddressException, MessagingException {
        paneLogin.setStyle("-fx-background-color: rgba(247,49,99,0.22)");
        rbKeeper.setSelected(false);
        rbOwner.setSelected(true);
    }

    @FXML
    private void actionRdKeeper(ActionEvent event) {
        paneLogin.setStyle("-fx-background-color: rgba(9,126,217,0.22)");
        rbOwner.setSelected(false);
        rbKeeper.setSelected(true);
    }
    
    
}
