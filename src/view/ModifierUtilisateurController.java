package view;

import dao.UtilisateurDAO;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.Utilisateur;

import java.sql.SQLException;

public class ModifierUtilisateurController {

    @FXML private TextField nomField;
    @FXML private TextField prenomField;
    @FXML private TextField emailField;
    @FXML private TextField typeClientField;
    @FXML private TextField roleField;

    private Utilisateur utilisateur;

    public void setUtilisateur(Utilisateur u) {
        this.utilisateur = u;
        nomField.setText(u.getNom());
        prenomField.setText(u.getPrenom());
        emailField.setText(u.getEmail());
        typeClientField.setText(u.getTypeClient());
        roleField.setText(u.getRole());
    }

    @FXML
    private void handleModifier() {
        try {
            utilisateur.setNom(nomField.getText());
            utilisateur.setPrenom(prenomField.getText());
            utilisateur.setEmail(emailField.getText());
            utilisateur.setTypeClient(typeClientField.getText());
            utilisateur.setRole(roleField.getText());

            UtilisateurDAO dao = new UtilisateurDAO();
            dao.updateUtilisateur(utilisateur);

            Stage stage = (Stage) nomField.getScene().getWindow();
            stage.close();

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Erreur", "Une erreur est survenue lors de la mise à jour.");
        }
    }

    private void showAlert(String titre, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
