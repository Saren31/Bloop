package utcapitole.miage.bloop.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import utcapitole.miage.bloop.model.entity.Utilisateur;
import utcapitole.miage.bloop.service.PostService;
import utcapitole.miage.bloop.service.UtilisateurService;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProfilController.class)
@ActiveProfiles("test")
@AutoConfigureMockMvc
class ProfilControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UtilisateurService utilisateurService;

    @MockitoBean
    private PostService postService;

    @Test
    @WithMockUser(username = "testuser", roles = "USER")
    void testAfficherMonProfil_Succes() throws Exception {
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setIdUser(1L);
        utilisateur.setNomUser("Test");

        when(utilisateurService.getUtilisateurConnecte()).thenReturn(utilisateur);
        when(utilisateurService.getUtilisateurParId(1L)).thenReturn(utilisateur);

        mockMvc.perform(get("/profil/voirProfil"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("utilisateur"))
                .andExpect(view().name("voirProfil"));
    }

    @Test
    @WithMockUser(username = "testuser", roles = "USER")
    void testAfficherMonProfil_RedirectionAccueil() throws Exception {
        when(utilisateurService.getUtilisateurConnecte()).thenReturn(null);

        mockMvc.perform(get("/profil/voirProfil"))
                .andExpect(status().isOk())
                .andExpect(view().name("accueil"));
    }
}