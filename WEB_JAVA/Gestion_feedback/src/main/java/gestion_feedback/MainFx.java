package gestion_feedback;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import java.io.IOException;

public class MainFx extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {
        // Charge la page principale (liste des feedbacks)
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/AfficherFeedbacks.fxml"));
        Scene scene = new Scene(loader.load(), 1200, 800);  // Taille optimisée pour le nouveau design

        // Le fond est déjà géré dans le FXML avec le dégradé moderne
        // Pas besoin de le redéfinir ici

        // Configuration de la fenêtre principale
        primaryStage.setTitle("MarketHub - Gestion des Feedbacks");
        primaryStage.setScene(scene);
        primaryStage.setResizable(true);
        primaryStage.setMinWidth(900);   // Largeur minimale pour un bon affichage
        primaryStage.setMinHeight(650);  // Hauteur minimale pour un bon affichage

        // Optionnel : Icône de l'application (décommentez si vous avez une icône)
        // primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/images/icon.png")));

        // Maximiser la fenêtre au démarrage pour une meilleure expérience (optionnel)
        // primaryStage.setMaximized(true);

        primaryStage.show();

        // Message de bienvenue dans la console
        System.out.println("╔════════════════════════════════════════╗");
        System.out.println("║   MarketHub - Gestion des Feedbacks   ║");
        System.out.println("║         Application démarrée ✓         ║");
        System.out.println("╚════════════════════════════════════════╝");
    }

    @Override
    public void stop() {
        // Nettoyage avant la fermeture de l'application
        System.out.println("Application fermée. À bientôt !");
    }

    public static void main(String[] args) {
        launch(args);
    }
}