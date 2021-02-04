/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Model.Keeper;
import java.io.FileInputStream;
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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author og218
 */
public class HomeKeeperController implements Initializable {

    @FXML
    private Label lbName;
    @FXML
    private Label lbLastTime;
    @FXML
    private ImageView imgPicture;
    
    private Keeper keeper;
    private Image image;
    @FXML
    private Button btnlogOut;
    @FXML
    private Pane paneRequests;
    
    private FXMLLoader fxmlLoaderPanes;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    } 
    
    public void setKeeper(Keeper keeper){
        this.keeper = keeper;
        lbName.setText("Welcome back "+keeper.getName()+" "+keeper.getLastName()+". Have a great day!");
        lbLastTime.setText("Last Connection: "+keeper.getLastSeason());
        try {
            image = new Image(new FileInputStream(keeper.getPicture()),139, 140, true, true);
            imgPicture.setImage(image);
        } catch (Exception e) {
            System.out.println("Problem load the picture");
        }
        loadAndPlaceFXMLTotalRequest();
    }

    @FXML
    private void actionLogOut(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/View/Login.fxml"));
        Parent root = fxmlLoader.load();
        Stage stage = new Stage();
        stage.setTitle("Welcome to PetDay");
        stage.setScene(new Scene(root));
        Stage actual = (Stage)btnlogOut.getScene().getWindow();
        actual.close();
        stage.show();
    }
    
     @FXML
    private void actionClickTotal(ActionEvent event) {
        loadAndPlaceFXMLTotalRequest();
    }

    @FXML
    private void actionClickAccepted(ActionEvent event) {
        loadAndPlaceFXMLAcceptedRequest();
    }

    private void loadAndPlaceFXMLTotalRequest() {
        try {
            paneRequests.getChildren().clear();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/View/RequestTotal.FXML"));
            Parent root = fxmlLoader.load();
            RequestTotalController requestTotalController = (RequestTotalController) fxmlLoader.getController();
            requestTotalController.setKeeper(this.keeper);
            Pane pane = new Pane(root);
            paneRequests.getChildren().add(root);
            paneRequests.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadAndPlaceFXMLAcceptedRequest() {
        try {
            paneRequests.getChildren().clear();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/View/RequestAccepted.FXML"));
            Parent root = fxmlLoader.load();
            RequestAcceptedController requestAcceptedController = (RequestAcceptedController) fxmlLoader.getController();
            requestAcceptedController.setKeeper(this.keeper);
            Pane pane = new Pane(root);
            paneRequests.getChildren().add(root);
            paneRequests.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    } 

    @FXML
    private void actionManageAccount(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/View/ManAccountKeeper.fxml"));
        Parent root = fxmlLoader.load();
        ManAccountKeeperController manAcountKeeperController = (ManAccountKeeperController)fxmlLoader.getController();
        manAcountKeeperController.setKeeper(this.keeper);
        Stage stage = new Stage();
        stage.setTitle("Manage Keeper Acount");
        stage.setScene(new Scene(root));
        Stage actual = (Stage)btnlogOut.getScene().getWindow();
        actual.close();
        stage.show();
    }
    
}
