package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import model.UtilisateurDAO;

import java.io.IOException;

public class ConnexController {

    @FXML
    private TextField utilisateurField;

    @FXML
    private PasswordField motDePasseField;

    @FXML
    private Button boutonSeConnecter;

    @FXML
    private Label creerCompteLabel;

    private UtilisateurDAO utilisateurDAO = new UtilisateurDAO();

    @FXML
    public void initialize() {
        creerCompteLabel.setOnMouseClicked(event -> redirigerVersCreationCompte(event));
        boutonSeConnecter.setOnAction(event -> seConnecter());
    }
    @FXML
    private void seConnecter() {
        String utilisateur = utilisateurField.getText().trim();
        String motDePasse = motDePasseField.getText();

        if (utilisateur.isEmpty() || motDePasse.isEmpty()) {
            afficherAlerte("Erreur", "Veuillez remplir tous les champs", Alert.AlertType.ERROR);
            return;
        }

        boolean authentifie = utilisateurDAO.verifierUtilisateur(utilisateur, motDePasse);
        if (authentifie) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/acceuilView.fxml"));
                Parent root = loader.load();

                Stage stage = (Stage) boutonSeConnecter.getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.setTitle("Accueil");
                stage.show();
            } catch (IOException e) {
                afficherAlerte("Erreur", "Impossible de charger la page d'accueil", Alert.AlertType.ERROR);
                e.printStackTrace();
            }
        } else {
            afficherAlerte("Échec de la connexion", "Nom d'utilisateur ou mot de passe incorrect", Alert.AlertType.ERROR);
        }
    }

    private void redirigerVersCreationCompte(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/creerCompt.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Créer un compte");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            afficherAlerte("Erreur", "Impossible de charger la page de création de compte", Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    private void afficherAlerte(String titre, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
