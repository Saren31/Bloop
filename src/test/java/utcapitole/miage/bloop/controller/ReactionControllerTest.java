package utcapitole.miage.bloop.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import utcapitole.miage.bloop.model.entity.Utilisateur;
import utcapitole.miage.bloop.service.PostService;
import utcapitole.miage.bloop.service.ReactionService;
import utcapitole.miage.bloop.service.UtilisateurService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class ReactionControllerTest {

    @Mock
    private ReactionService reactionService;

    @Mock
    private PostService postService;

    @Mock
    private UtilisateurService utilisateurService;

    @InjectMocks
    private ReactionController controller;

    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
        // Montee du controller en standalone (pas de contexte Spring complet)
        this.mockMvc = MockMvcBuilders
                .standaloneSetup(controller)
                .build();
    }

    @Test
    void testLikePost_UtilisateurConnecte() throws Exception {
        Utilisateur utilisateur = new Utilisateur();
        when(utilisateurService.getUtilisateurConnecte()).thenReturn(utilisateur);

        mockMvc.perform(post("/reaction/like/42").with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/profil/voirProfil"));

        verify(reactionService).toggleLike(42L, utilisateur);
    }

    @Test
    void testLikePost_UtilisateurNonConnecte() throws Exception {
        when(utilisateurService.getUtilisateurConnecte()).thenReturn(null);

        mockMvc.perform(post("/reaction/like/42").with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/profil/voirProfil"));

        verify(reactionService, never()).toggleLike(anyLong(), any());
    }

    @Test
    void testDislikePost_UtilisateurConnecte() throws Exception {
        Utilisateur utilisateur = new Utilisateur();
        when(utilisateurService.getUtilisateurConnecte()).thenReturn(utilisateur);

        mockMvc.perform(post("/reaction/dislike/42").with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/profil/voirProfil"));

        verify(reactionService).toggleDislike(42L, utilisateur);
    }

    @Test
    void testDislikePost_UtilisateurNonConnecte() throws Exception {
        when(utilisateurService.getUtilisateurConnecte()).thenReturn(null);

        mockMvc.perform(post("/reaction/dislike/42").with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/profil/voirProfil"));

        verify(reactionService, never()).toggleDislike(anyLong(), any());
    }
}
