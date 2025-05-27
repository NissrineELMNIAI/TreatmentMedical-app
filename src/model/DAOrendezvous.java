package model;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class DAOrendezvous {

    private static final String URL = "jdbc:mysql://localhost:3306/treatmentmedical_db";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    private Connection connection;

    public DAOrendezvous() {
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

    public List<RendezVous> getAllRendezVous() {
        List<RendezVous> rendezVousList = new ArrayList<>();
        if (!isConnected()) {
            System.err.println("Pas de connexion à la base de données.");
            return rendezVousList;
        }
        String query = "SELECT * FROM rendezvous ORDER BY date, heure";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                int id = rs.getInt("id");
                LocalDate date = rs.getDate("date").toLocalDate();
                LocalTime heure = rs.getTime("heure").toLocalTime();
                String objet = rs.getString("object");

                RendezVous rdv = new RendezVous(id, date, heure, objet);
                rendezVousList.add(rdv);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rendezVousList;
    }

    public boolean addRendezVous(RendezVous rendezVous) {
        if (!isConnected()) {
            System.err.println("Pas de connexion à la base de données.");
            return false;
        }
        String query = "INSERT INTO rendezvous (date, heure, object) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setDate(1, Date.valueOf(rendezVous.getDate()));
            pstmt.setTime(2, Time.valueOf(rendezVous.getHeure()));
            pstmt.setString(3, rendezVous.getObjet());

            int rowsInserted = pstmt.executeUpdate();
            return rowsInserted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteRendezVous(int rendezVousId) {
        if (!isConnected()) {
            System.err.println("Pas de connexion à la base de données.");
            return false;
        }
        String query = "DELETE FROM rendezvous WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, rendezVousId);
            int rowsDeleted = pstmt.executeUpdate();
            return rowsDeleted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateRendezVous(RendezVous rendezVous) {
        if (!isConnected()) {
            System.err.println("Pas de connexion à la base de données.");
            return false;
        }
        String query = "UPDATE rendezvous SET date = ?, heure = ?, object = ? WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setDate(1, Date.valueOf(rendezVous.getDate()));
            pstmt.setTime(2, Time.valueOf(rendezVous.getHeure()));
            pstmt.setString(3, rendezVous.getObjet());
            pstmt.setInt(4, rendezVous.getId());

            int rowsUpdated = pstmt.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
