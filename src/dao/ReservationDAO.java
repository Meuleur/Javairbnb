package dao;

import model.Reservation;
import utils.DatabaseConnection;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


/**
 * La classe {@code ReservationDAO} fournit les opérations de gestion
 * des réservations dans la base de données.
 * Elle permet d'insérer des réservations, d'en récupérer selon divers critères,
 * et de mettre à jour leur statut.
 */

public class ReservationDAO {
    private final Connection conn;


    /**
     * Constructeur par défaut qui établit une connexion à la base de données.
     *
     * @throws SQLException si une erreur survient lors de l'établissement de la connexion
     */

    public ReservationDAO() throws SQLException {
        this.conn = DatabaseConnection.getConnection();
    }

    /**
     * Insère une nouvelle demande de réservation dans la base de données.
     * La date de réservation est automatiquement fixée à l'heure actuelle.
     * Le champ {@code valide} est initialisé à {@code FALSE}.
     *
     * @param r l'objet {@code Reservation} à insérer
     * @throws SQLException si une erreur SQL survient
     */

    public void insert(Reservation r) throws SQLException {
        String sql =
                "INSERT INTO Reservation " +
                        "(utilisateur_id, hebergement_id, date_arrivee, date_depart, date_reservation, valide, nombre_adultes, nombre_enfants, proprietaire_id) " +
                        "VALUES (?, ?, ?, ?, CURRENT_TIMESTAMP, FALSE, ?, ?, ?)";

        try (PreparedStatement p = conn.prepareStatement(sql)) {
            p.setInt(1, r.getUtilisateurId());
            p.setInt(2, r.getHebergementId());
            p.setDate(3, Date.valueOf(r.getDateArrivee()));
            p.setDate(4, Date.valueOf(r.getDateDepart()));
            p.setInt(5, r.getNombreAdultes());
            p.setInt(6, r.getNombreEnfants());
            p.setInt(7, r.getProprietaireId());
            p.executeUpdate();
        }
    }


    /**
     * Récupère toutes les réservations non encore validées pour un propriétaire donné.
     *
     * @param ownerId l'identifiant du propriétaire
     * @return la liste des {@code Reservation} en attente
     * @throws SQLException si une erreur SQL survient
     */
    public List<Reservation> getPendingForOwner(int ownerId) throws SQLException {
        String sql =
                "SELECT r.id, r.date_reservation, r.date_arrivee, r.date_depart, r.valide, " +
                        "r.utilisateur_id, r.hebergement_id, r.proprietaire_id, " +
                        "r.nombre_adultes, r.nombre_enfants, " +
                        "u.nom AS clientNom, u.prenom AS clientPrenom, h.nom AS hebergementTitre " +
                        "FROM Reservation r " +
                        "JOIN Utilisateur u ON r.utilisateur_id = u.id " +
                        "JOIN Hebergement h ON r.hebergement_id = h.id " +
                        "WHERE r.proprietaire_id = ? AND r.valide = FALSE";


        List<Reservation> list = new ArrayList<>();
        try (PreparedStatement p = conn.prepareStatement(sql)) {
            p.setInt(1, ownerId);
            try (ResultSet rs = p.executeQuery()) {
                while (rs.next()) {
                    Reservation r = new Reservation();
                    r.setId(rs.getInt("id"));
                    r.setUtilisateurId(rs.getInt("utilisateur_id"));
                    r.setHebergementId(rs.getInt("hebergement_id"));  // <<<<<< AJOUT INDISPENSABLE
                    r.setDateArrivee(rs.getDate("date_arrivee").toLocalDate());
                    r.setDateDepart(rs.getDate("date_depart").toLocalDate());
                    r.setDateReservation(rs.getTimestamp("date_reservation").toLocalDateTime());
                    r.setValide(rs.getBoolean("valide"));
                    r.setProprietaireId(rs.getInt("proprietaire_id"));
                    r.setNombreAdultes(rs.getInt("nombre_adultes"));
                    r.setNombreEnfants(rs.getInt("nombre_enfants"));
                    list.add(r);
                }
            }
        }
        return list;
    }

