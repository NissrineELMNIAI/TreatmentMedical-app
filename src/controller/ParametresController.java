package controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Utilisateur;

public class ParametresController {

    @FXML private TextField usernameField;
    @FXML private TextField emailField;

    private Utilisateur utilisateur;

    // Méthode appelée depuis l'extérieur pour injecter l'utilisateur
    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
        usernameField.setText(utilisateur.getUsername());
        emailField.setText(utilisateur.getEmail());
    }

    @FXML
    private void handleEnregistrer() {
        if (utilisateur != null) {
            utilisateur.setUsername(usernameField.getText());
            utilisateur.setEmail(emailField.getText());
            System.out.println("Paramètres enregistrés: " + utilisateur.getUsername() + ", " + utilisateur.getEmail());

            // TODO: Appeler DAO pour enregistrer les changements en BDD ici

            // Fermer la fenêtre
            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.close();
        }
    }

    @FXML
    private void handleAnnuler() {
        Stage stage = (Stage) usernameField.getScene().getWindow();
        stage.close();
    }
}
