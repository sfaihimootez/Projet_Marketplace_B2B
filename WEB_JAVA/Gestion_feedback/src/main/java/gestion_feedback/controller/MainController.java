package gestion_feedback.controller;

import gestion_feedback.model.Feedback;
import gestion_feedback.model.Reponse;
import gestion_feedback.service.FeedbackService;
import gestion_feedback.service.ReponseService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.Date;
import java.time.LocalDate;

public class MainController {

    // Services
    private final FeedbackService feedbackService = new FeedbackService();
    private final ReponseService reponseService = new ReponseService();

    // TableViews
    @FXML private TableView<Feedback> tableFeedback;
    @FXML private TableView<Reponse> tableReponse;

    // Champs Feedback
    @FXML private TextArea txtCommentaire;
    @FXML private TextField txtNote;
    @FXML private DatePicker dpDateStatut;

    // Champs Réponse
    @FXML private TextArea txtContenuReponse;
    @FXML private DatePicker dpDateReponse;

    // Variable pour garder le feedback sélectionné
    private Feedback feedbackSelectionne = null;

    @FXML
    public void initialize() {
        // Configuration des colonnes de la table Feedback
        tableFeedback.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("id"));
        tableFeedback.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("commentaire"));
        tableFeedback.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("note"));
        tableFeedback.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("dateStatut"));

        // Configuration des colonnes de la table Réponse (adapte selon tes colonnes exactes)
        tableReponse.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("id"));
        tableReponse.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("contenu"));
        tableReponse.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("dateReponse"));

        // Charger les feedbacks au démarrage
        chargerFeedbacks();

        // Quand on sélectionne un feedback → charger ses réponses
        tableFeedback.getSelectionModel().selectedItemProperty().addListener((obs, old, nouveau) -> {
            feedbackSelectionne = nouveau;
            if (nouveau != null) {
                chargerReponses(nouveau.getId());
            } else {
                tableReponse.setItems(FXCollections.observableArrayList());
            }
        });
    }

    private void chargerFeedbacks() {
        ObservableList<Feedback> liste = FXCollections.observableArrayList(feedbackService.getAll());
        tableFeedback.setItems(liste);
    }

    private void chargerReponses(int feedbackId) {
        ObservableList<Reponse> liste = FXCollections.observableArrayList(reponseService.getByFeedbackId(feedbackId));
        tableReponse.setItems(liste);
    }

    @FXML
    private void ajouterFeedback() {
        if (!controleSaisieFeedback()) return;

        Feedback f = new Feedback();
        f.setCommentaire(txtCommentaire.getText().trim());
        f.setNote(txtNote.getText().trim());
        f.setDateStatut(Date.valueOf(dpDateStatut.getValue()));

        feedbackService.ajouter(f);
        chargerFeedbacks();
        viderFormFeedback();
    }

    @FXML
    private void ajouterReponse() {
        if (feedbackSelectionne == null) {
            showAlert("Erreur", "Sélectionnez d'abord un feedback dans la table.");
            return;
        }

        if (!controleSaisieReponse()) return;

        Reponse r = new Reponse();
        r.setContenu(txtContenuReponse.getText().trim());
        r.setDateReponse(Date.valueOf(dpDateReponse.getValue()));
        r.setFeedbackId(feedbackSelectionne.getId());

        reponseService.ajouter(r);
        chargerReponses(feedbackSelectionne.getId());
        viderFormReponse();
    }

    private boolean controleSaisieFeedback() {
        if (txtCommentaire.getText().trim().isEmpty()) {
            showAlert("Erreur", "Le commentaire est obligatoire.");
            return false;
        }
        if (txtNote.getText().trim().isEmpty()) {
            showAlert("Erreur", "La note est obligatoire.");
            return false;
        }
        if (dpDateStatut.getValue() == null) {
            showAlert("Erreur", "La date est obligatoire.");
            return false;
        }
        return true;
    }

    private boolean controleSaisieReponse() {
        if (txtContenuReponse.getText().trim().isEmpty()) {
            showAlert("Erreur", "Le contenu de la réponse est obligatoire.");
            return false;
        }
        if (dpDateReponse.getValue() == null) {
            showAlert("Erreur", "La date de réponse est obligatoire.");
            return false;
        }
        return true;
    }

    private void viderFormFeedback() {
        txtCommentaire.clear();
        txtNote.clear();
        dpDateStatut.setValue(null);
    }

    private void viderFormReponse() {
        txtContenuReponse.clear();
        dpDateReponse.setValue(null);
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}