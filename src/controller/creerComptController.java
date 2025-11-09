package controller;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene; 
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.UtilisateurDAO;

public class creerComptController {

    @FXML private TextField usernameField, emailField;
    @FXML private PasswordField passwordField, confirmPasswordField;
    @FXML private Button createAccountButton;
    @FXML private Label loginLinkLabel;

    private final UtilisateurDAO utilisateurDAO = new UtilisateurDAO();

    @FXML
    public void initialize() {
        createAccountButton.setOnAction(this::handleCreateAccount);
        loginLinkLabel.setOnMouseClicked(e -> System.out.println("Retour à la connexion"));
    }

    @FXML
    private void handleCreateAccount(ActionEvent event) {
        String username = usernameField.getText().trim();
        String email = emailField.getText().trim();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        if (username.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            showAlert("Erreur", "Tous les champs sont requis.");
            return;
        }

        if (!password.equals(confirmPassword)) {
            showAlert("Erreur", "Les mots de passe ne correspondent pas.");
            return;
        }

        boolean success = utilisateurDAO.ajouterUtilisateur(username, email, password);

        if (success) {
            showAlert("Succès", "Compte créé avec succès !");
            ouvrirConnex(event);
        } else {
            showAlert("Erreur", "Nom d'utilisateur ou email déjà utilisé.");
        }
    }

    @FXML
    private void ouvrirConnex(ActionEvent event) {
        changerScene(event, "/view/connexionView.fxml");
    }

  private void changerScene(ActionEvent event, String fxmlPath) {
    try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
        Parent root = loader.load();

        // Obtenir le stage actuel et le fermer après changement de scène
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root)); // Remplacement de la scène
        stage.show();

        // Fermer la fenêtre actuelle
        stage.close();  

    } catch (IOException e) {
        e.printStackTrace();
        showAlert("Erreur", "Impossible de charger la page demandée.");
    }
}



    private void showAlert(String titre, String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
