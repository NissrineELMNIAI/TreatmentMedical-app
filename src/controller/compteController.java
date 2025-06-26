package controller;

import com.sun.javafx.stage.StageHelper;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import model.Utilisateur;
import javafx.stage.Modality;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import model.Session;
//import utils.StageHelper;
import javafx.stage.Window;

public class compteController {

    @FXML
    private Label nomLabel;

    @FXML
    private Label emailLabel;

    @FXML
    private ImageView profileImageView;

    @FXML
    private Label welcomeLabel;

    private Utilisateur utilisateur;

    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
        afficherInfosUtilisateur();
    }

    @FXML
    private void initialize() {
        Image profilImage = new Image(getClass().getResourceAsStream("/icons/photoProfile.png"));
        profileImageView.setImage(profilImage);
    }

    private void afficherInfosUtilisateur() {
        if (utilisateur != null) {
            nomLabel.setText(utilisateur.getUsername());
            emailLabel.setText(utilisateur.getEmail());
            welcomeLabel.setText("Bienvenue, " + utilisateur.getUsername() + " !");
        }
    }

    @FXML
private void handleModifierPhoto(ActionEvent event) {
    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Choisir une photo de profil");
    fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Images", ".png", ".jpg", ".jpeg", ".gif"));
    File file = fileChooser.showOpenDialog(profileImageView.getScene().getWindow());

    if (file != null) {
        try {
            profileImageView.setImage(new Image(new FileInputStream(file)));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}




    // Méthode pour ouvrir une nouvelle fenêtre "Ajouter Compte"
    @FXML
    private void handleAjouterCompte(ActionEvent event) {
        try {
            // Charge le FXML de la nouvelle interface
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/ajouterCompte.fxml"));
            Parent root = loader.load();

            // Crée une nouvelle scène et fenêtre (stage)
            Stage stage = new Stage();
            stage.setTitle("Ajouter un compte");
            stage.setScene(new Scene(root));
            // Bloque la fenêtre parente (modale)
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
           
        }
    }

    // Méthode pour déconnexion et retour à l'interface de connexion
@FXML
private void handleDeconnexion(ActionEvent event) {
    try {
       
        closeAllWindows();
        Session.setUtilisateur(null);
        showLoginWindow();
        
    } catch (IOException e) {
        e.printStackTrace();
        showAlert("Erreur", "Impossible d'ouvrir la fenêtre de connexion");
    }
}

private void closeAllWindows() {
    // Fermer toutes les fenêtres sauf la fenêtre primaire (si elle existe)
    for (Window window : Window.getWindows()) {
        if (window instanceof Stage) {
            Stage stage = (Stage) window;
            if (!stage.isShowing()) continue;
            stage.close();
        }
    }
}

private void showLoginWindow() throws IOException {
    FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/connexionView.fxml"));
    Parent root = loader.load();
    
    Stage loginStage = new Stage();
    loginStage.setScene(new Scene(root));
    loginStage.setTitle("Connexion");
    loginStage.show();
}

private void showAlert(String title, String message) {
    Alert alert = new Alert(Alert.AlertType.ERROR);
    alert.setTitle(title);
    alert.setHeaderText(null);
    alert.setContentText(message);
    alert.showAndWait();
}


}