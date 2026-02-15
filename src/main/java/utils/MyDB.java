package com.example.gestion_commande.Utils;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MyDB {

    // Paramètres de connexion
    private static final String DB_DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String DB_URL = "jdbc:mysql://localhost:3306/markethub_db";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";

    private static Connection connection;

    /**
     * Établir la connexion à la base de données
     * @return Connection objet de connexion
     */
    public static Connection getConnection() {
        try {
            // Charger le driver JDBC
            Class.forName(DB_DRIVER);

            // Créer la connexion
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                System.out.println("✓ Connexion à la base de données réussie!");
            }
        } catch (ClassNotFoundException e) {
            System.err.println("Erreur: Driver MySQL non trouvé!");
            System.err.println("Assurez-vous d'avoir ajouté la dépendance mysql-connector-java au projet.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("Erreur: Impossible de se connecter à la base de données!");
            System.err.println("Vérifiez les paramètres de connexion (URL, utilisateur, mot de passe)");
            e.printStackTrace();
        }

        return connection;
    }

    /**
     * Fermer la connexion à la base de données
     */
    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("✓ Connexion fermée!");
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la fermeture de la connexion!");
            e.printStackTrace();
        }
    }

    /**
     * Vérifier si la connexion est active
     * @return true si la connexion est active, false sinon
     */
    public static boolean isConnected() {
        try {
            return connection != null && !connection.isClosed();
        } catch (SQLException e) {
            return false;
        }
    }

    /**
     * Tester la connexion à la base de données
     */
    public static void testConnection() {
        try {
            Connection conn = getConnection();
            if (conn != null && !conn.isClosed()) {
                System.out.println("═══════════════════════════════════════");
                System.out.println("  TEST DE CONNEXION RÉUSSI");
                System.out.println("═══════════════════════════════════════");
                System.out.println("Database: markethub_db");
                System.out.println("URL: " + DB_URL);
                System.out.println("Utilisateur: " + DB_USER);
                System.out.println("═══════════════════════════════════════");
            } else {
                System.out.println("✗ La connexion n'a pas pu être établie!");
            }
        } catch (Exception e) {
            System.out.println("✗ Erreur lors du test de connexion!");
            e.printStackTrace();
        }
    }
}