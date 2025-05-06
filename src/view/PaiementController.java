package view;

import dao.PaiementDAO;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Paiement;

import java.sql.SQLException;
import java.time.LocalDate;


public class PaiementController {

    @FXML private TextField champNumeroCarte;
    @FXML private TextField champExpiration;
    @FXML private PasswordField champCVV;
  private int reservationId;
 private double montant;


    @FXML
    public void setInfosPaiement(int reservationId, double montant) {
        this.reservationId = reservationId;
        this.montant = montant;
    }

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


    private void fermerFenetre() {
        Stage stage = (Stage) champNumeroCarte.getScene().getWindow();
        stage.close();
    }

    private void showAlert(String titre, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
