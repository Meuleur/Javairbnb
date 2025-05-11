package dao;

import model.Hebergement;
import utils.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * La classe {@code HebergementDAO} fournit des méthodes pour accéder et manipuler les données
 * de la table Hebergement dans la base de données.
 * Elle suit le pattern DAO (Data Access Object).
 */

public class HebergementDAO {
    private Connection conn;


    /**
     * Constructeur qui initialise la connexion à la base de données
     * en utilisant la classe utilitaire {@code DatabaseConnection}.
     *
     * @throws SQLException si une erreur survient lors de l'ouverture de la connexion
     */

    public HebergementDAO() throws SQLException {
        this.conn = DatabaseConnection.getConnection();
    }


    /**
     * Récupère la liste de tous les hébergements enregistrés dans la base de données.
     *
     * @return une liste d'objets {@code Hebergement}
     * @throws SQLException si une erreur SQL survient lors de l'exécution de la requête
     */


    public List<Hebergement> getAll() throws SQLException {
        List<Hebergement> hebergements = new ArrayList<>();
        String sql = "SELECT * FROM Hebergement";
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);

        while (rs.next()) {
            Hebergement h = new Hebergement();
            h.setId(rs.getInt("id"));
            h.setNom(rs.getString("nom"));
            h.setAdresse(rs.getString("adresse"));
            h.setDescription(rs.getString("description"));
            h.setPrix(rs.getDouble("prix"));
            h.setValide (rs.getBoolean("valide"));
            h.setProprietaireId(rs.getInt("proprietaire_id"));
            h.setType(rs.getString("type"));
            hebergements.add(h);
        }

        rs.close();
        stmt.close();
        return hebergements;
    }


    /**
     * Insère un nouvel hébergement dans la base de données.
     *
     * @param h l'objet {@code Hebergement} à ajouter (l'attribut `id` est ignoré car généré automatiquement)
     * @throws SQLException si une erreur survient lors de l'exécution de l'instruction SQL
     */

    public void ajouterHebergement(Hebergement h) throws SQLException {
        String sql = "INSERT INTO Hebergement (nom, adresse, description, prix, type, proprietaire_id) VALUES (?, ?, ?, ?, ?, ?)";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, h.getNom());
        stmt.setString(2, h.getAdresse());
        stmt.setString(3, h.getDescription());
        stmt.setDouble(4, h.getPrix());
        stmt.setString(5, h.getType());
        stmt.setInt(6, h.getProprietaireId());
        stmt.executeUpdate();
        stmt.close();
    }



    /**
     * Récupère un hébergement spécifique selon son identifiant.
     *
     * @param id l'identifiant de l'hébergement à rechercher
     * @return un objet {@code Hebergement} si trouvé, sinon {@code null}
     * @throws SQLException si une erreur SQL survient
     */

    public Hebergement getById(int id) throws SQLException {
        String sql = "SELECT * FROM Hebergement WHERE id = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, id);
        ResultSet rs = stmt.executeQuery();

        Hebergement hebergement = null;
        if (rs.next()) {
            hebergement = new Hebergement();
            hebergement.setId(rs.getInt("id"));
            hebergement.setNom(rs.getString("nom"));
            hebergement.setAdresse(rs.getString("adresse"));
            hebergement.setDescription(rs.getString("description"));
            hebergement.setPrix(rs.getDouble("prix"));
            hebergement.setType(rs.getString("type"));
            hebergement.setValide(rs.getBoolean("valide"));
            hebergement.setProprietaireId(rs.getInt("proprietaire_id"));
        }

        rs.close();
        stmt.close();
        return hebergement;
    }

}
