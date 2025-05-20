package utcapitole.miage.bloop.controller;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import utcapitole.miage.bloop.model.entity.Evenement;
import utcapitole.miage.bloop.model.entity.Post;
import utcapitole.miage.bloop.model.entity.Utilisateur;
import utcapitole.miage.bloop.service.EvenementService;
import utcapitole.miage.bloop.service.PostService;
import utcapitole.miage.bloop.service.ReactionService;
import utcapitole.miage.bloop.service.UtilisateurService;

import java.io.IOException;
import java.security.Principal;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class ProfilControllerTest {

    @Mock private UtilisateurService utilisateurService;
    @Mock private PostService postService;
    @Mock private EvenementService evenementService;
    @Mock private ReactionService reactionService;

    @InjectMocks
    private ProfilController controller;

    private MockMvc mockMvc;

    private TestingAuthenticationToken auth(Utilisateur u) {
        return new TestingAuthenticationToken(u, null, "ROLE_USER");
    }

    private Utilisateur creerUtilisateurSimule() {
        Utilisateur u = new Utilisateur();
        u.setIdUser(1L);
        u.setNomUser("Test");
        return u;
    }

    /**
     * Filter de test : lit request.getUserPrincipal() pour l’authentification,
     * ou crée un AnonymousAuthenticationToken si null.
     */
    static class TestSecurityContextFilter implements Filter {
        @Override
        public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
                throws IOException, ServletException {
            HttpServletRequest request = (HttpServletRequest) req;
            Principal p = request.getUserPrincipal();
            Authentication auth;
            if (p instanceof Authentication) {
                auth = (Authentication) p;
            } else {
                auth = new AnonymousAuthenticationToken(
                        "key", "anonymousUser",
                        AuthorityUtils.createAuthorityList("ROLE_ANONYMOUS")
                );
            }
            SecurityContextHolder.getContext().setAuthentication(auth);
            chain.doFilter(req, res);
        }
    }

    @BeforeEach
    void setup() {
        SecurityContextHolder.clearContext();

        InternalResourceViewResolver vr = new InternalResourceViewResolver();
        vr.setPrefix("");
        vr.setSuffix(".html");

        this.mockMvc = MockMvcBuilders
                .standaloneSetup(controller)
                .addFilters(new TestSecurityContextFilter())  // <— ajoute notre filter
                .setViewResolvers(vr)
                .build();
    }

    @Test
    void testAfficherMonProfil_Succes() throws Exception {
        // Given
        Utilisateur u = new Utilisateur();
        u.setIdUser(1L);
        u.setNomUser("Test");

        // on garde uniquement le stub utilisé par le contrôleur
        when(utilisateurService.getUtilisateurConnecte()).thenReturn(u);
        // on supprime le when(utilisateurService.getUtilisateurById(1L))...

        // When / Then
        mockMvc.perform(get("/profil/voirProfil").principal(auth(u)))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("utilisateur"))
                .andExpect(view().name("voirProfil"));
    }

    @Test
    void testAfficherMonProfil_RedirectionAccueil() throws Exception {
        when(utilisateurService.getUtilisateurConnecte()).thenReturn(null);

        mockMvc.perform(get("/profil/voirProfil"))
                .andExpect(status().isOk())
                .andExpect(view().name("accueil"));
    }

    @Test
    void testVoirProfilAutre_Succes() throws Exception {
        Utilisateur moi = new Utilisateur();
        moi.setIdUser(1L);
        Utilisateur autre = new Utilisateur();
        autre.setIdUser(2L);

        when(utilisateurService.getUtilisateurById(2L)).thenReturn(autre);

        mockMvc.perform(get("/profil/voir/2").principal(auth(moi)))
                .andExpect(status().isOk())
                .andExpect(model().attribute("utilisateur", autre))
                .andExpect(view().name("VoirAutreProfil"));
    }

    @Test
    void testVoirProfilAutre_RedirectionVersMonProfil() throws Exception {
        Utilisateur moi = new Utilisateur();
        moi.setIdUser(1L);

        when(utilisateurService.getUtilisateurById(1L)).thenReturn(moi);

        mockMvc.perform(get("/profil/voir/1").principal(auth(moi)))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/profil/voirProfil"));
    }

    @Test
    void testVoirProfilAutre_UtilisateurNonTrouve() throws Exception {
        Utilisateur moi = new Utilisateur();
        moi.setIdUser(1L);

        when(utilisateurService.getUtilisateurById(99L)).thenReturn(null);

        mockMvc.perform(get("/profil/voir/99").principal(auth(moi)))
                .andExpect(status().isOk())
                .andExpect(view().name("accueil"));
    }

    @Test
    void testSupprimerProfilConnecte_Succes() throws Exception {
        // 1) Prépare l’utilisateur avec ID=1
        Utilisateur u = new Utilisateur();
        u.setIdUser(1L);
        // pas besoin de stub utilisateurService.getUtilisateurConnecte()

        // 2) Envoie la requête en fournissant le principal
        mockMvc.perform(delete("/profil/me")
                        .principal(auth(u))
                        .with(csrf())
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/auth/login?deleted"));

        // 3) Vérifie l’appel au service
        verify(utilisateurService).supprimerUtilisateurEtRelations(1L);
    }

    @Test
    void testSupprimerProfilConnecte_Echec() throws Exception {
        // On vide à nouveau ou pas de contexte → getAuthentication() valide mais getPrincipal() non Utilisateur
        SecurityContextHolder.clearContext();
        TestingAuthenticationToken badAuth =
                new TestingAuthenticationToken("notAUser", null);
        SecurityContextHolder.getContext().setAuthentication(badAuth);

        mockMvc.perform(delete("/profil/me").with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/auth/login?error"));

        verify(utilisateurService, never()).supprimerUtilisateurEtRelations(anyLong());
    }


    @Test
    void testAfficherFormulaireModification() throws Exception {
        Utilisateur u = new Utilisateur();
        when(utilisateurService.getUtilisateurConnecte()).thenReturn(u);

        mockMvc.perform(get("/profil/modifier").principal(auth(u)))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("utilisateur"))
                .andExpect(view().name("modifierProfil"));
    }

    @Test
    void testAfficherFormulaireModification_NonConnecte() throws Exception {
        when(utilisateurService.getUtilisateurConnecte()).thenReturn(null);

        mockMvc.perform(get("/profil/modifier"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/auth/login"));
    }

    @Test
    void testModifierProfil_Succes() throws Exception {
        Utilisateur u = new Utilisateur();
        when(utilisateurService.getUtilisateurConnecte()).thenReturn(u);

        mockMvc.perform(post("/profil/modifier")
                        .param("nomUser", "NouveauNom")
                        .param("prenomUser", "NouveauPrenom")
                        .param("pseudoUser", "NouveauPseudo")
                        .param("telUser", "1234")
                        .param("visibiliteUser", "true")
                        .principal(auth(u))
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/profil/voirProfil"));

        verify(utilisateurService).save(any(Utilisateur.class));
    }

    @Test
    void testModifierProfil_NonConnecte() throws Exception {
        when(utilisateurService.getUtilisateurConnecte()).thenReturn(null);

        mockMvc.perform(post("/profil/modifier")
                        .param("nomUser", "NouveauNom")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/auth/login"));
    }

    @Test
    void testRedirectionProfilParDefaut() throws Exception {
        mockMvc.perform(get("/profil"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/profil/voirProfil"));
    }

    @Test
    void testAfficherMonProfil_AvecEvenementsNull() throws Exception {
        Utilisateur u = new Utilisateur();
        u.setIdUser(1L);

        when(utilisateurService.getUtilisateurConnecte()).thenReturn(u);
        when(postService.getPostsByUtilisateur(1L)).thenReturn(List.of());
        when(utilisateurService.getEvenementsParUtilisateur(u)).thenReturn(null);

        mockMvc.perform(get("/profil/voirProfil").principal(auth(u)))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("utilisateur"))
                .andExpect(model().attributeExists("posts"))
                .andExpect(model().attributeExists("evenements"))
                .andExpect(view().name("voirProfil"));
    }

    @Test
    void testAfficherMonProfil_AvecEvenementsEtReactions() throws Exception {
        Utilisateur u = new Utilisateur();
        u.setIdUser(1L);

        // Prépare un post avec réactions
        Post post = new Post();
        post.setIdPost(10L);

        Evenement event = new Evenement();
        event.setId(20L);

        when(utilisateurService.getUtilisateurConnecte()).thenReturn(u);
        when(postService.getPostsByUtilisateur(1L)).thenReturn(List.of(post));
        when(reactionService.isLikedBy(post, u)).thenReturn(true);
        when(reactionService.isDislikedBy(post, u)).thenReturn(false);
        when(reactionService.countLikes(post)).thenReturn(5);
        when(reactionService.countDislikes(post)).thenReturn(2);
        when(utilisateurService.getEvenementsParUtilisateur(u)).thenReturn(List.of(event));
        when(evenementService.estInscrit(event, u)).thenReturn(true);
        when(evenementService.estInteresse(event, u)).thenReturn(false);

        mockMvc.perform(get("/profil/voirProfil").principal(auth(u)))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("utilisateur"))
                .andExpect(model().attributeExists("posts"))
                .andExpect(model().attributeExists("evenements"))
                .andExpect(model().attributeExists("inscritMap"))
                .andExpect(model().attributeExists("interesseMap"))
                .andExpect(view().name("voirProfil"));
    }

    @Test
    void testAfficherMonProfil_EvenementsAvecNullDansListe() throws Exception {
        Utilisateur u = new Utilisateur();
        u.setIdUser(1L);

        Evenement event = new Evenement();
        event.setId(21L);

        // La liste contient bien un null maintenant
        when(utilisateurService.getUtilisateurConnecte()).thenReturn(u);
        when(postService.getPostsByUtilisateur(1L)).thenReturn(List.of());
        when(utilisateurService.getEvenementsParUtilisateur(u))
                .thenReturn(Arrays.asList(event, null));  // ← remplacé List.of par Arrays.asList
        when(evenementService.estInscrit(event, u)).thenReturn(false);
        when(evenementService.estInteresse(event, u)).thenReturn(true);

        mockMvc.perform(get("/profil/voirProfil").principal(auth(u)))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("evenements"))
                .andExpect(model().attributeExists("inscritMap"))
                .andExpect(model().attributeExists("interesseMap"))
                .andExpect(view().name("voirProfil"));
    }

    @Test
    void testVoirProfilAutre_SansAuthentication() throws Exception {
        // 1) Simule un utilisateur “authentifié”
        //    (nécessaire pour passer le cast principal → Utilisateur)
        Utilisateur u = creerUtilisateurSimule();
        u.setIdUser(1L);
        TestingAuthenticationToken auth = auth(u);

        // 2) Stub du service pour renvoyer null sur l’ID 2
        when(utilisateurService.getUtilisateurById(2L)).thenReturn(null);

        // 3) Requête avec principal de type Utilisateur
        mockMvc.perform(get("/profil/voir/2").principal(auth))
                .andExpect(status().isOk())
                .andExpect(view().name("accueil"));
    }


}
