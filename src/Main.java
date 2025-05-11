import dao.UtilisateurDAO;
import model.Utilisateur;

import java.sql.SQLException;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        try {
            UtilisateurDAO utilisateurDAO = new UtilisateurDAO();

            Utilisateur user = new Utilisateur();
            user.setNom("Caron");
            user.setPrenom("Jean-Baptiste");
            user.setEmail("jb@example.com");
            user.setMotDePasse("azerty");
            user.setTypeClient("ancien");

            utilisateurDAO.insertUtilisateur(user);
            System.out.println("Utilisateur inséré avec ID : " + user.getId());

            // 💡 Récupération de tous les utilisateurs
            List<Utilisateur> utilisateurs = utilisateurDAO.getAllUtilisateurs();

            // 💡 Affichage
            for (Utilisateur u : utilisateurs) {
                System.out.println(u);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
