package view;

import dao.UtilisateurDAO;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import model.Utilisateur;
import javafx.scene.Parent;
import javafx.scene.Scene;
import java.sql.SQLException;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import utils.Session;
/**
 * Contrôleur de la page de connexion.
 * Permet aux utilisateurs de se connecter ou d'accéder à la page d'inscription.
 * Gère l'ouverture de la session et la redirection vers la page d'accueil.
 */
public class LoginController {

    @FXML private TextField emailField;
    @FXML private PasswordField motDePasseField;


    /**
     * Méthode appelée lors du clic sur le bouton de connexion.
     * Vérifie les identifiants et redirige vers la page d’accueil si la connexion est réussie.
     */
    @FXML
    private void handleLogin() {
        String email = emailField.getText();
        String motDePasse = motDePasseField.getText();

        try {
            UtilisateurDAO dao = new UtilisateurDAO();
            Utilisateur user = dao.findByEmailAndPassword(email, motDePasse);

            if (user != null) {

                Session.getInstance().setUtilisateurActuel(user);

                Parent accueilRoot = FXMLLoader.load(getClass().getResource("/view/AccueilView.fxml"));
                Stage stage = new Stage();
                stage.setTitle("Accueil");
                stage.setScene(new Scene(accueilRoot));
                stage.show();

                // Fermer la fenêtre actuelle de login
                Stage currentStage = (Stage) emailField.getScene().getWindow();
                currentStage.close();
            } else {
                showAlert("Erreur", "Email ou mot de passe incorrect.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Erreur", "Erreur lors de la connexion : " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Erreur", "Une erreur s’est produite lors du chargement de la page d’accueil.");
        }
    }
    /**
     * Affiche une alerte avec le message spécifié.
     *
     * @param titre   le titre de la boîte de dialogue
     * @param message le contenu du message à afficher
     */
    private void showAlert(String titre, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    /**
     * Ouvre la vue d'inscription lorsqu’un utilisateur clique sur "S’inscrire".
     */
    public void handleGoToRegister() {
        try {
            // 🔁 Charger la vue d'inscription
            Parent registerRoot = FXMLLoader.load(getClass().getResource("RegisterView.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Inscription");
            stage.setScene(new Scene(registerRoot));
            stage.show();

            // 🔒 Fermer la fenêtre de connexion actuelle
            Stage currentStage = (Stage) emailField.getScene().getWindow();
            currentStage.close();

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Erreur", "Impossible d'ouvrir la page d'inscription.");
        }
    }

}
