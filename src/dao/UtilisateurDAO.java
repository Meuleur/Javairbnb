package dao;

import model.Utilisateur;
import utils.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UtilisateurDAO {
    private Connection conn;

    public UtilisateurDAO() throws SQLException {
        this.conn = DatabaseConnection.getConnection();
    }

    // Correction : SELECT au lieu de INSERT
    public List<Utilisateur> getAllClients() throws SQLException {
        List<Utilisateur> clients = new ArrayList<>();
        String sql = "SELECT * FROM Utilisateur WHERE role = 'CLIENT'";
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);

        while (rs.next()) {
            Utilisateur c = new Utilisateur();
            c.setId(rs.getInt("id"));
            c.setNom(rs.getString("nom"));
            c.setPrenom(rs.getString("prenom"));
            c.setEmail(rs.getString("email"));
            c.setTypeClient(rs.getString("type_client"));
            c.setRole(rs.getString("role")); // Ajout du rôle

            clients.add(c);
        }
        rs.close();
        stmt.close();
        return clients;
    }

    public void insertUtilisateur(Utilisateur utilisateur) throws SQLException {
        String sql = "INSERT INTO Utilisateur (nom, prenom, email, mot_de_passe, type_client, role) VALUES (?, ?, ?, ?, ?, ?)";
        PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

        stmt.setString(1, utilisateur.getNom());
        stmt.setString(2, utilisateur.getPrenom());
        stmt.setString(3, utilisateur.getEmail());
        stmt.setString(4, utilisateur.getMotDePasse());
        stmt.setString(5, utilisateur.getTypeClient());
        stmt.setString(6, utilisateur.getRole());

        int affectedRows = stmt.executeUpdate();
        if (affectedRows == 0) {
            throw new SQLException("L'insertion de l'utilisateur a échoué, aucune ligne affectée.");
        }

        try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
            if (generatedKeys.next()) {
                utilisateur.setId(generatedKeys.getInt(1));
            } else {
                throw new SQLException("L'insertion de l'utilisateur a échoué, aucun ID généré.");
            }
        }

        stmt.close();
    }

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
            u.setRole(rs.getString("role")); // Ajout du rôle
            utilisateurs.add(u);
        }

        rs.close();
        stmt.close();
        return utilisateurs;
    }

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
            user.setRole(rs.getString("role")); // ✅ Lecture du rôle ici
            return user;
        }

        return null;
    }
    public void deleteUtilisateur(int id) throws SQLException {
        String sql = "DELETE FROM Utilisateur WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }
    public void updateUtilisateur(Utilisateur utilisateur) throws SQLException {
        String sql = "UPDATE Utilisateur SET nom = ?, prenom = ?, email = ?, type_client = ?, role = ? WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, utilisateur.getNom());
            stmt.setString(2, utilisateur.getPrenom());
            stmt.setString(3, utilisateur.getEmail());
            stmt.setString(4, utilisateur.getTypeClient());
            stmt.setString(5, utilisateur.getRole());
            stmt.setInt(6, utilisateur.getId());
            stmt.executeUpdate();
        }
    }

}
