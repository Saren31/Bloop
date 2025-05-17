package utcapitole.miage.bloop.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

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
    public String afficherMessagerie() {
        return "messagerie";
    }
}