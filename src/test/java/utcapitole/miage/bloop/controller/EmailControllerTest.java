package utcapitole.miage.bloop.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import utcapitole.miage.bloop.model.entity.Utilisateur;
import utcapitole.miage.bloop.repository.jpa.UtilisateurRepository;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class EmailControllerTest {

    @Mock
    private UtilisateurRepository utilisateurRepository;

    @InjectMocks
    private EmailController controller;

    private MockMvc buildMockMvc() {
        // Configure view resolver so that view names like "confirmer_inscription" resolve without looping
        InternalResourceViewResolver vr = new InternalResourceViewResolver();
        vr.setPrefix("");
        vr.setSuffix(".html");
        return MockMvcBuilders
                .standaloneSetup(controller)
                .setViewResolvers(vr)
                .build();
    }

    @Test
    void shouldConfirmUserWithValidToken() throws Exception {
        // Given
        Utilisateur u = new Utilisateur();
        u.setEmailUser("test@ut-capitole.fr");
        when(utilisateurRepository.findByTokenInscription("valid-token"))
                .thenReturn(u);

        MockMvc mockMvc = buildMockMvc();

        // When / Then
        mockMvc.perform(get("/confirm").param("token", "valid-token"))
                .andExpect(status().isOk())
                .andExpect(view().name("confirmer_inscription"))
                .andExpect(model().attribute("success", true));
    }

    @Test
    void shouldHandleInvalidToken() throws Exception {
        // Given
        when(utilisateurRepository.findByTokenInscription("invalid"))
                .thenReturn(null);

        MockMvc mockMvc = buildMockMvc();

        // When / Then
        mockMvc.perform(get("/confirm").param("token", "invalid"))
                .andExpect(status().isOk())
                .andExpect(view().name("confirmer_inscription"))
                .andExpect(model().attribute("success", false));
    }
}
