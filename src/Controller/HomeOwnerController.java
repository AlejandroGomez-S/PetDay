/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Model.Owner;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author og218
 */
public class HomeOwnerController implements Initializable {

    @FXML
    private Label lbName;
    @FXML
    private Label lbLastSeason;
    
    private Owner owner;
    @FXML
    private Button btnManagePets;
    @FXML
    private ImageView imgManagePets;
    @FXML
    private Button btnManageRequest;
    @FXML
    private ImageView imgManageRequest;
    @FXML
    private Button btnManageAccount;
    @FXML
    private ImageView imgManageAccount;
    @FXML
    private Button btnLogOut;
    @FXML
    private ImageView imgLogOut;
    
    private HomeOwnerController ownHomeOwnerCotroller;
    
    // Cargar un nuevo stage en la misma ventana y setear cosas al controlador
    Scene fxmlFile;
    Parent root;
    Stage window;
    FXMLLoader fxmlLoader;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
    public void setOwner(Owner owner, HomeOwnerController ownController){
        this.owner = owner;
        this.ownHomeOwnerCotroller = ownController;
        lbName.setText("Welcome "+owner.getName()+" "+owner.getLastName()+ " Have a great day!");
        lbLastSeason.setText("Last login was made at: "+owner.getLastSeason());
    }

    @FXML
    private void actionManagePets(ActionEvent event) throws IOException {
        fxmlLoader = new FXMLLoader(getClass().getResource("/View/ManagePets.fxml"));
        root = fxmlLoader.load();
        ManagePetsController managePetsController = (ManagePetsController)fxmlLoader.getController();
        managePetsController.setOwner(this.owner);
        openWindowModal("Manage your Pets");
    }

    @FXML
    private void actionManageRequests(ActionEvent event) throws IOException {
        fxmlLoader = new FXMLLoader(getClass().getResource("/View/ManageRequestOwner.fxml"));
        root = fxmlLoader.load();
        ManageRequestOwnerController manageRequestOwnerController = (ManageRequestOwnerController)fxmlLoader.getController();
        manageRequestOwnerController.setOwner(this.owner);
        openWindowModal("Manage your Requests");
    }

    @FXML
    private void actionManageAccount(ActionEvent event) throws IOException {
        fxmlLoader = new FXMLLoader(getClass().getResource("/View/ManAccountOwner.fxml"));
        root = fxmlLoader.load();
        ManAccountOwnerController manAccountOwnerController = (ManAccountOwnerController)fxmlLoader.getController();
        manAccountOwnerController.setOwner(this.owner, ownHomeOwnerCotroller);
        openWindowModal("Account Managemet");
    }

    @FXML
    private void actionLogOut(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/View/Login.fxml"));
        Parent root = fxmlLoader.load();
        Stage stage = new Stage();
        stage.setTitle("Welcome to PetDay");
        stage.setScene(new Scene(root));
        Stage actual = (Stage)btnLogOut.getScene().getWindow();
        actual.close();
        stage.show();
    }
    
    private void openWindowModal(String text) throws IOException{
        fxmlFile = new Scene(root);
        window = new Stage();
        window.setScene(fxmlFile);
        window.initModality(Modality.APPLICATION_MODAL);
        window.setAlwaysOnTop(true);
        window.setIconified(false);
        window.setTitle(text);
        window.showAndWait();
    }
    
    // Permite cerrar el stage desde otro stage que se encunetre abierto
    public void close(){
        Stage stage = (Stage) btnLogOut.getScene().getWindow();
        stage.close();
    }
}
