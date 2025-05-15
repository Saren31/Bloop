package utcapitole.miage.bloop;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import utcapitole.miage.bloop.controller.EmailController;
import utcapitole.miage.bloop.model.entity.Utilisateur;
import utcapitole.miage.bloop.repository.UtilisateurRepository;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EmailController.class)
class EmailControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UtilisateurRepository utilisateurRepository;

    @Test
    void testConfirmUserAvecTokenValide() throws Exception {
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setTokenInscription("abc123");

        Mockito.when(utilisateurRepository.findByTokenInscription("abc123")).thenReturn(utilisateur);

        mockMvc.perform(get("/confirm").param("token", "abc123"))
                .andExpect(status().isOk())
                .andExpect(view().name("confirmer_profil"));
    }

    @Test
    void testConfirmUserAvecTokenInvalide() throws Exception {
        Mockito.when(utilisateurRepository.findByTokenInscription("wrongToken")).thenReturn(null);

        mockMvc.perform(get("/confirm").param("token", "wrongToken"))
                .andExpect(status().isOk())
                .andExpect(view().name("confirmer_profil"));
    }
}
