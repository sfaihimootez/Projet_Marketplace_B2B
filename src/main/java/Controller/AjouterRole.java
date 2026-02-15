package Controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.*;
import models.Role;
import services.RoleService;

public class AjouterRole {

    @FXML private TextField nomRoleField;
    @FXML private TableView<Role> roleTable;
    @FXML private TableColumn<Role, Integer> colId;
    @FXML private TableColumn<Role, String> colNom;

    private RoleService service = new RoleService();

   /* @FXML
    public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id_role"));
        colNom.setCellValueFactory(new PropertyValueFactory<>("nomRole"));
        loadRoles();
    }*/

    @FXML
    public void ajouterRole() {
        Role r = new Role();
        r.setNomRole(nomRoleField.getText());
        service.add(r);
        loadRoles();
        nomRoleField.clear();
    }

    private void loadRoles() {
        roleTable.setItems(FXCollections.observableArrayList(service.getAll()));
    }
}
