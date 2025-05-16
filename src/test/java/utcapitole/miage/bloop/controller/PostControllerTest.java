package utcapitole.miage.bloop.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import utcapitole.miage.bloop.model.entity.Post;
import utcapitole.miage.bloop.repository.UtilisateurRepository;
import utcapitole.miage.bloop.service.PostService;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PostController.class)
@AutoConfigureMockMvc(addFilters = false)
class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PostService postService;

    @MockitoBean
    private PasswordEncoder passwordEncoder;

    @MockitoBean
    private UtilisateurRepository utilisateurRepository;

    @Test
    @WithMockUser
    void testAfficherFormulaire() throws Exception {
        mockMvc.perform(get("/post/creer"))
                .andExpect(status().isOk())
                .andExpect(view().name("creerPost"));
    }

    @Test
    @WithMockUser
    void testCreerPostSucces() throws Exception {
        mockMvc.perform(get("/post/creer")) // Simule l'accès au formulaire
                .andExpect(status().isOk());

        mockMvc.perform(multipart("/post/creer")
                        .file("imageFile", "image-content".getBytes())
                        .param("textePost", "Un texte de test"))
                .andExpect(status().isOk())
                .andExpect(view().name("confirmationPost"));
    }

    @Test
    @WithMockUser
    void testCreerPostTexteVide() throws Exception {
        mockMvc.perform(post("/post/creer")
                        .param("textePost", ""))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("error"))
                .andExpect(view().name("creerPost"));

        verify(postService, never()).creerPost(any());
    }

    @Test
    @WithMockUser
    void testCreerPostImageInvalide() throws Exception {
        mockMvc.perform(multipart("/post/creer")
                        .file("imageFile", "texte".getBytes())
                        .param("textePost", "Un texte de test"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("error"))
                .andExpect(view().name("creerPost"));

        verify(postService, never()).creerPost(any());
    }
}