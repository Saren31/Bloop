package utcapitole.miage.bloop.service;

import org.junit.jupiter.api.Test;
import utcapitole.miage.bloop.model.entity.Utilisateur;
import utcapitole.miage.bloop.repository.UtilisateurRepository;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class UtilisateurServiceTest {

    private final UtilisateurRepository utilisateurRepository = mock(UtilisateurRepository.class);
    private final UtilisateurService service = new UtilisateurService(utilisateurRepository);

    @Test
    void testRecupererTousLesUtilisateurs() {
        Utilisateur u1 = new Utilisateur();
        when(utilisateurRepository.findAll()).thenReturn(List.of(u1));

        List<Utilisateur> result = service.recupererTousLesUtilisateurs();

        assertThat(result).hasSize(1);
    }

    @Test
    void testRecupererUtilisateurParId() {
        Utilisateur u1 = new Utilisateur();
        when(utilisateurRepository.findById(1L)).thenReturn(Optional.of(u1));

        Optional<Utilisateur> result = service.recupererUtilisateurParId(1L);

        assertThat(result).contains(u1);
    }
}

