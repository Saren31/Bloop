package utcapitole.miage.bloop.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import utcapitole.miage.bloop.model.entity.Groupe;
import utcapitole.miage.bloop.model.entity.Utilisateur;
import utcapitole.miage.bloop.service.GroupeService;
import utcapitole.miage.bloop.service.UtilisateurService;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(GroupeController.class)
@ActiveProfiles("test")
@AutoConfigureMockMvc(addFilters = false)
class GroupeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private GroupeService groupeService;

    @MockitoBean
    private UtilisateurService utilisateurService;

    @Test
    void testValiderGroupe_Succes() throws Exception {
        // Mock des services
        when(utilisateurService.getUtilisateurConnecte()).thenReturn(new Utilisateur());
        when(groupeService.enregistrerGroupe(any(Groupe.class))).thenReturn("accueil");

        // Envoi de la requête
        mockMvc.perform(post("/groupes/valider")
                        .param("nomGroupe", "Test Groupe")
                        .param("themeGroupe", "Test Thème")
                        .param("descriptionGroupe", "Description de test"))
                .andExpect(status().isOk())
                .andExpect(view().name("accueil"));
    }

    @Test
    void testRejoindreGroupe_Succes() throws Exception {
        // Mock des services
        Groupe groupe = new Groupe();
        groupe.setIdGroupe(1L);
        when(groupeService.trouverGroupeParId(1L)).thenReturn(groupe);
        when(utilisateurService.getUtilisateurConnecte()).thenReturn(new Utilisateur());

        // Envoi de la requête
        mockMvc.perform(post("/groupes/rejoindre/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("accueil"));
    }

    @Test
    void testRejoindreGroupe_UtilisateurDejaMembre() throws Exception {
        // Mock des services
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setIdUser(1L);

        Groupe groupe = new Groupe();
        groupe.setIdGroupe(1L);
        groupe.getMembres().add(utilisateur);

        when(groupeService.trouverGroupeParId(1L)).thenReturn(groupe);
        when(utilisateurService.getUtilisateurConnecte()).thenReturn(utilisateur);

        // Envoi de la requête
        mockMvc.perform(post("/groupes/rejoindre/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("accueil"));

        // Vérification que l'utilisateur n'est pas ajouté une deuxième fois
        assertThat(groupe.getMembres().size()).isEqualTo(1);
    }
}
