package view;

import dao.ReservationDAO;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.Reservation;
import model.Hebergement;
import utils.Session;

import java.sql.SQLException;
import java.time.LocalDate;
/**
 * Contrôleur du formulaire de réservation.
 * Permet à l'utilisateur de sélectionner ses dates de séjour et le nombre de personnes,
 * puis d’envoyer une demande de réservation pour un hébergement spécifique.
 */
public class FormulaireReservationController {

    @FXML private DatePicker dateArrivee;
    @FXML private DatePicker dateDepart;
    @FXML private Spinner<Integer> nombrePersonnes;
    @FXML private Spinner<Integer> spinnerAdultes;
    @FXML private Spinner<Integer> spinnerEnfants;
    @FXML private Button btnPayer;

    private Hebergement hebergement;

    /**
     * Définit l'hébergement pour lequel l'utilisateur souhaite réserver.
     * Appelée depuis le contrôleur parent (Accueil).
     *
     * @param h l’hébergement concerné
     */
    public void setHebergement(Hebergement h) {
        this.hebergement = h;
    }
    /**
     * Initialise les spinners pour adultes et enfants avec des valeurs par défaut.
     */
    @FXML
    public void initialize() {
        spinnerAdultes.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 10, 1));
        spinnerEnfants.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 10, 0));
    }

    /**
     * Vérifie les dates et envoie la demande de réservation à la base de données.
     */
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
    /**
     * Ferme la fenêtre actuelle.
     */
    private void fermerFenetre() {
        Stage stage = (Stage) dateArrivee.getScene().getWindow();
        stage.close();
    }
    /**
     * Affiche une alerte avec un titre et un message personnalisés.
     *
     * @param titre   le titre de l’alerte
     * @param message le message à afficher
     */
    private void montrerAlerte(String titre, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


}
