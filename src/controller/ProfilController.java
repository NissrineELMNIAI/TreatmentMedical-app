
package controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

public class ProfilController implements Initializable {

    @FXML private Label nomLabel;
    @FXML private Label emailLabel;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        nomLabel.setText("Nom : Lamyae");
        emailLabel.setText("Email : lamyae@example.com");
    }

    @FXML
    private void handleLogout(ActionEvent event) {
        System.out.println("Déconnexion...");
    }
}

