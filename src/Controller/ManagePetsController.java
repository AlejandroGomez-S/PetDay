/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Model.DaoPet;
import Model.Owner;
import Model.Pet;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.regex.Pattern;
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
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

/**
 * FXML Controller class
 *
 * @author og218
 */
public class ManagePetsController implements Initializable {

    @FXML
    private Label lbIdOwner;
    private Owner owner;
    @FXML
    private TableView<Pet> tbvPets;
    @FXML
    private TableColumn<Pet, String> ColPetName;
    @FXML
    private Button btnAppPet;
    @FXML
    private Button btnUpdatePet;
    
    Scene fxmlFile;
    Parent root;
    Stage window;
    FXMLLoader fxmlLoader;
    
    @FXML
    private Label lbPathPicture;
    @FXML
    private Button btnUptloadPicture;
    @FXML
    private ImageView imgPicture;
    
    private Pet pet;
    
    private ObservableList<String> petSex =  FXCollections.observableArrayList("Male", "Female");
    @FXML
    private TextField txfName;
    @FXML
    private TextArea txfInformation;
    @FXML
    private TextField txfAge;
    @FXML
    private TextField txfType;
    @FXML
    private ChoiceBox chSex;
    private Image image;
    private FileChooser fileChooser; 
    private File file ;
    @FXML
    private Button btnClear;
    @FXML
    private TextField txfId;
    @FXML
    private Button btnDeletePet;
    @FXML
    private Button btnPrincipal;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        pet = new Pet();
        pet.setId(-1);
        chSex.setValue("Select the pet's sex");
        chSex.setItems(petSex);   
        file = null;
        fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif")
        );
    }    
    
    public void setOwner(Owner owner){
        this.owner = owner;
        lbIdOwner.setText("Here you can manage yoru pets, have great day "+owner.getName()+" "+owner.getLastName());
        showPets();
    }

    @FXML
    private boolean actionAddPet(ActionEvent event) {
        // Comprobaciónd de campos
        Window window = btnUpdatePet.getScene().getWindow();
        if (!testName(txfName.getText())) {
            showAlert(Alert.AlertType.ERROR, window, "The name must have between 3 and 25 characteres, no especial characteres",
                    "Wrong name", "Error");
            return false;
        }
        if (!testInfo(txfInformation.getText())) {
            showAlert(Alert.AlertType.ERROR, window, "The information must have must have between 0 and 200 characteres, no especial characteres",
                    "Wrong Information", "Error");
            return false;
        }
        if (!testAge(txfAge.getText())) {
            showAlert(Alert.AlertType.ERROR, window, "The age is in years, must contain jsut numbers, no especial characteres",
                    "Wrong Age", "Error");
            return false;
        }
        if (!testType(txfType.getText())) {
            showAlert(Alert.AlertType.ERROR, window, "The type must have must between 3 and 15 characteres, no especial characteres",
                    "Wrong Type", "Error");
            return false;
        }
        if(chSex.getValue().equals("Select the pet's sex")){
            showAlert(Alert.AlertType.ERROR, window, "You must select the sex of your pet, they have one!", "Select any sex", "Wrong sex");
            return false;
        }
        if(file == null){
            showAlert(Alert.AlertType.ERROR, window, "Select any picture, so we can recognize your pet!", "Select picture", "Error");
            return false;
        }
        DaoPet daoPet = new DaoPet();
        pet.setName(txfName.getText());
        pet.setInformation(txfInformation.getText());
        pet.setAge(Integer.parseInt(txfAge.getText()));
        pet.setPicture(file);
        pet.setType(txfType.getText());
        int sex = (chSex.getValue().equals("Female"))?1:2;
        pet.setSex(sex);
        pet.setOwner(this.owner.getId());
        boolean flag = daoPet.register(pet);
        if(flag){
            showAlert(Alert.AlertType.ERROR, window, "Pet added", "Correct","Succesfull" );
            showPets();
            clear();
            return flag;
        }else{
            return flag;
        }
    }

    @FXML
    private void actionUpdatePet(ActionEvent event) {
        // Comprobaciónd de campos
        Window window = btnUpdatePet.getScene().getWindow();
        if (!testName(txfName.getText())) {
            showAlert(Alert.AlertType.ERROR, window, "The name must have between 3 and 25 characteres, no especial characteres",
                    "Wrong name", "Error");
            return;
        }
        if (!testInfo(txfInformation.getText())) {
            showAlert(Alert.AlertType.ERROR, window, "The information must have must have between 0 and 200 characteres, no especial characteres",
                    "Wrong Information", "Error");
            return;
        }
        if (!testAge(txfAge.getText())) {
            showAlert(Alert.AlertType.ERROR, window, "The age is in years, must contain jsut numbers, no especial characteres",
                    "Wrong Age", "Error");
            return;
        }

        if (!testType(txfType.getText())) {
            showAlert(Alert.AlertType.ERROR, window, "The type must have must between 3 and 15 characteres, no especial characteres",
                    "Wrong Type", "Error");
            return;
        }

        DaoPet daoPet = new DaoPet();
        Pet petUpload = new Pet();
        boolean flag;
        petUpload.setId(Integer.parseInt(txfId.getText()));
        petUpload.setName(txfName.getText());
        petUpload.setInformation(txfInformation.getText());
        int sex = (chSex.getValue().equals("Female"))?1:2;
        petUpload.setSex(sex);
        petUpload.setAge(Integer.parseInt(txfAge.getText()));
        petUpload.setType(txfType.getText());
        if(file == null){
            flag = daoPet.uploadPetNoPicture(petUpload);
        }else{
            petUpload.setPicture(file);
            flag = daoPet.uploadPetPicture(petUpload);
        }
        if(flag){
            showAlert(Alert.AlertType.ERROR, window, "Update Succesfull", "Succes", "Exelent");
        }else{
            showAlert(Alert.AlertType.ERROR, window, "There was a mistake", "Couldn't be done", "Error");
        }
        clear();
        showPets();
    }

    @FXML
    private void actionDeletePet(ActionEvent event) {
        DaoPet daoPet = new DaoPet();
        if(daoPet.deletePet(Integer.parseInt(txfId.getText()))){
            System.out.println("Delete action made");
            showPets();
            clear();
        }else{
            System.out.println("NO such pet");
        }
    }

    @FXML
    private void actionUploadPicture(ActionEvent event) {
        Stage stage = new Stage();
        stage = (Stage)btnUptloadPicture.getScene().getWindow();
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
    private void actionClear(ActionEvent event) {
        clear();
    }
    
    private void clear(){
        txfName.clear();
        txfAge.clear();
        txfInformation.clear();
        txfType.clear();
        imgPicture.imageProperty().set(null);
        pet = new Pet();
        txfId.clear();
        chSex.setValue("Select the pet's sex");
        pet.setId(-1);
        file = null;
        lbPathPicture.setText("Path Picture");
        btnAppPet.setDisable(false);
        btnDeletePet.setDisable(true);
        btnUpdatePet.setDisable(true);
    }
    
    public void showPets() {
        DaoPet daoPet = new DaoPet();
        ObservableList<Pet> petList = daoPet.getPets(this.owner.getId());
        ColPetName.setCellValueFactory(new PropertyValueFactory<Pet, String>("name"));
        tbvPets.setItems(petList);
    }
    
    //Permite recuperar la información de la mascota al hacer click en el nombre de la table view
    @FXML
    private void actionClickOnRow(MouseEvent event) {
        DaoPet daoPet = new DaoPet();
        Pet petRow = tbvPets.getSelectionModel().getSelectedItem();
        if(daoPet.getPetByName(this.owner.getId(), petRow)){
            txfAge.setText(""+petRow.getAge());
            txfId.setText(""+petRow.getId());
            txfInformation.setText(petRow.getInformation());
            txfName.setText(petRow.getName());
            txfType.setText(petRow.getType());
            String sex = (petRow.getSex() == 1)? "Female": "Male";
            chSex.setValue(sex);
            try {
                image = new Image(new FileInputStream(petRow.getPicture()),130, 145, true, true);
                imgPicture.setImage(image);
            } catch (Exception e) {
                e.printStackTrace();
                
            }
            btnUpdatePet.setDisable(false);
            btnDeletePet.setDisable(false);
            btnAppPet.setDisable(true);
        }else{
            System.out.println("No such a pet");
        }
    }
   
    // Restricciones a los campos
    private boolean testName(String name){
        String regex = "[a-zA-Z0-9\\s]{3,25}";
        return Pattern.matches(regex, name);
    }
    
    private boolean testInfo(String info){
        String regex = "[a-zA-Z0-9\\s.,]{0,200}";
        return Pattern.matches(regex, info);
    }
    
    private boolean testAge(String age){
        String regex = "[0-9]{1,2}";
        return (Pattern.matches(regex, age) && Integer.parseInt(age) < 100);
    }
   
    private boolean testType(String type){
        String regex = "[a-zA-Z]{3,15}";
        return Pattern.matches(regex, type);
    }
    
    public static void showAlert(Alert.AlertType alertype, Window owner, String message, String headerText, String title){
        Alert alert = new Alert(alertype);
        alert.setHeaderText(headerText);
        alert.setContentText(message);
        alert.setTitle(title);
        alert.initOwner(owner);
        alert.showAndWait();
    }

    @FXML
    private void actionClickPrincipal(ActionEvent event) {
        Stage stage = (Stage) btnPrincipal.getScene().getWindow();
        stage.close();
    }
}
