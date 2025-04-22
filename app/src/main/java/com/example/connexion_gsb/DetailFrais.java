package com.example.connexion_gsb;

public class DetailFrais {
    public int id;
    public String categorie;
    public double montant;
    public String date; // null si frais forfaitaire

    public DetailFrais(int id, String categorie, double montant) {
        this.id = id;
        this.categorie = categorie;
        this.montant = montant;
        this.date = null;
    }

    public DetailFrais(int id, String categorie, double montant, String date) {
        this.id = id;
        this.categorie = categorie;
        this.montant = montant;
        this.date = date;
    }
}
