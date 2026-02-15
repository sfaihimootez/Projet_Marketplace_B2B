package gestion_feedback.dao;

import gestion_feedback.model.Reponse;
import gestion_feedback.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReponseDAO {

    private Connection conn;

    public ReponseDAO() {
        try {
            conn = DBConnection.getConnection();
        } catch (SQLException e) {
            System.err.println("Erreur connexion DAO Reponse : " + e.getMessage());
        }
    }

    public boolean ajouter(Reponse reponse) {
        String sql = "INSERT INTO reponse (contenu, date_reponse, feedback_id) VALUES (?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, reponse.getContenu());
            ps.setDate(2, reponse.getDateReponse());
            ps.setInt(3, reponse.getFeedbackId());
            int rows = ps.executeUpdate();
            if (rows > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        reponse.setId(rs.getInt(1));
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Erreur ajout réponse : " + e.getMessage());
        }
        return false;
    }

    public List<Reponse> getByFeedbackId(int feedbackId) {
        List<Reponse> list = new ArrayList<>();
        String sql = "SELECT * FROM reponse WHERE feedback_id = ? ORDER BY date_reponse ASC";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, feedbackId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Reponse r = new Reponse();
                    r.setId(rs.getInt("id"));
                    r.setContenu(rs.getString("contenu"));
                    r.setDateReponse(rs.getDate("date_reponse"));
                    r.setFeedbackId(rs.getInt("feedback_id"));
                    list.add(r);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur liste réponses : " + e.getMessage());
        }
        return list;
    }
    // Mise à jour d'une réponse
    public boolean update(Reponse reponse) {
        String sql = "UPDATE reponse SET contenu = ?, date_reponse = ? WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, reponse.getContenu());
            ps.setDate(2, reponse.getDateReponse());
            ps.setInt(3, reponse.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erreur update réponse : " + e.getMessage());
            return false;
        }
    }

    // Suppression d'une réponse
    public boolean supprimer(int id) {
        String sql = "DELETE FROM reponse WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erreur suppression réponse : " + e.getMessage());
            return false;
        }
    }
}