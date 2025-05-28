package model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DAOtraitement {

    private static final String URL = "jdbc:mysql://localhost:3306/treatmentmedical_db";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    private Connection connection;

    public DAOtraitement() {
        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            System.err.println("Erreur de connexion à la base de données: " + e.getMessage());
        }
    }

    public List<traitement> getAllTraitements() {
        List<traitement> traitements = new ArrayList<>();
        String query = "SELECT * FROM traitement";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                traitements.add(new traitement(
                    rs.getInt("id"),
                    rs.getString("nom"),
                    rs.getDouble("prix"),
                    rs.getInt("foisParJour"),
                    rs.getString("type"),
                    rs.getString("duree")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des traitements: " + e.getMessage());
        }
        return traitements;
    }

    public boolean addTraitement(traitement traitement) {
        String query = "INSERT INTO traitement (nom, prix, foisParJour, type, duree) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement pstmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, traitement.getNom());
            pstmt.setDouble(2, traitement.getPrix());
            pstmt.setInt(3, traitement.getFoisParJour());
            pstmt.setString(4, traitement.getType());
            pstmt.setString(5, traitement.getDuree());

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        traitement.setId(generatedKeys.getInt(1));
                        return true;
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'ajout du traitement: " + e.getMessage());
        }
        return false;
    }

    public boolean updateTraitement(traitement traitement) {
        String query = "UPDATE traitement SET nom = ?, prix = ?, foisParJour = ?, type = ?, duree = ? WHERE id = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, traitement.getNom());
            pstmt.setDouble(2, traitement.getPrix());
            pstmt.setInt(3, traitement.getFoisParJour());
            pstmt.setString(4, traitement.getType());
            pstmt.setString(5, traitement.getDuree());
            pstmt.setInt(6, traitement.getId());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erreur lors de la mise à jour du traitement: " + e.getMessage());
        }
        return false;
    }

    public boolean deleteTraitement(int id) {
        String query = "DELETE FROM traitement WHERE id = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erreur lors de la suppression du traitement: " + e.getMessage());
        }
        return false;
    }

    public void close() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la fermeture de la connexion: " + e.getMessage());
        }
    }
  public List<traitement> getAllTraitementsWithType() {
    List<traitement> traitements = new ArrayList<>();
    String query = "SELECT id, nom, prix, foisParJour, type, duree FROM traitement";

    try (Statement stmt = connection.createStatement();
         ResultSet rs = stmt.executeQuery(query)) {

        while (rs.next()) {
            traitements.add(new traitement(
                rs.getInt("id"),
                rs.getString("nom"),
                rs.getDouble("prix"),
                rs.getInt("foisParJour"),
                rs.getString("type"),
                rs.getString("duree")
            ));
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
        return traitements;
  }

}