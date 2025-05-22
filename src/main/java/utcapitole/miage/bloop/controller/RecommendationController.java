package utcapitole.miage.bloop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import utcapitole.miage.bloop.model.entity.Utilisateur;
import utcapitole.miage.bloop.service.RecommendationService;

import java.util.List;

/**
 * Contrôleur pour gérer les recommandations d'amis.
 * Fournit un endpoint pour afficher les recommandations d'amis pour l'utilisateur connecté.
 */
@Controller
@RequestMapping("/recommandations")
public class RecommendationController {

    private final RecommendationService recommendationService;

    /**
     * Constructeur pour injecter le service de recommandations.
     *
     * @param recommendationService Service pour gérer les recommandations d'amis.
     */
    @Autowired
    public RecommendationController(RecommendationService recommendationService) {
        this.recommendationService = recommendationService;
    }

    /**
     * Affiche les recommandations d'amis pour l'utilisateur actuellement connecté.
     *
     * @param model Le modèle pour passer les données à la vue.
     * @param utilisateurConnecte L'utilisateur actuellement connecté.
     * @return Le nom de la vue affichant les recommandations.
     */
    @GetMapping
    public String afficherRecommandations(Model model, @AuthenticationPrincipal Utilisateur utilisateurConnecte) {
        List<Utilisateur> utilisateursRecommandes = recommendationService.recommendFriends(utilisateurConnecte.getIdUser());

        // Ajout des utilisateurs recommandés au modèle
        model.addAttribute("recommandations", utilisateursRecommandes);

        return "recommandations";
    }
}