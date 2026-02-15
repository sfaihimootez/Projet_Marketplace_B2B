package com.example.gestion_commande.Services;


import com.example.gestion_commande.Entities.Commande;
import com.example.gestion_commande.Utils.MyDB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CommandeService implements ICommandeService {

    private Connection connection;

    public CommandeService() {
        this.connection = MyDB.getConnection();
    }

    @Override
    public boolean ajouter(Commande commande) {
        String sql = "INSERT INTO commande (numeroCommande, client, dateCommande, montantTotal, adresseLivraison, statut) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pst = connection.prepareStatement(sql)) {
            pst.setString(1, commande.getNumeroCommande());
            pst.setString(2, commande.getClient());
            pst.setDate(3, Date.valueOf(commande.getDateCommande()));
            pst.setDouble(4, commande.getMontantTotal());
            pst.setString(5, commande.getAdresseLivraison());
            pst.setString(6, commande.getStatut());

            int rowsInserted = pst.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("✓ Commande ajoutée avec succès!");
                return true;
            }
        } catch (SQLException e) {
            System.err.println("✗ Erreur lors de l'ajout de la commande!");
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean modifier(Commande commande) {
        String sql = "UPDATE commande SET numeroCommande = ?, client = ?, dateCommande = ?, " +
                "montantTotal = ?, adresseLivraison = ?, statut = ? WHERE idCommande = ?";
        try (PreparedStatement pst = connection.prepareStatement(sql)) {
            pst.setString(1, commande.getNumeroCommande());
            pst.setString(2, commande.getClient());
            pst.setDate(3, Date.valueOf(commande.getDateCommande()));
            pst.setDouble(4, commande.getMontantTotal());
            pst.setString(5, commande.getAdresseLivraison());
            pst.setString(6, commande.getStatut());
            pst.setInt(7, commande.getIdCommande());

            int rowsUpdated = pst.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("✓ Commande modifiée avec succès!");
                return true;
            }
        } catch (SQLException e) {
            System.err.println("✗ Erreur lors de la modification de la commande!");
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean supprimer(int idCommande) {
        String sql = "DELETE FROM commande WHERE idCommande = ?";
        try (PreparedStatement pst = connection.prepareStatement(sql)) {
            pst.setInt(1, idCommande);

            int rowsDeleted = pst.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("✓ Commande supprimée avec succès!");
                return true;
            }
        } catch (SQLException e) {
            System.err.println("✗ Erreur lors de la suppression de la commande!");
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public List<Commande> afficher() {
        List<Commande> commandes = new ArrayList<>();
        String sql = "SELECT * FROM commande";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Commande commande = new Commande(
                        rs.getInt("idCommande"),
                        rs.getString("numeroCommande"),
                        rs.getString("client"),
                        rs.getDate("dateCommande").toLocalDate(),
                        rs.getDouble("montantTotal"),
                        rs.getString("adresseLivraison"),
                        rs.getString("statut")
                );
                commandes.add(commande);
            }
        } catch (SQLException e) {
            System.err.println("✗ Erreur lors de l'affichage des commandes!");
            e.printStackTrace();
        }
        return commandes;
    }

    @Override
    public Commande rechercherParId(int idCommande) {
        String sql = "SELECT * FROM commande WHERE idCommande = ?";
        try (PreparedStatement pst = connection.prepareStatement(sql)) {
            pst.setInt(1, idCommande);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    return new Commande(
                            rs.getInt("idCommande"),
                            rs.getString("numeroCommande"),
                            rs.getString("client"),
                            rs.getDate("dateCommande").toLocalDate(),
                            rs.getDouble("montantTotal"),
                            rs.getString("adresseLivraison"),
                            rs.getString("statut")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("✗ Erreur lors de la recherche de la commande!");
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Commande rechercherParNumero(String numeroCommande) {
        String sql = "SELECT * FROM commande WHERE numeroCommande = ?";
        try (PreparedStatement pst = connection.prepareStatement(sql)) {
            pst.setString(1, numeroCommande);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    return new Commande(
                            rs.getInt("idCommande"),
                            rs.getString("numeroCommande"),
                            rs.getString("client"),
                            rs.getDate("dateCommande").toLocalDate(),
                            rs.getDouble("montantTotal"),
                            rs.getString("adresseLivraison"),
                            rs.getString("statut")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("✗ Erreur lors de la recherche de la commande!");
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Commande> rechercherParClient(String client) {
        List<Commande> commandes = new ArrayList<>();
        String sql = "SELECT * FROM commande WHERE client LIKE ?";
        try (PreparedStatement pst = connection.prepareStatement(sql)) {
            pst.setString(1, "%" + client + "%");
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    Commande commande = new Commande(
                            rs.getInt("idCommande"),
                            rs.getString("numeroCommande"),
                            rs.getString("client"),
                            rs.getDate("dateCommande").toLocalDate(),
                            rs.getDouble("montantTotal"),
                            rs.getString("adresseLivraison"),
                            rs.getString("statut")
                    );
                    commandes.add(commande);
                }
            }
        } catch (SQLException e) {
            System.err.println("✗ Erreur lors de la recherche par client!");
            e.printStackTrace();
        }
        return commandes;
    }
}