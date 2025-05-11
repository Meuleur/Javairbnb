package view;

import dao.UtilisateurDAO;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.Utilisateur;
import utils.Session;

import java.sql.SQLException;

public class LoginController {

    @FXML private TextField emailField;
    @FXML private PasswordField motDePasseField;

    // 🔑 Stockage accessible partout après connexion
    public static Utilisateur utilisateurConnecte;

    @FXML
    private void handleLogin() {
        String email = emailField.getText();
        String motDePasse = motDePasseField.getText();

        try {
            UtilisateurDAO dao = new UtilisateurDAO();
            Utilisateur user = dao.findByEmailAndPassword(email, motDePasse);

            if (user != null) {
                Session.getInstance().setUtilisateurActuel(user);  // Session
                utilisateurConnecte = user;                       // Accès direct

                System.out.println("Utilisateur connecté : " + utilisateurConnecte.getEmail() +
                        ", Rôle : " + utilisateurConnecte.getRole());

                Parent accueilRoot = FXMLLoader.load(getClass().getResource("/view/AccueilView.fxml"));
                Stage stage = new Stage();
                stage.setTitle("Accueil");
                stage.setScene(new Scene(accueilRoot));
                stage.show();

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

    private void showAlert(String titre, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void handleGoToRegister() {
        try {
            Parent registerRoot = FXMLLoader.load(getClass().getResource("RegisterView.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Inscription");
            stage.setScene(new Scene(registerRoot));
            stage.show();

            Stage currentStage = (Stage) emailField.getScene().getWindow();
            currentStage.close();

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Erreur", "Impossible d'ouvrir la page d'inscription.");
        }
    }
}
