package utcapitole.miage.bloop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import utcapitole.miage.bloop.model.entity.Utilisateur;
import utcapitole.miage.bloop.service.RecommendationService;
import utcapitole.miage.bloop.service.UtilisateurService;

import java.util.List;

@Controller
@RequestMapping("/recommandations")
public class RecommendationController {

    private final RecommendationService recommendationService;

    @Autowired
    public RecommendationController(RecommendationService recommendationService) {
        this.recommendationService = recommendationService;
    }

    @GetMapping
    public String afficherRecommandations(Model model, @AuthenticationPrincipal Utilisateur utilisateurConnecte) {
        List<Utilisateur> utilisateursRecommandes = recommendationService.recommendFriends(utilisateurConnecte.getIdUser());

        // Ajout des utilisateurs au modèle
        model.addAttribute("recommandations", utilisateursRecommandes);

        return "recommandations";
    }
}