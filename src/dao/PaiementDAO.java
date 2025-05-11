package dao;

import model.Paiement;
import utils.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;


/**
 * La classe {@code PaiementDAO} permet d'effectuer des opérations CRUD
 * sur la table {@code Paiement} de la base de données.
 * Elle gère notamment l'insertion de nouveaux paiements et la vérification
 * de leur existence pour une réservation donnée.
 */

public class PaiementDAO {
    private Connection conn;


    /**
     * Constructeur qui établit la connexion à la base de données
     * à l’aide de la classe {@code DatabaseConnection}.
     *
     * @throws SQLException si la connexion échoue
     */

    public PaiementDAO() throws SQLException {
        this.conn = DatabaseConnection.getConnection();
    }


    /**
     * Insère un paiement dans la base de données.
     *
     * @param paiement l’objet {@code Paiement} à insérer
     * @throws SQLException si une erreur survient lors de l'exécution de l'instruction SQL
     */
    public void insert(Paiement paiement) throws SQLException {
        String sql = "INSERT INTO Paiement (reservation_id, montant, date_paiement) VALUES (?, ?, ?)";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, paiement.getReservationId());
        stmt.setDouble(2, paiement.getMontant());
        stmt.setDate(3, java.sql.Date.valueOf(paiement.getDatePaiement()));
        stmt.executeUpdate();
        stmt.close();
    }
    /**
     * Vérifie si un paiement a déjà été effectué pour une réservation donnée.
     *
     * @param reservationId l’identifiant de la réservation
     * @return {@code true} si un paiement existe pour cette réservation, {@code false} sinon
     * @throws SQLException si une erreur SQL survient
     */

    public boolean isPaye(int reservationId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM Paiement WHERE reservation_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, reservationId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        }
        return false;
    }


}
