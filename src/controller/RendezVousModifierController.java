package controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.DAOrendezvous;
import model.RendezVous;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class RendezVousModifierController {

    @FXML
    private DatePicker datePicker;

    @FXML
    private ComboBox<String> comboHeure; // ← à la place de TextField textFieldHeure
// Format HH:mm

    @FXML
    private TextField textFieldObjet;

    @FXML
    private Button btnSave;

    @FXML
    private Button btnCancel;

    private RendezVous rendezVous;
    private final DAOrendezvous daoRendezVous = new DAOrendezvous();

    public void initialize() {
        // Rien à initialiser ici pour l'instant
    }

    public void setRendezVous(RendezVous rdv) {
        this.rendezVous = rdv;

        // Remplir les champs avec les valeurs du rendez-vous
        datePicker.setValue(rdv.getDate());
        comboHeure.setValue(rendezVous.getHeure().format(DateTimeFormatter.ofPattern("HH:mm")));
        textFieldObjet.setText(rdv.getObjet());
    }

    @FXML
    private void handleSave() {
        LocalDate date = datePicker.getValue();
        String heureStr = comboHeure.getValue();
        String objet = textFieldObjet.getText().trim();

        if (date == null || heureStr.isEmpty() || objet.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Veuillez remplir tous les champs.");
            return;
        }

        LocalTime heure;
        try {
            heure = LocalTime.parse(heureStr);
        } catch (DateTimeParseException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Format de l'heure invalide (HH:mm).");
            return;
        }

        // Mettre à jour les données du rendez-vous
        rendezVous.setDate(date);
        rendezVous.setHeure(heure);
        rendezVous.setObjet(objet);

        boolean success = daoRendezVous.updateRendezVous(rendezVous);
        if (success) {
            showAlert(Alert.AlertType.INFORMATION, "Succès", "Rendez-vous modifié avec succès.");
            closeWindow();
        } else {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Échec de la modification du rendez-vous.");
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
