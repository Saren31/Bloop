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
     * Cette méthode retourne le nom de la vue "accueil" à afficher.
     *
     * @return Le nom de la vue "accueil".
     */
    @GetMapping("/accueil")
    public String accueil() {
        return "accueil";
    }

    /**
     * Gère les requêtes GET pour la racine "/".
     * Cette méthode redirige également vers la vue "accueil".
     *
     * @return Le nom de la vue "accueil".
     */
    @GetMapping("/")
    public String index() {
        return "accueil";
    }

}