package com.example.connexion_gsb;

public class FicheFrais {
    public int id;
    public String date;
    public float montant;
    public String statut;
    public String email;

    // Visiteur
    public FicheFrais(int id, String date, float montant, String statut) {
        this.id = id;
        this.date = date;
        this.montant = montant;
        this.statut = statut;
    }

    // Comptable
    public FicheFrais(int id, String date, float montant, String statut, String email) {
        this.id = id;
        this.date = date;
        this.montant = montant;
        this.statut = statut;
        this.email = email;
    }
}
