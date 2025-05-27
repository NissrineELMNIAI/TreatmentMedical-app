package controller;

import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.UtilisateurDAO;

public class AjouterCompteController {

    @FXML private TextField usernameField;
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;

    private UtilisateurDAO utilisateurDAO = new UtilisateurDAO();

    @FXML
    private void handleAjouter() {
        String username = usernameField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();

        if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
            System.out.println("Veuillez remplir tous les champs !");
            return;
        }

        boolean success = utilisateurDAO.ajouterUtilisateur(username, email, password);
        if (success) {
            System.out.println("Compte ajouté avec succès !");
            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.close();
        } else {
            System.out.println("Erreur lors de l'ajout du compte.");
        }
    }

    @FXML
    private void handleAnnuler() {
        Stage stage = (Stage) usernameField.getScene().getWindow();
        stage.close();
    }
}
