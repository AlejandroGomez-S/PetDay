/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Model.DaoOwner;
import Model.DaoRequest;
import Model.Keeper;
import Model.MailSend;
import Model.Owner;
import Model.Pet;
import Model.Request;
import java.io.FileInputStream;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Window;
import javax.mail.MessagingException;

/**
 * FXML Controller class
 *
 * @author og218
 */
public class RequestTotalController implements Initializable {

    @FXML
    private ImageView imgPicturePet;
    @FXML
    private TextArea txfPetDescription;
    @FXML
    private Button btnAcceptRequest;
    @FXML
    private Label lbPetName;
    @FXML
    private Label lbOwnerName;
    @FXML
    private TableColumn<Request, String> colStartDate;
    @FXML
    private TableColumn<Request, String> ColFinishDate;
    @FXML
    private TableColumn<Request, Integer> ColIdPet;
    @FXML
    private TableColumn<Request, Integer> ColidOwner;
    @FXML
    private TableColumn<Request, Integer> ColIdRequest;
    @FXML
    private Label lbOwnerAddress;
    @FXML
    private TableView<Request> tbvRequests;

    private Keeper keeper;
    
    private DaoRequest daoRequest;
    @FXML
    private Label lbPetSex;
    
    private Request requestSelected;
    // Para cargar la imagen de la mascota sobre un mismo objeto Image
    private Image image;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }  
    
     public void setKeeper(Keeper keeper){
        this.keeper = keeper;
        daoRequest = new DaoRequest();
        showRequestTbv();
        btnAcceptRequest.setDisable(true);  
    }

    @FXML
    private void actionAcceptRequest(ActionEvent event) throws MessagingException {
        Window window = btnAcceptRequest.getScene().getWindow();
        if(daoRequest.assingKeeper(requestSelected.getId(), this.keeper.getId())){
            showRequestTbv();
            showAlert(Alert.AlertType.INFORMATION, window, "The owner knows you want to take care of the pet, \n"
                    + "So wait to be contacted", "You have accepted a Request", "Succes");
            DaoOwner daoOwner = new DaoOwner();
            Owner requestOwner = daoOwner.getInformation(requestSelected.getOwner());
            MailSend mailSend = new MailSend();
            String message = "Mr/s "+requestOwner.getName()+ " " + requestOwner.getLastName()+ ", from PetDay, wish a great day\n \n"
                    + "We want to tell you that one of the request you made was accepted, so please put in conctac with "+ this.keeper.getName() + " "+this.keeper.getLastName()+""
                    + "\n. For more information go to PetDay App \n\n"
                    + "Request Data \n"
                    + "Date of beggining: "+ requestSelected.getDateIni()+", date of ending: "+requestSelected.getDateFin()+ "\n"
                    + "Have a good rest of day.";
            
            mailSend.sendEmail(requestOwner.getEmail(), "PetDay: Request Accepted", message);
        }else{
            showAlert(Alert.AlertType.ERROR, window, "There was a mistake", "", "Error");
        }
    }
    
    private void showRequestTbv(){
        ObservableList<Request> requestList = daoRequest.getTotalRequest();
        colStartDate.setCellValueFactory(new PropertyValueFactory<Request, String>("dateIni"));
        ColFinishDate.setCellValueFactory(new PropertyValueFactory<Request, String>("dateFin"));
        ColIdRequest.setCellValueFactory(new PropertyValueFactory<Request, Integer>("id"));
        ColIdPet.setCellValueFactory(new PropertyValueFactory<Request, Integer>("pet"));
        ColidOwner.setCellValueFactory(new PropertyValueFactory<Request, Integer>("owner"));
        tbvRequests.setItems(requestList);
        btnAcceptRequest.setDisable(false);
    }

    @FXML
    private void actionClickOnRow(MouseEvent event) {
        requestSelected = tbvRequests.getSelectionModel().getSelectedItem();
        Pet petEmpty = new Pet();
        petEmpty.setId(requestSelected.getPet());
        Owner ownerEmpty = new Owner();
        ownerEmpty.setId(requestSelected.getOwner());
        if(daoRequest.getInfoPetOwner(requestSelected.getId(), petEmpty, ownerEmpty)){
            lbOwnerName.setText(ownerEmpty.getName());
            lbOwnerAddress.setText(ownerEmpty.getAddres());
            lbPetName.setText(petEmpty.getName());
            lbPetSex.setText((petEmpty.getSex() == 1)?"Female":"Male");
            txfPetDescription.setText(petEmpty.getInformation());
            try {
                image = new Image(new FileInputStream(petEmpty.getPicture()),129, 121, true, true);
                imgPicturePet.setImage(image);
            } catch (Exception e) {
                e.printStackTrace();
            }
            btnAcceptRequest.setDisable(false);
        }else{
            System.out.println("there was a problem");
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
