package utils;

import model.Utilisateur;

public class Session {
    private static Session instance;
    private Utilisateur utilisateurActuel;

    private Session() {}

    public static Session getInstance() {
        if (instance == null) instance = new Session();
        return instance;
    }

    public Utilisateur getUtilisateurActuel() {
        return utilisateurActuel;
    }

    public void setUtilisateurActuel(Utilisateur utilisateur) {
        this.utilisateurActuel = utilisateur;
    }
}
