package com.example.gestion_commande.Controllers;

import com.example.gestion_commande.Entities.Commande;
import com.example.gestion_commande.Entities.Livraison;
import com.example.gestion_commande.Services.CommandeService;
import com.example.gestion_commande.Services.LivraisonService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public class AfficherCommandesAvecLivraisonController {

    @FXML private VBox commandesVBox;
    @FXML private Parent navbar;

    private CommandeService commandeService;
    private LivraisonService livraisonService;

    @FXML
    public void initialize() {
        commandeService = new CommandeService();
        livraisonService = new LivraisonService();
        chargerCommandesAvecLivraison();

        if (navbar != null) {
            NavbarController navbarController = (NavbarController) navbar.getProperties().get("controller");
            if (navbarController != null) {
                navbarController.setActiveButton("livraison");
            }
        }
    }

    private void chargerCommandesAvecLivraison() {
        List<Commande> commandes = commandeService.afficher();
        commandesVBox.getChildren().clear();

        if (commandes.isEmpty()) {
            Label label = new Label("Aucune commande trouvée");
            label.setFont(new Font("Arial", 16));
            label.setTextFill(Color.web("#666666"));
            commandesVBox.getChildren().add(label);
            return;
        }

        for (Commande commande : commandes) {
            Livraison livraison = livraisonService.rechercherParIdCommande(commande.getIdCommande());
            commandesVBox.getChildren().add(creerLigneCommande(commande, livraison));
        }
    }

    private HBox creerLigneCommande(Commande commande, Livraison livraison) {
        HBox ligne = new HBox();
        ligne.setPrefHeight(80);
        ligne.setSpacing(15);
        ligne.setStyle("-fx-background-color: white; -fx-border-color: #CCCCCC; -fx-border-width: 1; -fx-border-radius: 5; -fx-background-radius: 5;");
        ligne.setPadding(new Insets(15));

        VBox numeroBox = new VBox();
        numeroBox.setPrefWidth(120);
        numeroBox.setSpacing(5);
        Label numeroTitle = new Label("N° Commande");
        numeroTitle.setFont(new Font("Arial Bold", 11));
        numeroTitle.setTextFill(Color.web("#666666"));
        Label numeroValue = new Label(commande.getNumeroCommande());
        numeroValue.setFont(new Font("Arial Bold", 14));
        numeroValue.setTextFill(Color.web("#1F4AA8"));
        numeroBox.getChildren().addAll(numeroTitle, numeroValue);

        VBox clientBox = new VBox();
        clientBox.setPrefWidth(150);
        clientBox.setSpacing(5);
        Label clientTitle = new Label("Client");
        clientTitle.setFont(new Font("Arial Bold", 11));
        clientTitle.setTextFill(Color.web("#666666"));
        Label clientValue = new Label(commande.getClient());
        clientValue.setFont(new Font("Arial", 13));
        clientBox.getChildren().addAll(clientTitle, clientValue);

        VBox montantBox = new VBox();
        montantBox.setPrefWidth(100);
        montantBox.setSpacing(5);
        Label montantTitle = new Label("Montant");
        montantTitle.setFont(new Font("Arial Bold", 11));
        montantTitle.setTextFill(Color.web("#666666"));
        Label montantValue = new Label(String.format("%.2f TND", commande.getMontantTotal()));
        montantValue.setFont(new Font("Arial Bold", 13));
        montantValue.setTextFill(Color.web("#FF9800"));
        montantBox.getChildren().addAll(montantTitle, montantValue);

        VBox statutCommBox = new VBox();
        statutCommBox.setPrefWidth(120);
        statutCommBox.setSpacing(5);
        Label statutCommTitle = new Label("Statut Cmd");
        statutCommTitle.setFont(new Font("Arial Bold", 11));
        statutCommTitle.setTextFill(Color.web("#666666"));
        Label statutCommValue = new Label(commande.getStatut());
        statutCommValue.setFont(new Font("Arial Bold", 11));
        statutCommValue.setTextFill(Color.WHITE);

        String couleurStatutComm = commande.getStatut().equals("Confirmée") ? "#4CAF50" :
                commande.getStatut().equals("En attente") ? "#FF9800" : "#d9534f";
        statutCommValue.setStyle("-fx-background-color: " + couleurStatutComm + "; -fx-padding: 5px 8px; -fx-border-radius: 3;");

        statutCommBox.getChildren().addAll(statutCommTitle, statutCommValue);

        VBox statutLivBox = new VBox();
        statutLivBox.setPrefWidth(150);
        statutLivBox.setSpacing(5);
        Label statutLivTitle = new Label("Statut Livraison");
        statutLivTitle.setFont(new Font("Arial Bold", 11));
        statutLivTitle.setTextFill(Color.web("#666666"));

        Label statutLivValue;
        if (livraison != null) {
            statutLivValue = new Label(livraison.getStatutLivraison());
            String couleurStatutLiv = livraison.getStatutLivraison().equals("Livré") ? "#4CAF50" :
                    livraison.getStatutLivraison().equals("En cours") ? "#FF9800" : "#d9534f";
            statutLivValue.setStyle("-fx-background-color: " + couleurStatutLiv + "; -fx-text-fill: white; -fx-padding: 5px 8px; -fx-border-radius: 3;");
        } else {
            statutLivValue = new Label("Pas de livraison");
            statutLivValue.setStyle("-fx-background-color: #CCCCCC; -fx-text-fill: white; -fx-padding: 5px 8px; -fx-border-radius: 3;");
        }

        statutLivValue.setFont(new Font("Arial Bold", 11));
        statutLivBox.getChildren().addAll(statutLivTitle, statutLivValue);

        VBox blBox = new VBox();
        blBox.setPrefWidth(120);
        blBox.setSpacing(5);
        Label blTitle = new Label("N° BL");
        blTitle.setFont(new Font("Arial Bold", 11));
        blTitle.setTextFill(Color.web("#666666"));
        Label blValue = new Label(livraison != null ? livraison.getNumeroBL() : "-");
        blValue.setFont(new Font("Arial", 12));
        blBox.getChildren().addAll(blTitle, blValue);

        VBox livreurBox = new VBox();
        livreurBox.setPrefWidth(140);
        livreurBox.setSpacing(5);
        Label livreurTitle = new Label("Livreur");
        livreurTitle.setFont(new Font("Arial Bold", 11));
        livreurTitle.setTextFill(Color.web("#666666"));
        Label livreurValue = new Label(livraison != null ? livraison.getLivreur() : "-");
        livreurValue.setFont(new Font("Arial", 12));
        livreurBox.getChildren().addAll(livreurTitle, livreurValue);

        Button modifierLivraisonBtn = new Button("Modifier");
        modifierLivraisonBtn.setPrefWidth(80);
        modifierLivraisonBtn.setStyle("-fx-background-color: #FF9800; -fx-text-fill: white; -fx-font-size: 11.0; -fx-font-weight: bold; -fx-cursor: hand;");

        if (livraison == null) {
            modifierLivraisonBtn.setDisable(true);
            modifierLivraisonBtn.setStyle("-fx-background-color: #CCCCCC; -fx-text-fill: white; -fx-font-size: 11.0; -fx-cursor: default;");
        } else {
            modifierLivraisonBtn.setOnAction(e -> ouvrirModifierLivraison(livraison));
        }

        ligne.getChildren().addAll(numeroBox, clientBox, montantBox, statutCommBox,
                statutLivBox, blBox, livreurBox, modifierLivraisonBtn);

        return ligne;
    }

    private void ouvrirModifierLivraison(Livraison livraison) {
        try {
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setTitle("Modifier Livraison");
            dialog.setHeaderText("Modifier le statut de livraison: " + livraison.getNumeroBL());

            VBox formBox = new VBox();
            formBox.setSpacing(12);
            formBox.setPadding(new Insets(15));

            Label blLabel = new Label("Numéro BL:");
            blLabel.setStyle("-fx-font-weight: bold;");
            TextField blField = new TextField(livraison.getNumeroBL());
            blField.setDisable(true);

            Label livreurLabel = new Label("Livreur:");
            livreurLabel.setStyle("-fx-font-weight: bold;");
            TextField livreurField = new TextField(livraison.getLivreur());

            Label dateLabel = new Label("Date de Livraison:");
            dateLabel.setStyle("-fx-font-weight: bold;");
            DatePicker datePicker = new DatePicker(livraison.getDateLivraison());

            Label statutLabel = new Label("Statut de Livraison:");
            statutLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #FF6B00;");
            ComboBox<String> statutCombo = new ComboBox<>();
            statutCombo.getItems().addAll("En cours", "Livré", "Retardé");
            statutCombo.setValue(livraison.getStatutLivraison());
            statutCombo.setPrefWidth(200);

            Label notesLabel = new Label("Notes:");
            notesLabel.setStyle("-fx-font-weight: bold;");
            TextArea notesArea = new TextArea(livraison.getNoteDelivery() != null ? livraison.getNoteDelivery() : "");
            notesArea.setPrefRowCount(3);
            notesArea.setWrapText(true);

            formBox.getChildren().addAll(
                    blLabel, blField,
                    livreurLabel, livreurField,
                    dateLabel, datePicker,
                    statutLabel, statutCombo,
                    notesLabel, notesArea
            );

            dialog.getDialogPane().setContent(formBox);
            dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

            if (dialog.showAndWait().get() == ButtonType.OK) {
                try {
                    livraison.setLivreur(livreurField.getText().trim());
                    livraison.setDateLivraison(datePicker.getValue());
                    livraison.setStatutLivraison(statutCombo.getValue());
                    livraison.setNoteDelivery(notesArea.getText().trim());

                    if (livraisonService.modifier(livraison)) {
                        afficherAlert("Succès", "Livraison modifiée avec succès!", Alert.AlertType.INFORMATION);
                        chargerCommandesAvecLivraison();
                    } else {
                        afficherAlert("Erreur", "Erreur lors de la modification!", Alert.AlertType.ERROR);
                    }
                } catch (Exception e) {
                    afficherAlert("Erreur", "Erreur lors de la modification!", Alert.AlertType.ERROR);
                    e.printStackTrace();
                }
            }

        } catch (Exception e) {
            afficherAlert("Erreur", "Erreur lors de l'ouverture du formulaire!", Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    @FXML
    private void retourner() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/gestion_commande/AfficherCommandesView.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) commandesVBox.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Afficher Commandes");
            stage.show();
        } catch (IOException e) {
            afficherAlert("Erreur", "Impossible de charger la page!", Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    @FXML
    private void actualiser() {
        chargerCommandesAvecLivraison();
        afficherAlert("Info", "Données actualisées!", Alert.AlertType.INFORMATION);
    }

    private void afficherAlert(String titre, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}