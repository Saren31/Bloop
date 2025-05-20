package utcapitole.miage.bloop.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import utcapitole.miage.bloop.model.entity.Utilisateur;
import utcapitole.miage.bloop.repository.jpa.UtilisateurRepository;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
@WebMvcTest(EmailController.class)
@AutoConfigureMockMvc(addFilters = false)
class EmailControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UtilisateurRepository utilisateurRepository;

    @Test
    void shouldConfirmUserWithValidToken() throws Exception {
        Utilisateur u = new Utilisateur();
        u.setEmailUser("test@ut-capitole.fr");

        when(utilisateurRepository.findByTokenInscription("valid-token")).thenReturn(u);

        mockMvc.perform(get("/confirm").param("token", "valid-token"))
                .andExpect(status().isOk())
                .andExpect(view().name("confirmer_inscription"))
                .andExpect(model().attribute("success", true));
    }

    @Test
    void shouldHandleInvalidToken() throws Exception {
        when(utilisateurRepository.findByTokenInscription("invalid")).thenReturn(null);

        mockMvc.perform(get("/confirm").param("token", "invalid"))
                .andExpect(status().isOk())
                .andExpect(view().name("confirmer_inscription"))
                .andExpect(model().attribute("success", false));
    }
}
