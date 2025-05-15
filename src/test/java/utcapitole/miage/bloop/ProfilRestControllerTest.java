package utcapitole.miage.bloop;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import utcapitole.miage.bloop.controller.ProfilRestController;
import utcapitole.miage.bloop.model.entity.Utilisateur;
import utcapitole.miage.bloop.repository.UtilisateurRepository;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProfilRestController.class)
public class ProfilRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UtilisateurRepository utilisateurRepository;

    /**@Test
    void testConnexionAvecIdentifiantsValides() throws Exception {

        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setEmailUser("test@etu.fr");
        utilisateur.setMdpUser("123456");
        utilisateur.setNomUser("Jean");

        Mockito.when(utilisateurRepository.findByEmailUser("test@etu.fr"))
                .thenReturn(utilisateur);

        mockMvc.perform(post("/profil/seConnecter")
                        .param("email", "test@etu.fr")
                        .param("mdp", "123456"))
                .andExpect(status().isOk())
                .andExpect(content().string("Connexion réussie !"));
    }

    @Test
    void testConnexionAvecEmailInvalide() throws Exception {

        Mockito.when(utilisateurRepository.findByEmailUser("wrong@etu.fr"))
                .thenReturn(null);

        mockMvc.perform(post("/profil/seConnecter")
                        .param("email", "wrong@etu.fr")
                        .param("mdp", "123456"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Email incorrect"));
    }

    @Test
    void testConnexionAvecMauvaisMotDePasse() throws Exception {
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setEmailUser("test@etu.fr");
        utilisateur.setMdpUser("correct");

        Mockito.when(utilisateurRepository.findByEmailUser("test@etu.fr"))
                .thenReturn(utilisateur);

        mockMvc.perform(post("/profil/seConnecter")
                        .param("email", "test@etu.fr")
                        .param("mdp", "wrong"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Mot de passe incorrect"));
    }*/
}