package com.example.gestion_commande.Services;


import com.example.gestion_commande.Entities.Commande;

import java.util.List;

public interface ICommandeService {

    /**
     * Ajouter une nouvelle commande
     * @param commande l'objet Commande à ajouter
     * @return true si l'ajout est réussi, false sinon
     */
    boolean ajouter(Commande commande);

    /**
     * Modifier une commande existante
     * @param commande l'objet Commande avec les modifications
     * @return true si la modification est réussie, false sinon
     */
    boolean modifier(Commande commande);

    /**
     * Supprimer une commande
     * @param idCommande l'ID de la commande à supprimer
     * @return true si la suppression est réussie, false sinon
     */
    boolean supprimer(int idCommande);

    /**
     * Afficher toutes les commandes
     * @return une liste contenant toutes les commandes
     */
    List<Commande> afficher();

    /**
     * Rechercher une commande par son ID
     * @param idCommande l'ID de la commande
     * @return l'objet Commande trouvé, null sinon
     */
    Commande rechercherParId(int idCommande);

    /**
     * Rechercher une commande par son numéro
     * @param numeroCommande le numéro de la commande
     * @return l'objet Commande trouvé, null sinon
     */
    Commande rechercherParNumero(String numeroCommande);

    /**
     * Rechercher les commandes par client
     * @param client le nom du client
     * @return une liste des commandes du client
     */
    List<Commande> rechercherParClient(String client);
}