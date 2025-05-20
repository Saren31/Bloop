package utcapitole.miage.bloop.service;

import org.springframework.stereotype.Service;
import utcapitole.miage.bloop.dto.RecommendationDTO;
import utcapitole.miage.bloop.model.entity.Utilisateur;
import utcapitole.miage.bloop.repository.jpa.UtilisateurRepository;
import utcapitole.miage.bloop.repository.neo4j.UserGraphRepository;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class RecommendationService {

    private final UserGraphRepository userGraphRepository;
    private final UtilisateurRepository utilisateurRepository;

    public RecommendationService(UserGraphRepository userGraphRepository,
                                 UtilisateurRepository utilisateurRepository) {
        this.userGraphRepository = userGraphRepository;
        this.utilisateurRepository = utilisateurRepository;
    }

    public List<Utilisateur> recommendFriends(Long userId) {
        // Récupère d'abord les IDs recommandés
        List<RecommendationDTO> recommended = userGraphRepository.recommendFriends(userId);

        return recommended.stream()
                .map(dto -> utilisateurRepository.findById(dto.getId()).orElse(null))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }
}