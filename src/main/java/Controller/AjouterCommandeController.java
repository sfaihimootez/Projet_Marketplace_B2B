package com.example.gestion_commande.Controllers;

import com.example.gestion_commande.Entities.Commande;
import com.example.gestion_commande.Services.CommandeService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.io.IOException;
import java.time.LocalDate;

public class AjouterCommandeController {

    @FXML private TextField numeroCommandeField;
    @FXML private TextField clientField;
    @FXML private DatePicker dateCommandePicker;
    @FXML private TextField montantTotalField;
    @FXML private TextField adresseLivraisonField;
    @FXML private ComboBox<String> statutComboBox;
    @FXML private Parent navbar;

    private CommandeService commandeService;

    @FXML
    public void initialize() {
        commandeService = new CommandeService();

        statutComboBox.getItems().addAll("En attente", "Confirmée", "Annulée");
        statutComboBox.setValue("En attente");
        dateCommandePicker.setValue(LocalDate.now());

        // ✅ RESTRICTIONS DE SAISIE
        restreindreSaisieChiffres(numeroCommandeField);
        restreindreSaisieLettres(clientField);

        if (navbar != null) {
            NavbarController navbarController = (NavbarController) navbar.getProperties().get("controller");
            if (navbarController != null) {
                navbarController.setActiveButton("ajouter");
            }
        }
    }

    // ✅ Méthode pour restreindre la saisie aux chiffres uniquement
    private void restreindreSaisieChiffres(TextField textField) {
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && !newValue.matches("\\d*")) {
                textField.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
    }

    // ✅ Méthode pour restreindre la saisie aux lettres uniquement
    private void restreindreSaisieLettres(TextField textField) {
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && !newValue.matches("[a-zA-Z\\s\\-']*")) {
                textField.setText(newValue.replaceAll("[^a-zA-Z\\s\\-']", ""));
            }
        });
    }

    @FXML
    private void ajouterCommande() {
        if (!validerFormulaire()) {
            return;
        }

        try {
            String numeroCommande = numeroCommandeField.getText().trim();
            String client = clientField.getText().trim();
            LocalDate dateCommande = dateCommandePicker.getValue();
            double montantTotal = Double.parseDouble(montantTotalField.getText().trim());
            String adresseLivraison = adresseLivraisonField.getText().trim();
            String statut = statutComboBox.getValue();

            Commande commande = new Commande(numeroCommande, client, dateCommande, montantTotal,
                    adresseLivraison, statut);

            if (commandeService.ajouter(commande)) {
                afficherAlert("Succès", "Commande ajoutée avec succès!", Alert.AlertType.INFORMATION);
                effacer();
            } else {
                afficherAlert("Erreur", "Erreur lors de l'ajout de la commande!", Alert.AlertType.ERROR);
            }
        } catch (NumberFormatException e) {
            afficherAlert("Erreur", "Veuillez entrer un montant valide!", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void afficherCommandes() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/gestion_commande/AfficherCommandesView.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) numeroCommandeField.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Afficher Commandes");
            stage.show();
        } catch (IOException e) {
            afficherAlert("Erreur", "Impossible de charger la page d'affichage!", Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    @FXML
    private void effacer() {
        numeroCommandeField.clear();
        clientField.clear();
        montantTotalField.clear();
        adresseLivraisonField.clear();
        dateCommandePicker.setValue(LocalDate.now());
        statutComboBox.setValue("En attente");
    }

    private boolean validerFormulaire() {
        // ✅ Validation du numéro de commande
        String numeroCommande = numeroCommandeField.getText().trim();
        if (numeroCommande.isEmpty()) {
            afficherAlert("Validation", "Veuillez entrer le numéro de commande!", Alert.AlertType.WARNING);
            return false;
        }

        if (!numeroCommande.matches("^[0-9]+$")) {
            afficherAlert("Validation", "Le numéro de commande doit être un nombre entier (chiffres uniquement)", Alert.AlertType.WARNING);
            return false;
        }

        if (numeroCommande.length() < 1 || numeroCommande.length() > 10) {
            afficherAlert("Validation", "Le numéro de commande doit contenir entre 3 et 10 chiffres", Alert.AlertType.WARNING);
            return false;
        }

        // ✅ Validation du client (lettres uniquement)
        String client = clientField.getText().trim();
        if (client.isEmpty()) {
            afficherAlert("Validation", "Veuillez entrer le nom du client", Alert.AlertType.WARNING);
            return false;
        }

        if (!client.matches("^[a-zA-Z\\s\\-']+$")) {
            afficherAlert("Validation", "Le nom du client doit contenir uniquement des lettres\n(Ex: Mohamed Ali, Jean-Marie, O'Connor)", Alert.AlertType.WARNING);
            return false;
        }

        if (client.length() < 2 || client.length() > 50) {
            afficherAlert("Validation", "Le nom du client doit contenir entre 2 et 50 caractères", Alert.AlertType.WARNING);
            return false;
        }

        // ✅ Validation de la date
        if (dateCommandePicker.getValue() == null) {
            afficherAlert("Validation", "Veuillez sélectionner une date", Alert.AlertType.WARNING);
            return false;
        }

        // ✅ Validation du montant
        String montantText = montantTotalField.getText().trim();
        if (montantText.isEmpty()) {
            afficherAlert("Validation", "Veuillez entrer le montant total!", Alert.AlertType.WARNING);
            return false;
        }

        try {
            double montant = Double.parseDouble(montantText);
            if (montant <= 0) {
                afficherAlert("Validation", "Le montant doit être supérieur à 0", Alert.AlertType.WARNING);
                return false;
            }
            if (montant > 999999.99) {
                afficherAlert("Validation", "Le montant ne peut pas dépasser 999 999.99 TND", Alert.AlertType.WARNING);
                return false;
            }
        } catch (NumberFormatException e) {
            afficherAlert("Validation", "Le montant doit être un nombre valide (Ex: 450.00, 1250.50)", Alert.AlertType.WARNING);
            return false;
        }

        // ✅ Validation de l'adresse
        String adresse = adresseLivraisonField.getText().trim();
        if (adresse.isEmpty()) {
            afficherAlert("Validation", "Veuillez entrer l'adresse de livraison", Alert.AlertType.WARNING);
            return false;
        }

        if (adresse.length() < 5 || adresse.length() > 200) {
            afficherAlert("Validation", "L'adresse doit contenir entre 5 et 200 caractères", Alert.AlertType.WARNING);
            return false;
        }

        return true;
    }

    private void afficherAlert(String titre, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}