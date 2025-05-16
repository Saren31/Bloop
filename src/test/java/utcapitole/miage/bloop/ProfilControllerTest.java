package utcapitole.miage.bloop;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import utcapitole.miage.bloop.controller.ProfilController;
import utcapitole.miage.bloop.model.entity.Utilisateur;
import utcapitole.miage.bloop.repository.UtilisateurRepository;
import utcapitole.miage.bloop.service.EmailService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@WebMvcTest(ProfilController.class)
class ProfilControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UtilisateurRepository utilisateurRepository;

    @MockitoBean
    private EmailService emailService;

    @Test
    void testRegisterUserAvecEmailValide() throws Exception {
        mockMvc.perform(post("/profil/register_user")
                        .param("emailUser", "test@ut-capitole.fr")
                        .param("nomUser", "Nom")
                        .param("prenomUser", "Prenom")
                        .param("mdpUser", "mdp")
                        .param("pseudoUser", "pseudo"))
                .andExpect(status().isOk())
                .andExpect(view().name("accueil"));

        Mockito.verify(emailService).envoyerMessageConfirmation(Mockito.anyString(), Mockito.anyString());
    }

    @Test
    void testRegisterUserAvecEmailInvalide() throws Exception {
        mockMvc.perform(post("/profil/register_user")
                        .param("emailUser", "test@gmail.com")
                        .param("nomUser", "Nom")
                        .param("prenomUser", "Prenom")
                        .param("mdpUser", "mdp")
                        .param("pseudoUser", "pseudo"))
                .andExpect(status().isOk())
                .andExpect(view().name("inscription"))
                .andExpect(model().attributeExists("error"));
    }

    // ------------------- Yan US06 pour voir mon profil -------------------------------//
    @Test
    void testVoirProfilAvecUtilisateurSession() throws Exception {
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setNomUser("Dupont");
        utilisateur.setEmailUser("dupont@ut-capitole.fr");

        MockHttpSession session = new MockHttpSession();
        session.setAttribute("utilisateur", utilisateur);

        mockMvc.perform(get("/profil/voirProfil").session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("voirProfil"))
                .andExpect(model().attributeExists("utilisateur"));
    }

    @Test
    void testVoirProfilSansUtilisateurSession() throws Exception {
        mockMvc.perform(get("/profil/voirProfil"))
                .andExpect(status().isOk())
                .andExpect(view().name("accueil"));
    }
}
