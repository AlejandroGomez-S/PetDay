
package Controller;

import Model.DaoKeeper;
import Model.DaoOwner;
import Model.DaoPet;
import Model.DaoRequest;
import Model.Keeper;
import Model.MailSend;
import Model.Owner;
import Model.Pet;
import Model.Request;
import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.regex.Pattern;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import javax.mail.MessagingException;

/**
 * FXML Controller class
 *
 * @author Oscar Alejandro Gómez Suarez
 */
public class ManageRequestOwnerController implements Initializable {

    private Owner owner;
    @FXML
    private ChoiceBox chPet;
    private DaoPet daoPet;
    private DaoRequest daoRequest;
    private ObservableList<String> petList =  FXCollections.observableArrayList("");
    @FXML
    private DatePicker dpInicialization;
    @FXML
    private TextField txfHInicialization;
    @FXML
    private TextField txfMInicialization;
    @FXML
    private Label lbDateIni;
    @FXML
    private DatePicker dpFinalization;
    @FXML
    private TextField txfHFinalization;
    @FXML
    private Label lbDateFin;
    @FXML
    private TextField txfMFinalization;
    @FXML
    private TableView<ShowRequest> tbvRequests;
    @FXML
    private TableColumn<ShowRequest, String> colDateIni;
    @FXML
    private TableColumn<ShowRequest, String> colDateFin;
    @FXML
    private TableColumn<ShowRequest, String> ColPet;
    @FXML
    private TableColumn<ShowRequest, String> ColAccepted;
    @FXML
    private TableColumn<ShowRequest, Integer> colId;
    @FXML
    private TableColumn<ShowRequest, Integer> ColIdPet;
    @FXML
    private TableColumn<ShowRequest, Integer> ColIdKeeper;
    @FXML
    private Pane paneAdd;
    @FXML
    private Button btnCreateRequest;
    @FXML
    private Button btnDelete;
    @FXML
    private Button btnShowInformation;
    @FXML
    private Button btnDeclineService;
    @FXML
    private Button btnClear;
    @FXML
    private Label lbErrorIni;
    @FXML
    private Label lbErrorFin;
    private ShowRequest showRequestTable;
    
    // Permite abrir una nueva ventana 
    Scene fxmlFile;
    Parent root;
    Stage window;
    // Son necesarios para openModalWindow
    @FXML
    private Button btnGotoPrincipal;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
    // Clase privada que permite mostrar la información en la table view
    public static class ShowRequest{
        private final SimpleIntegerProperty IdRequest;
        private final SimpleStringProperty dateIni;
        private SimpleStringProperty dateFin;
        private final SimpleIntegerProperty idPet;
        private SimpleStringProperty namePet;
        private final SimpleIntegerProperty idkeeper;
        private SimpleStringProperty nameKeeper;
        private DaoKeeper daoKeeper;
        private DaoPet daoPet;

        public ShowRequest(int IdRequest, String dateIni, String dateFin, int idPet, int idkeeper) {
            this.daoPet = new DaoPet();
            this.IdRequest = new SimpleIntegerProperty(IdRequest);
            this.dateIni = new SimpleStringProperty(dateIni);
            this.dateFin = new SimpleStringProperty(dateFin);
            this.idPet = new SimpleIntegerProperty(idPet);
            this.idkeeper = new SimpleIntegerProperty(idkeeper);
            this.namePet = new SimpleStringProperty(daoPet.getNameById(idPet));
            if(this.idkeeper.get() != 0){ // Esto funciona porque null en enteros cuando se trae de la base de datos equivale a 0
                daoKeeper = new DaoKeeper();
                this.nameKeeper = new SimpleStringProperty(daoKeeper.getKeeperName(this.idkeeper.get()));
            }else{
                this.nameKeeper = new SimpleStringProperty("Not Accepted");
            }
        }

        public int getIdRequest() {
            return IdRequest.get();
        }

        public String getDateIni() {
            return dateIni.get();
        }

        public String getDateFin() {
            return dateFin.get();
        }

        public int getIdPet() {
            return idPet.get();
        }

        public String getNamePet() {
            return namePet.get();
        }

