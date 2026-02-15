package Controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import models.Role;
import models.User;
import services.RoleService;
import services.UserService;

import java.io.IOException;
import java.util.regex.Pattern;

public class AjouterUser {

    @FXML
    private TextField nameField;

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private TextField visiblePasswordField;

    @FXML
    private Button togglePasswordButton;

    @FXML
    private ChoiceBox<Role> roleChoice;

    private UserService userService = new UserService();
    private RoleService roleService = new RoleService();
    private boolean isPasswordVisible = false;

    // Pattern pour validation email
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
    );

    // Initialisation du ChoiceBox avec les rôles de la base de données
    @FXML
    public void initialize() {
        // Charger tous les rôles depuis la base de données
        roleChoice.getItems().addAll(roleService.getAll());

        // Définir le premier rôle comme valeur par défaut s'il existe
        if (!roleChoice.getItems().isEmpty()) {
            roleChoice.setValue(roleChoice.getItems().get(0));
        }

        // Synchroniser les champs de mot de passe
        setupPasswordSync();

        // Ajouter des validations en temps réel
        setupRealtimeValidation();
    }

    // Synchroniser le PasswordField et le TextField
    private void setupPasswordSync() {
        // Quand on tape dans le PasswordField, mettre à jour le TextField
        passwordField.textProperty().addListener((observable, oldValue, newValue) -> {
            visiblePasswordField.setText(newValue);
        });

        // Quand on tape dans le TextField visible, mettre à jour le PasswordField
        visiblePasswordField.textProperty().addListener((observable, oldValue, newValue) -> {
            passwordField.setText(newValue);
        });
    }

    // Basculer la visibilité du mot de passe
    @FXML
    private void togglePasswordVisibility() {
        isPasswordVisible = !isPasswordVisible;

        if (isPasswordVisible) {
            // Afficher le mot de passe en clair
            visiblePasswordField.setVisible(true);
            visiblePasswordField.setManaged(true);
            passwordField.setVisible(false);
            passwordField.setManaged(false);
            togglePasswordButton.setText("🙈"); // Icône "cacher"
        } else {
            // Masquer le mot de passe
            passwordField.setVisible(true);
            passwordField.setManaged(true);
            visiblePasswordField.setVisible(false);
            visiblePasswordField.setManaged(false);
            togglePasswordButton.setText("👁️"); // Icône "voir"
        }
    }

    // Configuration de la validation en temps réel
    private void setupRealtimeValidation() {
        // Validation du nom
        nameField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.isEmpty()) {
                nameField.setStyle("-fx-border-color: #27ae60; -fx-border-width: 2px;");
            } else {
                nameField.setStyle("");
            }
        });

        // Validation de l'email
        emailField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (isValidEmail(newValue)) {
                emailField.setStyle("-fx-border-color: #27ae60; -fx-border-width: 2px;");
            } else if (!newValue.isEmpty()) {
                emailField.setStyle("-fx-border-color: #e74c3c; -fx-border-width: 2px;");
            } else {
                emailField.setStyle("");
            }
        });

        // Validation du mot de passe (sur les deux champs)
        passwordField.textProperty().addListener((observable, oldValue, newValue) -> {
            String style;
            if (newValue.length() >= 6) {
                style = "-fx-border-color: #27ae60; -fx-border-width: 2px; -fx-background-color: #f8f9fa; -fx-background-radius: 8px; -fx-border-radius: 8px;";
            } else if (!newValue.isEmpty()) {
                style = "-fx-border-color: #e74c3c; -fx-border-width: 2px; -fx-background-color: #f8f9fa; -fx-background-radius: 8px; -fx-border-radius: 8px;";
            } else {
                style = "";
            }
            passwordField.setStyle(style);
            visiblePasswordField.setStyle(style);
        });
    }

    // Gestion du bouton Ajouter avec validation complète
    @FXML
    private void handleAddUser() {
        // Réinitialiser les styles d'erreur
        resetFieldStyles();

        // Validation des champs
        StringBuilder errors = new StringBuilder();
        boolean hasError = false;

        // 1. Validation du nom
        if (nameField.getText().trim().isEmpty()) {
            nameField.setStyle("-fx-border-color: #e74c3c; -fx-border-width: 2px;");
            errors.append("• Le nom est obligatoire\n");
            hasError = true;
        } else if (nameField.getText().trim().length() < 3) {
            nameField.setStyle("-fx-border-color: #e74c3c; -fx-border-width: 2px;");
            errors.append("• Le nom doit contenir au moins 3 caractères\n");
            hasError = true;
        }

        // 2. Validation de l'email
        if (emailField.getText().trim().isEmpty()) {
            emailField.setStyle("-fx-border-color: #e74c3c; -fx-border-width: 2px;");
            errors.append("• L'email est obligatoire\n");
            hasError = true;
        } else if (!isValidEmail(emailField.getText().trim())) {
            emailField.setStyle("-fx-border-color: #e74c3c; -fx-border-width: 2px;");
            errors.append("• L'email n'est pas valide (ex: user@example.com)\n");
            hasError = true;
        } else if (emailExists(emailField.getText().trim())) {
            emailField.setStyle("-fx-border-color: #e74c3c; -fx-border-width: 2px;");
            errors.append("• Cet email existe déjà\n");
            hasError = true;
        }

        // 3. Validation du mot de passe
        if (passwordField.getText().isEmpty()) {
            passwordField.setStyle("-fx-border-color: #e74c3c; -fx-border-width: 2px;");
            visiblePasswordField.setStyle("-fx-border-color: #e74c3c; -fx-border-width: 2px;");
            errors.append("• Le mot de passe est obligatoire\n");
            hasError = true;
        } else if (passwordField.getText().length() < 6) {
            passwordField.setStyle("-fx-border-color: #e74c3c; -fx-border-width: 2px;");
            visiblePasswordField.setStyle("-fx-border-color: #e74c3c; -fx-border-width: 2px;");
            errors.append("• Le mot de passe doit contenir au moins 6 caractères\n");
            hasError = true;
        }

        // 4. Validation du rôle
        if (roleChoice.getValue() == null) {
            errors.append("• Veuillez sélectionner un rôle\n");
            hasError = true;
        }

        // Afficher les erreurs si nécessaire
        if (hasError) {
            showAlert(Alert.AlertType.ERROR, "Erreur de validation", errors.toString());
            return;
        }

        try {
            // Créer un nouvel utilisateur
            User newUser = new User();
            newUser.setNom(nameField.getText().trim());
            newUser.setEmail(emailField.getText().trim().toLowerCase());
            newUser.setMotDePasse(passwordField.getText());
            newUser.setRole(roleChoice.getValue());

            // Ajouter l'utilisateur à la base de données
            userService.add(newUser);

            // Afficher un message de succès
            showAlert(Alert.AlertType.INFORMATION, "Succès",
                    "✅ Utilisateur '" + newUser.getNom() + "' ajouté avec succès !");

            // Réinitialiser les champs
            clearFields();

        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erreur",
                    "❌ Erreur lors de l'ajout : " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Méthode pour afficher la liste des utilisateurs
    @FXML
    private void handleShowUserList() {
        try {
            // Charger la page de liste des utilisateurs
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/UserTable.fxml"));
            Parent root = loader.load();

            // Obtenir la scène actuelle
            Scene scene = nameField.getScene();

            // Changer la vue de la scène actuelle
            scene.setRoot(root);

        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur",
                    "Impossible d'ouvrir la liste des utilisateurs : " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Validation de l'email avec regex
    private boolean isValidEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        return EMAIL_PATTERN.matcher(email.trim()).matches();
    }

    // Vérifier si l'email existe déjà
    private boolean emailExists(String email) {
        try {
            return userService.getAll().stream()
                    .anyMatch(u -> u.getEmail().equalsIgnoreCase(email.trim()));
        } catch (Exception e) {
            return false;
        }
    }

    // Réinitialiser les styles des champs
    private void resetFieldStyles() {
        nameField.setStyle("");
        emailField.setStyle("");
        passwordField.setStyle("");
        visiblePasswordField.setStyle("");
    }

    // Méthode utilitaire pour réinitialiser les champs
    private void clearFields() {
        nameField.clear();
        emailField.clear();
        passwordField.clear();
        visiblePasswordField.clear();
        resetFieldStyles();
        isPasswordVisible = false;
        passwordField.setVisible(true);
        passwordField.setManaged(true);
        visiblePasswordField.setVisible(false);
        visiblePasswordField.setManaged(false);
        togglePasswordButton.setText("👁️");

        if (!roleChoice.getItems().isEmpty()) {
            roleChoice.setValue(roleChoice.getItems().get(0));
        }
    }

    // Méthode utilitaire pour afficher des alertes
    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);

        // Style personnalisé pour les alertes
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.setStyle("-fx-font-family: 'Arial'; -fx-font-size: 14px;");

        alert.showAndWait();
    }
}
