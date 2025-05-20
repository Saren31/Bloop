package utcapitole.miage.bloop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import utcapitole.miage.bloop.model.entity.Utilisateur;
import utcapitole.miage.bloop.repository.jpa.UtilisateurRepository;

/**
 * Contrôleur pour gérer les requêtes liées à la confirmation par e-mail.
 */
@Controller
public class EmailController {

    private final UtilisateurRepository utilisateurRepository;

    /**
     * Constructeur pour injecter le dépôt des utilisateurs.
     *
     * @param utilisateurRepository Le dépôt utilisé pour accéder aux données des utilisateurs.
     */
    @Autowired
    public EmailController(UtilisateurRepository utilisateurRepository) {
        this.utilisateurRepository = utilisateurRepository;
    }

    /**
     * Gère les requêtes GET pour confirmer l'inscription d'un utilisateur.
     *
     * @param token Le jeton d'inscription utilisé pour valider l'utilisateur.
     * @param model Le modèle utilisé pour ajouter des attributs à la vue.
     * @return Le nom de la vue "confirmer_inscription" à afficher.
     */
    @GetMapping("/confirm")
    public String confirmUser(@RequestParam("token") String token, Model model) {
        Utilisateur user = utilisateurRepository.findByTokenInscription(token);
        if (user != null) {
            user.setValiderInscription(true); // Marque l'inscription comme validée.
            user.setTokenInscription(null);  // Supprime le jeton d'inscription.
            utilisateurRepository.save(user); // Sauvegarde les modifications de l'utilisateur.
            model.addAttribute("success", true); // Ajoute un attribut indiquant le succès.
        } else {
            model.addAttribute("success", false); // Ajoute un attribut indiquant l'échec.
        }
        return "confirmer_inscription";
    }
}