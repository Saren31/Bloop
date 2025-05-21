package utcapitole.miage.bloop.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import utcapitole.miage.bloop.model.entity.Utilisateur;
import utcapitole.miage.bloop.service.RecommendationService;

import java.util.List;

/**
 * Contrôleur REST pour gérer les endpoints liés aux recommandations.
 */
@RestController
public class RecommendationRestController {

    private final RecommendationService recommendationService;

    /**
     * Constructeur pour injecter la dépendance RecommendationService.
     *
     * @param recommendationService le service utilisé pour gérer la logique des recommandations
     */
    public RecommendationRestController(RecommendationService recommendationService) {
        this.recommendationService = recommendationService;
    }

    /**
     * Endpoint pour récupérer les recommandations d'amis pour un utilisateur spécifique.
     *
     * @param userId l'ID de l'utilisateur pour lequel les recommandations doivent être récupérées
     * @return une liste d'utilisateurs recommandés
     */
    @GetMapping("/recommendations/{userId}")
    public List<Utilisateur> getRecommendations(@PathVariable Long userId) {
        return recommendationService.recommendFriends(userId);
    }
}