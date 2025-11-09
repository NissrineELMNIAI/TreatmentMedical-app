package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import model.RendezVous;

import java.time.format.DateTimeFormatter;
import java.time.LocalDate;
import java.time.LocalTime;

public class RendezVousVisualiserController {

    @FXML
    private Label labelInfo;

    public void setRendezVous(RendezVous rendezVous) {
        if (rendezVous != null) {
            LocalDate date = rendezVous.getDate();
            LocalTime heure = rendezVous.getHeure();
            String objet = rendezVous.getObjet() != null ? rendezVous.getObjet() : "Non renseigné";

            String dateStr = (date != null)
                    ? date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                    : "Date non renseignée";

            String heureStr = (heure != null)
                    ? heure.format(DateTimeFormatter.ofPattern("HH:mm"))
                    : "Heure non renseignée";

            String texte = String.format(
                "Rendez-vous\n\nDate : %s\nHeure : %s\n\nObjet : %s",
                dateStr, heureStr, objet
            );

            labelInfo.setText(texte);
        } else {
            labelInfo.setText("Aucun rendez-vous sélectionné.");
        }
    }
}
