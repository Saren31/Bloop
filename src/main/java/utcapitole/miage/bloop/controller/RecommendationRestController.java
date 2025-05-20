package utcapitole.miage.bloop.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import utcapitole.miage.bloop.dto.RecommendationDTO;
import utcapitole.miage.bloop.model.entity.Utilisateur;
import utcapitole.miage.bloop.service.RecommendationService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
public class RecommendationRestController {

    private final RecommendationService recommendationService;

    public RecommendationRestController(RecommendationService recommendationService) {
        this.recommendationService = recommendationService;
    }

    @GetMapping("/recommendations/{userId}")
    public List<Utilisateur> getRecommendations(@PathVariable Long userId) {
        try {
            List<Utilisateur> recommendations = recommendationService.recommendFriends(userId);
            return recommendations;
        } catch (Exception e) {
            e.printStackTrace();
            // Retourne une liste vide en cas d'erreur
            return new ArrayList<>();
        }
    }
}
