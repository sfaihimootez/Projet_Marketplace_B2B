package com.example.gestion_commande.Entities;

import java.time.LocalDate;

public class Commande {
    private int idCommande;
    private String numeroCommande;
    private String client;
    private LocalDate dateCommande;
    private double montantTotal;
    private String adresseLivraison;
    private String statut; // En attente, Confirmée, Annulée

    // Constructeurs
    public Commande() {
    }

    public Commande(String numeroCommande, String client, LocalDate dateCommande,
                    double montantTotal, String adresseLivraison, String statut) {
        this.numeroCommande = numeroCommande;
        this.client = client;
        this.dateCommande = dateCommande;
        this.montantTotal = montantTotal;
        this.adresseLivraison = adresseLivraison;
        this.statut = statut;
    }

    public Commande(int idCommande, String numeroCommande, String client, LocalDate dateCommande,
                    double montantTotal, String adresseLivraison, String statut) {
        this.idCommande = idCommande;
        this.numeroCommande = numeroCommande;
        this.client = client;
        this.dateCommande = dateCommande;
        this.montantTotal = montantTotal;
        this.adresseLivraison = adresseLivraison;
        this.statut = statut;
    }

    // Getters et Setters
    public int getIdCommande() {
        return idCommande;
    }

    public void setIdCommande(int idCommande) {
        this.idCommande = idCommande;
    }

    public String getNumeroCommande() {
        return numeroCommande;
    }

    public void setNumeroCommande(String numeroCommande) {
        this.numeroCommande = numeroCommande;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public LocalDate getDateCommande() {
        return dateCommande;
    }

    public void setDateCommande(LocalDate dateCommande) {
        this.dateCommande = dateCommande;
    }

    public double getMontantTotal() {
        return montantTotal;
    }

    public void setMontantTotal(double montantTotal) {
        this.montantTotal = montantTotal;
    }

    public String getAdresseLivraison() {
        return adresseLivraison;
    }

    public void setAdresseLivraison(String adresseLivraison) {
        this.adresseLivraison = adresseLivraison;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    @Override
    public String toString() {
        return "Commande{" +
                "idCommande=" + idCommande +
                ", numeroCommande='" + numeroCommande + '\'' +
                ", client='" + client + '\'' +
                ", dateCommande=" + dateCommande +
                ", montantTotal=" + montantTotal +
                ", adresseLivraison='" + adresseLivraison + '\'' +
                ", statut='" + statut + '\'' +
                '}';
    }
}