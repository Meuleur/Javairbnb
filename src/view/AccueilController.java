package view;

import dao.HebergementDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Hebergement;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;


public class AccueilController {

    @FXML private TableView<Hebergement> tableHebergements;
    @FXML private TableColumn<Hebergement, String> colNom;
    @FXML private TableColumn<Hebergement, String> colAdresse;
    @FXML private TableColumn<Hebergement, String> colDescription;
    @FXML private TableColumn<Hebergement, Double> colPrix;
    @FXML private TableColumn<Hebergement, Void> colAction;
    @FXML private ComboBox<String> comboTri;
    @FXML private TextField champRecherche;
    @FXML private TableColumn<Hebergement, String> colType;

    private ObservableList<Hebergement> data;


    @FXML
    public void initialize() {
        colNom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        colAdresse.setCellValueFactory(new PropertyValueFactory<>("adresse"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colPrix.setCellValueFactory(new PropertyValueFactory<>("prix"));
        colType.setCellValueFactory(new PropertyValueFactory<>("type"));

        colAction.setCellFactory(param -> new TableCell<>() {
            private final Button btn = new Button("Réserver");
            {
                btn.setOnAction(event -> {
                    Hebergement h = getTableView().getItems().get(getIndex());
                    ouvrirFormulaireReservation(h);
                });
            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : btn);
            }
        });

        try {
            HebergementDAO dao = new HebergementDAO();
            List<Hebergement> liste = dao.getAll();
            data = FXCollections.observableArrayList(liste);
            tableHebergements.setItems(data);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleOuvrirFormulaireAnnonce() {
        try {
            // 1) Créer et configurer le FXMLLoader avec le chemin absolu du FXML
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/view/NouvelleAnnonceView.fxml")
            );

            // 2) Charger le Parent
            Parent root = loader.load();

            // 3) Ouvrir la nouvelle fenêtre
            Stage stage = new Stage();
            stage.setTitle("Poster une annonce");
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Erreur", "Impossible d'ouvrir le formulaire de nouvelle annonce.");
        }
    }



    private void showAlert(String titre, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void refreshTable() {
        try {
            HebergementDAO dao = new HebergementDAO();
            List<Hebergement> liste = dao.getAll();
            ObservableList<Hebergement> data = FXCollections.observableArrayList(liste);
            tableHebergements.setItems(data);
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Erreur", "Impossible de rafraîchir la liste des hébergements.");
        }
    }
    @FXML
    private void ouvrirPageDemandesClient() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/ClientReservationView.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) tableHebergements.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Mes réservations");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void ouvrirFormulaireReservation(Hebergement h) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/FormulaireReservationView.fxml"));
            Parent root = loader.load();

            FormulaireReservationController ctrl = loader.getController();
            ctrl.setHebergement(h);

            Stage stage = new Stage();
            stage.setTitle("Réserver : " + h.getNom());
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Erreur", "Impossible d’ouvrir le formulaire de réservation.");
        }
    }
    @FXML
    private void ouvrirMonCompte() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/MonCompteView.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) tableHebergements.getScene().getWindow();            stage.setScene(new Scene(root));
            stage.setTitle("Mon compte");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void trierHebergements() {
        if (comboTri.getValue() == null) return;

        switch (comboTri.getValue()) {
            case "Prix croissant":
                FXCollections.sort(data, (h1, h2) -> Double.compare(h1.getPrix(), h2.getPrix()));
                break;
            case "Prix décroissant":
                FXCollections.sort(data, (h1, h2) -> Double.compare(h2.getPrix(), h1.getPrix()));
                break;
            case "Type d'hébergement":
                FXCollections.sort(data, (h1, h2) -> h1.getType().compareToIgnoreCase(h2.getType()));
                break;
            default:
                break;
        }
        tableHebergements.refresh(); // Mettre à jour la vue
    }
    @FXML
    private void filtrerHebergements() {
        String filtre = champRecherche.getText().toLowerCase();

        if (filtre.isEmpty()) {
            tableHebergements.setItems(data);
            return;
        }

        ObservableList<Hebergement> filtres = data.filtered(
                h -> h.getAdresse().toLowerCase().contains(filtre) || h.getNom().toLowerCase().contains(filtre)
        );

        tableHebergements.setItems(filtres);
    }

}
