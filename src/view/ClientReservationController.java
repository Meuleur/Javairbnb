package view;

import dao.HebergementDAO;
import dao.PaiementDAO;
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
import model.Hebergement;
import model.Reservation;
import utils.Session;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class ClientReservationController {

    @FXML private TableView<Reservation> tableDemandes;
    @FXML private TableColumn<Reservation, String> colHebergement;
    @FXML private TableColumn<Reservation, LocalDate> colDateArrivee;
    @FXML private TableColumn<Reservation, LocalDate> colDateDepart;
    @FXML private TableColumn<Reservation, String> colStatut;
    @FXML private TableView<Reservation> tableReceptions;
    @FXML private TableColumn<Reservation, String> colClient;
    @FXML private TableColumn<Reservation, String> colHebergementTitre;
    @FXML private TableColumn<Reservation, LocalDate> colDateArriveeProp;
    @FXML private TableColumn<Reservation, LocalDate> colDateDepartProp;
    @FXML private TableColumn<Reservation, Void> colActions;
    @FXML private TableColumn<Reservation, Void> colActionPayer;
    @FXML private TableColumn<Reservation, String> colPrix;


    @FXML
    public void initialize() {
        // Colonnes pour tableDemandes
        colHebergement.setCellValueFactory(data ->
                new ReadOnlyStringWrapper(data.getValue().getHebergementTitre()));
        colDateArrivee.setCellValueFactory(data ->
                new ReadOnlyObjectWrapper<>(data.getValue().getDateArrivee()));
        colDateDepart.setCellValueFactory(data ->
                new ReadOnlyObjectWrapper<>(data.getValue().getDateDepart()));
        colStatut.setCellValueFactory(data -> {
            String statut = data.getValue().isValide() ? "Acceptée" : "En attente";
            return new ReadOnlyStringWrapper(statut);
        });
        colPrix.setCellValueFactory(data -> {
            try {
                HebergementDAO hebergementDAO = new HebergementDAO();
                Hebergement hebergement = hebergementDAO.getById(data.getValue().getHebergementId());
                if (hebergement != null) {
                    return new ReadOnlyStringWrapper(String.format("%.2f €", hebergement.getPrix()));
                } else {
                    return new ReadOnlyStringWrapper("N/A");
                }
            } catch (SQLException e) {
                e.printStackTrace();
                return new ReadOnlyStringWrapper("Erreur");
            }
        });

        // >>> ICI bouton "Payer"
        colActionPayer.setCellFactory(col -> new TableCell<>() {
            private final Button btnPayer = new Button("Payer");
            private final Label lblPaye = new Label("Payé");

            {
                btnPayer.setOnAction(event -> {
                    Reservation r = getTableView().getItems().get(getIndex());
                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/PaiementView.fxml"));
                        Parent root = loader.load();

                        PaiementController ctrl = loader.getController();

                        HebergementDAO hebergementDAO = new HebergementDAO();
                        Hebergement hebergement = hebergementDAO.getById(r.getHebergementId());

                        if (hebergement != null) {
                            double montantAPayer = hebergement.getPrix();
                            ctrl.setInfosPaiement(r.getId(), montantAPayer);

                            Stage stage = new Stage();
                            stage.setTitle("Paiement pour " + r.getHebergementTitre());
                            stage.setScene(new Scene(root));
                            stage.show();

                            // Recharge la table une fois la fenêtre de paiement fermée
                            stage.setOnHidden(e -> chargerDemandes());

                        } else {
                            System.out.println("Erreur : Hébergement introuvable pour l'ID " + r.getHebergementId());
                        }

                    } catch (IOException | SQLException e) {
                        e.printStackTrace();
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    Reservation r = getTableView().getItems().get(getIndex());

                    if (r.isValide()) {
                        try {
                            PaiementDAO paiementDAO = new PaiementDAO();
                            boolean dejaPaye = paiementDAO.isPaye(r.getId());

                            if (dejaPaye) {
                                setGraphic(lblPaye); // Affiche le texte "Payé"
                            } else {
                                setGraphic(btnPayer); // Affiche le bouton "Payer"
                            }
                        } catch (SQLException e) {
                            e.printStackTrace();
                            setGraphic(new Label("Erreur"));
                        }
                    } else {
                        setGraphic(null); // Si pas encore validé, rien
                    }
                }
            }
        });




        // Colonnes pour tableReceptions
        colClient.setCellValueFactory(data -> new ReadOnlyStringWrapper(
                data.getValue().getClientPrenom() + " " + data.getValue().getClientNom()));
        colHebergementTitre.setCellValueFactory(data ->
                new ReadOnlyStringWrapper(data.getValue().getHebergementTitre()));
        colDateArriveeProp.setCellValueFactory(data ->
                new ReadOnlyObjectWrapper<>(data.getValue().getDateArrivee()));
        colDateDepartProp.setCellValueFactory(data ->
                new ReadOnlyObjectWrapper<>(data.getValue().getDateDepart()));
        colActions.setCellFactory(col -> new TableCell<>() {
            private final Button btn = new Button("Valider");
            {
                btn.setOnAction(event -> {
                    Reservation r = getTableView().getItems().get(getIndex());
                    try {
                        new ReservationDAO().setValide(r.getId(), true);
                        chargerDemandes();
                        chargerReceptions();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                });
            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : btn);
            }
        });

        chargerDemandes();
        chargerReceptions();
    }


    private void chargerDemandes() {
        try {
            int clientId = Session.getInstance().getUtilisateurActuel().getId();
            List<Reservation> reservations = new ReservationDAO().getByUtilisateur(clientId);
            tableDemandes.setItems(FXCollections.observableArrayList(reservations));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private void chargerReceptions() {
        try {
            int proprietaireId = Session.getInstance().getUtilisateurActuel().getId();
            List<Reservation> receptions = new ReservationDAO().getPendingForOwner(proprietaireId);
            tableReceptions.setItems(FXCollections.observableArrayList(receptions));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void retourAccueil() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/AccueilView.fxml"));
            Parent root = loader.load();

            // Méthode générique pour récupérer la fenêtre active
            Stage stage = (Stage) Stage.getWindows().filtered(Window::isShowing).get(0);
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