        public int getIdkeeper() {
            return idkeeper.get();
        }

        public String getNameKeeper() {
            return nameKeeper.get();
        }
    }
    
    // Fin de la clase privada
    /**
     * Initializes the controller class.
     */
    public void setOwner(Owner owner){
        this.owner = owner;
        daoPet = new DaoPet();
        petList = daoPet.getPetsName(owner.getId());
        chPet.setValue("Choose Pet");
        chPet.setItems(petList);
        daoRequest = new DaoRequest();
        showRequestTable = null;
        showRequestsTable();
    }

    @FXML
    private void actionShowDateIni(KeyEvent event) {
        if (dpInicialization.getValue() == null) {
            lbDateIni.setText("Select a Date, and then time");
            txfHInicialization.clear();
            txfMInicialization.clear();
            
        } else {
            String date = dpInicialization.getValue() + " " + txfHInicialization.getText() + ":";
            date += txfMInicialization.getText() + ":00";
            if (testHour(txfHInicialization.getText(), txfMInicialization.getText())) {
                lbDateIni.setText(date);
                lbErrorIni.setText("");
            } else {
                txfHFinalization.clear();
                txfMFinalization.clear();
                lbDateIni.setText("Incorrect Format");
                lbErrorIni.setText("Error");
            }
        }
    }
    
    @FXML
    private void actionShowDateFin(KeyEvent event) {
        if (dpFinalization.getValue() == null) {
            lbDateFin.setText("Select a Date, and then time");
            txfHFinalization.clear();
            txfMFinalization.clear();
        } else {
            String date = dpFinalization.getValue() + " " + txfHFinalization.getText() + ":";
            date += txfMFinalization.getText() + ":00";
            if (testHour(txfHFinalization.getText(), txfMFinalization.getText())) {
                if (dpInicialization.getValue().compareTo(dpFinalization.getValue()) < 0) {
                    lbDateFin.setText(date);
                    lbErrorFin.setText("");
                } else if (dpInicialization.getValue().compareTo(dpFinalization.getValue()) > 0) {
                    lbDateFin.setText("The Finalization Date must be after the Inicialization Date");
                    dpFinalization.setValue(null);
                    txfHFinalization.clear();
                    txfMFinalization.clear();
                    lbErrorFin.setText("Error");
                } else if (dpInicialization.getValue().compareTo(dpFinalization.getValue()) == 0) {
                    if (Integer.parseInt(txfHInicialization.getText()) > Integer.parseInt(txfHFinalization.getText())) {
                        txfHFinalization.clear();
                        txfMFinalization.clear();
                        lbDateFin.setText("The Finalization hour must be after the Inialization time H");
                        lbErrorFin.setText("Error");
                    } else if (Integer.parseInt(txfHInicialization.getText()) == Integer.parseInt(txfHFinalization.getText())) {
                        if (Integer.parseInt(txfMInicialization.getText()) >= Integer.parseInt(txfMFinalization.getText())) {
                            txfHFinalization.clear();
                            txfMFinalization.clear();
                            lbDateFin.setText("The Finalization hour must be after the Inialization time M");
                            lbErrorFin.setText("Error");
                        }
                        else{
                           lbDateFin.setText(date);
                           lbErrorFin.setText("");
                        }
                    }
                }
            } else {
                lbDateFin.setText("Incorrect Format");
                lbErrorFin.setText("Error");
            }
        }
    }

    @FXML
    private void actionPickDateIni(MouseEvent event) {
        txfHInicialization.clear();
        txfMInicialization.clear();
    }

    @FXML
    private void actionPickDateFin(MouseEvent event) {
        txfHFinalization.clear();
        txfMFinalization.clear();
    }

    @FXML
    private void actionTipedHourIni(KeyEvent event) {
        txfMInicialization.clear();
    }

    @FXML
    private void actionTipedHourFin(KeyEvent event) {
        txfMFinalization.clear();
    }
    
