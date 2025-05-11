package view;

import dao.UtilisateurDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.Utilisateur;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;


import java.sql.SQLException;
import java.util.List;

public class ClientController {

    @FXML
    private TableView<Utilisateur> tableUtilisateurs;
    @FXML
    private TableColumn<Utilisateur, String> colNom;
    @FXML
    private TableColumn<Utilisateur, String> colPrenom;
    @FXML
    private TableColumn<Utilisateur, String> colEmail;
    @FXML
    private TableColumn<Utilisateur, String> colTypeClient;
    @FXML
    private TableColumn<Utilisateur, String> colRole;

    @FXML
    public void initialize() {
        colNom.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getNom()));
        colPrenom.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getPrenom()));
        colEmail.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getEmail()));
        colTypeClient.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getTypeClient()));
        colRole.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getRole()));

        try {
            UtilisateurDAO dao = new UtilisateurDAO();
            List<Utilisateur> utilisateurs = dao.getAllUtilisateurs();
            ObservableList<Utilisateur> observableList = FXCollections.observableArrayList(utilisateurs);
            tableUtilisateurs.setItems(observableList);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleFermer() {
        Stage stage = (Stage) tableUtilisateurs.getScene().getWindow();
        stage.close();
    }
    @FXML
    private void handleSupprimer() {
        Utilisateur selection = tableUtilisateurs.getSelectionModel().getSelectedItem();
        if (selection != null) {
            try {
                UtilisateurDAO dao = new UtilisateurDAO();
                dao.deleteUtilisateur(selection.getId());
                tableUtilisateurs.getItems().remove(selection);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            showAlert("Sélection requise", "Veuillez sélectionner un utilisateur à supprimer.");
        }
    }

    private void showAlert(String titre, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    @FXML
    private void handleModifierUtilisateur() {
        Utilisateur selection = tableUtilisateurs.getSelectionModel().getSelectedItem();
        if (selection != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/ModifierUtilisateurView.fxml"));
                Parent root = loader.load();

                ModifierUtilisateurController controller = loader.getController();
                controller.setUtilisateur(selection);

                Stage stage = new Stage();
                stage.setTitle("Modifier Utilisateur");
                stage.setScene(new Scene(root));
                stage.showAndWait();

                // Recharger les utilisateurs après modification
                UtilisateurDAO dao = new UtilisateurDAO();
                List<Utilisateur> utilisateurs = dao.getAllUtilisateurs();
                tableUtilisateurs.setItems(FXCollections.observableArrayList(utilisateurs));

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            showAlert("Sélection requise", "Veuillez sélectionner un utilisateur à modifier.");
        }
    }

}
