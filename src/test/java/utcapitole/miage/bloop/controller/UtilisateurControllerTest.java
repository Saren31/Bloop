package utcapitole.miage.bloop.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import utcapitole.miage.bloop.model.entity.Utilisateur;
import utcapitole.miage.bloop.service.UtilisateurService;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UtilisateurController.class)
@ActiveProfiles("test")
@AutoConfigureMockMvc(addFilters = false)
class UtilisateurControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UtilisateurService utilisateurService;

    @Test
    void testListerUtilisateurs() throws Exception {
        Utilisateur u = new Utilisateur();
        u.setNomUser("Test");

        when(utilisateurService.recupererTousLesUtilisateurs()).thenReturn(Arrays.asList(u));

        mockMvc.perform(get("/utilisateurs"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nomUser").value("Test"));
    }

    @Test
    void testRecupererUtilisateur_Succes() throws Exception {
        Utilisateur u = new Utilisateur();
        u.setIdUser(1L);
        u.setNomUser("Test");

        when(utilisateurService.recupererUtilisateurParId(1L)).thenReturn(Optional.of(u));

        mockMvc.perform(get("/utilisateurs/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nomUser").value("Test"));
    }

    @Test
    void testRecupererUtilisateur_NonTrouve() throws Exception {
        when(utilisateurService.recupererUtilisateurParId(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/utilisateurs/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testRechercherParPseudo() throws Exception {
        Utilisateur u1 = new Utilisateur();
        u1.setPseudoUser("testUser1");
        Utilisateur u2 = new Utilisateur();
        u2.setPseudoUser("testUser2");

        when(utilisateurService.rechercherParPseudo("test")).thenReturn(Arrays.asList(u1, u2));

        mockMvc.perform(get("/utilisateurs/recherche_pseudo")
                        .param("pseudo", "test"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].pseudoUser").value("testUser1"))
                .andExpect(jsonPath("$[1].pseudoUser").value("testUser2"));
    }


}
