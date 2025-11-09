package controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.Patient;
import model.DAOpatient;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class PatientModifierController {

    @FXML
    private TextField textFieldNom;

    @FXML
    private TextField textFieldPrenom;

    @FXML
    private DatePicker datePickerNaiss;

    @FXML
    private ComboBox<String> comboSexe;

    @FXML
    private Button btnSave;

    @FXML
    private Button btnCancel;

    private Patient patient;

    private final DAOpatient patientDAO = new DAOpatient();

    public void initialize() {
        // Initialiser le ComboBox avec les options sexe
        comboSexe.getItems().addAll("Masculin", "Féminin");
    }

    public void setPatient(Patient patient) {
        this.patient = patient;

        // Remplir les champs avec les données existantes
        textFieldNom.setText(patient.getNom());
        textFieldPrenom.setText(patient.getPrenom());
        datePickerNaiss.setValue(patient.getDateNaiss());

        // Convertir sexe stocké (ex: "M" ou "F") en texte complet pour l'affichage
        String sexeTexte = switch (patient.getSexe().toUpperCase()) {
            case "M" -> "Masculin";
            case "F" -> "Féminin";
            default -> "";
        };
        comboSexe.setValue(sexeTexte);
    }

    @FXML
    private void handleSave() {
        String nom = textFieldNom.getText().trim();
        String prenom = textFieldPrenom.getText().trim();
        LocalDate dateNaiss = datePickerNaiss.getValue();
        String sexeSelection = comboSexe.getValue();

        if (nom.isEmpty() || prenom.isEmpty() || dateNaiss == null || sexeSelection == null) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Veuillez remplir tous les champs.");
            return;
        }

        // Convertir "Masculin"/"Féminin" en "M"/"F"
        String sexeCode = sexeSelection.equalsIgnoreCase("Masculin") ? "M" : "F";

        // Mettre à jour l'objet patient
        patient.setNom(nom);
        patient.setPrenom(prenom);
        patient.setDateNaiss(dateNaiss);
        patient.setSexe(sexeCode);

        // Mettre à jour la base via DAO
        boolean success = patientDAO.updatePatient(patient);
        if (success) {
            showAlert(Alert.AlertType.INFORMATION, "Succès", "Patient modifié avec succès.");
            closeWindow();
        } else {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Échec de la modification.");
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
