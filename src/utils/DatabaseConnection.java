package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    public static Connection getConnection() throws SQLException {
        // URL de connexion à ta base de données existante (celle que tu as créée manuellement)
        String url = "jdbc:mysql://localhost:3306/booking_db?serverTimezone=UTC";
        String user = "root";      // ton utilisateur MySQL
        String password = "";      // ton mot de passe MySQL (laisser vide si non défini)

        try {
            // Charger le driver MySQL JDBC
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("Driver JDBC non trouvé : " + e.getMessage());
        }

        // Établir la connexion à la base de données et la retourner
        return DriverManager.getConnection(url, user, password);
    }

    // Méthode de test pour vérifier la connexion (à supprimer une fois testé)
    public static void main(String[] args) {
        try {
            Connection connection = getConnection();
            System.out.println("Connexion réussie à la base de données.");
            connection.close();
        } catch (SQLException e) {
            System.err.println("Erreur lors de la connexion à la base de données : " + e.getMessage());
        }
    }
}