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

public class RegisterController {

    @FXML private TextField nomField;
    @FXML private TextField prenomField;
    @FXML private TextField emailField;
    @FXML private PasswordField motDePasseField;
    @FXML private TextField typeClientField;

    @FXML
    public void handleRegister() {
        String nom = nomField.getText();
        String prenom = prenomField.getText();
        String email = emailField.getText();
        String motDePasse = motDePasseField.getText();
        String typeClient = typeClientField.getText();

        if (nom.isEmpty() || prenom.isEmpty() || email.isEmpty() || motDePasse.isEmpty() || typeClient.isEmpty()) {
            showAlert("Champs manquants", "Veuillez remplir tous les champs.");
            return;
        }

        if (!email.contains("@")) {
            showAlert("Email invalide", "Veuillez entrer une adresse email valide.");
            return;
        }

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
            handleBackToLogin();

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Erreur", "Échec de l'inscription : " + e.getMessage());
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void handleBackToLogin() {
        try {
            Parent loginRoot = FXMLLoader.load(getClass().getResource("LoginView.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Connexion");
            stage.setScene(new Scene(loginRoot));
            stage.show();

            ((Stage) nomField.getScene().getWindow()).close();

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Erreur", "Impossible d'ouvrir la page de connexion.");
        }
    }

    private void clearFields() {
        nomField.clear();
        prenomField.clear();
        emailField.clear();
        motDePasseField.clear();
        typeClientField.clear();
    }
}
