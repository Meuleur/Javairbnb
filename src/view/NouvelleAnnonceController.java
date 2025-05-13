package view;

import dao.HebergementDAO;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import model.Hebergement;
import javafx.scene.control.ComboBox;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;
import javafx.scene.Scene;
import utils.Session;

/**
 * Contrôleur de la page de création d'une nouvelle annonce.
 * Permet à un propriétaire de renseigner les informations d'un hébergement
 * et de l’ajouter à la base de données.
 */

public class NouvelleAnnonceController {

    @FXML private TextField nomField;
    @FXML private TextField adresseField;
    @FXML private TextField descriptionField;
    @FXML private TextField prixField;
    @FXML private ComboBox<String> typeField;


    /**
     * Gère la création d'une annonce à partir des champs saisis.
     * Valide les entrées, enregistre l’hébergement et redirige vers la page d'accueil.
     */
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
    /**
     * Réinitialise tous les champs du formulaire.
     */
    private void clearFields() {
        nomField.clear();
        adresseField.clear();
        descriptionField.clear();
        prixField.clear();
    }
    /**
     * Affiche une boîte de dialogue avec un message donné.
     *
     * @param titre   le titre de la fenêtre d’alerte
     * @param message le message à afficher
     */
    private void showAlert(String titre, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
