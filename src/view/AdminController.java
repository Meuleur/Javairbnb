package view;

import dao.HebergementDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Hebergement;

import java.io.IOException;

public class AdminController {

    @FXML
    private TableView<Hebergement> tableHebergements;
    @FXML
    private TableColumn<Hebergement, String> colNom;
    @FXML
    private TableColumn<Hebergement, String> colAdresse;
    @FXML
    private TableColumn<Hebergement, Double> colPrix;
    @FXML
    private TableColumn<Hebergement, String> colType;
    @FXML
    private ObservableList<Hebergement> hebergements;
    @FXML
    private TableColumn<Hebergement, String> colDescription;
    @FXML
    private TableColumn<Hebergement, String> colValide;




    @FXML
    public void initialize() {
        colNom.setCellValueFactory(data -> data.getValue().nomProperty());
        colAdresse.setCellValueFactory(data -> data.getValue().adresseProperty());
        colPrix.setCellValueFactory(data -> data.getValue().prixProperty().asObject());
        colType.setCellValueFactory(data -> data.getValue().typeProperty());
        colDescription.setCellValueFactory(data -> data.getValue().descriptionProperty());

        // Amélioration de l'affichage du statut
        colValide.setCellValueFactory(data -> {
            boolean isActive = data.getValue().isValide();
            String displayValue = isActive ? "Actif" : "Inactif";
            return new javafx.beans.property.SimpleStringProperty(displayValue);
        });

        hebergements = FXCollections.observableArrayList(HebergementDAO.getTousLesHebergements());
        tableHebergements.setItems(hebergements);
    }



    @FXML
    private void handleAjouter() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/AjouterHebergementView.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Ajouter un hébergement");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

            // Recharger la liste après ajout
            hebergements.setAll(HebergementDAO.getTousLesHebergements());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleModifier() {
        Hebergement selection = tableHebergements.getSelectionModel().getSelectedItem();
        if (selection != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/ModifierHebergementView.fxml"));
                Parent root = loader.load();

                ModifierHebergementController controller = loader.getController();
                controller.setHebergement(selection);

                Stage stage = new Stage();
                stage.setTitle("Modifier un hébergement");
                stage.setScene(new Scene(root));
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.showAndWait();

                // Recharger la liste après modification
                hebergements.setAll(HebergementDAO.getTousLesHebergements());

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            showAlert("Aucun hébergement sélectionné pour modification.");
        }
    }

    @FXML
    private void handleSupprimer() {
        Hebergement selection = tableHebergements.getSelectionModel().getSelectedItem();
        if (selection != null) {
            boolean confirm = showConfirmation("Supprimer cet hébergement ?");
            if (confirm) {
                boolean success = HebergementDAO.supprimerHebergement(selection.getId());
                if (success) {
                    hebergements.remove(selection);
                } else {
                    showAlert("Échec de la suppression.");
                }
            }
        } else {
            showAlert("Aucun hébergement sélectionné.");
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING, message, ButtonType.OK);
        alert.showAndWait();
    }

    private boolean showConfirmation(String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, message, ButtonType.YES, ButtonType.NO);
        alert.showAndWait();
        return alert.getResult() == ButtonType.YES;
    }

    @FXML
    private void handleToggleValide() {
        Hebergement selection = tableHebergements.getSelectionModel().getSelectedItem();
        if (selection != null) {
            try {
                boolean newValue = !selection.isValide();
                selection.setValide(newValue);

                HebergementDAO dao = new HebergementDAO();
                dao.updateHebergement(selection);

                // Recharger la liste après changement
                hebergements.setAll(HebergementDAO.getTousLesHebergements());

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            showAlert("Aucun hébergement sélectionné.");
        }
    }


}
