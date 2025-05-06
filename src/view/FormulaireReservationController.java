package view;

import dao.ReservationDAO;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.Hebergement;
import model.Reservation;
import utils.Session;

import java.sql.SQLException;
import java.time.LocalDate;

public class FormulaireReservationController {

    @FXML private DatePicker dateArrivee;
    @FXML private DatePicker dateDepart;
    @FXML private Spinner<Integer> nombrePersonnes;
    @FXML private Spinner<Integer> spinnerAdultes;
    @FXML private Spinner<Integer> spinnerEnfants;
    @FXML private Button btnPayer;

    private Hebergement hebergement;

    // Appelée depuis le contrôleur parent pour transmettre l’hébergement
    public void setHebergement(Hebergement h) {
        this.hebergement = h;
    }

    @FXML
    public void initialize() {
        spinnerAdultes.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 10, 1));
        spinnerEnfants.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 10, 0));
    }


    @FXML
    private void envoyerDemande() {
        LocalDate arrivee = dateArrivee.getValue();
        LocalDate depart = dateDepart.getValue();

        if (arrivee == null || depart == null || !depart.isAfter(arrivee)) {
            montrerAlerte("Erreur", "La date de départ doit être après la date d’arrivée.");
            return;
        }

        Reservation r = new Reservation();
        r.setHebergementId(hebergement.getId());
        r.setUtilisateurId(Session.getInstance().getUtilisateurActuel().getId());
        r.setDateArrivee(arrivee);
        r.setDateDepart(depart);
        r.setNombreAdultes(spinnerAdultes.getValue());
        r.setNombreEnfants(spinnerEnfants.getValue());
        r.setProprietaireId(hebergement.getProprietaireId());

        try {
            new ReservationDAO().insert(r);
            montrerAlerte("Succès", "Demande envoyée !");
            fermerFenetre();
        } catch (SQLException e) {
            e.printStackTrace();
            montrerAlerte("Erreur", "Échec de l’envoi de la demande.");
        }
    }

    private void fermerFenetre() {
        Stage stage = (Stage) dateArrivee.getScene().getWindow();
        stage.close();
    }

    private void montrerAlerte(String titre, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


}
