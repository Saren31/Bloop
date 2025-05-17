package utcapitole.miage.bloop.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import utcapitole.miage.bloop.model.entity.Utilisateur;
import utcapitole.miage.bloop.service.PostService;
import utcapitole.miage.bloop.service.UtilisateurService;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
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

    private RequestPostProcessor utilisateurPrincipal(Utilisateur utilisateur) {
        return request -> {
            TestingAuthenticationToken auth = new TestingAuthenticationToken(utilisateur, null, "ROLE_USER");
            SecurityContextHolder.getContext().setAuthentication(auth);
            return request;
        };
    }

    @Test
    @WithMockUser(username = "testuser", roles = "USER")
    void testVoirProfilAutre_Succes() throws Exception {
        Utilisateur moi = new Utilisateur();
        moi.setIdUser(1L);
        Utilisateur autre = new Utilisateur();
        autre.setIdUser(2L);

        when(utilisateurService.getUtilisateurParId(2L)).thenReturn(autre);

        mockMvc.perform(get("/profil/voir/2").with(utilisateurPrincipal(moi)))
                .andExpect(status().isOk())
                .andExpect(model().attribute("utilisateur", autre))
                .andExpect(view().name("VoirAutreProfil"));
    }

    @Test
    @WithMockUser(username = "testuser", roles = "USER")
    void testVoirProfilAutre_RedirectionVersMonProfil() throws Exception {
        Utilisateur moi = new Utilisateur();
        moi.setIdUser(1L);

        when(utilisateurService.getUtilisateurParId(1L)).thenReturn(moi);

        mockMvc.perform(get("/profil/voir/1").with(utilisateurPrincipal(moi)))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/profil/voirProfil"));
    }

    @Test
    @WithMockUser(username = "testuser", roles = "USER")
    void testVoirProfilAutre_UtilisateurNonTrouve() throws Exception {
        Utilisateur moi = new Utilisateur();
        moi.setIdUser(1L);

        when(utilisateurService.getUtilisateurParId(99L)).thenReturn(null);

        mockMvc.perform(get("/profil/voir/99").with(utilisateurPrincipal(moi)))
                .andExpect(status().isOk())
                .andExpect(view().name("accueil"));
    }

    @Test
    @WithMockUser(username = "testuser", roles = "USER")
    void testSupprimerProfilConnecte_Succes() throws Exception {
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setIdUser(1L);

        mockMvc.perform(delete("/profil/me")
                        .with(csrf())
                        .with(utilisateurPrincipal(utilisateur)))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/auth/login?deleted"));

        verify(utilisateurService).supprimerUtilisateurEtRelations(1L);
    }

    @Test
    @WithMockUser(username = "testuser", roles = "USER")
    void testSupprimerProfilConnecte_Echec() throws Exception {
        // Mock SecurityContextHolder avec principal non Utilisateur
        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn("notAUser");
        var context = mock(org.springframework.security.core.context.SecurityContext.class);
        when(context.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(context);

        mockMvc.perform(delete("/profil/me").with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/auth/login?error"));

        verify(utilisateurService, never()).supprimerUtilisateurEtRelations(anyLong());
    }
}