package com.example.gestion_commande.Controllers;

import com.example.gestion_commande.Entities.Commande;
import com.example.gestion_commande.Services.CommandeService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.List;

public class AfficherCommandesController {

    @FXML private FlowPane commandesFlowPane;
    @FXML private Parent navbar;

    private CommandeService commandeService;

    @FXML
    public void initialize() {
        commandeService = new CommandeService();
        chargerCommandes();

        if (navbar != null) {
            NavbarController navbarController = (NavbarController) navbar.getProperties().get("controller");
            if (navbarController != null) {
                navbarController.setActiveButton("afficher");
            }
        }
    }

    private void chargerCommandes() {
        List<Commande> commandes = commandeService.afficher();
        commandesFlowPane.getChildren().clear();

        if (commandes.isEmpty()) {
            Label label = new Label("Aucune commande trouvée");
            label.setFont(new Font("Arial", 16));
            label.setTextFill(Color.web("#666666"));
            commandesFlowPane.getChildren().add(label);
            return;
        }

        for (Commande commande : commandes) {
            commandesFlowPane.getChildren().add(creerCardCommande(commande));
        }
    }

    private VBox creerCardCommande(Commande commande) {
        VBox card = new VBox();
        card.setPrefWidth(280);
        card.setPrefHeight(380);
        card.setStyle("-fx-background-color: white; -fx-border-color: #FF9800; -fx-border-width: 3; -fx-border-radius: 10; -fx-background-radius: 10;");
        card.setSpacing(10);
        card.setPadding(new javafx.geometry.Insets(15));

        HBox headerBox = new HBox();
        headerBox.setSpacing(10);

        Label numeroLabel = new Label(commande.getNumeroCommande());
        numeroLabel.setFont(new Font("Arial Bold", 16));
        numeroLabel.setTextFill(Color.web("#1F4AA8"));

        Label statutLabel = new Label(commande.getStatut());
        statutLabel.setFont(new Font("Arial Bold", 11));
        statutLabel.setTextFill(Color.WHITE);
        String couleurStatut = commande.getStatut().equals("Confirmée") ? "#4CAF50" :
                commande.getStatut().equals("En attente") ? "#FF9800" : "#d9534f";
        statutLabel.setStyle("-fx-background-color: " + couleurStatut + "; -fx-padding: 5px 10px; -fx-border-radius: 5;");

        headerBox.getChildren().addAll(numeroLabel, statutLabel);

        Label clientLabelTitle = new Label("Client :");
        clientLabelTitle.setFont(new Font("Arial Bold", 12));
        clientLabelTitle.setTextFill(Color.web("#666666"));

        Label clientLabel = new Label(commande.getClient());
        clientLabel.setFont(new Font("Arial", 12));
        clientLabel.setWrapText(true);

        Label dateLabelTitle = new Label("Date :");
        dateLabelTitle.setFont(new Font("Arial Bold", 12));
        dateLabelTitle.setTextFill(Color.web("#666666"));

        Label dateLabel = new Label(commande.getDateCommande().toString());
        dateLabel.setFont(new Font("Arial", 12));

        Label montantLabelTitle = new Label("Montant :");
        montantLabelTitle.setFont(new Font("Arial Bold", 12));
        montantLabelTitle.setTextFill(Color.web("#666666"));

        Label montantLabel = new Label(String.format("%.2f TND", commande.getMontantTotal()));
        montantLabel.setFont(new Font("Arial Bold", 14));
        montantLabel.setTextFill(Color.web("#FF9800"));

        Label adresseLabelTitle = new Label("Adresse :");
        adresseLabelTitle.setFont(new Font("Arial Bold", 12));
        adresseLabelTitle.setTextFill(Color.web("#666666"));

        Label adresseLabel = new Label(commande.getAdresseLivraison());
        adresseLabel.setFont(new Font("Arial", 11));
        adresseLabel.setWrapText(true);

        HBox buttonsBox = new HBox();
        buttonsBox.setSpacing(8);
        buttonsBox.setPrefHeight(40);

        Button ajouterLivraisonBtn = new Button("Ajouter Livraison");
        ajouterLivraisonBtn.setPrefWidth(130);
        ajouterLivraisonBtn.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 10.0; -fx-font-weight: bold; -fx-cursor: hand;");
        ajouterLivraisonBtn.setOnAction(e -> ouvrirAjouterLivraison(commande.getIdCommande()));

        Button modifierBtn = new Button("Modifier");
        modifierBtn.setPrefWidth(75);
        modifierBtn.setStyle("-fx-background-color: #1F4AA8; -fx-text-fill: white; -fx-font-size: 10.0; -fx-font-weight: bold; -fx-cursor: hand;");
        modifierBtn.setOnAction(e -> ouvrirModifierCommande(commande));

        Button supprimerBtn = new Button("Supprimer");
        supprimerBtn.setPrefWidth(75);
        supprimerBtn.setStyle("-fx-background-color: #d9534f; -fx-text-fill: white; -fx-font-size: 10.0; -fx-font-weight: bold; -fx-cursor: hand;");
        supprimerBtn.setOnAction(e -> supprimerCommande(commande.getIdCommande()));

        buttonsBox.getChildren().addAll(ajouterLivraisonBtn, modifierBtn, supprimerBtn);

        card.getChildren().addAll(headerBox, clientLabelTitle, clientLabel, dateLabelTitle, dateLabel,
                montantLabelTitle, montantLabel, adresseLabelTitle, adresseLabel, buttonsBox);

        VBox.setVgrow(adresseLabel, javafx.scene.layout.Priority.ALWAYS);

        return card;
    }

