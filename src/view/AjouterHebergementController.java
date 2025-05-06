package view;

import dao.HebergementDAO;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.Hebergement;

public class AjouterHebergementController {

    @FXML private TextField nomField;
    @FXML private TextField adresseField;
    @FXML private TextField descriptionField;
    @FXML private TextField prixField;
    @FXML private TextField typeField;
    @FXML private TextField proprietaireField;

    @FXML
    private void handleAjouter() {
        try {
            Hebergement h = new Hebergement();
            h.setNom(nomField.getText());
            h.setAdresse(adresseField.getText());
            h.setDescription(descriptionField.getText());
            h.setPrix(Double.parseDouble(prixField.getText()));
            h.setType(typeField.getText());
            h.setProprietaireId(Integer.parseInt(proprietaireField.getText()));

            HebergementDAO dao = new HebergementDAO();
            dao.ajouterHebergement(h);

            // Fermer la fenêtre
            Stage stage = (Stage) nomField.getScene().getWindow();
            stage.close();

        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Erreur : " + e.getMessage());
            alert.showAndWait();
        }
    }
}
