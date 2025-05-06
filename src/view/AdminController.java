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

    private ObservableList<Hebergement> hebergements;

    @FXML
    public void initialize() {
        // Associer les colonnes avec les propriétés du modèle
        colNom.setCellValueFactory(data -> data.getValue().nomProperty());
        colAdresse.setCellValueFactory(data -> data.getValue().adresseProperty());
        colPrix.setCellValueFactory(data -> data.getValue().prixProperty().asObject());
        colType.setCellValueFactory(data -> data.getValue().typeProperty());

        // Charger les données
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
            stage.initModality(Modality.APPLICATION_MODAL); // bloque la fenêtre principale
            stage.showAndWait();

            // Rechargement après ajout
            hebergements.setAll(HebergementDAO.getTousLesHebergements());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleModifier() {
        Hebergement selection = tableHebergements.getSelectionModel().getSelectedItem();
        if (selection != null) {
            System.out.println("Modifier : " + selection.getNom());
            // TODO : ouvrir un formulaire de modification pré-rempli
        }
    }

    @FXML
    private void handleSupprimer() {
        Hebergement selection = tableHebergements.getSelectionModel().getSelectedItem();
        if (selection != null) {
            boolean success = HebergementDAO.supprimerHebergement(selection.getId());
            if (success) {
                hebergements.remove(selection);
            }
        }
    }
}
