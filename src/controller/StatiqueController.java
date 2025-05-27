package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.*;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.time.Period;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class StatiqueController implements Initializable {

    @FXML
    private PieChart sexePieChart;

    @FXML
    private VBox barChartContainer;

    private Connection getConnection() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/treatmentmedical_db";
        String user = "root";
        String password = "";
        return DriverManager.getConnection(url, user, password);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Map<String, String> sexeMap = new HashMap<>();
        int hommesCount = 0;
        int femmesCount = 0;

        ObservableList<XYChart.Data<String, Number>> barDataList = FXCollections.observableArrayList();

        try (Connection conn = getConnection()) {
            String sql = "SELECT nom, prenom, date_naissance, sexe FROM patient";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String nom = rs.getString("nom");
                String prenom = rs.getString("prenom");
                Date dateNaissance = rs.getDate("date_naissance");
                String sexe = rs.getString("sexe").toUpperCase();

                int age = 0;
                if (dateNaissance != null) {
                    LocalDate birthDate = dateNaissance.toLocalDate();
                    age = Period.between(birthDate, LocalDate.now()).getYears();
                }

                // Clé unique : nom + prénom
                String patientKey = nom + " " + prenom;

                System.out.println("Patient: " + patientKey + ", Age: " + age + ", Sexe: " + sexe);

                sexeMap.put(patientKey, sexe);
                barDataList.add(new XYChart.Data<>(patientKey, age));

                if ("M".equals(sexe)) {
                    hommesCount++;
                } else if ("F".equals(sexe)) {
                    femmesCount++;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        // PieChart avec couleurs fixes bleu / rouge
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
                new PieChart.Data("Hommes", hommesCount),
                new PieChart.Data("Femmes", femmesCount)
        );
        sexePieChart.setData(pieChartData);

        // Couleurs valides : bleu clair et rouge tomate
        String[] pieColors = { "#a4c6e1", "#ff6347" };  // bleu clair, rouge

        int i = 0;
        for (PieChart.Data data : sexePieChart.getData()) {
            data.getNode().setStyle("-fx-pie-color: " + pieColors[i % pieColors.length] + ";");
            i++;
        }

        // BarChart
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Patient");

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Âge");

        BarChart<String, Number> ageBarChart = new BarChart<>(xAxis, yAxis);
        ageBarChart.setTitle("Âge des patients");
        ageBarChart.setPrefSize(600, 400);

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Âge");
        series.getData().addAll(barDataList);

        ageBarChart.getData().add(series);
        barChartContainer.getChildren().add(ageBarChart);

        // Appliquer la couleur des barres : bleu clair pour H, rouge pour F
        for (XYChart.Data<String, Number> data : series.getData()) {
            data.nodeProperty().addListener((obs, oldNode, newNode) -> {
                if (newNode != null) {
                    String sexe = sexeMap.get(data.getXValue());
                    if ("M".equals(sexe)) {
                        newNode.setStyle("-fx-bar-fill: #a4c6e1;");  // bleu clair
                    } else if ("F".equals(sexe)) {
                        newNode.setStyle("-fx-bar-fill: #ff6347;");  // rouge tomate
                    }
                }
            });
        }
    }
}
