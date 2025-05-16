package utcapitole.miage.bloop;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import utcapitole.miage.bloop.model.entity.Utilisateur;
import utcapitole.miage.bloop.repository.UtilisateurRepository;
import utcapitole.miage.bloop.service.UtilisateurService;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UtilisateurServiceTest {

    // ------------------------- Pour US06 Voir mon profil ---------------------- //
    private UtilisateurRepository utilisateurRepository;
    private UtilisateurService utilisateurService;

    @BeforeEach
    void setUp() {
        utilisateurRepository = mock(UtilisateurRepository.class);
        utilisateurService = new UtilisateurService();
        utilisateurService = Mockito.spy(utilisateurService);
        utilisateurService.utilisateurRepository = utilisateurRepository;
    }

    @Test
    void testGetUtilisateurParId_WhenUserExists() {
        Utilisateur mockUser = new Utilisateur();
        mockUser.setIdUser(1L);
        mockUser.setNomUser("Dupont");

        when(utilisateurRepository.findById(1L)).thenReturn(Optional.of(mockUser));

        Utilisateur result = utilisateurService.getUtilisateurParId(1L);

        assertNotNull(result);
        assertEquals("Dupont", result.getNomUser());
        verify(utilisateurRepository, times(1)).findById(1L);
    }

    @Test
    void testGetUtilisateurParId_WhenUserDoesNotExist() {
        when(utilisateurRepository.findById(999L)).thenReturn(Optional.empty());

        Utilisateur result = utilisateurService.getUtilisateurParId(999L);

        assertNull(result);
        verify(utilisateurRepository, times(1)).findById(999L);
    }
}

