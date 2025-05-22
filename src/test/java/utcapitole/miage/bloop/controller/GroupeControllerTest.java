package utcapitole.miage.bloop.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import utcapitole.miage.bloop.model.entity.Groupe;
import utcapitole.miage.bloop.model.entity.Utilisateur;
import utcapitole.miage.bloop.service.GroupeService;
import utcapitole.miage.bloop.service.PostService;
import utcapitole.miage.bloop.service.UtilisateurService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

@ExtendWith(MockitoExtension.class)
class GroupeControllerTest {

    @Mock
    private GroupeService groupeService;

    @Mock
    private PostService postService;

    @Mock
    private UtilisateurService utilisateurService;

    @InjectMocks
    private GroupeController controller;

    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
        // Pour que les noms de vue ".html" se résolvent sans boucle
        InternalResourceViewResolver vr = new InternalResourceViewResolver();
        vr.setPrefix("");
        vr.setSuffix(".html");

        this.mockMvc = MockMvcBuilders
                .standaloneSetup(controller)
                .setViewResolvers(vr)
                .build();
    }

    @Test
    void shouldValiderGroupeSuccess() throws Exception {
        // Given
        when(utilisateurService.getUtilisateurConnecte()).thenReturn(new Utilisateur());
        when(groupeService.enregistrerGroupe(any(Groupe.class)))
                .thenReturn("accueil");

        // When / Then
        mockMvc.perform(post("/groupes/valider")
                        .param("nomGroupe", "Test Groupe")
                        .param("themeGroupe", "Test Thème")
                        .param("descriptionGroupe", "Description de test")
                        .with(csrf())
                )
                .andExpect(status().isFound()) // 302
                .andExpect(redirectedUrl("/accueil"));
    }

    @Test
    void shouldRejoindreGroupeSuccess() throws Exception {
        // Given
        Groupe groupe = new Groupe();
        groupe.setIdGroupe(1L);
        when(groupeService.trouverGroupeParId(1L)).thenReturn(groupe);
        when(utilisateurService.getUtilisateurConnecte()).thenReturn(new Utilisateur());

        // When / Then
        mockMvc.perform(post("/groupes/rejoindre/1")
                        .with(csrf())
                )
                .andExpect(status().isOk())
                .andExpect(view().name("accueil"));
    }

    @Test
    void shouldNotAddMemberIfAlreadyInGroup() throws Exception {
        // Given : un utilisateur déjà membre
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setIdUser(1L);

        Groupe groupe = new Groupe();
        groupe.setIdGroupe(1L);
        groupe.getMembres().add(utilisateur);

        when(groupeService.trouverGroupeParId(1L)).thenReturn(groupe);
        when(utilisateurService.getUtilisateurConnecte()).thenReturn(utilisateur);

        // When
        mockMvc.perform(post("/groupes/rejoindre/1")
                        .with(csrf())
                )
                .andExpect(status().isOk())
                .andExpect(view().name("accueil"));

        // Then : pas de double ajout
        assertThat(groupe.getMembres()).hasSize(1);
    }

    @Test
    void shouldAfficherFormulaireCreerGroupe() throws Exception {
        mockMvc.perform(get("/groupes/creer"))
                .andExpect(status().isOk())
                .andExpect(view().name("creer_groupe"));
    }

    @Test
    void shouldAfficherPageGroupe() throws Exception {
        // Given : utilisateur connecté (nécessaire si la page l’exige)
        when(utilisateurService.getUtilisateurConnecte()).thenReturn(new Utilisateur());

        mockMvc.perform(get("/groupes/groupe"))
                .andExpect(status().isOk())
                .andExpect(view().name("groupe"));
    }
}
