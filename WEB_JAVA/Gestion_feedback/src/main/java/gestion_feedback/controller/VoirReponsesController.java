package gestion_feedback.controller;

import gestion_feedback.model.Reponse;
import gestion_feedback.service.ReponseService;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

public class VoirReponsesController {

    @FXML private VBox vboxReponses;
    @FXML private TextArea txtContenuReponse;
    @FXML private DatePicker dpDateReponse;
    @FXML private Label lblTitre;

    private final ReponseService service = new ReponseService();
    private int feedbackId;

    public void setFeedbackId(int id) {
        this.feedbackId = id;
        if (lblTitre != null) {
            lblTitre.setText("💬 Réponses au Feedback #" + id);
        }
    }

    @FXML
    public void initialize() {
        // Définir la date actuelle par défaut
        dpDateReponse.setValue(LocalDate.now());
    }

    public void chargerReponses() {
        vboxReponses.getChildren().clear();
        List<Reponse> reponses = service.getByFeedbackId(feedbackId);

        if (reponses.isEmpty()) {
            VBox emptyState = new VBox(20);
            emptyState.setAlignment(Pos.CENTER);
            emptyState.setStyle("-fx-padding: 50; -fx-background-color: #f8f9fa; -fx-background-radius: 10;");

            Label iconLabel = new Label("💭");
            iconLabel.setStyle("-fx-font-size: 60px;");

            Label messageLabel = new Label("Aucune réponse pour ce feedback");
            messageLabel.setStyle("-fx-font-size: 18px; -fx-text-fill: #666666; -fx-font-weight: bold;");

            Label subMessage = new Label("Soyez le premier à répondre !");
            subMessage.setStyle("-fx-font-size: 13px; -fx-text-fill: #999999;");

            emptyState.getChildren().addAll(iconLabel, messageLabel, subMessage);
            vboxReponses.getChildren().add(emptyState);
            return;
        }

        for (Reponse r : reponses) {
            VBox card = createReponseCard(r);
            vboxReponses.getChildren().add(card);
        }
    }

