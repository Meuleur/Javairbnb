package view;

import dao.HebergementDAO;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.Hebergement;

public class ModifierHebergementController {

    @FXML private TextField nomField;
    @FXML private TextField adresseField;
    @FXML private TextField descriptionField;
    @FXML private TextField prixField;
    @FXML private TextField typeField;
    @FXML private TextField proprietaireField;

    private Hebergement hebergement;

    public void setHebergement(Hebergement h) {
        this.hebergement = h;
        nomField.setText(h.getNom());
        adresseField.setText(h.getAdresse());
        descriptionField.setText(h.getDescription());
        prixField.setText(String.valueOf(h.getPrix()));
        typeField.setText(h.getType());
        proprietaireField.setText(String.valueOf(h.getProprietaireId()));
    }

    @FXML
    private void handleModifier() {
        try {
            hebergement.setNom(nomField.getText());
            hebergement.setAdresse(adresseField.getText());
            hebergement.setDescription(descriptionField.getText());
            hebergement.setPrix(Double.parseDouble(prixField.getText()));
            hebergement.setType(typeField.getText());
            hebergement.setProprietaireId(Integer.parseInt(proprietaireField.getText()));

            HebergementDAO dao = new HebergementDAO();
            // TODO : ajoute une méthode updateHebergement(Hebergement h)
            dao.updateHebergement(hebergement);

            Stage stage = (Stage) nomField.getScene().getWindow();
            stage.close();

        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Erreur : " + e.getMessage());
            alert.showAndWait();
        }
    }
}
