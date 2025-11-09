package model;

public class Session {
    private static Utilisateur utilisateur;

    public static void setUtilisateur(Utilisateur user) {
        utilisateur = user;
    }

    public static Utilisateur getUtilisateur() {
        return utilisateur;
    }

    public static void clear() {
        utilisateur = null;
    }
}