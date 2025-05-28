package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import model.DAOpatient;
import model.DAOrendezvous;
import model.DAOtraitement;
import model.RendezVous;

import java.time.Month;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import model.DAOpatient;
import model.DAOtraitement;
import model.Patient;
import model.traitement;
public class StatistiquesController {

    @FXML private Label totalPatientsLabel;
    @FXML private Label totalTraitementsLabel;
    @FXML private Label totalRdvLabel;
    @FXML private PieChart patientsSexeChart;
    @FXML private PieChart traitementsTypeChart;
    @FXML private BarChart<String, Number> rdvMoisChart;

    private final DAOpatient patientDAO = new DAOpatient();
    private final DAOtraitement traitementDAO = new DAOtraitement();
    private final DAOrendezvous rdvDAO = new DAOrendezvous();

    @FXML
    public void initialize() {
        loadPatientsStats();
        loadTraitementsStats();
        loadRdvStats();
    }

private void loadPatientsStats() {
    List<Patient> patients = patientDAO.getAllPatientsWithSexe();
    totalPatientsLabel.setText(String.valueOf(patients.size()));

    // Compter par sexe
    Map<String, Long> countBySexe = patients.stream()
            .collect(Collectors.groupingBy(
                    Patient::getSexe,
                    Collectors.counting()
            ));

    ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
    countBySexe.forEach((sexe, count) ->
            pieChartData.add(new PieChart.Data(sexe + " (" + count + ")", count))
    );

    patientsSexeChart.setData(pieChartData);
}



private void loadTraitementsStats() {
    List<traitement> traitements = traitementDAO.getAllTraitementsWithType();
    totalTraitementsLabel.setText(String.valueOf(traitements.size()));

    // Compter par type
    Map<String, Long> countByType = traitements.stream()
            .collect(Collectors.groupingBy(
                    traitement::getType,
                    Collectors.counting()
            ));

    ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
    countByType.forEach((type, count) ->
            pieChartData.add(new PieChart.Data(type + " (" + count + ")", count))
    );

    traitementsTypeChart.setData(pieChartData);
}


    private void loadRdvStats() {
        List<RendezVous> rdvs = rdvDAO.getAllRendezVous(); // <-- Utilise les objets RendezVous
        totalRdvLabel.setText(String.valueOf(rdvs.size()));

        // Compter par mois
        Map<Month, Long> countByMonth = new HashMap<>();
        for (Month month : Month.values()) {
            countByMonth.put(month, 0L);
        }

        rdvs.forEach(rdv -> {
            if (rdv.getDate() != null) {
                Month month = rdv.getDate().getMonth();
                countByMonth.put(month, countByMonth.get(month) + 1);
            }
        });

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        countByMonth.entrySet().stream()
            .sorted(Map.Entry.comparingByKey()) // trie les mois
            .forEach(entry -> 
                series.getData().add(new XYChart.Data<>(entry.getKey().toString(), entry.getValue()))
            );

        rdvMoisChart.getData().add(series);
    }
}
