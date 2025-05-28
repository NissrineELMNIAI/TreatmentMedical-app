package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import javafx.scene.control.Button;
import javafx.stage.Modality;

public class accueilController {

    @FXML
    private void ouvrirPatients(ActionEvent event) {
        changerScene(event, "/view/patientView.fxml");
    }

    @FXML
    private void ouvrirTraitements(ActionEvent event) {
        changerScene(event, "/view/treatmentView.fxml");
    }

    @FXML
    private void ouvrirRendezVous(ActionEvent event) {
        changerScene(event, "/view/rendezVousView.fxml");
    }

    @FXML
    private void ouvrirConnexion(ActionEvent event) {
        changerScene(event, "/view/connexionView.fxml");
    }

    private void changerScene(ActionEvent event, String cheminFXML) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(cheminFXML));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
private Button staticon;

@FXML
private void handleCompteClick(ActionEvent event) {
    try {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/compte.fxml"));
        Parent root = fxmlLoader.load();

        Stage stage = new Stage();
        stage.setTitle("Profil Utilisateur");
        stage.setScene(new Scene(root));
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();
    } catch (IOException e) {
        e.printStackTrace();
    }
}
@FXML
private void ouvrirStatistiques(ActionEvent event) {
    try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/statistiqueView.fxml"));
        Parent root = loader.load();
        Stage stage = new Stage();
        stage.setTitle("Statistiques");
        stage.setScene(new Scene(root));
        stage.show();
    } catch (IOException e) {
        e.printStackTrace();
    }
}
}
