package com.example.gestion_commande.Services;

import com.example.gestion_commande.Entities.Livraison;

import java.util.List;

public interface ILivraisonService {

    /**
     * Ajouter une nouvelle livraison
     * @param livraison l'objet Livraison à ajouter
     * @return true si l'ajout est réussi, false sinon
     */
    boolean ajouter(Livraison livraison);

    /**
     * Modifier une livraison existante
     * @param livraison l'objet Livraison avec les modifications
     * @return true si la modification est réussie, false sinon
     */
    boolean modifier(Livraison livraison);

    /**
     * Supprimer une livraison
     * @param idLivraison l'ID de la livraison à supprimer
     * @return true si la suppression est réussie, false sinon
     */
    boolean supprimer(int idLivraison);

    /**
     * Afficher toutes les livraisons
     * @return une liste contenant toutes les livraisons
     */
    List<Livraison> afficher();

    /**
     * Rechercher une livraison par son ID
     * @param idLivraison l'ID de la livraison
     * @return l'objet Livraison trouvé, null sinon
     */
    Livraison rechercherParId(int idLivraison);

    /**
     * Rechercher une livraison par l'ID de la commande (jointure 1-to-1)
     * @param idCommande l'ID de la commande
     * @return l'objet Livraison trouvé, null sinon
     */
    Livraison rechercherParIdCommande(int idCommande);

    /**
     * Rechercher une livraison par numéro BL
     * @param numeroBL le numéro du bon de livraison
     * @return l'objet Livraison trouvé, null sinon
     */
    Livraison rechercherParNumeroBL(String numeroBL);

    /**
     * Rechercher les livraisons par livreur
     * @param livreur le nom du livreur
     * @return une liste des livraisons du livreur
     */
    List<Livraison> rechercherParLivreur(String livreur);

    /**
     * Rechercher les livraisons par statut
     * @param statut le statut de livraison
     * @return une liste des livraisons avec ce statut
     */
    List<Livraison> rechercherParStatut(String statut);
}