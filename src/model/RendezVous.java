package model;

import java.time.LocalDate;
import java.time.LocalTime;

public class RendezVous {

    private int id; 
    private LocalDate date;
    private LocalTime heure;
    private String objet;


    public RendezVous(LocalDate date, LocalTime heure, String objet) {
        this.date = date;
        this.heure = heure;
        this.objet = objet;
    }

    public RendezVous(int id, LocalDate date, LocalTime heure, String objet) {
        this.id = id;
        this.date = date;
        this.heure = heure;
        this.objet = objet;
    }

    // Getters et Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getHeure() {
        return heure;
    }

    public void setHeure(LocalTime heure) {
        this.heure = heure;
    }

    public String getObjet() {
        return objet;
    }

    public void setObjet(String objet) {
        this.objet = objet;
    }

    @Override
    public String toString() {
        return "RendezVous{" +
                "id=" + id +
                ", date=" + date +
                ", heure=" + heure +
                ", objet='" + objet + '\'' +
                '}';
    }
}
