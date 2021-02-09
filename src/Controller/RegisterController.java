
package Controller;

import Model.Owner;
import java.io.IOException;
import java.net.URL;
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
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.Window;

/**
 * FXML Controller class
 *
 * @author Oscar Alejandro Gómez Suarez
 */
public class RegisterController implements Initializable {

    @FXML
    private AnchorPane anchorRegisterZone;
    @FXML
    private ChoiceBox chRole;
    @FXML
    private Button btnRegister;
    @FXML
    private Button btnClear;
    @FXML
    private Button btnBack;
    //Permiten ejecutar las funciones del otro controlador, y sin que se tengan que cargar otros.
    private FXMLLoader registrerOwnerContrl;
    private FXMLLoader registerkeeperControler;
    
    // Para mantener el choice Picker
    private ObservableList<String> roles =  FXCollections.observableArrayList("Owner", "Keeper");
    @FXML
    private Pane panetoHide;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        chRole.setValue("Select the Role");
        chRole.setItems(roles);
        chRole.setOnAction((event) -> {
            panetoHide.setVisible(false);
            String selection = (String) chRole.getValue();
            if(selection.equals("Owner")){
                // Se asigna el fxml a la variable de la clase para obtener acceso al controlador
                // especifico del fxml que esta siendo actualmente cargado en el achorpane
                registrerOwnerContrl = loadAndPlaceFXML("/View/RegisterOwner.fxml");
                btnRegister.setDisable(false);
                btnClear.setDisable(false);
            }else{
                registerkeeperControler = loadAndPlaceFXML("/View/RegisterKeeper.fxml");
                btnRegister.setDisable(false);
                btnClear.setDisable(false);
            }
        });
    }    
    
    @FXML
    private void actionRegister(ActionEvent event) throws IOException {
        // Esta ventana es necesaria para lanzar mensajes desde esta
        Window owner = btnBack.getScene().getWindow();
        
        if(chRole.getValue().equals("Owner")){
            RegisterOwnerController ownerController = registrerOwnerContrl.getController();
            if(ownerController.registerOwner()){
                showAlert(Alert.AlertType.INFORMATION,owner , "Registro Exitoso" ,"Correcto" , "Succes");
                goBack();
            }else{
                System.out.println("Error al momento de registrar el owner");
            }  
        }else{
            RegisterKeeperController keeperController = registerkeeperControler.getController();
            if(keeperController.registerKeeper()){
                showAlert(Alert.AlertType.INFORMATION,owner , "Registro Exitoso" ,"Correcto" , "Succes");
                goBack();
            }else{
                System.out.println("Error al momento de hacer el registro del keeper");
            }
        }
    }

    @FXML
    private void actionClear(ActionEvent event) throws IOException {
        if(chRole.getValue().equals("Owner")){
            RegisterOwnerController ownerController = registrerOwnerContrl.getController(); 
            ownerController.clear();
        }else{
           RegisterKeeperController keeperController = registerkeeperControler.getController(); 
           keeperController.clear();
        }
    }

    @FXML
    private void actionBack(ActionEvent event) throws IOException {
        goBack();
    }
     
   
    private void goBack() throws IOException{
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/View/Login.fxml"));
        Parent root = fxmlLoader.load();
        Stage stage = new Stage();
        stage.setTitle("PetDay! Welcome");
        stage.setScene(new Scene(root));
        Stage actual = (Stage)btnBack.getScene().getWindow();
        actual.close();
        stage.show();
    }
   
    //Permite cargar un FXML dentro de un AnchorPane, esto para tener varias cosas dentro de la misma pestaña
    private FXMLLoader loadAndPlaceFXML(String resource){
        try {
            anchorRegisterZone.getChildren().clear();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(resource));
            Parent root = fxmlLoader.load();
            AnchorPane anchor = new AnchorPane(root);
            anchorRegisterZone.getChildren().add(root);
            anchorRegisterZone.setVisible(true);
            //Este lo retorno para poder obtener esta instacia y poder acceder a este controlador unicamente.
            return fxmlLoader;
        } catch (Exception e) {
            e.printStackTrace();
             System.out.println("NO lo esta logrando");
             return null;
        }
    }
    
    //Permite mostrar mensajes 
    public static void showAlert(Alert.AlertType alertype, Window owner, String message, String headerText, String title){
        Alert alert = new Alert(alertype);
        alert.setHeaderText(headerText);
        alert.setContentText(message);
        alert.setTitle(title);
        alert.initOwner(owner);
        alert.showAndWait();
    }
    
}
