package dao;

import model.Utilisateur;
import utils.DatabaseConnection;
import java.util.List;
import java.util.ArrayList;
import java.sql.*;

/**
 * La classe {@code UtilisateurDAO} gère les opérations de base de données liées aux utilisateurs,
 * telles que l'insertion, la récupération ou l'authentification.
 */

public class UtilisateurDAO {
    private Connection conn;

    /**
     * Constructeur qui établit une connexion à la base de données.
     *
     * @throws SQLException si la connexion échoue
     */

    public UtilisateurDAO() throws SQLException {
        this.conn = DatabaseConnection.getConnection();
    }

    /**
     * Récupère tous les utilisateurs ayant un rôle de client.
     * ⚠️ Remarque : le SQL ici est incorrect (utilise un INSERT au lieu d’un SELECT).
     * Il faut corriger la ligne :
     * ```java
     * String sql = "SELECT * FROM Utilisateur WHERE type_client = 'client'";
     * ```
     *
     * @return une liste d’objets {@code Utilisateur} représentant les clients
     * @throws SQLException si une erreur SQL survient
     */
        public List<Utilisateur> getAllClients() throws SQLException {
        List<Utilisateur> clients = new ArrayList<>();
        String sql = "INSERT INTO Utilisateur (nom, prenom, email, mot_de_passe, type_client) VALUES (?, ?, ?, ?, ?)";
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);

        while (rs.next()) {
            Utilisateur c = new Utilisateur();
            c.setId(rs.getInt("id_client"));
            c.setNom(rs.getString("nom"));
            c.setPrenom(rs.getString("prenom"));
            c.setEmail(rs.getString("email"));

            clients.add(c);
        }
        rs.close();
        stmt.close();
        return clients;
    }

    /**
     * Insère un nouvel utilisateur dans la base de données.
     * Si l'insertion réussit, l’ID généré est automatiquement assigné à l’objet.
     *
     * @param utilisateur l’utilisateur à insérer
     * @throws SQLException si une erreur survient lors de l’insertion ou de la récupération de l’ID
     */
    public void insertUtilisateur(Utilisateur utilisateur) throws SQLException {
        String sql = "INSERT INTO Utilisateur (nom, prenom, email, mot_de_passe, type_client) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

        stmt.setString(1, utilisateur.getNom());
        stmt.setString(2, utilisateur.getPrenom());
        stmt.setString(3, utilisateur.getEmail());
        stmt.setString(4, utilisateur.getMotDePasse());
        stmt.setString(5, utilisateur.getTypeClient());

        int affectedRows = stmt.executeUpdate();
        if (affectedRows == 0) {
            throw new SQLException("L'insertion de l'utilisateur a échoué, aucune ligne affectée.");
        }

        // Récupérer l'ID auto-généré
        try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
            if (generatedKeys.next()) {
                utilisateur.setId(generatedKeys.getInt(1));
            } else {
                throw new SQLException("L'insertion de l'utilisateur a échoué, aucun ID généré.");
            }
        }

        stmt.close();
    }
    /**
     * Récupère tous les utilisateurs (peu importe leur rôle).
     *
     * @return une liste d’utilisateurs
     * @throws SQLException si une erreur SQL survient
     */
    public List<Utilisateur> getAllUtilisateurs() throws SQLException {
        List<Utilisateur> utilisateurs = new ArrayList<>();
        String sql = "SELECT * FROM Utilisateur";
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);

        while (rs.next()) {
            Utilisateur u = new Utilisateur();
            u.setId(rs.getInt("id"));
            u.setNom(rs.getString("nom"));
            u.setPrenom(rs.getString("prenom"));
            u.setEmail(rs.getString("email"));
            u.setMotDePasse(rs.getString("mot_de_passe"));
            u.setTypeClient(rs.getString("type_client"));
            utilisateurs.add(u);
        }

        rs.close();
        stmt.close();
        return utilisateurs;
    }
    /**
     * Recherche un utilisateur correspondant à un couple email/mot de passe.
     * Cette méthode est utilisée pour l’authentification.
     *
     * @param email l’adresse email à vérifier
     * @param motDePasse le mot de passe correspondant
     * @return un objet {@code Utilisateur} si les identifiants sont valides, sinon {@code null}
     * @throws SQLException si une erreur SQL survient
     */
    public Utilisateur findByEmailAndPassword(String email, String motDePasse) throws SQLException {
        String sql = "SELECT * FROM Utilisateur WHERE email = ? AND mot_de_passe = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, email);
        stmt.setString(2, motDePasse);
        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            Utilisateur user = new Utilisateur();
            user.setId(rs.getInt("id"));
            user.setNom(rs.getString("nom"));
            user.setPrenom(rs.getString("prenom"));
            user.setEmail(rs.getString("email"));
            user.setMotDePasse(rs.getString("mot_de_passe"));
            user.setTypeClient(rs.getString("type_client"));
            return user;
        }

        return null;
    }

}

