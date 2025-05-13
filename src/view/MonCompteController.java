package view;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;

/**
 * Contrôleur de la page "Mon Compte".
 * Permet à l'utilisateur connecté de naviguer vers ses annonces, ses réservations,
 * ses paiements ou de se déconnecter.
 */
public class MonCompteController {

    /**
     * Ouvre le formulaire pour créer une nouvelle annonce.
     * Ferme la fenêtre actuelle.
     */
    @FXML
    private void ouvrirFormulaireAnnonce() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/NouvelleAnnonceView.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Poster une annonce");
            stage.setScene(new Scene(root));
            stage.show();

            Stage currentStage = (Stage) Stage.getWindows().filtered(Window::isShowing).get(0);
            currentStage.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * Ouvre la page des réservations de l'utilisateur.
     * Ferme la fenêtre actuelle.
     */
    @FXML
    private void ouvrirMesReservations() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/ClientReservationView.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Mes réservations");
            stage.setScene(new Scene(root));
            stage.show();
            Stage currentStage = (Stage) Stage.getWindows().filtered(Window::isShowing).get(0);
            currentStage.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * Retourne à la page d’accueil.
     * Ferme la fenêtre actuelle.
     */
    @FXML
    private void retourAccueil() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/AccueilView.fxml"));
            Parent root = loader.load();

            // Récupère la fenêtre actuelle (celle du bouton cliqué)
            Stage currentStage = (Stage) Stage.getWindows().filtered(Window::isShowing).get(0);
            currentStage.setScene(new Scene(root));
            currentStage.setTitle("Accueil");
            currentStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Déconnecte l’utilisateur et retourne à la page de connexion.
     */
    @FXML
    private void seDeconnecter() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/LoginView.fxml"));  // <-- adapte si ton fichier s'appelle autrement
            Parent root = loader.load();

            // Ferme la fenêtre actuelle et recharge la scène de login
            Stage stage = (Stage) Stage.getWindows().filtered(Window::isShowing).get(0);
            stage.setScene(new Scene(root));
            stage.setTitle("Connexion");
            stage.show();
            Stage currentStage = (Stage) Stage.getWindows().filtered(Window::isShowing).get(0);
            currentStage.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * Ouvre la page listant les hébergements que l’utilisateur a réservés et payés.
     * Ferme la fenêtre actuelle.
     */
    @FXML
    private void ouvrirHebergementsPayes() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/HebergementsPayesView.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Mes hébergements payés");
            stage.setScene(new Scene(root));
            stage.show();

            Stage currentStage = (Stage) Stage.getWindows().filtered(Window::isShowing).get(0);
            currentStage.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