    private VBox createReponseCard(Reponse r) {
        VBox card = new VBox(12);
        card.setStyle("-fx-background-color: white; -fx-border-color: #E0E0E0; -fx-border-width: 1; -fx-padding: 20; -fx-background-radius: 10; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 8, 0, 0, 2);");

        // En-tête avec numéro et date
        HBox header = new HBox(15);
        header.setAlignment(Pos.CENTER_LEFT);

        Label numLabel = new Label("Réponse #" + r.getId());
        numLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #1F4AA8;");

        Label dateLabel = new Label("📅 " + r.getDateReponse());
        dateLabel.setStyle("-fx-font-size: 13px; -fx-text-fill: #999999;");

        header.getChildren().addAll(numLabel, dateLabel);

        // Contenu de la réponse
        Label contenuLabel = new Label(r.getContenu());
        contenuLabel.setWrapText(true);
        contenuLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #333333; -fx-padding: 10 0;");

        // Séparateur
        Separator separator = new Separator();
        separator.setStyle("-fx-background-color: #E0E0E0;");

        // Boutons d'action
        HBox boutons = new HBox(12);
        boutons.setAlignment(Pos.CENTER_RIGHT);

        Button btnModifier = new Button("✏️ Modifier");
        btnModifier.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white; -fx-font-size: 13px; -fx-padding: 8 18; -fx-background-radius: 18; -fx-cursor: hand; -fx-font-weight: bold;");
        btnModifier.setOnAction(e -> modifierReponse(r));

        Button btnSupprimer = new Button("🗑️ Supprimer");
        btnSupprimer.setStyle("-fx-background-color: #f44336; -fx-text-fill: white; -fx-font-size: 13px; -fx-padding: 8 18; -fx-background-radius: 18; -fx-cursor: hand; -fx-font-weight: bold;");
        btnSupprimer.setOnAction(e -> supprimerReponse(r.getId()));

        // Effets hover
        btnModifier.setOnMouseEntered(e -> btnModifier.setStyle("-fx-background-color: #1976D2; -fx-text-fill: white; -fx-font-size: 13px; -fx-padding: 8 18; -fx-background-radius: 18; -fx-cursor: hand; -fx-font-weight: bold; -fx-scale-x: 1.05; -fx-scale-y: 1.05;"));
        btnModifier.setOnMouseExited(e -> btnModifier.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white; -fx-font-size: 13px; -fx-padding: 8 18; -fx-background-radius: 18; -fx-cursor: hand; -fx-font-weight: bold;"));

        btnSupprimer.setOnMouseEntered(e -> btnSupprimer.setStyle("-fx-background-color: #d32f2f; -fx-text-fill: white; -fx-font-size: 13px; -fx-padding: 8 18; -fx-background-radius: 18; -fx-cursor: hand; -fx-font-weight: bold; -fx-scale-x: 1.05; -fx-scale-y: 1.05;"));
        btnSupprimer.setOnMouseExited(e -> btnSupprimer.setStyle("-fx-background-color: #f44336; -fx-text-fill: white; -fx-font-size: 13px; -fx-padding: 8 18; -fx-background-radius: 18; -fx-cursor: hand; -fx-font-weight: bold;"));

        boutons.getChildren().addAll(btnModifier, btnSupprimer);

        card.getChildren().addAll(header, contenuLabel, separator, boutons);
        return card;
    }

    private void modifierReponse(Reponse r) {
        Dialog<Reponse> dialog = new Dialog<>();
        dialog.setTitle("Modifier Réponse");
        dialog.setHeaderText("Modifiez le contenu de la réponse");

        ButtonType btnConfirmer = new ButtonType("✓ Confirmer", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(btnConfirmer, ButtonType.CANCEL);

        TextArea txtContenu = new TextArea(r.getContenu());
        txtContenu.setPromptText("Nouveau contenu...");
        txtContenu.setPrefRowCount(5);
        txtContenu.setWrapText(true);
        txtContenu.setStyle("-fx-font-size: 14px;");

        DatePicker dpDate = new DatePicker(r.getDateReponse().toLocalDate());
        dpDate.setStyle("-fx-font-size: 14px;");

        VBox content = new VBox(15);
        content.setStyle("-fx-padding: 20;");
        content.getChildren().addAll(
                new Label("📝 Contenu :"), txtContenu,
                new Label("📅 Date :"), dpDate
        );
        dialog.getDialogPane().setContent(content);

        dialog.setResultConverter(button -> {
            if (button == btnConfirmer) {
                if (txtContenu.getText().trim().isEmpty()) {
                    showAlert("Erreur", "Le contenu ne peut pas être vide.", Alert.AlertType.ERROR);
                    return null;
                }
                r.setContenu(txtContenu.getText().trim());
                r.setDateReponse(Date.valueOf(dpDate.getValue()));
                return r;
            }
            return null;
        });

        dialog.showAndWait().ifPresent(updated -> {
            if (service.update(updated)) {
                showAlert("Succès", "Réponse modifiée avec succès !", Alert.AlertType.INFORMATION);
                chargerReponses();
            } else {
                showAlert("Erreur", "Échec de la modification.", Alert.AlertType.ERROR);
            }
        });
    }

    private void supprimerReponse(int id) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation de suppression");
        alert.setHeaderText("Voulez-vous vraiment supprimer cette réponse ?");
        alert.setContentText("Cette action est irréversible.");

        if (alert.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
            if (service.supprimer(id)) {
                showAlert("Succès", "Réponse supprimée avec succès !", Alert.AlertType.INFORMATION);
                chargerReponses();
            } else {
                showAlert("Erreur", "Échec de la suppression.", Alert.AlertType.ERROR);
            }
        }
    }

    @FXML
    private void ajouterReponse() {
        // Validation
        if (txtContenuReponse.getText().trim().isEmpty()) {
            showAlert("Erreur", "Le contenu est obligatoire.", Alert.AlertType.ERROR);
            return;
        }
        if (dpDateReponse.getValue() == null) {
            showAlert("Erreur", "La date est obligatoire.", Alert.AlertType.ERROR);
            return;
        }

        Reponse r = new Reponse();
        r.setContenu(txtContenuReponse.getText().trim());
        r.setDateReponse(Date.valueOf(dpDateReponse.getValue()));
        r.setFeedbackId(feedbackId);

        if (service.ajouter(r)) {
            showAlert("Succès", "Réponse ajoutée avec succès !", Alert.AlertType.INFORMATION);
            txtContenuReponse.clear();
            dpDateReponse.setValue(LocalDate.now());
            chargerReponses();
        } else {
            showAlert("Erreur", "Échec de l'ajout.", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void fermer() {
        txtContenuReponse.getScene().getWindow().hide();
    }

    private void showAlert(String title, String msg, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}