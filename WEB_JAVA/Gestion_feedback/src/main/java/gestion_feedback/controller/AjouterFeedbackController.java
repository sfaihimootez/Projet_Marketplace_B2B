package gestion_feedback.controller;

import gestion_feedback.model.Feedback;
import gestion_feedback.service.FeedbackService;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import java.sql.Date;
import java.time.LocalDate;

public class AjouterFeedbackController {

    @FXML private TextArea txtCommentaire;
    @FXML private HBox hboxEtoiles;
    @FXML private DatePicker dpDate;
    @FXML private Label lblNoteSelectionnee;

    private final FeedbackService service = new FeedbackService();
    private Feedback feedbackAModifier = null;
    private int noteSelectionnee = 0;
    private final Text[] etoiles = new Text[5];

    @FXML
    public void initialize() {
        configurerEtoiles();
        // Définir la date actuelle par défaut
        dpDate.setValue(LocalDate.now());
    }

    private void configurerEtoiles() {
        hboxEtoiles.setAlignment(Pos.CENTER);
        hboxEtoiles.setSpacing(10);

        for (int i = 0; i < 5; i++) {
            final int index = i;
            Text etoile = new Text("★");
            etoile.setFont(Font.font("Arial", 45));
            etoile.setFill(Color.web("#E0E0E0"));
            etoile.setStyle("-fx-cursor: hand;");

            etoiles[i] = etoile;

            // Effet hover
            etoile.setOnMouseEntered(e -> {
                for (int j = 0; j <= index; j++) {
                    etoiles[j].setFill(Color.web("#FFD700"));
                }
            });

            etoile.setOnMouseExited(e -> {
                afficherEtoiles(noteSelectionnee);
            });

            // Clic pour sélectionner
            etoile.setOnMouseClicked(e -> {
                noteSelectionnee = index + 1;
                afficherEtoiles(noteSelectionnee);
                updateLabelNote();
            });

            hboxEtoiles.getChildren().add(etoile);
        }
    }

    private void afficherEtoiles(int note) {
        for (int i = 0; i < 5; i++) {
            if (i < note) {
                etoiles[i].setFill(Color.web("#FFD700"));
            } else {
                etoiles[i].setFill(Color.web("#E0E0E0"));
            }
        }
    }

    private void updateLabelNote() {
        String[] labels = {"", "Très mauvais", "Mauvais", "Moyen", "Bon", "Excellent"};
        lblNoteSelectionnee.setText(labels[noteSelectionnee] + " (" + noteSelectionnee + "/5)");

        // Couleur dynamique selon la note
        String couleur = switch (noteSelectionnee) {
            case 1, 2 -> "#d9534f";
            case 3 -> "#FF9800";
            case 4, 5 -> "#4CAF50";
            default -> "#666666";
        };
        lblNoteSelectionnee.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: " + couleur + ";");
    }

    public void preRemplir(Feedback f) {
        this.feedbackAModifier = f;
        txtCommentaire.setText(f.getCommentaire());
        dpDate.setValue(f.getDateStatut().toLocalDate());

        // Extraire la note si c'est un nombre
        try {
            String noteStr = f.getNote();
            if (noteStr.contains("/")) {
                noteStr = noteStr.split("/")[0].trim();
            }
            int note = Integer.parseInt(noteStr);
            noteSelectionnee = Math.min(Math.max(note, 0), 5);
            afficherEtoiles(noteSelectionnee);
            updateLabelNote();
        } catch (NumberFormatException e) {
            // Si ce n'est pas un nombre, on laisse à 0
        }
    }

    @FXML
    private void ajouter() {
        // Validation
        if (txtCommentaire.getText().trim().isEmpty()) {
            showAlert("Erreur", "Le commentaire est obligatoire.", Alert.AlertType.ERROR);
            return;
        }

        if (noteSelectionnee == 0) {
            showAlert("Erreur", "Veuillez sélectionner une note.", Alert.AlertType.ERROR);
            return;
        }

        if (dpDate.getValue() == null) {
            showAlert("Erreur", "La date est obligatoire.", Alert.AlertType.ERROR);
            return;
        }

        // Création ou mise à jour
        Feedback f = feedbackAModifier != null ? feedbackAModifier : new Feedback();
        f.setCommentaire(txtCommentaire.getText().trim());
        f.setNote(String.valueOf(noteSelectionnee)); // Stocker juste le chiffre
        f.setDateStatut(Date.valueOf(dpDate.getValue()));

        boolean succes = feedbackAModifier != null ? service.update(f) : service.ajouter(f);

        if (succes) {
            showAlert("Succès", feedbackAModifier != null ? "Feedback modifié avec succès !" : "Feedback ajouté avec succès !", Alert.AlertType.INFORMATION);
            txtCommentaire.getScene().getWindow().hide();
        } else {
            showAlert("Erreur", "Une erreur est survenue lors de l'opération.", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void retour() {
        txtCommentaire.getScene().getWindow().hide();
    }

    private void showAlert(String title, String msg, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}