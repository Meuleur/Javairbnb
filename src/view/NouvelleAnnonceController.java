package view;

import dao.HebergementDAO;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Hebergement;
import utils.Session;
public class NouvelleAnnonceController {

    @FXML private TextField nomField;
    @FXML private TextField adresseField;
    @FXML private TextField descriptionField;
    @FXML private TextField prixField;
    @FXML private ComboBox<String> typeField;

    @FXML
    private void handlePosterAnnonce() {
        String nom = nomField.getText();
        String adresse = adresseField.getText();
        String description = descriptionField.getText();
        String type = typeField.getValue();
        double prix;

        try {
            prix = Double.parseDouble(prixField.getText());
        } catch (NumberFormatException e) {
            showAlert("Erreur", "Le prix doit être un nombre.");
            return;
        }

        if (nom.isEmpty() || adresse.isEmpty() || description.isEmpty()) {
            showAlert("Erreur", "Tous les champs doivent être remplis.");
            return;
        }

        if (type == null || type.isEmpty()) {
            showAlert("Erreur", "Veuillez indiquer le type d'hébergement.");
            return;
        }

        Hebergement h = new Hebergement();
        h.setNom(nom);
        h.setAdresse(adresse);
        h.setDescription(description);
        h.setPrix(prix);
        h.setType(type);
        h.setProprietaireId(Session.getInstance().getUtilisateurActuel().getId()); // ← AJOUT ICI


        try {
            HebergementDAO dao = new HebergementDAO();
            dao.ajouterHebergement(h);
            showAlert("Succès", "Annonce postée !");

            // Charger la page d'accueil et rafraîchir les données
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/AccueilView.fxml"));
            Parent root = loader.load();

            // Récupère le contrôleur et appelle la méthode refreshTable()
            AccueilController accueilController = loader.getController();
            accueilController.refreshTable();

            // Ouvre une nouvelle fenêtre pour l'accueil
            Stage stage = new Stage();
            stage.setTitle("Accueil");
            stage.setScene(new Scene(root));
            stage.show();

            // Ferme la fenêtre actuelle de création d'annonce
            ((Stage) typeField.getScene().getWindow()).close();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Erreur", "Impossible de poster l'annonce.");
        }
    }

    private void clearFields() {
        nomField.clear();
        adresseField.clear();
        descriptionField.clear();
        prixField.clear();
    }

    private void showAlert(String titre, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
