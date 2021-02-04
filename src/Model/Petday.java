/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author og218
 */
public class Petday extends Application{

    @Override
    public void start(Stage stage) throws Exception {
        try {
            Parent parentRoot = FXMLLoader.load(getClass().getResource("/View/Login.fxml"));
            stage.setTitle("PetDay! Login");
            stage.setResizable(false);
            stage.setIconified(false);
            Scene scene = new Scene(parentRoot);
            stage.setScene(scene);
            stage.show(); 
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}
