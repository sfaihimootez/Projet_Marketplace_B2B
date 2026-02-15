package gestion_feedback.model;

import java.sql.Date;

public class Feedback {

    private int id;
    private String commentaire;
    private String note;
    private Date dateStatut;

    public Feedback() {
    }

    public Feedback(String commentaire, String note, Date dateStatut) {
        this.commentaire = commentaire;
        this.note = note;
        this.dateStatut = dateStatut;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCommentaire() {
        return commentaire;
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Date getDateStatut() {
        return dateStatut;
    }

    public void setDateStatut(Date dateStatut) {
        this.dateStatut = dateStatut;
    }

    @Override
    public String toString() {
        return "Feedback{" +
                "id=" + id +
                ", commentaire='" + commentaire + '\'' +
                ", note='" + note + '\'' +
                ", dateStatut=" + dateStatut +
                '}';
    }
}