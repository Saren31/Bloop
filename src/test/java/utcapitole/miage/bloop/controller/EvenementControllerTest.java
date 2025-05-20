package utcapitole.miage.bloop.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import utcapitole.miage.bloop.model.entity.Evenement;
import utcapitole.miage.bloop.model.entity.Utilisateur;
import utcapitole.miage.bloop.repository.jpa.UtilisateurRepository;
import utcapitole.miage.bloop.service.EvenementService;
import utcapitole.miage.bloop.service.UtilisateurService;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EvenementController.class)
@ActiveProfiles("test")
@AutoConfigureMockMvc(addFilters = false)
class EvenementControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private EvenementService evenementService;

    @MockitoBean
    private UtilisateurService utilisateurService;

    @MockitoBean
    private UtilisateurRepository utilisateurRepository;

    @Test
    void testAfficherFormulaire() throws Exception {
        mockMvc.perform(get("/evenement/creer"))
                .andExpect(status().isOk())
                .andExpect(view().name("creerEvenement"))
                .andExpect(model().attributeExists("evenement"));
    }

    @Test
    void testCreerEvenementRedirection() throws Exception {
        Utilisateur utilisateur = new Utilisateur();
        when(utilisateurService.getUtilisateurConnecte()).thenReturn(utilisateur);

        mockMvc.perform(post("/evenement/creer")
                        .param("titre", "Titre")
                        .param("description", "Desc"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/profil/voirProfil"));

        verify(evenementService).creerEvenement(any(Evenement.class));
    }

    @Test
    void testAfficherMesEvenements() throws Exception {
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setIdUser(1L);
        when(utilisateurService.getUtilisateurConnecte()).thenReturn(utilisateur);
        when(evenementService.getEvenementsParOrganisateur(1L)).thenReturn(List.of());

        mockMvc.perform(get("/evenement/mesEvenements"))
                .andExpect(status().isOk())
                .andExpect(view().name("mesEvenements"))
                .andExpect(model().attributeExists("evenements"));
    }
}