package model;

import java.security.MessageDigest;
import java.sql.*;

public class UtilisateurDAO {
    private static final String URL = "jdbc:mysql://localhost:3306/treatmentmedical_db";
    private static final String USER = "root";
    private static final String PASS = "";

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }

    public boolean ajouterUtilisateur(String username, String email, String password) {
        String hashed = hashPassword(password);
        String sql = "INSERT INTO utilisateurs (username, email, password) VALUES (?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, email);
            stmt.setString(3, hashed);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Erreur : " + e.getMessage());
            return false;
        }
    }

    public boolean verifierUtilisateurParEmail(String email, String motDePasse) {
        String hashed = hashPassword(motDePasse); 
        String sql = "SELECT * FROM utilisateurs WHERE email = ? AND password = ?";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            stmt.setString(2, hashed);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashed = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : hashed) sb.append(String.format("%02x", b));
            return sb.toString();
        } catch (Exception e) {
            throw new RuntimeException("Erreur de hashage", e);
        }
    }
    public Utilisateur getUtilisateurParEmail(String email) {
    String sql = "SELECT * FROM utilisateurs WHERE email = ?";
    try (Connection conn = getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {

        stmt.setString(1, email);
        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            Utilisateur utilisateur = new Utilisateur();
            utilisateur.setId(rs.getInt("id")); // Assure-toi que ta classe Utilisateur a ces setters
            utilisateur.setUsername(rs.getString("username"));
            utilisateur.setEmail(rs.getString("email"));
            utilisateur.setPassword(rs.getString("password")); // ou motDePasse selon ta classe
            return utilisateur;
        }

    } catch (SQLException e) {
        e.printStackTrace();
    }

    return null;
}

}
