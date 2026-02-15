package gestion_feedback.service;

import gestion_feedback.dao.FeedbackDAO;
import gestion_feedback.model.Feedback;

import java.util.List;

public class FeedbackService {

    private final FeedbackDAO dao = new FeedbackDAO();

    public boolean ajouter(Feedback feedback) {
        if (feedback.getCommentaire() == null || feedback.getCommentaire().trim().isEmpty()) {
            System.err.println("Commentaire obligatoire");
            return false;
        }
        if (feedback.getNote() == null || feedback.getNote().trim().isEmpty()) {
            System.err.println("Note obligatoire");
            return false;
        }
        return dao.ajouter(feedback);
    }

    public List<Feedback> getAll() {
        return dao.getAll();
    }

    public boolean supprimer(int id) {
        return dao.supprimer(id);
    }

    public boolean update(Feedback feedback) {
        if (feedback.getId() <= 0) {
            System.err.println("ID invalide pour modification");
            return false;
        }
        return dao.update(feedback);
    }

    public Feedback getById(int id) {
        return dao.getById(id);
    }
}