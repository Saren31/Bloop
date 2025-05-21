package utcapitole.miage.bloop.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import utcapitole.miage.bloop.model.entity.Utilisateur;
import utcapitole.miage.bloop.service.UtilisateurService;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class UtilisateurControllerTest {

    @Mock
    private UtilisateurService utilisateurService;

    @InjectMocks
    private UtilisateurController controller;

    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
        // On ajoute le converter JSON pour que .jsonPath() fonctionne
        this.mockMvc = MockMvcBuilders
                .standaloneSetup(controller)
                .setMessageConverters(new MappingJackson2HttpMessageConverter())
                .build();
    }

    @Test
    void shouldListerUtilisateurs() throws Exception {
        Utilisateur u = new Utilisateur();
        u.setNomUser("Test");
        when(utilisateurService.recupererTousLesUtilisateurs())
                .thenReturn(List.of(u));

        mockMvc.perform(get("/utilisateurs")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nomUser").value("Test"));
    }

    @Test
    void shouldRecupererUtilisateur_Succes() throws Exception {
        Utilisateur u = new Utilisateur();
        u.setIdUser(1L);
        u.setNomUser("Test");
        when(utilisateurService.recupererUtilisateurParId(1L))
                .thenReturn(Optional.of(u));

        mockMvc.perform(get("/utilisateurs/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nomUser").value("Test"));
    }

    @Test
    void shouldRecupererUtilisateur_NonTrouve() throws Exception {
        when(utilisateurService.recupererUtilisateurParId(1L))
                .thenReturn(Optional.empty());

        mockMvc.perform(get("/utilisateurs/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldRechercherParPseudo() throws Exception {
        Utilisateur u1 = new Utilisateur();
        u1.setPseudoUser("testUser1");
        Utilisateur u2 = new Utilisateur();
        u2.setPseudoUser("testUser2");
        when(utilisateurService.rechercherParPseudo("test"))
                .thenReturn(List.of(u1, u2));

        mockMvc.perform(get("/utilisateurs/recherche_pseudo")
                        .param("pseudo", "test")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].pseudoUser").value("testUser1"))
                .andExpect(jsonPath("$[1].pseudoUser").value("testUser2"));
    }
}
