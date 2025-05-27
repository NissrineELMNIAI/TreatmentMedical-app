
package model;

public class traitement {
    private int id;
    private String nom;
    private double prix;
    private int foisParJour;
    private String type;
    private String duree;

    public traitement(int id, String nom, double prix, int foisParJour, String type, String duree) {
        this.id = id;
        this.nom = nom;
        this.prix = prix;
        this.foisParJour = foisParJour;
        this.type = type;
        this.duree = duree;
    }

    // Getters et setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public double getPrix() { return prix; }
    public void setPrix(double prix) { this.prix = prix; }

    public int getFoisParJour() { return foisParJour; }
    public void setFoisParJour(int foisParJour) { this.foisParJour = foisParJour; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getDuree() { return duree; }
    public void setDuree(String duree) { this.duree = duree; }
}
