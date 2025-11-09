package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.DAOtraitement;
import model.traitement;
import model.Session;
import java.io.IOException;
import java.util.List;
import javafx.scene.input.MouseEvent;
import model.Utilisateur;

public class traitementController {

    @FXML private TableView<traitement> tableTraitements;
    @FXML private TableColumn<traitement, Integer> colID;
    @FXML private TableColumn<traitement, String> colNom;
    @FXML private TableColumn<traitement, Double> colPrix;
    @FXML private TableColumn<traitement, Integer> colFoisParJour;
    @FXML private TableColumn<traitement, String> colType;
    @FXML private TableColumn<traitement, String> colDuree;
    @FXML private TableColumn<traitement, Void> colActions;
    @FXML private TextField searchField;
    @FXML private Button addIcon;

    private final DAOtraitement traitementDAO = new DAOtraitement();
    private ObservableList<traitement> traitementsList;
    private FilteredList<traitement> filteredTraitementsList;

    @FXML
    public void initialize() {
        setupTableColumns();
        loadTraitements();
        setupSearchFilter();

        addIcon.setOnAction(e -> openTraitementWindow(null, "/view/traitementAjouter.fxml", "Ajouter Traitement"));
    }
    private Utilisateur utilisateur;

    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
    }
    @FXML
private void ouvrirStatistiques(ActionEvent event) {
    try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/statistiqueView.fxml"));
        Parent root = loader.load();
        Stage stage = new Stage();
        stage.setTitle("Statistiques");
        stage.getIcons().add(new Image("/icons/sansBackground.png"));
        stage.setScene(new Scene(root));
        stage.show();
    } catch (IOException e) {
        e.printStackTrace();
    }
}
public void refreshTable() {
    loadTraitements(); 
}
    private void setupTableColumns() {
        colID.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        colPrix.setCellValueFactory(new PropertyValueFactory<>("prix"));
        colFoisParJour.setCellValueFactory(new PropertyValueFactory<>("foisParJour"));
        colType.setCellValueFactory(new PropertyValueFactory<>("type"));
        colDuree.setCellValueFactory(new PropertyValueFactory<>("duree"));

        colActions.setCellFactory(param -> new TableCell<>() {
            private final Button btnVisualiser = new Button();
            private final Button btnModifier = new Button();
            private final Button btnSupprimer = new Button();

            {
                btnVisualiser.setGraphic(getIcon("/icons/1.png"));
                btnVisualiser.setStyle("-fx-background-color: transparent; -fx-cursor: hand;");
                btnVisualiser.setTooltip(new Tooltip("Visualiser"));

                btnModifier.setGraphic(getIcon("/icons/2.png"));
                btnModifier.setStyle("-fx-background-color: transparent; -fx-cursor: hand;");
                btnModifier.setTooltip(new Tooltip("Modifier"));

                btnSupprimer.setGraphic(getIcon("/icons/3.png"));
                btnSupprimer.setStyle("-fx-background-color: transparent; -fx-cursor: hand;");
                btnSupprimer.setTooltip(new Tooltip("Supprimer"));

                btnVisualiser.setOnAction(event -> {
                    traitement traitement = getTableView().getItems().get(getIndex());
                    openTraitementWindow(traitement, "/view/traitementVisualiser.fxml", "Visualiser Traitement");
                });

                btnModifier.setOnAction(event -> {
                    traitement traitement = getTableView().getItems().get(getIndex());
                    openTraitementWindow(traitement, "/view/traitementModifier.fxml", "Modifier Traitement");
                });

                btnSupprimer.setOnAction(event -> {
                    traitement traitement = getTableView().getItems().get(getIndex());
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Confirmation de suppression");
                    alert.setHeaderText("Êtes-vous sûr de vouloir supprimer ce traitement ?");
                    alert.setContentText(null);

                    alert.showAndWait().ifPresent(response -> {
                        if (response == ButtonType.OK) {
                            boolean success = traitementDAO.deleteTraitement(traitement.getId());
                            if (success) {
                                traitementsList.remove(traitement);
                            } else {
                                showError("Suppression échouée", "Impossible de supprimer ce traitement.");
                            }
                        }
                    });
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    HBox pane = new HBox(5, btnVisualiser, btnModifier, btnSupprimer);
                    setGraphic(pane);
                }
            }
        });
    }

    private void loadTraitements() {
        try {
            List<traitement> list = traitementDAO.getAllTraitements();
            traitementsList = FXCollections.observableArrayList(list);
            filteredTraitementsList = new FilteredList<>(traitementsList, p -> true);
            tableTraitements.setItems(filteredTraitementsList);
        } catch (Exception e) {
            showError("Erreur de chargement", "Impossible de charger les traitements.");
            e.printStackTrace();
        }
    }

    private void setupSearchFilter() {
        searchField.textProperty().addListener((obs, oldValue, newValue) -> {
            filteredTraitementsList.setPredicate(traitement -> {
                if (newValue == null || newValue.isEmpty()) return true;
                String lower = newValue.toLowerCase();
                return traitement.getNom().toLowerCase().contains(lower)
                        || traitement.getType().toLowerCase().contains(lower)
                        || traitement.getDuree().toLowerCase().contains(lower)
                        || String.valueOf(traitement.getPrix()).contains(lower)
                        || String.valueOf(traitement.getFoisParJour()).contains(lower);
            });
        });
    }

 private void openTraitementWindow(traitement traitement, String fxmlPath, String title) {
    try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
        Parent root = loader.load();
        Object controller = loader.getController();

        // Si ajout
        if (controller instanceof traitementAjouterController) {
            ((traitementAjouterController) controller).setParentController(this);
        }

        // Si visualisation
        if (controller instanceof traitementVisualiserController && traitement != null) {
            ((traitementVisualiserController) controller).setTraitement(traitement);
        }

        // Si modification
        if (controller instanceof traitementModifierController && traitement != null) {
            ((traitementModifierController) controller).setTraitement(traitement);
        }

        Stage stage = new Stage();
        stage.setTitle(title);
        stage.setScene(new Scene(root));
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();

        refreshTable(); // Rafraîchir après fermeture
    } catch (IOException e) {
        showError("Erreur", "Impossible d'ouvrir la fenêtre: " + e.getMessage());
    }
}


    private ImageView getIcon(String path) {
        ImageView icon = new ImageView(new Image(getClass().getResourceAsStream(path)));
        icon.setFitHeight(16);
        icon.setFitWidth(16);
        return icon;
    }

    private void showError(String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void switchView(ActionEvent event, String fxmlPath) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
        Parent root = loader.load();
        Scene scene = ((Node) event.getSource()).getScene();
        Stage stage = (Stage) scene.getWindow();
        stage.setScene(new Scene(root));
    }

    @FXML
    private void ouvrirRendezVous(ActionEvent event) {
            try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/rendezVousView.fxml"));
        Parent root = loader.load();

        // Transfert de l'utilisateur connecté
        rendezVousController rendezVousController = loader.getController();
        rendezVousController.setUtilisateur(utilisateur);

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setTitle("Rendez-Vous - MemoPharma");
        stage.show();

    } catch (IOException e) {
        e.printStackTrace();
    }
    }
    
     @FXML
    private void ouvrirTraitements(ActionEvent event) {
           try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/treatmentView.fxml"));
        Parent root = loader.load();

        // Transfert de l'utilisateur connecté
       traitementController traitementController = loader.getController();
        traitementController.setUtilisateur(utilisateur);

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setTitle("Traitements - MemoPharma");
        stage.show();

    } catch (IOException e) {
        e.printStackTrace();
    }
    }

    
    @FXML
