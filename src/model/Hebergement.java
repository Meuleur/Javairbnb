package model;

/**
 * Représente un hébergement proposé par un propriétaire.
 * Contient les informations principales telles que le nom, l'adresse, le prix, le type,
 * ainsi que son statut de validation.
 */


public class Hebergement {
    private int id;
    private String nom;
    private String adresse;
    private String description;
    private double prix;
    private String type;
    private boolean valide;
    private int proprietaireId;       // ← nouveau champ

    public boolean isValide() {
        return valide;
    }
    public void setValide(boolean valide) {
        this.valide = valide;
    }

    // Getters et setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getAdresse() { return adresse; }
    public void setAdresse(String adresse) { this.adresse = adresse; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public double getPrix() { return prix; }
    public void setPrix(double prix) { this.prix = prix; }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return nom + " - " + adresse;
    }

    public int getProprietaireId() { return proprietaireId; }
    public void setProprietaireId(int id) { this.proprietaireId = id; }
}

