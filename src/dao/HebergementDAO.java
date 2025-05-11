package dao;

import model.Hebergement;
import utils.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HebergementDAO {
    private final Connection conn;

    public HebergementDAO() throws SQLException {
        this.conn = DatabaseConnection.getConnection();
    }

    public static List<Hebergement> getTousLesHebergements() {
        List<Hebergement> hebergements = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM Hebergement")) {

            while (rs.next()) {
                Hebergement h = new Hebergement();
                h.setId(rs.getInt("id"));
                h.setNom(rs.getString("nom"));
                h.setAdresse(rs.getString("adresse"));
                h.setDescription(rs.getString("description"));
                h.setPrix(rs.getDouble("prix"));
                h.setValide(rs.getBoolean("valide"));
                h.setProprietaireId(rs.getInt("proprietaire_id"));
                h.setType(rs.getString("type"));
                hebergements.add(h);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return hebergements;
    }

    public void ajouterHebergement(Hebergement h) throws SQLException {
        String sql = "INSERT INTO Hebergement (nom, adresse, description, prix, type, proprietaire_id) VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, h.getNom());
            stmt.setString(2, h.getAdresse());
            stmt.setString(3, h.getDescription());
            stmt.setDouble(4, h.getPrix());
            stmt.setString(5, h.getType());
            stmt.setInt(6, h.getProprietaireId());
            stmt.executeUpdate();
        }
    }

    public void updateHebergement(Hebergement h) throws SQLException {
        String sql = "UPDATE Hebergement SET " +
                "nom = ?, " +
                "adresse = ?, " +
                "description = ?, " +
                "prix = ?, " +
                "type = ?, " +
                "proprietaire_id = ?, " +
                "valide = ? " +  // Mise à jour du champ valide
                "WHERE id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, h.getNom());
            stmt.setString(2, h.getAdresse());
            stmt.setString(3, h.getDescription());
            stmt.setDouble(4, h.getPrix());
            stmt.setString(5, h.getType());
            stmt.setInt(6, h.getProprietaireId());
            stmt.setBoolean(7, h.isValide());  // Applique l'état actif/inactif
            stmt.setInt(8, h.getId());

            stmt.executeUpdate();
        }
    }

    public static boolean supprimerHebergement(int id) {
        String sql = "DELETE FROM Hebergement WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Hebergement getById(int id) throws SQLException {
        String sql = "SELECT * FROM Hebergement WHERE id = ?";
        Hebergement h = null;

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                h = new Hebergement();
                h.setId(rs.getInt("id"));
                h.setNom(rs.getString("nom"));
                h.setAdresse(rs.getString("adresse"));
                h.setDescription(rs.getString("description"));
                h.setPrix(rs.getDouble("prix"));
                h.setType(rs.getString("type"));
                h.setValide(rs.getBoolean("valide"));
                h.setProprietaireId(rs.getInt("proprietaire_id"));
            }

            rs.close();
        }

        return h;
    }
}
