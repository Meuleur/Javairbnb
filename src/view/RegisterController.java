package view;

import dao.UtilisateurDAO;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.Utilisateur;

import java.sql.SQLException;

/**
 * Contrôleur de la page d’inscription.
 * Permet à un nouvel utilisateur de créer un compte en remplissant les champs requis,
 * puis redirige vers la page de connexion après validation.
 */
public class RegisterController {

    @FXML private TextField nomField;
    @FXML private TextField prenomField;
    @FXML private TextField emailField;
    @FXML private PasswordField motDePasseField;
    @FXML private TextField typeClientField;

    /**
     * Gère la logique d'inscription :
     * - Validation des champs
     * - Création d'un objet Utilisateur
     * - Insertion dans la base de données
     */


    @FXML
    public void handleRegister() {
        String nom = nomField.getText();
        String prenom = prenomField.getText();
        String email = emailField.getText();
        String motDePasse = motDePasseField.getText();
        String typeClient = typeClientField.getText();

        // ✅ Vérification des champs obligatoires
        if (nom.isEmpty() || prenom.isEmpty() || email.isEmpty() || motDePasse.isEmpty() || typeClient.isEmpty()) {
            showAlert("Champs manquants", "Veuillez remplir tous les champs.");
            return;
        }

        // (Optionnel) validation email basique
        if (!email.contains("@")) {
            showAlert("Email invalide", "Veuillez entrer une adresse email valide.");
            return;
        }

        // (Optionnel) mot de passe minimum 6 caractères
        if (motDePasse.length() < 6) {
            showAlert("Mot de passe trop court", "Le mot de passe doit contenir au moins 6 caractères.");
            return;
        }

        Utilisateur user = new Utilisateur();
        user.setNom(nom);
        user.setPrenom(prenom);
        user.setEmail(email);
        user.setMotDePasse(motDePasse);
        user.setTypeClient(typeClient);
        user.setRole("CLIENT");

        try {
            UtilisateurDAO dao = new UtilisateurDAO();
            dao.insertUtilisateur(user);

            showAlert("Succès", "Inscription réussie ! Redirection vers la connexion...");
            handleBackToLogin(); // Redirection seulement après validation + enregistrement réussi

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Erreur", "Échec de l'inscription : " + e.getMessage());
        }
    }

    /**
     * Affiche une alerte d’information ou d’erreur.
     *
     * @param title   Titre de la boîte de dialogue
     * @param message Message à afficher
     */
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    /**
     * Redirige l’utilisateur vers la page de connexion après inscription.
     */
    public void handleBackToLogin() {
        try {
            Parent loginRoot = FXMLLoader.load(getClass().getResource("LoginView.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Connexion");
            stage.setScene(new Scene(loginRoot));
            stage.show();

            // Fermer la fenêtre d'inscription
            ((Stage) nomField.getScene().getWindow()).close();

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Erreur", "Impossible d'ouvrir la page de connexion.");
        }
    }
    /**
     * Réinitialise tous les champs du formulaire (non utilisée actuellement).
     */
    private void clearFields() {
        nomField.clear();
        prenomField.clear();
        emailField.clear();
        motDePasseField.clear();
        typeClientField.clear();
    }
}
