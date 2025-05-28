package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import model.traitement;

public class traitementVisualiserController {

    @FXML private Label labelParagraphe;
    @FXML private VBox detailsContainer;

    public void setTraitement(traitement t) {
        if (t != null) {
            String paragraphe = String.format(
                "Le traitement nommé \"%s\" coûte %.2f € et doit être pris %d fois par jour. " +
                "Il s'agit d'un traitement de type \"%s\" d'une durée de %s.",
                t.getNom(),
                t.getPrix(),
                t.getFoisParJour(),
                t.getType(),
                t.getDuree()
            );
            labelParagraphe.setText(paragraphe);
}
}
}