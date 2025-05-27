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
import model.DAOpatient;
import model.Patient;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javafx.scene.input.MouseEvent;

public class PatientController {

    @FXML private TableView<Patient> tablePatients;
    @FXML private TableColumn<Patient, Integer> colID;
    @FXML private TableColumn<Patient, String> colNom;
    @FXML private TableColumn<Patient, String> colPrenom;
    @FXML private TableColumn<Patient, LocalDate> colDate;
    @FXML private TableColumn<Patient, String> colSexe;
    @FXML private TableColumn<Patient, Void> colActions;
    @FXML private TextField searchField;
    @FXML private Button addIcon;

    private final DAOpatient patientDAO = new DAOpatient();

    private ObservableList<Patient> patientsList;
    private FilteredList<Patient> filteredPatientsList;

    @FXML
    public void initialize() {
        setupTableColumns();
        loadPatients();
        setupSearchFilter();

        addIcon.setOnAction(e -> openPatientWindow(null, "/view/patientAjouter.fxml", "Ajouter Patient"));
    }

    private void setupTableColumns() {
        colID.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        colPrenom.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("dateNaiss"));
        colSexe.setCellValueFactory(new PropertyValueFactory<>("sexe"));

        colDate.setCellFactory(column -> new TableCell<>() {
            private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            @Override
            protected void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setText(empty || date == null ? null : date.format(formatter));
            }
        });

        colActions.setCellFactory(param -> new TableCell<>() {
            private final Button btnVisualiser = new Button();
            private final Button btnModifier = new Button();
            private final Button btnSupprimer = new Button();

            {
                btnVisualiser.setGraphic(getIcon("/icons/eye.png"));
                btnVisualiser.setStyle("-fx-background-color: transparent; -fx-cursor: hand;");
                btnVisualiser.setTooltip(new Tooltip("Visualiser"));

                btnModifier.setGraphic(getIcon("/icons/edit.png"));
                btnModifier.setStyle("-fx-background-color: transparent; -fx-cursor: hand;");
                btnModifier.setTooltip(new Tooltip("Modifier"));

                btnSupprimer.setGraphic(getIcon("/icons/delete.png"));
                btnSupprimer.setStyle("-fx-background-color: transparent; -fx-cursor: hand;");
                btnSupprimer.setTooltip(new Tooltip("Supprimer"));

                btnVisualiser.setOnAction(event -> {
                    Patient patient = getTableView().getItems().get(getIndex());
                    openPatientWindow(patient, "/view/patientVisualiser.fxml", "Visualiser Patient");
                });

                btnModifier.setOnAction(event -> {
                    Patient patient = getTableView().getItems().get(getIndex());
                    openPatientWindow(patient, "/view/patientModifier.fxml", "Modifier Patient");
                });

                btnSupprimer.setOnAction(event -> {
                    Patient patient = getTableView().getItems().get(getIndex());
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Confirmation de suppression");
                    alert.setHeaderText("Êtes-vous sûr de vouloir supprimer ce patient ?");
                    alert.setContentText(null);

                    alert.showAndWait().ifPresent(response -> {
                        if (response == ButtonType.OK) {
                            boolean success = patientDAO.deletePatient(patient.getId());
                            if (success) {
                                patientsList.remove(patient);
                            } else {
                                showError("Suppression échouée", "Impossible de supprimer ce patient.");
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

    private void loadPatients() {
        try {
            List<Patient> list = patientDAO.getAllPatients();
            patientsList = FXCollections.observableArrayList(list);
            filteredPatientsList = new FilteredList<>(patientsList, p -> true);
            tablePatients.setItems(filteredPatientsList);
        } catch (Exception e) {
            showError("Erreur de chargement", "Impossible de charger les patients.");
            e.printStackTrace();
        }
    }

    private void setupSearchFilter() {
        searchField.textProperty().addListener((obs, oldValue, newValue) -> {
            filteredPatientsList.setPredicate(patient -> {
                if (newValue == null || newValue.isEmpty()) return true;
                String lower = newValue.toLowerCase();
                return patient.getNom().toLowerCase().contains(lower)
                        || patient.getPrenom().toLowerCase().contains(lower)
                        || patient.getSexe().toLowerCase().contains(lower);
            });
        });
    }

    private void openPatientWindow(Patient patient, String fxmlPath, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            Object controller = loader.getController();

            if (controller instanceof PatientVisualiserController && patient != null) {
                ((PatientVisualiserController) controller).setPatient(patient);
            } else if (controller instanceof PatientModifierController && patient != null) {
                ((PatientModifierController) controller).setPatient(patient);
            }

            Stage stage = new Stage();
            stage.setTitle(title);
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
            loadPatients();
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

    @FXML private void ouvrirPatients(ActionEvent event) {}
    @FXML private void ouvrirTraitements(ActionEvent event) throws IOException { switchView(event, "/view/treatmentView.fxml"); }
    @FXML private void ouvrirRendezVous(ActionEvent event) throws IOException { switchView(event, "/view/rendezVousView.fxml"); }
  //  @FXML private void ouvrirAccueil(ActionEvent event) throws IOException { switchView(event, "/view/accueilView.fxml"); }
    @FXML
private void ouvrirAccueil(MouseEvent event) {
    try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/accueilView.fxml"));
        Parent root = loader.load();


        // Remplacer la scène actuelle
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    } catch (IOException e) {
        e.printStackTrace();
    }
}
@FXML
private Button staticon;

@FXML

private void ouvrirStatistiques(ActionEvent event) {
    try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/statique.fxml")); // chemin correct
        Parent root = loader.load();

        Stage stage = new Stage();
        stage.setTitle("Statistiques des patients");
        stage.setScene(new Scene(root));
        stage.show();
    } catch (IOException e) {
        e.printStackTrace();
    }
}

@FXML
private void handleCompteClick(ActionEvent event) {
    try {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/compte.fxml"));
        Parent root = fxmlLoader.load();

        Stage stage = new Stage();
        stage.setTitle("Profil Utilisateur");
        stage.setScene(new Scene(root));
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();
    } catch (IOException e) {
        e.printStackTrace();
    }
}


} 