    @FXML
    private void actionAddRequest(ActionEvent event) throws ParseException {
        Window window = btnClear.getScene().getWindow();
        if(txfHFinalization.getText().equals("") || txfHInicialization.getText().equals("") 
                || txfMFinalization.getText().equals("") || txfMInicialization.getText().equals("")
                || lbErrorFin.getText().equals("Error") || lbErrorIni.getText().equals("Error")){
            showAlert(Alert.AlertType.ERROR, window, "Error in the Dates, check it out, and fix it", "Date problems", "Error");
            return;
        }
        if(chPet.getValue().equals("Choose Pet")){
            showAlert(Alert.AlertType.ERROR, window, "You must Select a pet to be cared, if you dont have one, go to the manage pets panel",
                    "Select any pet", "Error");
            return;
        }
        // Comprobar si las fechas de las solicitudes ya paso
        String dateIni = lbDateIni.getText();
        Date date  = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dateIni);
        Date actual = new Date();
        if(actual.after(date)){
            showAlert(AlertType.ERROR, window, "The inicialization time already happend! \n"
                    + "please select a time before this time", "Date init incorrect", "Change Inicialization Time");
            return;
        }
        // Fin de la comprobación, si no pasa, se acaba la funcion y no se agrega el request
        
        
        Pet petSelected = new Pet();
        petSelected.setName(chPet.getValue() + "");
        if (daoPet.getPetByName(this.owner.getId(), petSelected)) {
            Request request = new Request();
            request.setDateIni(lbDateIni.getText());
            request.setDateFin(lbDateFin.getText());
            request.setOwner(this.owner.getId());
            request.setPet(petSelected.getId());
            if(daoRequest.register(request))
                showAlert(Alert.AlertType.INFORMATION, window, "Request Added","Succes", "Good");
                clean();
                showRequestsTable();
        } else {
            showAlert(Alert.AlertType.ERROR, window, "Request couldn't be created", "Failure", "Bad");
        }
    }
    
    private void showRequestsTable(){
        ArrayList<Request> requestlist = daoRequest.getRequests(this.owner.getId());
        ObservableList<ShowRequest> requestObservable = FXCollections.observableArrayList();
        for(int r = 0; r < requestlist.size(); r++){
            int idRe = requestlist.get(r).getId();
            String dateIni = requestlist.get(r).getDateIni();
            String dateFin = requestlist.get(r).getDateFin();
            int idPet = requestlist.get(r).getPet();
            int idKeeper = requestlist.get(r).getKeeper();
            ShowRequest sRequest = new ShowRequest(idRe, dateIni, dateFin, idPet, idKeeper);
            requestObservable.add(sRequest);
        }
        colId.setCellValueFactory(new PropertyValueFactory<ShowRequest, Integer>("IdRequest"));
        colDateIni.setCellValueFactory(new PropertyValueFactory<ShowRequest, String>("dateIni"));
        colDateFin.setCellValueFactory(new PropertyValueFactory<ShowRequest, String>("dateFin"));
        ColIdPet.setCellValueFactory(new PropertyValueFactory<ShowRequest, Integer>("idPet"));
        ColPet.setCellValueFactory(new PropertyValueFactory<ShowRequest, String>("namePet"));
        ColIdKeeper.setCellValueFactory(new PropertyValueFactory<ShowRequest, Integer>("idKeeper"));
        ColAccepted.setCellValueFactory(new PropertyValueFactory<ShowRequest, String>("nameKeeper"));
        tbvRequests.setItems(requestObservable);
    }
   
    private void clean(){
        dpInicialization.setValue(null);
        dpFinalization.setValue(null);
        txfHFinalization.clear();
        txfHInicialization.clear();
        txfMFinalization.clear();
        txfMInicialization.clear();
        btnDeclineService.setDisable(true);
        btnDelete.setDisable(true);
        btnShowInformation.setDisable(true);
        showRequestTable = null;
        chPet.setValue("Choose Pet");
    }
    
    //Esta parte hace visible el pane que permite agregar Request 
    @FXML
    private void actionCreateRequest(ActionEvent event) {
        clean();
        paneAdd.setDisable(false);
    }
    
     @FXML
    private void actionDelete(ActionEvent event) throws MessagingException {
        Window window = btnClear.getScene().getWindow();
        if(daoRequest.delete(showRequestTable.getIdRequest())){
            showRequestsTable();
            showAlert(Alert.AlertType.INFORMATION, window, "Delete Succesful", "Delete correct", "Good");
            sendDeclineService(this.showRequestTable.getIdkeeper());
        }else{
            showAlert(Alert.AlertType.ERROR, window, "The request couldn't be deleted", "There was a mistake", "Error");
        }
    }

    @FXML
    private void actionClear(ActionEvent event) {
        clean();
    }

    private boolean testHour(String hour, String minute){
        if(hour.equals("") || minute.equals(""))
            return false;
        return (Integer.parseInt(hour) > 0) && (Integer.parseInt(hour) < 25) && Integer.parseInt(minute) >= 0 && Integer.parseInt(minute) < 60 ;
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
    private void actionClickOnRow(MouseEvent event) {
        showRequestTable = tbvRequests.getSelectionModel().getSelectedItem();
        btnDelete.setDisable(false);
        paneAdd.setDisable(true);
        if(showRequestTable.getIdkeeper() != 0){
            btnDeclineService.setDisable(false);
            btnShowInformation.setDisable(false);
        }else{
            btnDeclineService.setDisable(true);
            btnShowInformation.setDisable(true);
        }
    }
    
    @FXML
    private void actionShowInformation(ActionEvent event) throws IOException {
        Keeper keeEmpty = new Keeper();
        keeEmpty.setId(showRequestTable.getIdkeeper());
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/View/InfoKeeper.fxml"));
        root = fxmlLoader.load();
        InfoKeeperController infoKeeperController = (InfoKeeperController)fxmlLoader.getController();
        Keeper kee = new Keeper();
        kee.setId(showRequestTable.getIdkeeper());
        infoKeeperController.setKeeper(kee);
        openModalWindow("Keeper Information");
    }

    @FXML
    private void actionDeclineService(ActionEvent event) throws MessagingException {
        Window win = btnClear.getScene().getWindow();
        Alert alert = new Alert(Alert.AlertType.WARNING, "Do you really want to cancel " + showRequestTable.getNameKeeper() + " Services", ButtonType.OK, ButtonType.CANCEL);
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.setAlwaysOnTop(true);
        stage.toFront();
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            if (daoRequest.declineRequest(showRequestTable.getIdRequest())) {
                showRequestsTable();
                showAlert(AlertType.INFORMATION, win, "Service Declined", "", "Action made");
                sendDeclineService(showRequestTable.getIdkeeper());
            }
        } else {
            showAlert(AlertType.INFORMATION, win, "Service couldn't be declined", "", "Action not made");
        }
    }
    
    private void openModalWindow(String title) throws IOException{
        fxmlFile = new Scene(root);
        window = new Stage();
        window.setScene(fxmlFile);
        window.initModality(Modality.APPLICATION_MODAL);
        window.setAlwaysOnTop(true);
        window.setIconified(false);
        window.setTitle(title);
        window.showAndWait();
    }
    
     @FXML
    private void actionClickRrincipal(ActionEvent event) {
        Stage stage = (Stage) btnGotoPrincipal.getScene().getWindow();
        stage.close();
    }
    
    private boolean sendDeclineService(int idKeeper) throws MessagingException {
        DaoKeeper daoKeeper = new DaoKeeper();
        Keeper requestkeeper = new Keeper();
        requestkeeper.setId(idKeeper);
        if (daoKeeper.getKeeperInfo(requestkeeper)) {
            MailSend mailSend = new MailSend();
            String message = "Mr/s " + requestkeeper.getName() + " " + requestkeeper.getLastName() + ", from PetDay, wish a great day\n \n"
                    + "We want to tell you that one of the request you had accepted was declined, so the next time ot wil be!"
                    + "\n. For more information go to PetDay App \n\n"
                    + "Request Data \n"
                    + "Date of beggining: " + showRequestTable.getDateIni() + ", date of ending: " + showRequestTable.getDateFin() + "\n"
                    + "Have a good rest of day.";

            mailSend.sendEmail(requestkeeper.getEmail(), "PetDay: Request Accepted", message);
            return true;
        }
        return false;
    }
   
}
