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
import javafx.scene.image.Image;
import javafx.stage.Modality;
import model.Session;
import model.Utilisateur;

public class accueilController {

    private Utilisateur utilisateur;

    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
    }
  @FXML
    private void ouvrirRendezVous(ActionEvent event) {
            try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/rendezVousView.fxml"));
        Parent root = loader.load();

        // Transfert de l'utilisateur connecté
        rendezVousController rendezVousController = loader.getController();
        rendezVousController.setUtilisateur(utilisateur);

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setTitle("Rendez-Vous - MemoPharma");
        stage.show();

    } catch (IOException e) {
        e.printStackTrace();
    }
    }
    
     @FXML
    private void ouvrirTraitements(ActionEvent event) {
           try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/treatmentView.fxml"));
        Parent root = loader.load();

        // Transfert de l'utilisateur connecté
       traitementController traitementController = loader.getController();
        traitementController.setUtilisateur(utilisateur);

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setTitle("Traitements - MemoPharma");
        stage.show();

    } catch (IOException e) {
        e.printStackTrace();
    }
    }

    
    @FXML
private void ouvrirPatients(ActionEvent event) {
    try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/patientView.fxml"));
        Parent root = loader.load();

        // Transfert de l'utilisateur connecté
        PatientController patientController = loader.getController();
        patientController.setUtilisateur(utilisateur);

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setTitle("Patients - MemoPharma");
        stage.show();

    } catch (IOException e) {
        e.printStackTrace();
    }
}

    @FXML
    private void ouvrirConnexion(ActionEvent event) {
        changerScene(event, "/view/connexionView.fxml");
    }

    private void changerScene(ActionEvent event, String cheminFXML) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(cheminFXML));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setTitle("MemoPharma");
            stage.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void ouvrirAcceuil(ActionEvent event) {
           try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/acceuilView.fxml"));
        Parent root = loader.load();

        // Transfert de l'utilisateur connecté
       accueilController accueilController = loader.getController();
        accueilController.setUtilisateur(utilisateur);

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setTitle("Acceuil - MemoPharma");
        stage.show();

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

        // Transfert de l'utilisateur connecté via Session
        compteController controller = fxmlLoader.getController();
        controller.setUtilisateur(Session.getUtilisateur());

        Stage stage = new Stage();
        stage.setTitle("Profil Utilisateur");
        stage.getIcons().add(new Image("/icons/sansBackground.png"));
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
        stage.getIcons().add(new Image("/icons/sansBackground.png"));
        stage.setScene(new Scene(root));
        stage.show();
    } catch (IOException e) {
        e.printStackTrace();
    }
}
}
