package controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.DAOrendezvous;
import model.RendezVous;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;

public class RendezVousAjouterController implements Initializable {

    @FXML private DatePicker datePicker;
    @FXML private ComboBox<String> comboHeure;
    @FXML private TextField textFieldObjet;
    @FXML private Button btnAdd;
    @FXML private Button btnCancel;

    private final DAOrendezvous daoRendezvous = new DAOrendezvous();
    private RendezVous rendezvousToEdit = null;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ObservableList<String> hours = FXCollections.observableArrayList();
        for (int i = 8; i <= 17; i++) {
            for (int j = 0; j < 60; j += 30) {
                String hour = String.format("%02d:%02d", i, j);
                hours.add(hour);
            }
        }
        comboHeure.setItems(hours);
        comboHeure.setPromptText("Sélectionner l'heure");
    }

    public void setRendezvous(RendezVous rendezvous) {
        this.rendezvousToEdit = rendezvous;
        if (rendezvous != null) {
            datePicker.setValue(rendezvous.getDate());
            comboHeure.setValue(rendezvous.getHeure().format(java.time.format.DateTimeFormatter.ofPattern("HH:mm")));
            textFieldObjet.setText(rendezvous.getObjet());
            btnAdd.setText("Modifier");
        }
    }

    @FXML
    private void handleAdd() {
        LocalDate selectedDate = datePicker.getValue();
        String selectedHeureStr = comboHeure.getValue();
        String objet = textFieldObjet.getText().trim();

        if (selectedDate == null || selectedHeureStr == null || selectedHeureStr.isEmpty() || objet.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Erreur de saisie", "Veuillez remplir tous les champs.");
            return;
        }

        LocalTime selectedHeure;
        try {
            selectedHeure = LocalTime.parse(selectedHeureStr);
        } catch (DateTimeParseException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur de format", "L'heure sélectionnée n'est pas valide.");
            return;
        }

        boolean success;
        if (rendezvousToEdit == null) {
            // Création d’un nouveau rendez-vous
            RendezVous newRdv = new RendezVous(0, selectedDate, selectedHeure, objet);
            success = daoRendezvous.addRendezVous(newRdv);
            showAlert(success ? Alert.AlertType.INFORMATION : Alert.AlertType.ERROR,
                    success ? "Succès" : "Erreur",
                    success ? "Rendez-vous ajouté avec succès !" : "Échec de l'ajout du rendez-vous.");
        } else {
            // Modification du rendez-vous existant
            rendezvousToEdit.setDate(selectedDate);
            rendezvousToEdit.setHeure(selectedHeure);
            rendezvousToEdit.setObjet(objet);
            success = daoRendezvous.updateRendezVous(rendezvousToEdit);
            showAlert(success ? Alert.AlertType.INFORMATION : Alert.AlertType.ERROR,
                    success ? "Succès" : "Erreur",
                    success ? "Rendez-vous modifié avec succès !" : "Échec de la modification du rendez-vous.");
        }

        if (success) closeWindow();
    }

    @FXML
    private void handleCancel() {
        closeWindow();
    }

    private void closeWindow() {
        Stage stage = (Stage) btnCancel.getScene().getWindow();
        stage.close();
    }

    private void showAlert(Alert.AlertType type, String title, String msg) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
