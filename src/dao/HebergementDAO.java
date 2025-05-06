package dao;

import model.Hebergement;
import utils.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HebergementDAO {
    private Connection conn;

    public HebergementDAO() throws SQLException {
        this.conn = DatabaseConnection.getConnection();
    }

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
