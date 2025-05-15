package utcapitole.miage.bloop.service;

import org.junit.jupiter.api.Test;
import utcapitole.miage.bloop.model.entity.Utilisateur;
import utcapitole.miage.bloop.repository.UtilisateurRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class RelationServiceTest {

    private final UtilisateurRepository utilisateurRepository = mock(UtilisateurRepository.class);
    private final RelationService relationService = new RelationService(utilisateurRepository);

    @Test
    void testDemandeSucces() {
        Utilisateur u1 = new Utilisateur(); u1.setIdUser(1L);
        Utilisateur u2 = new Utilisateur(); u2.setIdUser(2L);

        when(utilisateurRepository.findById(1L)).thenReturn(Optional.of(u1));
        when(utilisateurRepository.findById(2L)).thenReturn(Optional.of(u2));

        String result = relationService.envoyerDemandeAmitie(1L, 2L);

        assertThat(result).contains("succès");
        verify(utilisateurRepository).save(u1);
        verify(utilisateurRepository).save(u2);
    }

    @Test
    void testMemeUtilisateur() {
        String result = relationService.envoyerDemandeAmitie(1L, 1L);
        assertThat(result).contains("Vous ne pouvez pas");
    }

    @Test
    void testUtilisateurInexistant() {
        when(utilisateurRepository.findById(1L)).thenReturn(Optional.empty());

        String result = relationService.envoyerDemandeAmitie(1L, 2L);
        assertThat(result).contains("n'existe pas");
    }

    @Test
    void testDemandeDejaEnvoyee() {
        Utilisateur u1 = new Utilisateur(); u1.setIdUser(1L);
        Utilisateur u2 = new Utilisateur(); u2.setIdUser(2L);

        u1.getDemandesEnvoyees().add(u2);

        when(utilisateurRepository.findById(1L)).thenReturn(Optional.of(u1));
        when(utilisateurRepository.findById(2L)).thenReturn(Optional.of(u2));

        String result = relationService.envoyerDemandeAmitie(1L, 2L);
        assertThat(result).contains("déjà envoyé");
    }
}