    private void ouvrirModifierCommande(Commande commande) {
        try {
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setTitle("Modifier Commande");
            dialog.setHeaderText("Modifier la commande: " + commande.getNumeroCommande());

            VBox formBox = new VBox();
            formBox.setSpacing(12);
            formBox.setPadding(new javafx.geometry.Insets(15));

            Label numeroLabel = new Label("Numéro Commande:");
            numeroLabel.setStyle("-fx-font-weight: bold;");
            TextField numeroField = new TextField(commande.getNumeroCommande());
            numeroField.setDisable(true);

            Label clientLabel = new Label("Nom du Client:");
            clientLabel.setStyle("-fx-font-weight: bold;");
            TextField clientField = new TextField(commande.getClient());

            Label montantLabel = new Label("Montant Total (TND):");
            montantLabel.setStyle("-fx-font-weight: bold;");
            TextField montantField = new TextField(String.valueOf(commande.getMontantTotal()));

            Label adresseLabel = new Label("Adresse de Livraison:");
            adresseLabel.setStyle("-fx-font-weight: bold;");
            TextArea adresseArea = new TextArea(commande.getAdresseLivraison());
            adresseArea.setPrefRowCount(3);
            adresseArea.setWrapText(true);

            Label statutLabel = new Label("Statut:");
            statutLabel.setStyle("-fx-font-weight: bold;");
            ComboBox<String> statutCombo = new ComboBox<>();
            statutCombo.getItems().addAll("En attente", "Confirmée", "Annulée");
            statutCombo.setValue(commande.getStatut());

            formBox.getChildren().addAll(
                    numeroLabel, numeroField,
                    clientLabel, clientField,
                    montantLabel, montantField,
                    adresseLabel, adresseArea,
                    statutLabel, statutCombo
            );

            dialog.getDialogPane().setContent(formBox);
            dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

            if (dialog.showAndWait().get() == ButtonType.OK) {
                try {
                    if (clientField.getText().trim().isEmpty()) {
                        afficherAlert("Validation", "Veuillez entrer le nom du client!", Alert.AlertType.WARNING);
                        return;
                    }

                    double montant = Double.parseDouble(montantField.getText().trim());

                    if (adresseArea.getText().trim().isEmpty()) {
                        afficherAlert("Validation", "Veuillez entrer l'adresse!", Alert.AlertType.WARNING);
                        return;
                    }

                    commande.setClient(clientField.getText().trim());
                    commande.setMontantTotal(montant);
                    commande.setAdresseLivraison(adresseArea.getText().trim());
                    commande.setStatut(statutCombo.getValue());

                    if (commandeService.modifier(commande)) {
                        afficherAlert("Succès", "Commande modifiée avec succès!", Alert.AlertType.INFORMATION);
                        chargerCommandes();
                    } else {
                        afficherAlert("Erreur", "Erreur lors de la modification!", Alert.AlertType.ERROR);
                    }
                } catch (NumberFormatException e) {
                    afficherAlert("Erreur", "Le montant doit être un nombre valide!", Alert.AlertType.ERROR);
                }
            }

        } catch (Exception e) {
            afficherAlert("Erreur", "Erreur lors de l'ouverture du formulaire!", Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    private void ouvrirAjouterLivraison(int idCommande) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/gestion_commande/AjouterLivraisonView.fxml"));
            Parent root = loader.load();

            AjouterLivraisonController controller = loader.getController();
            controller.setIdCommande(idCommande);

            Stage stage = (Stage) commandesFlowPane.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Ajouter Livraison");
            stage.show();
        } catch (IOException e) {
            afficherAlert("Erreur", "Impossible de charger la page!", Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    private void supprimerCommande(int idCommande) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Supprimer cette commande ?");
        alert.setContentText("Cette action est irréversible!");

        if (alert.showAndWait().get() == ButtonType.OK) {
            if (commandeService.supprimer(idCommande)) {
                afficherAlert("Succès", "Commande supprimée avec succès!", Alert.AlertType.INFORMATION);
                chargerCommandes();
            } else {
                afficherAlert("Erreur", "Erreur lors de la suppression!", Alert.AlertType.ERROR);
            }
        }
    }

    @FXML
    private void retourner() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/gestion_commande/AjouterCommandeView.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) commandesFlowPane.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Ajouter Commande");
            stage.show();
        } catch (IOException e) {
            afficherAlert("Erreur", "Impossible de charger la page!", Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    @FXML
    private void actualiser() {
        chargerCommandes();
        afficherAlert("Info", "Données actualisées!", Alert.AlertType.INFORMATION);
    }

    @FXML
    private void afficherAvecLivraison() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/gestion_commande/AfficherCommandesAvecLivraisonView.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) commandesFlowPane.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Commandes avec Livraison");
            stage.show();
        } catch (IOException e) {
            afficherAlert("Erreur", "Impossible de charger la page!", Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    private void afficherAlert(String titre, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}