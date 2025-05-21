package utcapitole.miage.bloop.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import utcapitole.miage.bloop.model.entity.Evenement;
import utcapitole.miage.bloop.model.entity.Utilisateur;
import utcapitole.miage.bloop.repository.jpa.UtilisateurRepository;
import utcapitole.miage.bloop.service.EvenementService;
import utcapitole.miage.bloop.service.UtilisateurService;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class EvenementControllerTest {

    @Mock
    private EvenementService evenementService;

    @Mock
    private UtilisateurService utilisateurService;

    @Mock
    private UtilisateurRepository utilisateurRepository;

    @InjectMocks
    private EvenementController controller;

    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
        InternalResourceViewResolver vr = new InternalResourceViewResolver();
        vr.setPrefix("");
        vr.setSuffix(".html");
        this.mockMvc = MockMvcBuilders
                .standaloneSetup(controller)
                .setViewResolvers(vr)
                .build();
    }

    @Test
    void shouldAfficherFormulaireCreerEvenement() throws Exception {
        mockMvc.perform(get("/evenement/creer"))
                .andExpect(status().isOk())
                .andExpect(view().name("creerEvenement"))
                .andExpect(model().attributeExists("evenement"));
    }

    @Test
    void shouldCreerEvenementAndRedirect() throws Exception {
        // On simule un utilisateur connecté
        Utilisateur utilisateur = new Utilisateur();
        when(utilisateurService.getUtilisateurConnecte()).thenReturn(utilisateur);

        mockMvc.perform(post("/evenement/creer")
                        .param("titre", "Titre")
                        .param("description", "Desc")
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/profil/voirProfil"));

        // Vérifie que le service de création a bien été appelé
        verify(evenementService).creerEvenement(any(Evenement.class));
    }

    @Test
    void shouldAfficherMesEvenements() throws Exception {
        // Prépare un utilisateur avec ID pour filtrer ses événements
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setIdUser(1L);
        when(utilisateurService.getUtilisateurConnecte()).thenReturn(utilisateur);
        when(evenementService.getEvenementsParOrganisateur(1L)).thenReturn(List.of());

        mockMvc.perform(get("/evenement/mesEvenements"))
                .andExpect(status().isOk())
                .andExpect(view().name("mesEvenements"))
                .andExpect(model().attributeExists("evenements"));
    }

    @Test
    void shouldAfficherDetailEvenement() throws Exception {
        Evenement evenement = new Evenement();
        evenement.setId(10L);
        Utilisateur utilisateur = new Utilisateur();
        when(evenementService.getEvenementParId(10L)).thenReturn(evenement);
        when(utilisateurService.getUtilisateurConnecte()).thenReturn(utilisateur);
        when(evenementService.estInscrit(evenement, utilisateur)).thenReturn(true);
        when(evenementService.estInteresse(evenement, utilisateur)).thenReturn(false);

        mockMvc.perform(get("/evenement/10"))
                .andExpect(status().isOk())
                .andExpect(view().name("detailEvenement"))
                .andExpect(model().attribute("evenement", evenement))
                .andExpect(model().attribute("inscrit", true))
                .andExpect(model().attribute("interesse", false));
    }

    @Test
    void shouldRedirectIfEvenementNotFound() throws Exception {
        when(evenementService.getEvenementParId(99L)).thenReturn(null);

        mockMvc.perform(get("/evenement/99"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/profil/voirProfil"));
    }

    @Test
    void shouldInscrireUtilisateur() throws Exception {
        Evenement evenement = new Evenement();
        Utilisateur utilisateur = new Utilisateur();
        when(evenementService.getEvenementParId(5L)).thenReturn(evenement);
        when(utilisateurService.getUtilisateurConnecte()).thenReturn(utilisateur);

        mockMvc.perform(post("/evenement/inscription/5"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/evenement/5"));

        verify(evenementService).inscrireUtilisateur(evenement, utilisateur);
    }

    @Test
    void shouldDesinscrireUtilisateur() throws Exception {
        Evenement evenement = new Evenement();
        Utilisateur utilisateur = new Utilisateur();
        when(evenementService.getEvenementParId(6L)).thenReturn(evenement);
        when(utilisateurService.getUtilisateurConnecte()).thenReturn(utilisateur);

        mockMvc.perform(post("/evenement/desinscription/6"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/evenement/6"));

        verify(evenementService).retirerUtilisateur(evenement, utilisateur);
    }

    @Test
    void shouldMarquerInteresse() throws Exception {
        Evenement evenement = new Evenement();
        Utilisateur utilisateur = new Utilisateur();
        when(evenementService.getEvenementParId(7L)).thenReturn(evenement);
        when(utilisateurService.getUtilisateurConnecte()).thenReturn(utilisateur);

        mockMvc.perform(post("/evenement/interet/7"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/evenement/7"));

        verify(evenementService).marquerInteresse(evenement, utilisateur);
    }

    @Test
    void shouldRetirerInteresse() throws Exception {
        Evenement evenement = new Evenement();
        Utilisateur utilisateur = new Utilisateur();
        when(evenementService.getEvenementParId(8L)).thenReturn(evenement);
        when(utilisateurService.getUtilisateurConnecte()).thenReturn(utilisateur);

        mockMvc.perform(post("/evenement/retirerInteret/8"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/evenement/8"));

        verify(evenementService).retirerInteresse(evenement, utilisateur);
    }

    @Test
    void shouldAfficherMesInscriptions() throws Exception {
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setIdUser(1L);
        Evenement e = new Evenement();
        when(utilisateurService.getUtilisateurConnecte()).thenReturn(utilisateur);
        when(evenementService.getEvenementsOuUtilisateurEstInscrit(1L)).thenReturn(List.of(e));

        mockMvc.perform(get("/evenement/mesInscriptions"))
                .andExpect(status().isOk())
                .andExpect(view().name("mesInscriptions"))
                .andExpect(model().attributeExists("evenements"));
    }

    @Test
    void shouldRedirectToLoginIfNotConnectedOnMesInscriptions() throws Exception {
        when(utilisateurService.getUtilisateurConnecte()).thenReturn(null);

        mockMvc.perform(get("/evenement/mesInscriptions"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"));
    }
}
