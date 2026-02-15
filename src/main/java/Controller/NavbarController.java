package com.example.gestion_commande.Controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import java.io.IOException;

public class NavbarController {

    @FXML private HBox navbar;
    @FXML private Button btnAccueil;
    @FXML private Button btnAjouterCommande;
    @FXML private Button btnAfficherCommandes;
    @FXML private Button btnAvecLivraison;

    @FXML
    public void initialize() {
        // Initialisation
    }

    @FXML
    private void naviguerVersAccueil() {
        naviguerVers("/com/example/gestion_commande/AjouterCommandeView.fxml", "Ajouter Commande");
    }

    @FXML
    private void naviguerVersAjouterCommande() {
        naviguerVers("/com/example/gestion_commande/AjouterCommandeView.fxml", "Ajouter Commande");
    }

    @FXML
    private void naviguerVersAfficherCommandes() {
        naviguerVers("/com/example/gestion_commande/AfficherCommandesView.fxml", "Mes Commandes");
    }

    @FXML
    private void naviguerVersAvecLivraison() {
        naviguerVers("/com/example/gestion_commande/AfficherCommandesAvecLivraisonView.fxml", "Commandes avec Livraison");
    }

    private void naviguerVers(String fxmlPath, String titre) {
        try {
            Stage stage = (Stage) navbar.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            stage.setScene(new Scene(root));
            stage.setTitle(titre);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setActiveButton(String pageName) {
        resetButtonStyles();

        String activeStyle = "-fx-background-color: #FF9800; -fx-text-fill: white; -fx-font-size: 14.0; -fx-font-weight: bold; -fx-cursor: hand; -fx-background-radius: 5;";

        switch (pageName) {
            case "accueil":
            case "ajouter":
                btnAjouterCommande.setStyle(activeStyle);
                break;
            case "afficher":
                btnAfficherCommandes.setStyle(activeStyle);
                break;
            case "livraison":
                btnAvecLivraison.setStyle(activeStyle);
                break;
        }
    }

    private void resetButtonStyles() {
        String defaultStyle = "-fx-background-color: transparent; -fx-text-fill: white; -fx-font-size: 14.0; -fx-cursor: hand;";
        btnAccueil.setStyle(defaultStyle);
        btnAjouterCommande.setStyle(defaultStyle);
        btnAfficherCommandes.setStyle(defaultStyle);
        btnAvecLivraison.setStyle(defaultStyle);
    }
}