    /**
     * Met à jour le statut de validation d'une réservation.
     *
     * @param reservationId l'identifiant de la réservation
     * @param valide {@code true} si la réservation est acceptée, {@code false} sinon
     * @throws SQLException si une erreur SQL survient
     */
    public void setValide(int reservationId, boolean valide) throws SQLException {
        String sql = "UPDATE Reservation SET valide = ? WHERE id = ?";
        try (PreparedStatement p = conn.prepareStatement(sql)) {
            p.setBoolean(1, valide);
            p.setInt(2, reservationId);
            p.executeUpdate();
        }
    }
    /**
     * Récupère toutes les réservations effectuées par un utilisateur donné.
     *
     * @param userId l'identifiant de l'utilisateur
     * @return la liste des {@code Reservation} associées
     * @throws SQLException si une erreur SQL survient
     */
    public List<Reservation> getByUtilisateur(int userId) throws SQLException {
        String sql =
                "SELECT r.id, r.date_reservation, r.date_arrivee, r.date_depart, r.valide, " +
                        "r.utilisateur_id, r.hebergement_id, r.proprietaire_id, " +
                        "r.nombre_adultes, r.nombre_enfants, " +
                        "h.nom AS hebergementTitre " +
                        "FROM Reservation r " +
                        "JOIN Hebergement h ON r.hebergement_id = h.id " +
                        "WHERE r.utilisateur_id = ?";

        List<Reservation> result = new ArrayList<>();
        try (PreparedStatement p = conn.prepareStatement(sql)) {
            p.setInt(1, userId);
            ResultSet rs = p.executeQuery();
            while (rs.next()) {
                Reservation r = new Reservation();
                r.setId(rs.getInt("id"));
                r.setDateArrivee(rs.getDate("date_arrivee").toLocalDate());
                r.setDateDepart(rs.getDate("date_depart").toLocalDate());
                r.setDateReservation(rs.getTimestamp("date_reservation").toLocalDateTime());
                r.setValide(rs.getBoolean("valide"));
                r.setUtilisateurId(rs.getInt("utilisateur_id"));
                r.setHebergementId(rs.getInt("hebergement_id"));
                r.setProprietaireId(rs.getInt("proprietaire_id"));
                r.setNombreAdultes(rs.getInt("nombre_adultes"));
                r.setNombreEnfants(rs.getInt("nombre_enfants"));
                r.setHebergementTitre(rs.getString("hebergementTitre"));
                result.add(r);
            }
        }
        return result;
    }


    /**
     * Récupère toutes les réservations reçues par un propriétaire donné.
     *
     * @param proprietaireId l'identifiant du propriétaire
     * @return la liste des {@code Reservation} reçues
     * @throws SQLException si une erreur SQL survient
     */

    public List<Reservation> getByProprietaire(int proprietaireId) throws SQLException {
        String sql =
                "SELECT r.id, r.date_reservation, r.date_arrivee, r.date_depart, r.valide, " +
                        "r.utilisateur_id, r.hebergement_id, r.proprietaire_id, " +
                        "r.nombre_adultes, r.nombre_enfants, " +
                        "h.nom AS hebergementTitre " +
                        "FROM Reservation r " +
                        "JOIN Hebergement h ON r.hebergement_id = h.id " +
                        "WHERE r.proprietaire_id = ?";

        List<Reservation> result = new ArrayList<>();
        try (PreparedStatement p = conn.prepareStatement(sql)) {
            p.setInt(1, proprietaireId);
            ResultSet rs = p.executeQuery();
            while (rs.next()) {
                Reservation r = new Reservation();
                r.setId(rs.getInt("id"));
                r.setDateArrivee(rs.getDate("date_arrivee").toLocalDate());
                r.setDateDepart(rs.getDate("date_depart").toLocalDate());
                r.setDateReservation(rs.getTimestamp("date_reservation").toLocalDateTime());
                r.setValide(rs.getBoolean("valide"));
                r.setUtilisateurId(rs.getInt("utilisateur_id"));
                r.setHebergementId(rs.getInt("hebergement_id"));
                r.setProprietaireId(rs.getInt("proprietaire_id"));
                r.setNombreAdultes(rs.getInt("nombre_adultes"));
                r.setNombreEnfants(rs.getInt("nombre_enfants"));
                r.setHebergementTitre(rs.getString("hebergementTitre"));
                result.add(r);
            }
        }
        return result;
    }

    /**
     * Récupère toutes les réservations payées d’un utilisateur donné,
     * incluant les informations de paiement associées.
     *
     * @param userId l’identifiant de l’utilisateur
     * @return une liste de {@code Reservation} avec montants et dates de paiement
     * @throws SQLException si une erreur SQL survient
     */
    public List<Reservation> getPayeesByUtilisateur(int userId) throws SQLException {
        String sql = "SELECT r.*, h.nom AS hebergementTitre, p.montant AS montantPaye, p.date_paiement " +
                "FROM Reservation r " +
                "JOIN Hebergement h ON r.hebergement_id = h.id " +
                "JOIN Paiement p ON r.id = p.reservation_id " +
                "WHERE r.utilisateur_id = ?";

        List<Reservation> result = new ArrayList<>();
        try (PreparedStatement p = conn.prepareStatement(sql)) {
            p.setInt(1, userId);
            ResultSet rs = p.executeQuery();
            while (rs.next()) {
                Reservation r = new Reservation();
                r.setId(rs.getInt("id"));
                r.setHebergementId(rs.getInt("hebergement_id"));
                r.setDateArrivee(rs.getDate("date_arrivee").toLocalDate());
                r.setDateDepart(rs.getDate("date_depart").toLocalDate());
                r.setDateReservation(rs.getTimestamp("date_reservation").toLocalDateTime());
                r.setHebergementTitre(rs.getString("hebergementTitre"));
                r.setMontantPaye(rs.getDouble("montantPaye"));
                r.setDatePaiement(rs.getDate("date_paiement").toLocalDate());

                result.add(r);
            }
        }
        return result;
    }


}
