package view;

import dao.ReservationDAO;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.stage.Window;
import model.Reservation;
import utils.Session;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class HebergementsPayesController {

    @FXML private TableView<Reservation> tablePayes;
    @FXML private TableColumn<Reservation, String> colHebergement;
    @FXML private TableColumn<Reservation, LocalDate> colDateArrivee;
    @FXML private TableColumn<Reservation, LocalDate> colDateDepart;
    @FXML private TableColumn<Reservation, String> colMontant;
    @FXML private TableColumn<Reservation, LocalDate> colDatePaiement;

    @FXML
    public void initialize() {
        colHebergement.setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().getHebergementTitre()));
        colDateArrivee.setCellValueFactory(data -> new ReadOnlyObjectWrapper<>(data.getValue().getDateArrivee()));
        colDateDepart.setCellValueFactory(data -> new ReadOnlyObjectWrapper<>(data.getValue().getDateDepart()));
        colMontant.setCellValueFactory(data -> new ReadOnlyStringWrapper(String.format("%.2f €", data.getValue().getMontantPaye())));
        colDatePaiement.setCellValueFactory(data -> new ReadOnlyObjectWrapper<>(data.getValue().getDatePaiement()));

        chargerHebergementsPayes();
    }

    private void chargerHebergementsPayes() {
        try {
            int userId = Session.getInstance().getUtilisateurActuel().getId();
            List<Reservation> payees = new ReservationDAO().getPayeesByUtilisateur(userId);
            tablePayes.setItems(FXCollections.observableArrayList(payees));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void retourAccueil() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/AccueilView.fxml"));
            Parent root = loader.load();

            // On récupère la fenêtre actuelle et on remplace seulement la scène
            Stage stage = (Stage) Stage.getWindows().filtered(Window::isShowing).get(0);
            stage.setScene(new Scene(root));
            stage.setTitle("Accueil");
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
