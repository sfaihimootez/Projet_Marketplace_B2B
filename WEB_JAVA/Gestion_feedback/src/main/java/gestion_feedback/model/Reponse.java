package gestion_feedback.model;

import java.sql.Date;

public class Reponse {

    private int id;
    private String contenu;
    private Date dateReponse;
    private int feedbackId;   // clé étrangère vers Feedback

    public Reponse() {
    }

    public Reponse(String contenu, Date dateReponse, int feedbackId) {
        this.contenu = contenu;
        this.dateReponse = dateReponse;
        this.feedbackId = feedbackId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContenu() {
        return contenu;
    }

    public void setContenu(String contenu) {
        this.contenu = contenu;
    }

    public Date getDateReponse() {
        return dateReponse;
    }

    public void setDateReponse(Date dateReponse) {
        this.dateReponse = dateReponse;
    }

    public int getFeedbackId() {
        return feedbackId;
    }

    public void setFeedbackId(int feedbackId) {
        this.feedbackId = feedbackId;
    }

    @Override
    public String toString() {
        return "Reponse{" +
                "id=" + id +
                ", contenu='" + contenu + '\'' +
                ", dateReponse=" + dateReponse +
                ", feedbackId=" + feedbackId +
                '}';
    }
}