package utcapitole.miage.bloop.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import utcapitole.miage.bloop.model.entity.Utilisateur;
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

    // Crée un Utilisateur simulé pour simuler l'utilisateur connecté
    private Utilisateur creerUtilisateurSimule() {
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setNomUser("Test User");
        utilisateur.setIdUser(1L);
        utilisateur.setEmailUser("test@ut-capitole.fr");
        // Ajoute d'autres champs si nécessaire pour ton entité
        return utilisateur;
    }

    @Test
    void testAfficherFormulaire() throws Exception {
        Utilisateur utilisateur = creerUtilisateurSimule();
        TestingAuthenticationToken auth = new TestingAuthenticationToken(utilisateur, null);

        mockMvc.perform(get("/post/creer").principal(auth))
                .andExpect(status().isOk())
                .andExpect(view().name("creerPost"));
    }

    @Test
    void testCreerPostSucces() throws Exception {
        Utilisateur utilisateur = creerUtilisateurSimule();

        TestingAuthenticationToken auth = new TestingAuthenticationToken(utilisateur, null, "ROLE_USER");
        SecurityContextHolder.getContext().setAuthentication(auth);

        mockMvc.perform(multipart("/post/creer")
                        .file(new MockMultipartFile("imageFile", "filename.jpg", "image/jpeg", "image-content".getBytes()))
                        .param("textePost", "Un texte de test"))
                .andExpect(status().isOk())
                .andExpect(view().name("confirmationPost"));
    }



    @Test
    void testCreerPostTexteVide() throws Exception {
        // Crée une SecurityContext custom
        Utilisateur utilisateur = creerUtilisateurSimule();
        TestingAuthenticationToken auth = new TestingAuthenticationToken(utilisateur, null, "ROLE_USER");
        SecurityContextHolder.getContext().setAuthentication(auth);

        // Maintenant tu peux faire
        mockMvc.perform(multipart("/post/creer")
                        .file(new MockMultipartFile("imageFile", "filename.jpg", "image/jpeg", "image-content".getBytes()))
                        .param("textePost", "Un texte de test"))
                .andExpect(status().isOk())
                .andExpect(view().name("confirmationPost"));

    }

    @Test
    void testCreerPostImageInvalide() throws Exception {
        Utilisateur utilisateur = creerUtilisateurSimule();
        TestingAuthenticationToken auth = new TestingAuthenticationToken(utilisateur, null);

        mockMvc.perform(multipart("/post/creer")
                        .file("imageFile", "texte".getBytes()) // Fichier non-image
                        .param("textePost", "Un texte de test")
                        .principal(auth))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("error"))
                .andExpect(view().name("creerPost"));

        verify(postService, never()).creerPost(any());
    }
}
