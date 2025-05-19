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
import utcapitole.miage.bloop.model.entity.Post;
import utcapitole.miage.bloop.model.entity.Utilisateur;
import utcapitole.miage.bloop.repository.GroupeRepository;
import utcapitole.miage.bloop.repository.UtilisateurRepository;
import utcapitole.miage.bloop.service.CommentaireService;
import utcapitole.miage.bloop.service.PostService;
import utcapitole.miage.bloop.service.UtilisateurService;

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

    @MockitoBean
    private GroupeRepository groupeRepository;

    @MockitoBean
    private UtilisateurService utilisateurService;

    @MockitoBean
    private CommentaireService commentaireService;


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

    @Test
    void testCommenterPost() throws Exception {
        Utilisateur utilisateur = creerUtilisateurSimule();
        TestingAuthenticationToken auth = new TestingAuthenticationToken(utilisateur, null);
        SecurityContextHolder.getContext().setAuthentication(auth);

        when(commentaireService.ajouterCommentaire(1L, "Coucou", utilisateur)).thenReturn(null);

        mockMvc.perform(post("/post/1/commenter")
                        .param("texte", "Coucou")
                        .principal(auth))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/post/1"));
    }

    @Test
    void testAfficherPost() throws Exception {
        Post post = new Post();
        post.setIdPost(1L);

        when(postService.getPostParId(1L)).thenReturn(post);
        when(commentaireService.getCommentairesParPost(1L)).thenReturn(java.util.Collections.emptyList());

        mockMvc.perform(get("/post/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("afficherPost"))
                .andExpect(model().attributeExists("post"))
                .andExpect(model().attributeExists("commentaires"));
    }

    @Test
    void testSupprimerPost_Succes() throws Exception {
        Utilisateur utilisateur = creerUtilisateurSimule();
        Post post = new Post();
        post.setIdPost(1L);
        post.setUtilisateur(utilisateur);

        when(postService.getPostParId(1L)).thenReturn(post);

        TestingAuthenticationToken auth = new TestingAuthenticationToken(utilisateur, null);
        SecurityContextHolder.getContext().setAuthentication(auth);

        mockMvc.perform(delete("/post/1/supprimer").principal(auth))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/profil/voirProfil"));

        verify(postService).supprimerPost(1L);
    }

    @Test
    void testSupprimerPost_PostInexistant() throws Exception {
        Utilisateur utilisateur = creerUtilisateurSimule();

        when(postService.getPostParId(1L)).thenThrow(new IllegalArgumentException("Post introuvable pour l'identifiant donné."));

        TestingAuthenticationToken auth = new TestingAuthenticationToken(utilisateur, null);
        SecurityContextHolder.getContext().setAuthentication(auth);

        mockMvc.perform(delete("/post/1/supprimer").principal(auth))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/profil/voirProfil"));

        verify(postService, never()).supprimerPost(1L);
    }

    @Test
    void testSupprimerPost_NonAutorise() throws Exception {
        Utilisateur utilisateur = creerUtilisateurSimule();
        Utilisateur autre = new Utilisateur();
        autre.setIdUser(2L);

        Post post = new Post();
        post.setIdPost(1L);
        post.setUtilisateur(autre);

        when(postService.getPostParId(1L)).thenReturn(post);

        TestingAuthenticationToken auth = new TestingAuthenticationToken(utilisateur, null);
        SecurityContextHolder.getContext().setAuthentication(auth);

        mockMvc.perform(delete("/post/1/supprimer").principal(auth))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/profil/voirProfil"));

        verify(postService, never()).supprimerPost(1L);
    }
}
