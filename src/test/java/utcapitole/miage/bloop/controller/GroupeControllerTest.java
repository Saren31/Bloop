package utcapitole.miage.bloop.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import utcapitole.miage.bloop.model.entity.Groupe;
import utcapitole.miage.bloop.model.entity.Utilisateur;
import utcapitole.miage.bloop.service.GroupeService;
import utcapitole.miage.bloop.service.PostService;
import utcapitole.miage.bloop.service.UtilisateurService;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(GroupeController.class)
@ActiveProfiles("test")
@AutoConfigureMockMvc
class GroupeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private GroupeService groupeService;

    @MockitoBean
    private PostService postService;

    @MockitoBean
    private UtilisateurService utilisateurService;

    @Test
    @WithMockUser(username = "testuser", roles = "USER")
    void testValiderGroupe_Succes() throws Exception {
        when(utilisateurService.getUtilisateurConnecte()).thenReturn(new Utilisateur());
        when(groupeService.enregistrerGroupe(any(Groupe.class))).thenReturn("accueil");

        mockMvc.perform(post("/groupes/valider")
                        .with(SecurityMockMvcRequestPostProcessors.csrf()) // Ajout du jeton CSRF
                        .param("nomGroupe", "Test Groupe")
                        .param("themeGroupe", "Test Thème")
                        .param("descriptionGroupe", "Description de test"))
                .andExpect(status().isOk())
                .andExpect(view().name("accueil"));
    }

    @Test
    @WithMockUser(username = "testuser", roles = "USER")
    void testRejoindreGroupe_Succes() throws Exception {
        Groupe groupe = new Groupe();
        groupe.setIdGroupe(1L);
        when(groupeService.trouverGroupeParId(1L)).thenReturn(groupe);
        when(utilisateurService.getUtilisateurConnecte()).thenReturn(new Utilisateur());

        mockMvc.perform(post("/groupes/rejoindre/1")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())) // Ajout du jeton CSRF
                .andExpect(status().isOk())
                .andExpect(view().name("accueil"));
    }

    @Test
    @WithMockUser(username = "testuser", roles = "USER")
    void testRejoindreGroupe_UtilisateurDejaMembre() throws Exception {
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setIdUser(1L);

        Groupe groupe = new Groupe();
        groupe.setIdGroupe(1L);
        groupe.getMembres().add(utilisateur);

        when(groupeService.trouverGroupeParId(1L)).thenReturn(groupe);
        when(utilisateurService.getUtilisateurConnecte()).thenReturn(utilisateur);

        mockMvc.perform(post("/groupes/rejoindre/1")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())) // Ajout du jeton CSRF
                .andExpect(status().isOk())
                .andExpect(view().name("accueil"));

        assertThat(groupe.getMembres().size()).isEqualTo(1);
    }
}