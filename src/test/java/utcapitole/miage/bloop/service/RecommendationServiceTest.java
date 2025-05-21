package utcapitole.miage.bloop.service;

import org.junit.jupiter.api.Test;
import utcapitole.miage.bloop.dto.RecommendationDTO;
import utcapitole.miage.bloop.model.entity.Utilisateur;
import utcapitole.miage.bloop.repository.jpa.UtilisateurRepository;
import utcapitole.miage.bloop.repository.neo4j.UserGraphRepository;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class RecommendationServiceTest {

    private final UserGraphRepository userGraphRepository = mock(UserGraphRepository.class);
    private final UtilisateurRepository utilisateurRepository = mock(UtilisateurRepository.class);
    private final RecommendationService recommendationService =
            new RecommendationService(userGraphRepository, utilisateurRepository);

    @Test
    void testRecommendFriends_ReturnsUtilisateurList() {
        RecommendationDTO dto1 = new RecommendationDTO();
        dto1.setId(1L);
        RecommendationDTO dto2 = new RecommendationDTO();
        dto2.setId(2L);

        Utilisateur u1 = new Utilisateur(); u1.setIdUser(1L);
        Utilisateur u2 = new Utilisateur(); u2.setIdUser(2L);

        when(userGraphRepository.recommendFriends(5L)).thenReturn(List.of(dto1, dto2));
        when(utilisateurRepository.findById(1L)).thenReturn(Optional.of(u1));
        when(utilisateurRepository.findById(2L)).thenReturn(Optional.of(u2));

        List<Utilisateur> result = recommendationService.recommendFriends(5L);

        assertThat(result).containsExactly(u1, u2);
    }

    @Test
    void testRecommendFriends_FiltersNulls() {
        RecommendationDTO dto1 = new RecommendationDTO();
        dto1.setId(1L);
        RecommendationDTO dto2 = new RecommendationDTO();
        dto2.setId(2L);

        Utilisateur u1 = new Utilisateur(); u1.setIdUser(1L);

        when(userGraphRepository.recommendFriends(5L)).thenReturn(List.of(dto1, dto2));
        when(utilisateurRepository.findById(1L)).thenReturn(Optional.of(u1));
        when(utilisateurRepository.findById(2L)).thenReturn(Optional.empty());

        List<Utilisateur> result = recommendationService.recommendFriends(5L);

        assertThat(result).containsExactly(u1);
    }

    @Test
    void testRecommendFriends_EmptyRecommendation() {
        when(userGraphRepository.recommendFriends(5L)).thenReturn(List.of());

        List<Utilisateur> result = recommendationService.recommendFriends(5L);

        assertThat(result).isEmpty();
        verifyNoInteractions(utilisateurRepository);
    }
}