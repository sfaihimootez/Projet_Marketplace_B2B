package com.example.gestion_commande.Entities;

import java.time.LocalDate;

public class Livraison {
    private int idLivraison;
    private int idCommande; // Clé étrangère pour la jointure 1-to-1
    private String numeroBL; // Bon de livraison
    private LocalDate dateLivraison;
    private String livreur;
    private String statutLivraison; // En cours, Livré, Retardé
    private String noteDelivery;

    // Constructeurs
    public Livraison() {
    }

    public Livraison(int idCommande, String numeroBL, LocalDate dateLivraison,
                     String livreur, String statutLivraison, String noteDelivery) {
        this.idCommande = idCommande;
        this.numeroBL = numeroBL;
        this.dateLivraison = dateLivraison;
        this.livreur = livreur;
        this.statutLivraison = statutLivraison;
        this.noteDelivery = noteDelivery;
    }

    public Livraison(int idLivraison, int idCommande, String numeroBL, LocalDate dateLivraison,
                     String livreur, String statutLivraison, String noteDelivery) {
        this.idLivraison = idLivraison;
        this.idCommande = idCommande;
        this.numeroBL = numeroBL;
        this.dateLivraison = dateLivraison;
        this.livreur = livreur;
        this.statutLivraison = statutLivraison;
        this.noteDelivery = noteDelivery;
    }

    // Getters et Setters
    public int getIdLivraison() {
        return idLivraison;
    }

    public void setIdLivraison(int idLivraison) {
        this.idLivraison = idLivraison;
    }

    public int getIdCommande() {
        return idCommande;
    }

    public void setIdCommande(int idCommande) {
        this.idCommande = idCommande;
    }

    public String getNumeroBL() {
        return numeroBL;
    }

    public void setNumeroBL(String numeroBL) {
        this.numeroBL = numeroBL;
    }

    public LocalDate getDateLivraison() {
        return dateLivraison;
    }

    public void setDateLivraison(LocalDate dateLivraison) {
        this.dateLivraison = dateLivraison;
    }

    public String getLivreur() {
        return livreur;
    }

    public void setLivreur(String livreur) {
        this.livreur = livreur;
    }

    public String getStatutLivraison() {
        return statutLivraison;
    }

    public void setStatutLivraison(String statutLivraison) {
        this.statutLivraison = statutLivraison;
    }

    public String getNoteDelivery() {
        return noteDelivery;
    }

    public void setNoteDelivery(String noteDelivery) {
        this.noteDelivery = noteDelivery;
    }

    @Override
    public String toString() {
        return "Livraison{" +
                "idLivraison=" + idLivraison +
                ", idCommande=" + idCommande +
                ", numeroBL='" + numeroBL + '\'' +
                ", dateLivraison=" + dateLivraison +
                ", livreur='" + livreur + '\'' +
                ", statutLivraison='" + statutLivraison + '\'' +
                ", noteDelivery='" + noteDelivery + '\'' +
                '}';
    }
}