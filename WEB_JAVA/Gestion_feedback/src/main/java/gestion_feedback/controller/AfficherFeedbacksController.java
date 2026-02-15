package gestion_feedback.controller;

import gestion_feedback.model.Feedback;
import gestion_feedback.service.FeedbackService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class AfficherFeedbacksController {

    @FXML private VBox vboxFeedbacks;
    @FXML private TextField txtRecherche;
    @FXML private ComboBox<String> comboTri;

    private final FeedbackService service = new FeedbackService();
    private List<Feedback> feedbacksOriginaux = new ArrayList<>();

    @FXML
    public void initialize() {
        configurerRecherche();
        configurerTri();
        chargerFeedbacks();
    }

    private void configurerRecherche() {
        if (txtRecherche != null) {
            txtRecherche.textProperty().addListener((observable, oldValue, newValue) -> {
                filtrerEtAfficher();
            });
        }
    }

    private void configurerTri() {
        if (comboTri != null) {
            comboTri.getItems().addAll(
                    "Note (décroissante) ⭐",
                    "Note (croissante) ⭐",
                    "Date (récente)",
                    "Date (ancienne)",
                    "Par défaut"
            );
            comboTri.setValue("Par défaut");

            comboTri.setOnAction(e -> filtrerEtAfficher());
        }
    }

    private void chargerFeedbacks() {
        feedbacksOriginaux = service.getAll();
        filtrerEtAfficher();
    }

    private void filtrerEtAfficher() {
        vboxFeedbacks.getChildren().clear();

        // 1. Copier la liste
        List<Feedback> feedbacksFiltres = new ArrayList<>(feedbacksOriginaux);

        // 2. Filtrer par recherche
        if (txtRecherche != null && !txtRecherche.getText().trim().isEmpty()) {
            String recherche = txtRecherche.getText().toLowerCase().trim();
            feedbacksFiltres = feedbacksFiltres.stream()
                    .filter(f -> f.getCommentaire().toLowerCase().contains(recherche) ||
                            String.valueOf(f.getId()).contains(recherche))
                    .collect(Collectors.toList());
        }

        // 3. Trier selon le choix
        if (comboTri != null && comboTri.getValue() != null) {
            String triSelectionne = comboTri.getValue();

            if (triSelectionne.equals("Note (décroissante) ⭐")) {
                feedbacksFiltres.sort(new Comparator<Feedback>() {
                    @Override
                    public int compare(Feedback f1, Feedback f2) {
                        int note1 = extraireNote(f1.getNote());
                        int note2 = extraireNote(f2.getNote());
                        return Integer.compare(note2, note1);
                    }
                });
            } else if (triSelectionne.equals("Note (croissante) ⭐")) {
                feedbacksFiltres.sort(new Comparator<Feedback>() {
                    @Override
                    public int compare(Feedback f1, Feedback f2) {
                        int note1 = extraireNote(f1.getNote());
                        int note2 = extraireNote(f2.getNote());
                        return Integer.compare(note1, note2);
                    }
                });
            } else if (triSelectionne.equals("Date (récente)")) {
                feedbacksFiltres.sort(new Comparator<Feedback>() {
                    @Override
                    public int compare(Feedback f1, Feedback f2) {
                        return f2.getDateStatut().compareTo(f1.getDateStatut());
                    }
                });
            } else if (triSelectionne.equals("Date (ancienne)")) {
                feedbacksFiltres.sort(new Comparator<Feedback>() {
                    @Override
                    public int compare(Feedback f1, Feedback f2) {
                        return f1.getDateStatut().compareTo(f2.getDateStatut());
                    }
                });
            } else {
                // Par défaut - tri par ID
                feedbacksFiltres.sort(Comparator.comparing(Feedback::getId));
            }
        }

        // 4. Afficher les résultats
        if (feedbacksFiltres.isEmpty()) {
            afficherEtatVide();
            return;
        }

        for (Feedback f : feedbacksFiltres) {
            VBox card = createFeedbackCard(f);
            vboxFeedbacks.getChildren().add(card);
        }
    }

    private void afficherEtatVide() {
        VBox emptyState = new VBox(20);
        emptyState.setAlignment(Pos.CENTER);
        emptyState.setStyle("-fx-padding: 60; -fx-background-color: white; -fx-background-radius: 15; -fx-border-color: #E0E0E0; -fx-border-width: 2; -fx-border-radius: 15;");

        String recherche = (txtRecherche != null) ? txtRecherche.getText().trim() : "";

        Label iconLabel = new Label(recherche.isEmpty() ? "📭" : "🔍");
        iconLabel.setStyle("-fx-font-size: 72px;");

        Label messageLabel = new Label(
                recherche.isEmpty() ? "Aucun feedback pour le moment" : "Aucun résultat trouvé"
        );
        messageLabel.setStyle("-fx-font-size: 22px; -fx-text-fill: #666666; -fx-font-weight: bold;");

        Label subMessage = new Label(
                recherche.isEmpty() ? "Cliquez sur 'AJOUTER FEEDBACK' pour commencer" :
                        "Essayez avec d'autres mots-clés"
        );
        subMessage.setStyle("-fx-font-size: 14px; -fx-text-fill: #999999;");

        emptyState.getChildren().addAll(iconLabel, messageLabel, subMessage);
        vboxFeedbacks.getChildren().add(emptyState);
    }

    private int extraireNote(String noteStr) {
        try {
            if (noteStr.contains("/")) {
                noteStr = noteStr.split("/")[0].trim();
            }
            return Integer.parseInt(noteStr);
        } catch (NumberFormatException e) {
            if (noteStr.toLowerCase().contains("excellent")) return 5;
            else if (noteStr.toLowerCase().contains("bon")) return 4;
            else if (noteStr.toLowerCase().contains("moyen")) return 3;
            else if (noteStr.toLowerCase().contains("mauvais")) return 2;
            else return 1;
        }
    }

    private VBox createFeedbackCard(Feedback f) {
        VBox card = new VBox(15);
        card.setStyle("-fx-background-color: white; -fx-border-color: #E0E0E0; -fx-border-width: 1; -fx-padding: 25; -fx-background-radius: 12; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 3);");

        HBox header = new HBox(20);
        header.setAlignment(Pos.CENTER_LEFT);

        Label idLabel = new Label("Feedback #" + f.getId());
        idLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #1F4AA8;");

        HBox etoilesBox = creerAffichageEtoiles(f.getNote());
        header.getChildren().addAll(idLabel, etoilesBox);

        Label dateLabel = new Label("📅 " + f.getDateStatut());
        dateLabel.setStyle("-fx-font-size: 13px; -fx-text-fill: #999999;");

        Label commentaireLabel = new Label(f.getCommentaire());
        commentaireLabel.setWrapText(true);
        commentaireLabel.setStyle("-fx-font-size: 15px; -fx-text-fill: #333333; -fx-padding: 15 0;");

        Separator separator = new Separator();
        separator.setStyle("-fx-background-color: #E0E0E0;");

        HBox boutons = new HBox(15);
        boutons.setAlignment(Pos.CENTER_RIGHT);

        Button btnReponses = createStyledButton("💬 Réponses", "#4CAF50");
        btnReponses.setOnAction(e -> ouvrirReponses(f.getId()));

        Button btnModifier = createStyledButton("✏️ Modifier", "#2196F3");
        btnModifier.setOnAction(e -> modifierFeedback(f));

        Button btnSupprimer = createStyledButton("🗑️ Supprimer", "#f44336");
        btnSupprimer.setOnAction(e -> supprimerFeedback(f.getId()));

        boutons.getChildren().addAll(btnReponses, btnModifier, btnSupprimer);

        card.getChildren().addAll(header, dateLabel, commentaireLabel, separator, boutons);
        return card;
    }

    private HBox creerAffichageEtoiles(String noteStr) {
        HBox etoilesBox = new HBox(5);
        etoilesBox.setAlignment(Pos.CENTER_LEFT);

        int note = extraireNote(noteStr);

        for (int i = 0; i < 5; i++) {
            Text etoile = new Text("★");
            etoile.setFont(Font.font("Arial", 24));
            if (i < note) {
                etoile.setFill(Color.web("#FFD700"));
            } else {
                etoile.setFill(Color.web("#E0E0E0"));
            }
            etoilesBox.getChildren().add(etoile);
        }

        String[] labels = {"", "Très mauvais", "Mauvais", "Moyen", "Bon", "Excellent"};
        Label noteLabel = new Label(note > 0 && note <= 5 ? labels[note] : "");
        noteLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #666666; -fx-padding: 0 0 0 10;");

        etoilesBox.getChildren().add(noteLabel);
        return etoilesBox;
    }

    private Button createStyledButton(String text, String color) {
        Button btn = new Button(text);
        btn.setStyle("-fx-background-color: " + color + "; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 10 20; -fx-background-radius: 20; -fx-cursor: hand; -fx-font-weight: bold;");

        btn.setOnMouseEntered(e -> btn.setStyle("-fx-background-color: derive(" + color + ", -10%); -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 10 20; -fx-background-radius: 20; -fx-cursor: hand; -fx-font-weight: bold; -fx-scale-x: 1.05; -fx-scale-y: 1.05;"));
        btn.setOnMouseExited(e -> btn.setStyle("-fx-background-color: " + color + "; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 10 20; -fx-background-radius: 20; -fx-cursor: hand; -fx-font-weight: bold;"));

        return btn;
    }

    private void modifierFeedback(Feedback f) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/AjouterFeedback.fxml"));
            Parent root = loader.load();

            AjouterFeedbackController controller = loader.getController();
            controller.preRemplir(f);

            Stage stage = new Stage();
            stage.setScene(new Scene(root, 650, 700));
            stage.setTitle("Modifier Feedback #" + f.getId());
            stage.showAndWait();

            chargerFeedbacks();
        } catch (IOException e) {
            showAlert("Erreur", "Impossible d'ouvrir le formulaire de modification.", Alert.AlertType.ERROR);
        }
    }

    private void supprimerFeedback(int id) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation de suppression");
        alert.setHeaderText("Voulez-vous vraiment supprimer ce feedback ?");
        alert.setContentText("Cette action est irréversible.");

        if (alert.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
            if (service.supprimer(id)) {
                showAlert("Succès", "Feedback supprimé avec succès !", Alert.AlertType.INFORMATION);
                chargerFeedbacks();
            } else {
                showAlert("Erreur", "Échec de la suppression.", Alert.AlertType.ERROR);
            }
        }
    }

    @FXML
    private void ajouterFeedback() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/AjouterFeedback.fxml"));
        Parent root = loader.load();
        Stage stage = new Stage();
        stage.setScene(new Scene(root, 650, 700));
        stage.setTitle("Ajouter un Feedback");
        stage.showAndWait();
        chargerFeedbacks();
    }

    @FXML
    private void actualiser() {
        chargerFeedbacks();
        showAlert("Actualisation", "Liste des feedbacks actualisée !", Alert.AlertType.INFORMATION);
    }

    @FXML
    private void effacerRecherche() {
        if (txtRecherche != null) {
            txtRecherche.clear();
        }
    }

    private void ouvrirReponses(int feedbackId) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/VoirReponses.fxml"));
            Parent root = loader.load();

            VoirReponsesController controller = loader.getController();
            controller.setFeedbackId(feedbackId);
            controller.chargerReponses();

            Stage stage = new Stage();
            stage.setScene(new Scene(root, 750, 650));
            stage.setTitle("Réponses au Feedback #" + feedbackId);
            stage.showAndWait();

            chargerFeedbacks();
        } catch (IOException e) {
            showAlert("Erreur", "Impossible d'ouvrir les réponses.", Alert.AlertType.ERROR);
        }
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}