package utcapitole.miage.bloop.service;

import org.springframework.stereotype.Service;
import utcapitole.miage.bloop.dto.RecommendationDTO;
import utcapitole.miage.bloop.model.entity.Utilisateur;
import utcapitole.miage.bloop.repository.jpa.UtilisateurRepository;
import utcapitole.miage.bloop.repository.neo4j.UserGraphRepository;

import java.util.List;
import java.util.Objects;

/**
 * Service gérant la logique des recommandations d'amis.
 */
@Service
public class RecommendationService {

    private final UserGraphRepository userGraphRepository;
    private final UtilisateurRepository utilisateurRepository;

    /**
     * Constructeur pour l'injection des dépendances.
     *
     * @param userGraphRepository le repository pour accéder aux données de graphe des utilisateurs (Neo4j)
     * @param utilisateurRepository le repository pour accéder aux données utilisateur (JPA)
     */
    public RecommendationService(UserGraphRepository userGraphRepository,
                                 UtilisateurRepository utilisateurRepository) {
        this.userGraphRepository = userGraphRepository;
        this.utilisateurRepository = utilisateurRepository;
    }

    /**
     * Recommande des amis pour un utilisateur donné à partir de son ID.
     *
     * @param userId l'ID de l'utilisateur pour lequel générer des recommandations
     * @return une liste d'utilisateurs recommandés
     */
    public List<Utilisateur> recommendFriends(Long userId) {
        // Récupère les recommandations depuis le repository de graphe
        List<RecommendationDTO> recommended = userGraphRepository.recommendFriends(userId);

        // Convertit les recommandations en entités utilisateur et filtre les valeurs nulles
        return recommended.stream()
                .map(dto -> utilisateurRepository.findById(dto.getId()).orElse(null))
                .filter(Objects::nonNull)
                .toList();
    }
}