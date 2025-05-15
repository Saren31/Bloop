package utcapitole.miage.bloop;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.ui.Model;
import utcapitole.miage.bloop.controller.ProfilController;
import utcapitole.miage.bloop.model.entity.Utilisateur;
import utcapitole.miage.bloop.repository.UtilisateurRepository;
import utcapitole.miage.bloop.service.EmailService;
import utcapitole.miage.bloop.service.UtilisateurService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
@WebMvcTest(ProfilController.class)
@AutoConfigureMockMvc(addFilters = false)
class ProfilControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UtilisateurService utilisateurService;

    @MockitoBean
    private UtilisateurRepository utilisateurRepository;

    @MockitoBean
    private EmailService emailService;

    @MockitoBean
    private PasswordEncoder passwordEncoder;

    @Test
    void testRegisterUserAvecEmailValide() throws Exception {
        // Mock le service pour un email valide => renvoie la vue "accueil"
        when(utilisateurService.inscrireNouvelUtilisateur(any(Utilisateur.class), any(HttpServletRequest.class), any(Model.class)))
                .thenReturn("accueil");

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
        // Mock le service pour un email invalide => renvoie la vue "inscription"
        when(utilisateurService.inscrireNouvelUtilisateur(any(Utilisateur.class), any(HttpServletRequest.class), any(Model.class)))
                .thenReturn("inscription");

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

    @Test
    void testRegisterUserAlreadyExists() throws Exception {
        // Mock le service pour un utilisateur déjà existant => renvoie la vue "inscription"
        when(utilisateurService.inscrireNouvelUtilisateur(any(Utilisateur.class), any(HttpServletRequest.class), any(Model.class)))
                .thenReturn("inscription");

        mockMvc.perform(post("/profil/register_user")
                        .param("emailUser", "dupont@ut-capitole.fr")
                        .param("nomUser", "Dupont")
                        .param("prenomUser", "Jean")
                        .param("mdpUser", "mdp")
                        .param("pseudoUser", "jdupont"))
                .andExpect(status().isOk())
                .andExpect(view().name("inscription"));
    }

    @Test
    void testRegisterUserNewValid() throws Exception {
        // Mock le service pour un nouvel utilisateur valide => renvoie la vue "accueil"
        when(utilisateurService.inscrireNouvelUtilisateur(any(Utilisateur.class), any(HttpServletRequest.class), any(Model.class)))
                .thenReturn("accueil");

        mockMvc.perform(post("/profil/register_user")
                        .param("emailUser", "nouveau@ut-capitole.fr")
                        .param("nomUser", "Nouveau")
                        .param("prenomUser", "User")
                        .param("mdpUser", "mdp")
                        .param("pseudoUser", "nouveau"))
                .andExpect(status().isOk())
                .andExpect(view().name("accueil"));
    }

}
*/