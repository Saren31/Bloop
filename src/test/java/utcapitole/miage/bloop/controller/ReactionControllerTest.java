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
import utcapitole.miage.bloop.service.ReactionService;
import utcapitole.miage.bloop.service.UtilisateurService;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ReactionController.class)
@ActiveProfiles("test")
@AutoConfigureMockMvc
class ReactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ReactionService reactionService;

    @MockitoBean
    private PostService postService;

    @MockitoBean
    private UtilisateurService utilisateurService;

    @Test
    @WithMockUser(username = "testuser", roles = "USER")
    void testLikePost_UtilisateurConnecte() throws Exception {
        Utilisateur utilisateur = new Utilisateur();
        when(utilisateurService.getUtilisateurConnecte()).thenReturn(utilisateur);

        mockMvc.perform(post("/reaction/like/42").with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/profil/voirProfil"));

        verify(reactionService).toggleLike(42L, utilisateur);
    }

    @Test
    @WithMockUser(username = "testuser", roles = "USER")
    void testLikePost_UtilisateurNonConnecte() throws Exception {
        when(utilisateurService.getUtilisateurConnecte()).thenReturn(null);

        mockMvc.perform(post("/reaction/like/42").with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/profil/voirProfil"));

        verify(reactionService, never()).toggleLike(anyLong(), any());
    }

    @Test
    @WithMockUser(username = "testuser", roles = "USER")
    void testDislikePost_UtilisateurConnecte() throws Exception {
        Utilisateur utilisateur = new Utilisateur();
        when(utilisateurService.getUtilisateurConnecte()).thenReturn(utilisateur);

        mockMvc.perform(post("/reaction/dislike/42").with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/profil/voirProfil"));

        verify(reactionService).toggleDislike(42L, utilisateur);
    }

    @Test
    @WithMockUser(username = "testuser", roles = "USER")
    void testDislikePost_UtilisateurNonConnecte() throws Exception {
        when(utilisateurService.getUtilisateurConnecte()).thenReturn(null);

        mockMvc.perform(post("/reaction/dislike/42").with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/profil/voirProfil"));

        verify(reactionService, never()).toggleDislike(anyLong(), any());
    }
}