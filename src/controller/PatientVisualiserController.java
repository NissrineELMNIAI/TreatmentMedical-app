package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import model.Patient;

public class PatientVisualiserController {

    @FXML
    private Label labelInfo;
  public void setPatient(Patient patient) {
    if (patient != null) {
        String sexe = patient.getSexe();
        if ("F".equalsIgnoreCase(sexe)) sexe = "Féminin";
        else if ("M".equalsIgnoreCase(sexe)) sexe = "Masculin";

        String texte;
        if (patient.getDateNaiss() != null) {
            texte = String.format(
                "Patient : %s %s%n%nNé le : %02d/%02d/%04d%n%nSexe : %s",
                patient.getNom(),
                patient.getPrenom(),
                patient.getDateNaiss().getDayOfMonth(),
                patient.getDateNaiss().getMonthValue(),
                patient.getDateNaiss().getYear(),
                sexe
            );
        } else {
            // Si date de naissance absente, on l'indique autrement
            texte = String.format(
                "Patient : %s %s%n%nDate de naissance : non renseignée%n%nSexe : %s",
                patient.getNom(),
                patient.getPrenom(),
                sexe
            );
        }

        labelInfo.setText(texte);
    } else {
        labelInfo.setText("");
    }
}

}
