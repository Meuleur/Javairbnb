package view;

import dao.PaiementDAO;
import model.Paiement;
import java.sql.SQLException;
import java.time.LocalDate;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * Contrôleur de la page de paiement.
 * Permet à l'utilisateur de saisir ses informations de carte bancaire fictive,
 * et d'enregistrer un paiement associé à une réservation.
 */
public class PaiementController {

    @FXML private TextField champNumeroCarte;
    @FXML private TextField champExpiration;
    @FXML private PasswordField champCVV;
  private int reservationId;
 private double montant;

    /**
     * Définit les informations de paiement transmises depuis la page précédente.
     * @param reservationId ID de la réservation à payer
     * @param montant montant à régler
     */
    @FXML
    public void setInfosPaiement(int reservationId, double montant) {
        this.reservationId = reservationId;
        this.montant = montant;
    }

    /**
     * Valide les champs, crée un objet Paiement, et l’insère dans la base.
     */
    @FXML
    private void confirmerPaiement() {
        String numeroCarte = champNumeroCarte.getText();
        String expiration = champExpiration.getText();
        String cvv = champCVV.getText();

        if (numeroCarte.isEmpty() || expiration.isEmpty() || cvv.isEmpty()) {
            showAlert("Erreur", "Veuillez remplir tous les champs.");
            return;
        }

        try {
            Paiement paiement = new Paiement();
            paiement.setReservationId(reservationId);  // on va passer cet ID à l'ouverture du formulaire
            paiement.setMontant(montant);              // on passe aussi le montant à payer
            paiement.setDatePaiement(LocalDate.now());

            PaiementDAO dao = new PaiementDAO();
            dao.insert(paiement);

            showAlert("Succès", "Paiement enregistré !");
            fermerFenetre();
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Erreur", "Erreur lors de l'enregistrement du paiement.");
        }
    }

    /**
     * Ferme la fenêtre de paiement.
     */
    private void fermerFenetre() {
        Stage stage = (Stage) champNumeroCarte.getScene().getWindow();
        stage.close();
    }
    /**
     * Affiche une alerte avec un message personnalisé.
     * @param titre titre de la boîte de dialogue
     * @param message contenu du message
     */
    private void showAlert(String titre, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
