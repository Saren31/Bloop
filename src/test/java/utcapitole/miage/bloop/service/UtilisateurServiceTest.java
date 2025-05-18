package utcapitole.miage.bloop.service;

import org.junit.jupiter.api.Test;
import org.springframework.security.core.Authentication;
import utcapitole.miage.bloop.model.entity.Utilisateur;
import utcapitole.miage.bloop.repository.EvenementRepository;
import utcapitole.miage.bloop.repository.PostRepository;
import utcapitole.miage.bloop.repository.UtilisateurRepository;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

/**
 * Classe de test pour la classe UtilisateurService.
 * Utilise Mockito pour simuler le comportement du repository.
 */
class UtilisateurServiceTest {

    // Mock du repository UtilisateurRepository
    private final UtilisateurRepository utilisateurRepository = mock(UtilisateurRepository.class);
    private final EvenementRepository evenementRepository = mock(EvenementRepository.class);
    private final PostRepository postRepository = mock(PostRepository.class);


    // Instance du service à tester
    private final UtilisateurService service = new UtilisateurService(utilisateurRepository, postRepository);


    /**
     * Teste la méthode recupererTousLesUtilisateurs.
     * Vérifie que la méthode retourne tous les utilisateurs présents dans le repository.
     */
    @Test
    void testRecupererTousLesUtilisateurs() {
        // Préparation des données de test
        Utilisateur u1 = new Utilisateur();
        when(utilisateurRepository.findAll()).thenReturn(List.of(u1));

        // Appel de la méthode à tester
        List<Utilisateur> result = service.recupererTousLesUtilisateurs();

        // Vérification des résultats
        assertThat(result).hasSize(1);
    }

    /**
     * Teste la méthode recupererUtilisateurParId.
     * Vérifie que la méthode retourne un utilisateur correspondant à l'ID donné.
     */
    @Test
    void testRecupererUtilisateurParId() {
        // Préparation des données de test
        Utilisateur u1 = new Utilisateur();
        when(utilisateurRepository.findById(1L)).thenReturn(Optional.of(u1));

        // Appel de la méthode à tester
        Optional<Utilisateur> result = service.recupererUtilisateurParId(1L);

        // Vérification des résultats
        assertThat(result).contains(u1);
    }

    /**
     * Teste la méthode rechercherParPseudo.
     * Vérifie que la méthode retourne une liste d'utilisateurs dont le pseudo commence par une chaîne donnée.
     */
    @Test
    void testRechercherParPseudo() {
        // Préparation des données de test
        Utilisateur u1 = new Utilisateur();
        u1.setPseudoUser("testUser1");
        Utilisateur u2 = new Utilisateur();
        u2.setPseudoUser("testUser2");

        when(utilisateurRepository.findByPseudoStartingWith("test")).thenReturn(List.of(u1, u2));

        // Appel de la méthode à tester
        List<Utilisateur> result = service.rechercherParPseudo("test");

        // Vérification des résultats
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getPseudoUser()).isEqualTo("testUser1");
        assertThat(result.get(1).getPseudoUser()).isEqualTo("testUser2");
    }

    @Test
    void testGetUtilisateurParId_found() {
        Utilisateur u1 = new Utilisateur();
        when(utilisateurRepository.findById(1L)).thenReturn(Optional.of(u1));

        Utilisateur result = service.getUtilisateurParId(1L);

        assertThat(result).isEqualTo(u1);
    }

    @Test
    void testGetUtilisateurParId_notFound() {
        when(utilisateurRepository.findById(2L)).thenReturn(Optional.empty());

        Utilisateur result = service.getUtilisateurParId(2L);

        assertThat(result).isNull();
    }

    @Test
    void testGetUtilisateurConnecte() {
        Utilisateur utilisateur = new Utilisateur();
        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(utilisateur);

        // Mock SecurityContextHolder
        var context = mock(org.springframework.security.core.context.SecurityContext.class);
        when(context.getAuthentication()).thenReturn(authentication);
        org.springframework.security.core.context.SecurityContextHolder.setContext(context);

        Utilisateur result = service.getUtilisateurConnecte();

        assertThat(result).isEqualTo(utilisateur);
    }

    @Test
    void testSupprimerUtilisateurEtRelations() {
        Utilisateur utilisateur = mock(Utilisateur.class);
        when(utilisateurRepository.findById(1L)).thenReturn(Optional.of(utilisateur));
        // Mocks pour les relations
        when(utilisateur.getGroupes()).thenReturn(new java.util.ArrayList<>());
        when(utilisateur.getAmis()).thenReturn(new java.util.ArrayList<>());
        when(utilisateur.getDemandesEnvoyees()).thenReturn(new java.util.ArrayList<>());
        when(utilisateur.getDemandesRecues()).thenReturn(new java.util.ArrayList<>());

        service.supprimerUtilisateurEtRelations(1L);

        verify(postRepository).deleteAllByUtilisateur(utilisateur);
        verify(utilisateurRepository).delete(utilisateur);
    }

    @Test
    void testSaveUtilisateur() {
        Utilisateur utilisateur = new Utilisateur();
        service.save(utilisateur);
        verify(utilisateurRepository).save(utilisateur);
    }
}