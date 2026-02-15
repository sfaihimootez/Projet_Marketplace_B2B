package Controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import models.Role;
import models.User;
import services.UserService;

public class AfficherUser {

    @FXML
    private TableView<User> userTable;

    @FXML
    private TableColumn<User, Integer> colId;

    @FXML
    private TableColumn<User, String> colNom;

    @FXML
    private TableColumn<User, String> colEmail;

    @FXML
    private TableColumn<User, String> colMotDePasse;

    @FXML
    private TableColumn<User, String> colRole;

    @FXML
    private TextField searchField;

    @FXML
    private Label countLabel;

    private ObservableList<User> users = FXCollections.observableArrayList();
    private ObservableList<User> filteredUsers = FXCollections.observableArrayList();
    private UserService userService = new UserService();

    @FXML
    public void initialize() {
        // Lier les colonnes aux propriétés de User
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));

        // Colonne mot de passe avec bouton voir/cacher
        colMotDePasse.setCellFactory(column -> new TableCell<User, String>() {
            private final Label passwordLabel = new Label();
            private final Button toggleButton = new Button("👁️");
            private boolean isVisible = false;

            {
                passwordLabel.setStyle("-fx-font-family: monospace;");
                toggleButton.setStyle(
                        "-fx-background-color: transparent; " +
                                "-fx-cursor: hand; " +
                                "-fx-font-size: 14px; " +
                                "-fx-padding: 2 5 2 5;"
                );

                toggleButton.setOnAction(event -> {
                    isVisible = !isVisible;
                    User user = getTableView().getItems().get(getIndex());
                    if (isVisible) {
                        passwordLabel.setText(user.getMotDePasse());
                        toggleButton.setText("🙈");
                    } else {
                        passwordLabel.setText("••••••••");
                        toggleButton.setText("👁️");
                    }
                });

                HBox hbox = new HBox(5, passwordLabel, toggleButton);
                hbox.setStyle("-fx-alignment: center-left;");
                setGraphic(hbox);
            }

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || getIndex() < 0 || getIndex() >= getTableView().getItems().size()) {
                    setGraphic(null);
                } else {
                    User user = getTableView().getItems().get(getIndex());
                    isVisible = false;
                    passwordLabel.setText("••••••••");
                    toggleButton.setText("👁️");
                    setGraphic(getGraphic());
                }
            }
        });

        // Pour afficher le nom du rôle
        colRole.setCellValueFactory(cellData -> {
            Role role = cellData.getValue().getRole();
            return new javafx.beans.property.SimpleStringProperty(
                    role != null ? role.getNomRole() : ""
            );
        });

        // Style des colonnes
        colId.setStyle("-fx-alignment: CENTER;");
        colRole.setStyle("-fx-alignment: CENTER;");

        // Charger les utilisateurs depuis la base de données
        loadUsers();

        // Lier la TableView à la liste
        userTable.setItems(filteredUsers);

        // Configuration de la recherche en temps réel
        if (searchField != null) {
            searchField.textProperty().addListener((observable, oldValue, newValue) -> {
                filterUsers(newValue);
            });
        }

        // Style de sélection
        userTable.setRowFactory(tv -> {
            TableRow<User> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && !row.isEmpty()) {
                    User user = row.getItem();
                    showUserDetails(user);
                }
            });
            return row;
        });
    }

    // Méthode pour charger les utilisateurs depuis la base de données
    private void loadUsers() {
        try {
            users.clear();
            users.addAll(userService.getAll());
            filteredUsers.clear();
            filteredUsers.addAll(users);
            updateCountLabel();
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erreur",
                    "Erreur lors du chargement des utilisateurs : " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Filtrer les utilisateurs selon la recherche
    private void filterUsers(String searchText) {
        if (searchText == null || searchText.trim().isEmpty()) {
            filteredUsers.setAll(users);
        } else {
            String lowerSearch = searchText.toLowerCase().trim();
            filteredUsers.clear();

            for (User user : users) {
                if (user.getNom().toLowerCase().contains(lowerSearch) ||
                        user.getEmail().toLowerCase().contains(lowerSearch) ||
                        (user.getRole() != null && user.getRole().getNomRole().toLowerCase().contains(lowerSearch))) {
                    filteredUsers.add(user);
                }
            }
        }
        updateCountLabel();
    }

    // Mettre à jour le compteur
    private void updateCountLabel() {
        if (countLabel != null) {
            countLabel.setText(filteredUsers.size() + " utilisateur(s)");
        }
    }

    // Afficher les détails d'un utilisateur
    private void showUserDetails(User user) {
        String details = String.format(
                "📋 Détails de l'utilisateur\n\n" +
                        "ID: %d\n" +
                        "Nom: %s\n" +
                        "Email: %s\n" +
                        "Mot de passe: %s\n" +
                        "Rôle: %s",
                user.getId(),
                user.getNom(),
                user.getEmail(),
                "••••••••",
                user.getRole() != null ? user.getRole().getNomRole() : "N/A"
        );

        showAlert(Alert.AlertType.INFORMATION, "Informations", details);
    }

    // Méthode pour rafraîchir la liste
    @FXML
    private void handleRefresh() {
        loadUsers();
        if (searchField != null) {
            searchField.clear();
        }
        showAlert(Alert.AlertType.INFORMATION, "Rafraîchissement",
                "✅ Liste mise à jour avec succès !");
    }

    // Méthode pour supprimer l'utilisateur sélectionné
    @FXML
    private void handleDelete() {
        User selectedUser = userTable.getSelectionModel().getSelectedItem();

        if (selectedUser == null) {
            showAlert(Alert.AlertType.WARNING, "Aucune sélection",
                    "⚠️ Veuillez sélectionner un utilisateur à supprimer.");
            return;
        }

        // Confirmation de suppression
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Confirmation");
        confirmation.setHeaderText("Supprimer l'utilisateur");
        confirmation.setContentText("Êtes-vous sûr de vouloir supprimer '" +
                selectedUser.getNom() + "' ?");

        if (confirmation.showAndWait().get() == ButtonType.OK) {
            try {
                userService.delete(selectedUser);
                users.remove(selectedUser);
                filteredUsers.remove(selectedUser);
                updateCountLabel();
                showAlert(Alert.AlertType.INFORMATION, "Succès",
                        "✅ Utilisateur supprimé avec succès !");
            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, "Erreur",
                        "❌ Erreur lors de la suppression : " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    // Méthode pour modifier l'utilisateur sélectionné
    @FXML
    private void handleModify() {
        User selectedUser = userTable.getSelectionModel().getSelectedItem();

        if (selectedUser == null) {
            showAlert(Alert.AlertType.WARNING, "Aucune sélection",
                    "⚠️ Veuillez sélectionner un utilisateur à modifier.");
            return;
        }

        // Créer une boîte de dialogue personnalisée pour la modification
        Dialog<User> dialog = new Dialog<>();
        dialog.setTitle("Modifier l'utilisateur");
        dialog.setHeaderText("Modifier les informations de : " + selectedUser.getNom());

        // Configurer les boutons
        ButtonType saveButtonType = new ButtonType("💾 Enregistrer", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        // Créer les champs de formulaire
        javafx.scene.layout.GridPane grid = new javafx.scene.layout.GridPane();
        grid.setHgap(10);
        grid.setVgap(15);
        grid.setPadding(new javafx.geometry.Insets(20, 150, 10, 10));

        TextField nameField = new TextField(selectedUser.getNom());
        TextField emailField = new TextField(selectedUser.getEmail());

        // Champ mot de passe avec bouton voir/cacher - PRÉ-REMPLI avec mot de passe actuel
        PasswordField passwordField = new PasswordField();
        TextField visiblePasswordField = new TextField();

        // Pré-remplir avec le mot de passe actuel
        passwordField.setText(selectedUser.getMotDePasse());
        visiblePasswordField.setText(selectedUser.getMotDePasse());

        passwordField.setPromptText("Mot de passe");
        visiblePasswordField.setPromptText("Mot de passe");
        visiblePasswordField.setVisible(false);
        visiblePasswordField.setManaged(false);

        // Synchroniser les deux champs
        passwordField.textProperty().addListener((obs, old, val) -> visiblePasswordField.setText(val));
        visiblePasswordField.textProperty().addListener((obs, old, val) -> passwordField.setText(val));

        // StackPane pour superposer les champs
        javafx.scene.layout.StackPane passwordStack = new javafx.scene.layout.StackPane();

        // Bouton toggle
        Button toggleButton = new Button("👁️");
        toggleButton.setStyle("-fx-background-color: transparent; -fx-cursor: hand; -fx-font-size: 16px;");
        final boolean[] isPasswordVisible = {false};

        toggleButton.setOnAction(e -> {
            isPasswordVisible[0] = !isPasswordVisible[0];
            if (isPasswordVisible[0]) {
                visiblePasswordField.setVisible(true);
                visiblePasswordField.setManaged(true);
                passwordField.setVisible(false);
                passwordField.setManaged(false);
                toggleButton.setText("🙈");
            } else {
                passwordField.setVisible(true);
                passwordField.setManaged(true);
                visiblePasswordField.setVisible(false);
                visiblePasswordField.setManaged(false);
                toggleButton.setText("👁️");
            }
        });

        HBox passwordBox = new HBox(5);
        passwordStack.getChildren().addAll(passwordField, visiblePasswordField);
        passwordBox.getChildren().addAll(passwordStack, toggleButton);
        HBox.setHgrow(passwordStack, Priority.ALWAYS);

        ChoiceBox<models.Role> roleChoice = new ChoiceBox<>();
        try {
            services.RoleService roleService = new services.RoleService();
            roleChoice.getItems().addAll(roleService.getAll());
            roleChoice.setValue(selectedUser.getRole());
        } catch (Exception e) {
            e.printStackTrace();
        }

        grid.add(new Label("👤 Nom:"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label("📧 Email:"), 0, 1);
        grid.add(emailField, 1, 1);
        grid.add(new Label("🔒 Mot de passe:"), 0, 2);
        grid.add(passwordBox, 1, 2);
        grid.add(new Label("⭐ Rôle:"), 0, 3);
        grid.add(roleChoice, 1, 3);

        dialog.getDialogPane().setContent(grid);

        // Convertir le résultat quand le bouton Enregistrer est cliqué
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                selectedUser.setNom(nameField.getText().trim());
                selectedUser.setEmail(emailField.getText().trim().toLowerCase());

                // Mettre à jour le mot de passe (maintenant toujours présent car pré-rempli)
                String newPassword = passwordField.isVisible() ?
                        passwordField.getText() : visiblePasswordField.getText();
                selectedUser.setMotDePasse(newPassword);

                selectedUser.setRole(roleChoice.getValue());
                return selectedUser;
            }
            return null;
        });

        // Afficher la boîte de dialogue et traiter le résultat
        dialog.showAndWait().ifPresent(user -> {
            // Validation
            if (user.getNom().isEmpty() || user.getEmail().isEmpty() ||
                    user.getMotDePasse().isEmpty() || user.getRole() == null) {
                showAlert(Alert.AlertType.ERROR, "Erreur de validation",
                        "Tous les champs sont obligatoires.");
                return;
            }

            // Validation de l'email
            if (!user.getEmail().matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
                showAlert(Alert.AlertType.ERROR, "Erreur de validation",
                        "Le format de l'email n'est pas valide.");
                return;
            }

            try {
                // Mettre à jour dans la base de données
                userService.update(user);

                // Rafraîchir l'affichage
                userTable.refresh();

                showAlert(Alert.AlertType.INFORMATION, "Succès",
                        "✅ Utilisateur modifié avec succès !");
            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, "Erreur",
                        "❌ Erreur lors de la modification : " + e.getMessage());
                e.printStackTrace();
            }
        });
    }

    // Méthode pour retourner à la page d'ajout d'utilisateur
    @FXML
    private void handleBack() {
        try {
            // Charger la page d'ajout d'utilisateur
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(
                    getClass().getResource("/user.fxml")
            );
            javafx.scene.Parent root = loader.load();

            // Obtenir la scène actuelle
            javafx.scene.Scene scene = userTable.getScene();

            // Changer la vue de la scène actuelle
            scene.setRoot(root);

        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erreur",
                    "Impossible de retourner à la page d'ajout : " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Méthode utilitaire pour afficher des alertes
    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);

        // Style personnalisé
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.setStyle("-fx-font-family: 'Arial'; -fx-font-size: 14px;");

        alert.showAndWait();
    }
}
