package utcapitole.miage.bloop.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Contrôleur pour gérer les requêtes liées à la page d'accueil.
 */
@Controller
public class AccueilController {

    /**
     * Gère les requêtes GET pour l'URL "/accueil".
     *
     * @return Le nom de la vue "accueil" à afficher.
     */
    @GetMapping("/accueil")
    public String accueil() {
        return "accueil";
    }

    @GetMapping("/")
    public String index() {
        return "accueil";
    }

}