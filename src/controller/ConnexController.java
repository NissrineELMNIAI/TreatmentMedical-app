package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import model.Utilisateur;
import model.UtilisateurDAO;

import java.io.IOException;
import model.Session;

public class ConnexController {

    @FXML private TextField emailField;
    @FXML private PasswordField motDePasseField;
    @FXML private Button boutonSeConnecter;
    @FXML private Label creerCompteLabel;
    
    

    private UtilisateurDAO utilisateurDAO = new UtilisateurDAO();

    @FXML
    public void initialize() {
        creerCompteLabel.setOnMouseClicked(this::redirigerVersCreationCompte);
        boutonSeConnecter.setOnAction(event -> seConnecter());
    }
    
private void seConnecter() {
    String email = emailField.getText().trim();
    String motDePasse = motDePasseField.getText();

    if (email.isEmpty() || motDePasse.isEmpty()) {
        afficherAlerte("Erreur", "Veuillez remplir tous les champs", Alert.AlertType.ERROR);
        return;
    }

    boolean authentifie = utilisateurDAO.verifierUtilisateurParEmail(email, motDePasse);
    if (authentifie) {
        try {
            Utilisateur utilisateur = utilisateurDAO.getUtilisateurParEmail(email);
            Session.setUtilisateur(utilisateur); // ✅ Enregistrement global

            // Charger accueil.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/acceuilView.fxml"));
            Parent root = loader.load();

            // Passer l'utilisateur au contrôleur de l'accueil
            accueilController accueilController = loader.getController();
            accueilController.setUtilisateur(utilisateur);

            // Remplacer la scène actuelle
            Stage stage = (Stage) boutonSeConnecter.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Accueil - MemoPharma");
            stage.show();

        } catch (IOException e) {
            afficherAlerte("Erreur", "Impossible de charger la page d'accueil", Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    } else {
        afficherAlerte("Échec de la connexion", "Email ou mot de passe incorrect", Alert.AlertType.ERROR);
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