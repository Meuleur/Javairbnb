package model;

import java.time.LocalDate;


/**
 * Représente un paiement effectué pour une réservation.
 * Inclut le montant, la date du paiement, et l’identifiant de la réservation associée.
 */

public class Paiement {
    private int id;
    private int reservationId;
    private double montant;
    private LocalDate datePaiement;

    // Getters et Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getReservationId() { return reservationId; }
    public void setReservationId(int reservationId) { this.reservationId = reservationId; }

    public double getMontant() { return montant; }
    public void setMontant(double montant) { this.montant = montant; }

    public LocalDate getDatePaiement() { return datePaiement; }
    public void setDatePaiement(LocalDate datePaiement) { this.datePaiement = datePaiement; }
}
