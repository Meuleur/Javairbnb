package dao;

import model.Paiement;
import utils.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PaiementDAO {
    private Connection conn;

    public PaiementDAO() throws SQLException {
        this.conn = DatabaseConnection.getConnection();
    }

    public void insert(Paiement paiement) throws SQLException {
        String sql = "INSERT INTO Paiement (reservation_id, montant, date_paiement) VALUES (?, ?, ?)";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, paiement.getReservationId());
        stmt.setDouble(2, paiement.getMontant());
        stmt.setDate(3, java.sql.Date.valueOf(paiement.getDatePaiement()));
        stmt.executeUpdate();
        stmt.close();
    }

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
