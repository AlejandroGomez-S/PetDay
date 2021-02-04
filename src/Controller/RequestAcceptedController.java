/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Model.DaoRequest;
import Model.Keeper;
import Model.Owner;
import Model.Pet;
import Model.Request;
import java.io.FileInputStream;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

/**
 * FXML Controller class
 *
 * @author og218
 */
public class RequestAcceptedController implements Initializable {

    @FXML
    private TableView<Request> tbvAcceptedRequests;
    @FXML
    private TableColumn<Request, String> colStartDate;
    @FXML
    private TableColumn<Request, String> colFinishDate;
    @FXML
    private TableColumn<Request, Integer> colIdPet;
    @FXML
    private TableColumn<Request, Integer> colIdOwner;
    @FXML
    private TableColumn<Request, Integer> colIdRequest;
    @FXML
    private ImageView imgPicture;
    @FXML
    private Label lbPetName;
    @FXML
    private Label lbPetSex;
    @FXML
    private Label lbPetAge;
    @FXML
    private Label lbPetType;
    @FXML
    private TextArea lbDescription;
    @FXML
    private Label lbOwnerName;
    @FXML
    private Label lbOwnerPhone;
    @FXML
    private Label lbOwnerMovil;
    @FXML
    private Label lbOwnerAddress;
    @FXML
    private Label lbOwnerEmail;
    @FXML
    private Button btnDeclineRequest;
    
    private Request selectedRequest;
    private Keeper keeper;
    private DaoRequest daoRequest;
    private Image image;
    @FXML
    private Pane panePetInfo;
    @FXML
    private Pane paneOwnerInfo;
    @FXML
    private Label lbwarning1;
    @FXML
    private Label lbWarning2;
    @FXML
    private Label lbWarning3;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
    public void setKeeper(Keeper keeper){
        this.keeper = keeper;
        daoRequest = new DaoRequest();
        showAcceptedRequestTbv();
    }
    
    public void showAcceptedRequestTbv(){
        ObservableList<Request> requestList = daoRequest.getAcceptedRequest(this.keeper.getId());
        colStartDate.setCellValueFactory(new PropertyValueFactory<Request, String>("dateIni"));
        colFinishDate.setCellValueFactory(new PropertyValueFactory<Request, String>("dateFin"));
        colIdRequest.setCellValueFactory(new PropertyValueFactory<Request, Integer>("id"));
        colIdPet.setCellValueFactory(new PropertyValueFactory<Request, Integer>("pet"));
        colIdOwner.setCellValueFactory(new PropertyValueFactory<Request, Integer>("owner"));
        tbvAcceptedRequests.setItems(requestList);
        btnDeclineRequest.setDisable(false);
    }

    @FXML
    private void actionClickOnRow(MouseEvent event) {
        selectedRequest = tbvAcceptedRequests.getSelectionModel().getSelectedItem();
        Pet petEmpty = new Pet();
        petEmpty.setId(selectedRequest.getPet());
        Owner ownerEmpty = new Owner();
        ownerEmpty.setId(selectedRequest.getOwner());
        if(daoRequest.getInfoPetOwner(selectedRequest.getId(), petEmpty, ownerEmpty)){
            lbOwnerName.setText(ownerEmpty.getName()+" "+ownerEmpty.getLastName());
            lbOwnerPhone.setText(""+ownerEmpty.getPhoneF());
            lbOwnerMovil.setText(""+ownerEmpty.getPhone());
            lbOwnerAddress.setText(ownerEmpty.getAddres());
            lbOwnerEmail.setText(ownerEmpty.getEmail());
            lbPetName.setText("My name is "+petEmpty.getName());
            lbPetSex.setText("I am a "+((petEmpty.getSex() == 1)?"Female":"Male"));
            lbPetAge.setText("I am " + petEmpty.getAge()+" Y.O.");
            lbPetType.setText("I belong to "+ petEmpty.getType());
            lbDescription.setText(petEmpty.getInformation());
            try {
                image = new Image(new FileInputStream(petEmpty.getPicture()),151, 149, true, true);
                imgPicture.setImage(image);
            } catch (Exception e) {
                e.printStackTrace();
            }
            btnDeclineRequest.setDisable(false);
            paneOwnerInfo.setVisible(true);
            panePetInfo.setVisible(true);
            lbwarning1.setVisible(false);
            lbWarning2.setVisible(false);
            lbWarning3.setVisible(false);
        }else{
            System.out.println("there was a problem");
        } 
    }
    
     @FXML
    private void actionDeclineRequest(ActionEvent event) {
        if(daoRequest.declineRequest(this.selectedRequest.getId())){
            System.out.println("Se desasigno correctamente");
            showAcceptedRequestTbv();
            paneOwnerInfo.setVisible(false);
            panePetInfo.setVisible(false);
            lbwarning1.setVisible(true);
            lbWarning2.setVisible(true);
            lbWarning3.setVisible(true);
        }else{
            System.out.println("No se pudo desaceptar");
        }
    }
}
