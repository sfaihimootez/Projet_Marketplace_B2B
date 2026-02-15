package gestion_feedback.service;

import gestion_feedback.dao.ReponseDAO;
import gestion_feedback.model.Reponse;

import java.util.List;

public class ReponseService {

    private final ReponseDAO dao = new ReponseDAO();

    public boolean ajouter(Reponse reponse) {
        if (reponse.getContenu() == null || reponse.getContenu().trim().isEmpty()) {
            System.err.println("Contenu obligatoire");
            return false;
        }
        if (reponse.getFeedbackId() <= 0) {
            System.err.println("Feedback ID invalide");
            return false;
        }
        return dao.ajouter(reponse);
    }

    public List<Reponse> getByFeedbackId(int feedbackId) {
        return dao.getByFeedbackId(feedbackId);
    }
    // Mise à jour
    public boolean update(Reponse reponse) {
        if (reponse.getContenu() == null || reponse.getContenu().trim().isEmpty()) {
            System.err.println("Contenu obligatoire pour modification");
            return false;
        }
        return dao.update(reponse);
    }

    // Suppression
    public boolean supprimer(int id) {
        return dao.supprimer(id);
    }
}