package com.example.gestion_commande.Controllers;

import com.example.gestion_commande.Entities.Livraison;
import com.example.gestion_commande.Entities.Commande;
import com.example.gestion_commande.Services.LivraisonService;
import com.example.gestion_commande.Services.CommandeService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.io.IOException;
import java.time.LocalDate;

public class AjouterLivraisonController {

    @FXML private TextField idCommandeField;
    @FXML private TextField numeroBLField;
    @FXML private DatePicker dateLivraisonPicker;
    @FXML private TextField livreurField;
    @FXML private ComboBox<String> statutLivraisonComboBox;
    @FXML private TextArea noteDeliveryArea;
    @FXML private Parent navbar;

    private LivraisonService livraisonService;
    private CommandeService commandeService;
    private int idCommande;

    @FXML
    public void initialize() {
        livraisonService = new LivraisonService();
        commandeService = new CommandeService();

        statutLivraisonComboBox.getItems().addAll("En cours", "Livré", "Retardé");
        statutLivraisonComboBox.setValue("En cours");
        dateLivraisonPicker.setValue(LocalDate.now());

        if (navbar != null) {
            NavbarController navbarController = (NavbarController) navbar.getProperties().get("controller");
            if (navbarController != null) {
                navbarController.setActiveButton("livraison");
            }
        }
    }

    public void setIdCommande(int idCommande) {
        this.idCommande = idCommande;
        idCommandeField.setText(String.valueOf(idCommande));
    }

    @FXML
    private void ajouterLivraison() {
        if (!validerFormulaire()) {
            return;
        }

        try {
            Livraison livraisonExistante = livraisonService.rechercherParIdCommande(idCommande);

            if (livraisonExistante != null) {
                afficherAlert(
                        "⚠️ Livraison Existante",
                        "Cette commande a déjà une livraison associée.\n\n" +
                                "Numéro BL: " + livraisonExistante.getNumeroBL() + "\n" +
                                "Livreur: " + livraisonExistante.getLivreur() + "\n" +
                                "Statut: " + livraisonExistante.getStatutLivraison() + "\n\n" +
                                "Veuillez consulter la page 'AVEC LIVRAISON' pour modifier cette livraison.",
                        Alert.AlertType.WARNING
                );
                return;
            }

            String numeroBL = numeroBLField.getText().trim();
            LocalDate dateLivraison = dateLivraisonPicker.getValue();
            String livreur = livreurField.getText().trim();
            String statutLivraison = statutLivraisonComboBox.getValue();
            String noteDelivery = noteDeliveryArea.getText().trim();

            Livraison livraison = new Livraison(idCommande, numeroBL, dateLivraison, livreur,
                    statutLivraison, noteDelivery);

            if (livraisonService.ajouter(livraison)) {
                changerStatutCommande();
                afficherAlert("Succès", "Livraison ajoutée avec succès!", Alert.AlertType.INFORMATION);
                afficherAlert("Info", "Le statut de la commande a été changé à 'Confirmée'", Alert.AlertType.INFORMATION);
                retourner();
            } else {
                afficherAlert("Erreur", "Erreur lors de l'ajout de la livraison!", Alert.AlertType.ERROR);
            }
        } catch (Exception e) {
            afficherAlert("Erreur", "Une erreur s'est produite!", Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    private void changerStatutCommande() {
        try {
            Commande commande = commandeService.rechercherParId(idCommande);

            if (commande != null) {
                commande.setStatut("Confirmée");
                if (commandeService.modifier(commande)) {
                    System.out.println("✅ Statut de la commande changé à 'Confirmée'");
                } else {
                    System.err.println("❌ Erreur lors de la modification du statut");
                }
            }
        } catch (Exception e) {
            System.err.println("❌ Erreur lors du changement de statut");
            e.printStackTrace();
        }
    }

    @FXML
    private void retourner() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/gestion_commande/AfficherCommandesView.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) idCommandeField.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Afficher Commandes");
            stage.show();
        } catch (IOException e) {
            afficherAlert("Erreur", "Impossible de charger la page!", Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    @FXML
    private void effacer() {
        numeroBLField.clear();
        livreurField.clear();
        noteDeliveryArea.clear();
        dateLivraisonPicker.setValue(LocalDate.now());
        statutLivraisonComboBox.setValue("En cours");
    }

    private boolean validerFormulaire() {
        String numeroBL = numeroBLField.getText().trim();
        if (numeroBL.isEmpty()) {
            afficherAlert("Validation", "Veuillez entrer le numéro BL!", Alert.AlertType.WARNING);
            return false;
        }

        if (!numeroBL.matches("^[0-9]+$")) {
            afficherAlert("Validation", "Le numéro BL doit contenir uniquement des chiffres!\n(Ex: 001, 123, 2024)", Alert.AlertType.WARNING);
            return false;
        }

        if (dateLivraisonPicker.getValue() == null) {
            afficherAlert("Validation", "Veuillez sélectionner une date!", Alert.AlertType.WARNING);
            return false;
        }

        String livreur = livreurField.getText().trim();
        if (livreur.isEmpty()) {
            afficherAlert("Validation", "Veuillez entrer le nom du livreur!", Alert.AlertType.WARNING);
            return false;
        }

        if (!livreur.matches("^[a-zA-Z\\s'-]+$")) {
            afficherAlert("Validation", "Le nom du livreur doit contenir uniquement des lettres!\n(Ex: Ahmed, Jean-Marie, O'Brien)", Alert.AlertType.WARNING);
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