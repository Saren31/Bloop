package utcapitole.miage.bloop.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import utcapitole.miage.bloop.model.entity.Utilisateur;

/**
 * Contrôleur Spring MVC pour gérer les requêtes liées à la messagerie.
 */
@Controller
public class MessagerieController {

    /**
     * Gère les requêtes GET vers l'URL "/messagerie".
     *
     * @return Le nom de la vue "messagerie" à afficher.
     */
    @GetMapping("/messagerie")
    public String afficherMessagerie(@AuthenticationPrincipal Utilisateur utilisateurConnecte, Model model) {
        model.addAttribute("currentUserId", utilisateurConnecte.getIdUser());
        return "messagerie";
    }
}