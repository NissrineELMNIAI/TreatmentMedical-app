package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.traitement;
import model.DAOtraitement;

public class traitementModifierController {

    @FXML
    private TextField txtNom;
    @FXML
    private TextField txtPrix;
    @FXML
    private TextField txtFoisParJour;
    @FXML
    private TextField txtType;
    @FXML
    private TextField txtDuree;
    @FXML
    private Button btnSave;
    @FXML
    private Button btnCancel;

    private traitement traitement; // traitement à modifier
    private final DAOtraitement traitementDAO = new DAOtraitement();

    public void setTraitement(traitement traitement) {
        this.traitement = traitement;

        // Remplir les champs avec les données actuelles
        txtNom.setText(traitement.getNom());
        txtPrix.setText(String.valueOf(traitement.getPrix()));
        txtFoisParJour.setText(String.valueOf(traitement.getFoisParJour()));
        txtType.setText(traitement.getType());
        txtDuree.setText(traitement.getDuree());
    }

    @FXML
    private void handleSave() {
        try {
            // Mettre à jour les valeurs du traitement
            traitement.setNom(txtNom.getText());
            traitement.setPrix(Double.parseDouble(txtPrix.getText()));
            traitement.setFoisParJour(Integer.parseInt(txtFoisParJour.getText()));
            traitement.setType(txtType.getText());
            traitement.setDuree(txtDuree.getText());

            // Mettre à jour la base via DAO
            boolean success = traitementDAO.updateTraitement(traitement);
            if (success) {
                showAlert(Alert.AlertType.INFORMATION, "Succès", "Traitement modifié avec succès.");
                closeWindow();
            } else {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Échec de la modification du traitement.");
            }
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur de saisie", "Prix ou Fois Par Jour invalide.");
        }
    }

    @FXML
    private void handleCancel() {
        closeWindow();
    }

    private void closeWindow() {
        Stage stage = (Stage) btnCancel.getScene().getWindow();
        stage.close();
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
