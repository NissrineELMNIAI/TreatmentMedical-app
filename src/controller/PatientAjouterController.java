package controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.DAOpatient;
import model.Patient;

import java.time.LocalDate;

public class PatientAjouterController {

    @FXML private TextField textFieldNom;
    @FXML private TextField textFieldPrenom;
    @FXML private DatePicker datePickerNaiss;
    @FXML private ComboBox<String> comboSexe;
    @FXML private Button btnAdd;
    @FXML private Button btnCancel;

    private DAOpatient daoPatient = new DAOpatient();
    private Patient patientToEdit = null;

    @FXML
    public void initialize() {
        comboSexe.getItems().addAll("--Choisissez votre sexe--", "Masculin", "Féminin");
        comboSexe.setValue("--Choisissez votre sexe--"); // Définit Masculin comme valeur par défaut

    }

    public void setPatient(Patient p) {
        this.patientToEdit = p;
        if (p != null) {
            // Remplir le formulaire avec les infos du patient à modifier
            textFieldNom.setText(p.getNom());
            textFieldPrenom.setText(p.getPrenom());
            datePickerNaiss.setValue(p.getDateNaiss());
            comboSexe.setValue(p.getSexe());
            btnAdd.setText("Modifier");
        }
    }

    @FXML
    private void handleAdd() {
        String nom = textFieldNom.getText().trim();
        String prenom = textFieldPrenom.getText().trim();
        LocalDate dateNaiss = datePickerNaiss.getValue();
        String sexeSelection = comboSexe.getValue();

        if (nom.isEmpty() || prenom.isEmpty() || dateNaiss == null || sexeSelection == null) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Veuillez remplir tous les champs.");
            return;
        }

        String sexeCode = sexeSelection.equalsIgnoreCase("Masculin") ? "M" : "F";

        if (patientToEdit == null) {
            // Nouveau patient
            Patient newPatient = new Patient(0, nom, prenom, dateNaiss, sexeCode);
            boolean success = daoPatient.addPatient(newPatient);
            if (success) {
                showAlert(Alert.AlertType.INFORMATION, "Succès", "Patient ajouté avec succès.");
                closeWindow();
            } else {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Échec de l'ajout.");
            }
        } else {
            // Modifier patient existant
            patientToEdit.setNom(nom);
            patientToEdit.setPrenom(prenom);
            patientToEdit.setDateNaiss(dateNaiss);
            patientToEdit.setSexe(sexeCode);

            boolean success = daoPatient.updatePatient(patientToEdit);
            if (success) {
                showAlert(Alert.AlertType.INFORMATION, "Succès", "Patient modifié avec succès.");
                closeWindow();
            } else {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Échec de la modification.");
            }
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

    private void showAlert(Alert.AlertType type, String title, String msg) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
