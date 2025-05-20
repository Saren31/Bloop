package utcapitole.miage.bloop.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import utcapitole.miage.bloop.model.entity.Post;
import utcapitole.miage.bloop.model.entity.Utilisateur;
import utcapitole.miage.bloop.repository.jpa.GroupeRepository;
import utcapitole.miage.bloop.repository.jpa.UtilisateurRepository;
import utcapitole.miage.bloop.service.CommentaireService;
import utcapitole.miage.bloop.service.PostService;
import utcapitole.miage.bloop.service.UtilisateurService;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class PostControllerTest {

    @Mock
    private PostService postService;

    @Mock
    private org.springframework.security.crypto.password.PasswordEncoder passwordEncoder;

    @Mock
    private UtilisateurRepository utilisateurRepository;

    @Mock
    private GroupeRepository groupeRepository;

    @Mock
    private UtilisateurService utilisateurService;

    @Mock
    private CommentaireService commentaireService;

    @InjectMocks
    private PostController controller;

    private MockMvc mockMvc;

    private Utilisateur creerUtilisateurSimule() {
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setNomUser("Test User");
        utilisateur.setIdUser(1L);
        utilisateur.setEmailUser("test@ut-capitole.fr");
        return utilisateur;
    }

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
                        .file(new MockMultipartFile(
                                "imageFile",
                                "filename.jpg",
                                "image/jpeg",
                                "image-content".getBytes()))
                        .param("textePost", "Un texte de test"))
                .andExpect(status().isOk())
                .andExpect(view().name("confirmationPost"));
    }

    @Test
    void testCreerPostImageInvalide() throws Exception {
        Utilisateur utilisateur = creerUtilisateurSimule();
        TestingAuthenticationToken auth = new TestingAuthenticationToken(utilisateur, null);

        mockMvc.perform(multipart("/post/creer")
                        .file(new MockMultipartFile(
                                "imageFile",
                                "",
                                "application/octet-stream",
                                "texte".getBytes()))
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
        when(commentaireService.getCommentairesParPost(1L))
                .thenReturn(Collections.emptyList());

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
        when(postService.getPostParId(1L))
                .thenThrow(new IllegalArgumentException("Post introuvable pour l'identifiant donné."));
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

    @Test
    void testAfficherFormulaireModification_Autorise() throws Exception {
        Utilisateur utilisateur = creerUtilisateurSimule();
        Post post = new Post();
        post.setIdPost(1L);
        post.setUtilisateur(utilisateur);

        when(postService.getPostParId(1L)).thenReturn(post);
        TestingAuthenticationToken auth = new TestingAuthenticationToken(utilisateur, null);
        SecurityContextHolder.getContext().setAuthentication(auth);

        mockMvc.perform(get("/post/1/modifier").principal(auth))
                .andExpect(status().isOk())
                .andExpect(view().name("modifierPost"))
                .andExpect(model().attributeExists("post"));
    }

    @Test
    void testAfficherFormulaireModification_NonAutorise() throws Exception {
        Utilisateur utilisateur = creerUtilisateurSimule();
        Utilisateur autre = new Utilisateur();
        autre.setIdUser(2L);

        Post post = new Post();
        post.setIdPost(1L);
        post.setUtilisateur(autre);

        when(postService.getPostParId(1L)).thenReturn(post);
        TestingAuthenticationToken auth = new TestingAuthenticationToken(utilisateur, null);
        SecurityContextHolder.getContext().setAuthentication(auth);

        mockMvc.perform(get("/post/1/modifier").principal(auth))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/profil/voirProfil"));
    }

    @Test
    void testModifierPost_Autorise() throws Exception {
        // Prépare l’utilisateur et le post existant
        Utilisateur utilisateur = creerUtilisateurSimule();
        Post post = new Post();
        post.setIdPost(1L);
        post.setUtilisateur(utilisateur);

        when(postService.getPostParId(1L)).thenReturn(post);

        // <-- Ici, on stubbe la méthode void save(...)
        doNothing().when(postService).save(any(Post.class));

        // Simule l’authentification
        TestingAuthenticationToken auth = new TestingAuthenticationToken(utilisateur, null);
        SecurityContextHolder.getContext().setAuthentication(auth);

        // Appel du contrôleur
        mockMvc.perform(post("/post/1/modifier")
                        .param("textePost", "Nouveau texte")
                        .principal(auth))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/profil/voirProfil"));

        // Vérifie bien l’appel à la méthode void
        verify(postService).save(any(Post.class));
    }

    @Test
    void testConfirmerSuppression_Succes() throws Exception {
        Post post = new Post();
        post.setIdPost(1L);

        when(postService.getPostParId(1L)).thenReturn(post);

        mockMvc.perform(get("/post/1/confirmer-suppression"))
                .andExpect(status().isOk())
                .andExpect(view().name("confirmerSuppression"))
                .andExpect(model().attributeExists("post"));
    }
}
