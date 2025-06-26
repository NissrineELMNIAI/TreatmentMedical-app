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
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.DAOrendezvous;
import model.RendezVous;
import model.Session;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import model.Utilisateur;

public class rendezVousController {

    @FXML private TableView<RendezVous> tablePatients;
    @FXML private TableColumn<RendezVous, Integer> colID;
    @FXML private TableColumn<RendezVous, LocalDate> colDate;
    @FXML private TableColumn<RendezVous, LocalTime> colHeure;
    @FXML private TableColumn<RendezVous, String> colObjet;
    @FXML private TableColumn<RendezVous, Void> colActions;
    @FXML private TextField searchField;
    @FXML private Button addIcon;

    private final DAOrendezvous rdvDAO = new DAOrendezvous();
    private ObservableList<RendezVous> rdvList;
    private FilteredList<RendezVous> filteredRdvList;
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

    @FXML
    public void initialize() {
        setupTableColumns();
        loadRendezVous();
        setupSearchFilter();

        addIcon.setOnAction(e -> openRdvWindow(null, "/view/rendezVousAjouter.fxml", "Ajouter Rendez-Vous"));
    }

    private void setupTableColumns() {
        colID.setCellValueFactory(new PropertyValueFactory<>("id"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        colHeure.setCellValueFactory(new PropertyValueFactory<>("heure"));
        colObjet.setCellValueFactory(new PropertyValueFactory<>("objet"));

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        colDate.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setText(empty || date == null ? null : date.format(dateFormatter));
            }
        });

        colHeure.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(LocalTime time, boolean empty) {
                super.updateItem(time, empty);
                setText(empty || time == null ? null : time.format(timeFormatter));
            }
        });

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
                    RendezVous rdv = getTableView().getItems().get(getIndex());
                    openRdvWindow(rdv, "/view/rendezVousVisualiser.fxml", "Visualiser Rendez-Vous");
                });

                btnModifier.setOnAction(event -> {
                    RendezVous rdv = getTableView().getItems().get(getIndex());
                    openRdvWindow(rdv, "/view/rendezVousModifier.fxml", "Modifier Rendez-Vous");
                });

                btnSupprimer.setOnAction(event -> {
                    RendezVous rdv = getTableView().getItems().get(getIndex());
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Confirmation de suppression");
                    alert.setHeaderText("Êtes-vous sûr de vouloir supprimer ce rendez-vous ?");
                    alert.showAndWait().ifPresent(response -> {
                        if (response == ButtonType.OK) {
                            boolean success = rdvDAO.deleteRendezVous(rdv.getId());
                            if (success) {
                                rdvList.remove(rdv);
                            } else {
                                showError("Suppression échouée", "Impossible de supprimer ce rendez-vous.");
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

    private void loadRendezVous() {
        try {
            List<RendezVous> list = rdvDAO.getAllRendezVous();
            rdvList = FXCollections.observableArrayList(list);
            filteredRdvList = new FilteredList<>(rdvList, p -> true);
            tablePatients.setItems(filteredRdvList);
        } catch (Exception e) {
            showError("Erreur de chargement", "Impossible de charger les rendez-vous.");
            e.printStackTrace();
        }
    }

    private void setupSearchFilter() {
        searchField.textProperty().addListener((obs, oldValue, newValue) -> {
            filteredRdvList.setPredicate(rdv -> {
                if (newValue == null || newValue.isEmpty()) return true;
                String lower = newValue.toLowerCase();
                return rdv.getObjet().toLowerCase().contains(lower)
                    || rdv.getDate().toString().contains(lower)
                    || rdv.getHeure().toString().contains(lower);
            });
        });
    }

    private void openRdvWindow(RendezVous rdv, String fxmlPath, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            Object controller = loader.getController();

            if (controller instanceof RendezVousVisualiserController && rdv != null) {
                ((RendezVousVisualiserController) controller).setRendezVous(rdv);
            } else if (controller instanceof RendezVousModifierController && rdv != null) {
                ((RendezVousModifierController) controller).setRendezVous(rdv);
            }

            Stage stage = new Stage();
            stage.setTitle(title);
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
            loadRendezVous();
        } catch (IOException e) {
            showError("Erreur", "Erreur lors de l'ouverture de la fenêtre : " + e.getMessage());
            e.printStackTrace();
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
    @FXML private Button staticon;

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
