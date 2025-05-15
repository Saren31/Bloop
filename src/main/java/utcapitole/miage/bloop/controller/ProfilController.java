package utcapitole.miage.bloop.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import utcapitole.miage.bloop.model.entity.Utilisateur;

/**
 * Contrôleur pour gérer les requêtes liées au profil utilisateur.
 */
@Controller
@RequestMapping("/profil")
public class ProfilController {

    /**
     * Gère les requêtes GET pour afficher la page du profil de l'utilisateur connecté.
     *
     * @param model          Le modèle utilisé pour ajouter des attributs à la vue.
     * @param authentication L'objet Authentication contenant les informations de l'utilisateur connecté.
     * @return Le nom de la vue "profil" à afficher.
     */
    @GetMapping("/monProfil")
    public String monProfil(Model model, Authentication authentication) {
        Utilisateur utilisateur = (Utilisateur) authentication.getPrincipal();
        model.addAttribute("user", utilisateur);
        return "profil";
    }

}