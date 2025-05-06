package model;

import javafx.beans.property.*;

public class Hebergement {

    private IntegerProperty id = new SimpleIntegerProperty();
    private StringProperty nom = new SimpleStringProperty();
    private StringProperty adresse = new SimpleStringProperty();
    private StringProperty description = new SimpleStringProperty();
    private DoubleProperty prix = new SimpleDoubleProperty();
    private StringProperty type = new SimpleStringProperty();
    private BooleanProperty valide = new SimpleBooleanProperty();
    private IntegerProperty proprietaireId = new SimpleIntegerProperty();

    // Constructeurs
    public Hebergement() {}

    public Hebergement(int id, String nom, String adresse, String description, double prix, String type, boolean valide, int proprietaireId) {
        this.id.set(id);
        this.nom.set(nom);
        this.adresse.set(adresse);
        this.description.set(description);
        this.prix.set(prix);
        this.type.set(type);
        this.valide.set(valide);
        this.proprietaireId.set(proprietaireId);
    }

    // Getters JavaFX pour TableView
    public IntegerProperty idProperty() { return id; }
    public StringProperty nomProperty() { return nom; }
    public StringProperty adresseProperty() { return adresse; }
    public StringProperty descriptionProperty() { return description; }
    public DoubleProperty prixProperty() { return prix; }
    public StringProperty typeProperty() { return type; }
    public BooleanProperty valideProperty() { return valide; }
    public IntegerProperty proprietaireIdProperty() { return proprietaireId; }

    // Getters / Setters classiques
    public int getId() { return id.get(); }
    public void setId(int id) { this.id.set(id); }

    public String getNom() { return nom.get(); }
    public void setNom(String nom) { this.nom.set(nom); }

    public String getAdresse() { return adresse.get(); }
    public void setAdresse(String adresse) { this.adresse.set(adresse); }

    public String getDescription() { return description.get(); }
    public void setDescription(String description) { this.description.set(description); }

    public double getPrix() { return prix.get(); }
    public void setPrix(double prix) { this.prix.set(prix); }

    public String getType() { return type.get(); }
    public void setType(String type) { this.type.set(type); }

    public boolean isValide() { return valide.get(); }
    public void setValide(boolean valide) { this.valide.set(valide); }

    public int getProprietaireId() { return proprietaireId.get(); }
    public void setProprietaireId(int id) { this.proprietaireId.set(id); }

    @Override
    public String toString() {
        return nom.get() + " - " + adresse.get();
    }
}
