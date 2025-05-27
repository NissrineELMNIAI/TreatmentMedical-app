package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class compteController {

    @FXML
    private Label nomLabel;

    @FXML
    private Label emailLabel;

    @FXML
    private ImageView profileImageView;

    @FXML
    private Label welcomeLabel;

    @FXML
    private void initialize() {
        // Exemple de données
        nomLabel.setText("Lamyae Hamdaoui");
        emailLabel.setText("lamyae.Hamdaoui@ump.com");
        welcomeLabel.setText("Bienvenue sur votre espace personnel !");

        // Image par défaut
        Image profilImage = new Image(getClass().getResourceAsStream("/icons/imageDefaut.png"));
        profileImageView.setImage(profilImage);
    }

    @FXML
    private void handleModifierPhoto(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choisir une photo de profil");
        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );
        Stage stage = (Stage) profileImageView.getScene().getWindow();
        File selectedFile = fileChooser.showOpenDialog(stage);

        if (selectedFile != null) {
            try {
                Image newImage = new Image(new FileInputStream(selectedFile));
                profileImageView.setImage(newImage);
                welcomeLabel.setText("Photo mise à jour avec succès !");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                welcomeLabel.setText("Erreur lors du chargement de la photo.");
            }
        }
    }

    @FXML
private void handleParametres(ActionEvent event) {
    try {
        javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(getClass().getResource("/view/Parametres.fxml"));
        javafx.scene.Parent root = loader.load();
        Stage paramStage = new Stage();
        paramStage.setTitle("Paramètres du compte");
        paramStage.setScene(new javafx.scene.Scene(root));
        paramStage.show();
    } catch (Exception e) {
        e.printStackTrace();
    }
}


    @FXML
    private void handleAjouterCompte(ActionEvent event) {
        // Logique pour ajouter un autre compte
        System.out.println("Ajouter un autre compte demandé !");
    }

    @FXML
    private void handleDeconnexion(ActionEvent event) {
        // Logique déconnexion, par ex fermer session et revenir à login
        System.out.println("Déconnexion demandée !");
        Stage stage = (Stage) nomLabel.getScene().getWindow();
        stage.close();
    }
}
