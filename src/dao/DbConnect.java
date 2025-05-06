package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbConnect {
    public static void main(String[] args) {
        // URL de connexion à ta base de données existante (celle que tu as créée manuellement dans DBeaver)
        String url = "jdbc:mysql://localhost:3306/booking_db?serverTimezone=UTC";
        String user = "root";      // ton utilisateur MySQL
        String password = "";      // ton mot de passe MySQL (laisser vide si non défini)

        try {
            // Charger le driver MySQL JDBC
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Établir la connexion à la base de données
            Connection connection = DriverManager.getConnection(url, user, password);
            System.out.println("Connexion réussie à la base de données.");

            // À ce stade, la connexion est établie.
            // Tu peux procéder aux opérations sur la base : exécuter des requêtes SELECT, UPDATE, etc.

            // Fermer la connexion une fois les opérations terminées
            connection.close();
        } catch (ClassNotFoundException e) {
            System.err.println("Driver JDBC non trouvé : " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("Erreur lors de la connexion à la base de données : " + e.getMessage());
        }
    }
}