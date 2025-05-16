package utcapitole.miage.bloop.service;

import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import utcapitole.miage.bloop.model.entity.Utilisateur;
import utcapitole.miage.bloop.repository.UtilisateurRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class CustomUserDetailsServiceTest {

    private final UtilisateurRepository utilisateurRepository = mock(UtilisateurRepository.class);
    private final CustomUserDetailsService service = new CustomUserDetailsService(utilisateurRepository);

    @Test
    void testLoadUserByUsername_Success() {
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setEmailUser("test@ut-capitole.fr");

        when(utilisateurRepository.findByEmailUser("test@ut-capitole.fr")).thenReturn(utilisateur);

        assertThat(service.loadUserByUsername("test@ut-capitole.fr")).isEqualTo(utilisateur);
    }

    @Test
    void testLoadUserByUsername_NotFound() {
        when(utilisateurRepository.findByEmailUser("unknown@ut-capitole.fr")).thenReturn(null);

        assertThrows(UsernameNotFoundException.class, () ->
                service.loadUserByUsername("unknown@ut-capitole.fr"));
    }
}