private void ouvrirPatients(ActionEvent event) {
    try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/patientView.fxml"));
        Parent root = loader.load();

        // Transfert de l'utilisateur connecté
        PatientController patientController = loader.getController();
        patientController.setUtilisateur(utilisateur);

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setTitle("Patients - MemoPharma");
        stage.show();

    } catch (IOException e) {
        e.printStackTrace();
    }
}
    
    @FXML
    private void ouvrirAcceuil(ActionEvent event) {
           try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/acceuilView.fxml"));
        Parent root = loader.load();

        // Transfert de l'utilisateur connecté
       accueilController accueilController = loader.getController();
        accueilController.setUtilisateur(utilisateur);

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setTitle("Acceuil - MemoPharma");
        stage.show();

    } catch (IOException e) {
        e.printStackTrace();
    }
    }
    @FXML
private void modifierTraitement(traitement traitement) {
    try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/traitementModifier.fxml"));
        Parent root = loader.load();

        traitementModifierController controller = loader.getController();
        controller.setTraitement(traitement); // on lui passe le traitement à modifier

        Stage stage = new Stage();
        stage.setTitle("Modifier Traitement");
        stage.setScene(new Scene(root));
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();


    } catch (IOException e) {
        e.printStackTrace();
    }
}
@FXML
private void handleCompteClick(ActionEvent event) {
    try {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/compte.fxml"));
        Parent root = fxmlLoader.load();

        // Transfert de l'utilisateur connecté via Session
        compteController controller = fxmlLoader.getController();
        controller.setUtilisateur(Session.getUtilisateur());

        Stage stage = new Stage();
        stage.setTitle("Profil Utilisateur");
        stage.getIcons().add(new Image("/icons/sansBackground.png"));
        stage.setScene(new Scene(root));
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();
    } catch (IOException e) {
        e.printStackTrace();
    }
}


}