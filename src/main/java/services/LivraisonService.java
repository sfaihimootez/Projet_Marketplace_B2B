package com.example.gestion_commande.Services;


import com.example.gestion_commande.Entities.Livraison;
import com.example.gestion_commande.Utils.MyDB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LivraisonService implements ILivraisonService {

    private Connection connection;

    public LivraisonService() {
        this.connection = MyDB.getConnection();
    }

    @Override
    public boolean ajouter(Livraison livraison) {
        String sql = "INSERT INTO livraison (idCommande, numeroBL, dateLivraison, livreur, statutLivraison, noteDelivery) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pst = connection.prepareStatement(sql)) {
            pst.setInt(1, livraison.getIdCommande());
            pst.setString(2, livraison.getNumeroBL());
            pst.setDate(3, Date.valueOf(livraison.getDateLivraison()));
            pst.setString(4, livraison.getLivreur());
            pst.setString(5, livraison.getStatutLivraison());
            pst.setString(6, livraison.getNoteDelivery());

            int rowsInserted = pst.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("✓ Livraison ajoutée avec succès!");
                return true;
            }
        } catch (SQLException e) {
            System.err.println("✗ Erreur lors de l'ajout de la livraison!");
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean modifier(Livraison livraison) {
        String sql = "UPDATE livraison SET idCommande = ?, numeroBL = ?, dateLivraison = ?, " +
                "livreur = ?, statutLivraison = ?, noteDelivery = ? WHERE idLivraison = ?";
        try (PreparedStatement pst = connection.prepareStatement(sql)) {
            pst.setInt(1, livraison.getIdCommande());
            pst.setString(2, livraison.getNumeroBL());
            pst.setDate(3, Date.valueOf(livraison.getDateLivraison()));
            pst.setString(4, livraison.getLivreur());
            pst.setString(5, livraison.getStatutLivraison());
            pst.setString(6, livraison.getNoteDelivery());
            pst.setInt(7, livraison.getIdLivraison());

            int rowsUpdated = pst.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("✓ Livraison modifiée avec succès!");
                return true;
            }
        } catch (SQLException e) {
            System.err.println("✗ Erreur lors de la modification de la livraison!");
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean supprimer(int idLivraison) {
        String sql = "DELETE FROM livraison WHERE idLivraison = ?";
        try (PreparedStatement pst = connection.prepareStatement(sql)) {
            pst.setInt(1, idLivraison);

            int rowsDeleted = pst.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("✓ Livraison supprimée avec succès!");
                return true;
            }
        } catch (SQLException e) {
            System.err.println("✗ Erreur lors de la suppression de la livraison!");
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public List<Livraison> afficher() {
        List<Livraison> livraisons = new ArrayList<>();
        String sql = "SELECT * FROM livraison";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Livraison livraison = new Livraison(
                        rs.getInt("idLivraison"),
                        rs.getInt("idCommande"),
                        rs.getString("numeroBL"),
                        rs.getDate("dateLivraison").toLocalDate(),
                        rs.getString("livreur"),
                        rs.getString("statutLivraison"),
                        rs.getString("noteDelivery")
                );
                livraisons.add(livraison);
            }
        } catch (SQLException e) {
            System.err.println("✗ Erreur lors de l'affichage des livraisons!");
            e.printStackTrace();
        }
        return livraisons;
    }

    @Override
    public Livraison rechercherParId(int idLivraison) {
        String sql = "SELECT * FROM livraison WHERE idLivraison = ?";
        try (PreparedStatement pst = connection.prepareStatement(sql)) {
            pst.setInt(1, idLivraison);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    return new Livraison(
                            rs.getInt("idLivraison"),
                            rs.getInt("idCommande"),
                            rs.getString("numeroBL"),
                            rs.getDate("dateLivraison").toLocalDate(),
                            rs.getString("livreur"),
                            rs.getString("statutLivraison"),
                            rs.getString("noteDelivery")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("✗ Erreur lors de la recherche de la livraison!");
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Livraison rechercherParIdCommande(int idCommande) {
        String sql = "SELECT * FROM livraison WHERE idCommande = ?";
        try (PreparedStatement pst = connection.prepareStatement(sql)) {
            pst.setInt(1, idCommande);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    return new Livraison(
                            rs.getInt("idLivraison"),
                            rs.getInt("idCommande"),
                            rs.getString("numeroBL"),
                            rs.getDate("dateLivraison").toLocalDate(),
                            rs.getString("livreur"),
                            rs.getString("statutLivraison"),
                            rs.getString("noteDelivery")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("✗ Erreur lors de la recherche de la livraison!");
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Livraison rechercherParNumeroBL(String numeroBL) {
        String sql = "SELECT * FROM livraison WHERE numeroBL = ?";
        try (PreparedStatement pst = connection.prepareStatement(sql)) {
            pst.setString(1, numeroBL);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    return new Livraison(
                            rs.getInt("idLivraison"),
                            rs.getInt("idCommande"),
                            rs.getString("numeroBL"),
                            rs.getDate("dateLivraison").toLocalDate(),
                            rs.getString("livreur"),
                            rs.getString("statutLivraison"),
                            rs.getString("noteDelivery")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("✗ Erreur lors de la recherche de la livraison!");
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Livraison> rechercherParLivreur(String livreur) {
        List<Livraison> livraisons = new ArrayList<>();
        String sql = "SELECT * FROM livraison WHERE livreur LIKE ?";
        try (PreparedStatement pst = connection.prepareStatement(sql)) {
            pst.setString(1, "%" + livreur + "%");
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    Livraison livraison = new Livraison(
                            rs.getInt("idLivraison"),
                            rs.getInt("idCommande"),
                            rs.getString("numeroBL"),
                            rs.getDate("dateLivraison").toLocalDate(),
                            rs.getString("livreur"),
                            rs.getString("statutLivraison"),
                            rs.getString("noteDelivery")
                    );
                    livraisons.add(livraison);
                }
            }
        } catch (SQLException e) {
            System.err.println("✗ Erreur lors de la recherche par livreur!");
            e.printStackTrace();
        }
        return livraisons;
    }

    @Override
    public List<Livraison> rechercherParStatut(String statut) {
        List<Livraison> livraisons = new ArrayList<>();
        String sql = "SELECT * FROM livraison WHERE statutLivraison = ?";
        try (PreparedStatement pst = connection.prepareStatement(sql)) {
            pst.setString(1, statut);
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    Livraison livraison = new Livraison(
                            rs.getInt("idLivraison"),
                            rs.getInt("idCommande"),
                            rs.getString("numeroBL"),
                            rs.getDate("dateLivraison").toLocalDate(),
                            rs.getString("livreur"),
                            rs.getString("statutLivraison"),
                            rs.getString("noteDelivery")
                    );
                    livraisons.add(livraison);
                }
            }
        } catch (SQLException e) {
            System.err.println("✗ Erreur lors de la recherche par statut!");
            e.printStackTrace();
        }
        return livraisons;
    }
}