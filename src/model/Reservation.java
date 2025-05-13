package model;

import java.time.LocalDate;
import java.time.LocalDateTime;


/**
 * Représente une réservation effectuée par un utilisateur pour un hébergement.
 * Contient les dates d'arrivée et de départ, le nombre de personnes, et les statuts associés.
 */

public class Reservation {
    private int id;
    private int utilisateurId;
    private int hebergementId;
    private LocalDate dateArrivee;
    private LocalDate dateDepart;
    private LocalDateTime dateReservation;
    private boolean valide;

    // Pour l’affichage dans “Mes demandes”
    private String hebergementTitre;
    private String clientNom;
    private String clientPrenom;
    private int proprietaireId;
    // --- getters & setters ---
    private int nombreAdultes;
    private int nombreEnfants;
    private int nombreChambres;
    private double montantPaye;
    private LocalDate datePaiement;

    public LocalDate getDatePaiement() {
        return datePaiement;
    }

    public void setDatePaiement(LocalDate datePaiement) {
        this.datePaiement = datePaiement;
    }
    public double getMontantPaye() {
        return montantPaye;
    }

    public void setMontantPaye(double montantPaye) {
        this.montantPaye = montantPaye;
    }

    public int getNombreChambres() { return nombreChambres; }
    public void setNombreChambres(int nombreChambres) { this.nombreChambres = nombreChambres; }


    public int getNombreAdultes() { return nombreAdultes; }
    public void setNombreAdultes(int nombreAdultes) { this.nombreAdultes = nombreAdultes; }

    public int getNombreEnfants() { return nombreEnfants; }
    public void setNombreEnfants(int nombreEnfants) { this.nombreEnfants = nombreEnfants; }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getUtilisateurId() { return utilisateurId; }
    public void setUtilisateurId(int utilisateurId) {
        this.utilisateurId = utilisateurId;
    }

    public int getHebergementId() { return hebergementId; }
    public void setHebergementId(int hebergementId) {
        this.hebergementId = hebergementId;
    }

    public LocalDate getDateArrivee() { return dateArrivee; }
    public void setDateArrivee(LocalDate dateArrivee) {
        this.dateArrivee = dateArrivee;
    }

    public LocalDate getDateDepart() { return dateDepart; }
    public void setDateDepart(LocalDate dateDepart) {
        this.dateDepart = dateDepart;
    }

    public LocalDateTime getDateReservation() { return dateReservation; }
    public void setDateReservation(LocalDateTime dateReservation) {
        this.dateReservation = dateReservation;
    }

    public boolean isValide() { return valide; }
    public void setValide(boolean valide) { this.valide = valide; }

    public String getHebergementTitre() { return hebergementTitre; }
    public void setHebergementTitre(String hebergementTitre) {
        this.hebergementTitre = hebergementTitre;
    }

    public String getClientNom() { return clientNom; }
    public void setClientNom(String clientNom) { this.clientNom = clientNom; }

    public String getClientPrenom() { return clientPrenom; }
    public void setClientPrenom(String clientPrenom) {
        this.clientPrenom = clientPrenom;
    }

    public int getProprietaireId() {
        return proprietaireId;
    }
    public void setProprietaireId(int proprietaireId) {
        this.proprietaireId = proprietaireId;
    }
}
