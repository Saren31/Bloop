package utcapitole.miage.bloop.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utcapitole.miage.bloop.model.entity.Utilisateur;
import utcapitole.miage.bloop.repository.jpa.UtilisateurRepository;
import utcapitole.miage.bloop.repository.neo4j.UserGraphRepository;

import java.util.List;

import static org.mockito.Mockito.*;

class GraphSyncServiceTest {

    private UtilisateurRepository utilisateurRepository;
    private UserGraphRepository userGraphRepository;
    private GraphSyncService graphSyncService;

    @BeforeEach
    void setUp() {
        utilisateurRepository = mock(UtilisateurRepository.class);
        userGraphRepository = mock(UserGraphRepository.class);
        graphSyncService = new GraphSyncService(utilisateurRepository, userGraphRepository);
    }

    @Test
    void testSyncAllToGraph_SansAmis() {
        Utilisateur u1 = new Utilisateur();
        u1.setIdUser(1L);
        u1.setNomUser("Alice");
        u1.setAmis(List.of());

        when(utilisateurRepository.findAll()).thenReturn(List.of(u1));

        graphSyncService.syncAllToGraph();

        verify(userGraphRepository).save(argThat(node ->
                node.getId().equals(1L) &&
                        node.getNom().equals("Alice") &&
                        (node.getAmis() == null || node.getAmis().isEmpty())
        ));
    }

    @Test
    void testSyncAllToGraph_AvecAmis() {
        Utilisateur u1 = new Utilisateur();
        u1.setIdUser(1L);
        u1.setNomUser("Alice");

        Utilisateur u2 = new Utilisateur();
        u2.setIdUser(2L);
        u2.setNomUser("Bob");

        u1.setAmis(List.of(u2));
        u2.setAmis(List.of());

        when(utilisateurRepository.findAll()).thenReturn(List.of(u1, u2));

        graphSyncService.syncAllToGraph();

        verify(userGraphRepository).save(argThat(node ->
                node.getId().equals(1L) &&
                        node.getNom().equals("Alice") &&
                        node.getAmis().stream().anyMatch(ami -> ami.getId().equals(2L) && ami.getNom().equals("Bob"))
        ));
        verify(userGraphRepository).save(argThat(node ->
                node.getId().equals(2L) &&
                        node.getNom().equals("Bob")
        ));
    }
}