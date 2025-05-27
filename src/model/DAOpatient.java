package model;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DAOpatient {

    private static final String URL = "jdbc:mysql://localhost:3306/treatmentmedical_db";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    private Connection connection;

    public DAOpatient() {
        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private boolean isConnected() {
        try {
            return connection != null && !connection.isClosed();
        } catch (SQLException e) {
            return false;
        }
    }
public List<Patient> getAllPatients() {
    List<Patient> patients = new ArrayList<>();
    if (!isConnected()) {
        System.err.println("Pas de connexion à la base de données.");
        return patients;
    }
    String query = "SELECT * FROM patient";
    try (Statement stmt = connection.createStatement();
         ResultSet rs = stmt.executeQuery(query)) {
        while (rs.next()) {
            java.sql.Date sqlDate = rs.getDate("date_naissance");
            // Vérifier si la date est nulle avant conversion
            LocalDate dateNaissance = (sqlDate != null) ? sqlDate.toLocalDate() : null;

            patients.add(new Patient(
                    rs.getInt("id"),
                    rs.getString("nom"),
                    rs.getString("prenom"),
                    dateNaissance,
                    rs.getString("sexe")
            ));
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return patients;
}


    public boolean addPatient(Patient patient) {
        if (!isConnected()) {
            System.err.println("Pas de connexion à la base de données.");
            return false;
        }
        String query = "INSERT INTO patient(nom, prenom, date_naissance, sexe) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, patient.getNom());
            pstmt.setString(2, patient.getPrenom());
            pstmt.setDate(3, java.sql.Date.valueOf(patient.getDateNaiss()));
            pstmt.setString(4, patient.getSexe());

            int rowsInserted = pstmt.executeUpdate();
            return rowsInserted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deletePatient(int patientId) {
        if (!isConnected()) {
            System.err.println("Pas de connexion à la base de données.");
            return false;
        }
        String query = "DELETE FROM patient WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, patientId);
            int rowsDeleted = pstmt.executeUpdate();
            return rowsDeleted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updatePatient(Patient patient) {
        if (!isConnected()) {
            System.err.println("Pas de connexion à la base de données.");
            return false;
        }
        String query = "UPDATE patient SET nom = ?, prenom = ?, date_naissance = ?, sexe = ? WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, patient.getNom());
            pstmt.setString(2, patient.getPrenom());
            pstmt.setDate(3, java.sql.Date.valueOf(patient.getDateNaiss()));
            pstmt.setString(4, patient.getSexe());
            pstmt.setInt(5, patient.getId());

            int rowsUpdated = pstmt.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public static int getNombrePatientsParSexe(String sexe) {
    int count = 0;
    String query = "SELECT COUNT(*) FROM patient WHERE sexe = ?";

    try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/treatmentmedical_db", "root", "");
         PreparedStatement stmt = conn.prepareStatement(query)) {

        stmt.setString(1, sexe);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            count = rs.getInt(1);
        }

    } catch (SQLException e) {
        e.printStackTrace();
    }

    return count;
}

}