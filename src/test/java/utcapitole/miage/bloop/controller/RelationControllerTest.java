package utcapitole.miage.bloop.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import utcapitole.miage.bloop.service.RelationService;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RelationController.class)
@ActiveProfiles("test")
@AutoConfigureMockMvc(addFilters = false)
class RelationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private RelationService relationService;

    @Test
    void testEnvoyerDemandeAmitie_Succes() throws Exception {
        when(relationService.envoyerDemandeAmitie(1L, 2L)).thenReturn("Demande envoyée avec succès");

        mockMvc.perform(post("/relations/demande")
                        .param("idEnvoyeur", "1")
                        .param("idReceveur", "2"))
                .andExpect(status().isOk())
                .andExpect(content().string("Demande envoyée avec succès"));
    }

    @Test
    void testEnvoyerDemandeAmitie_Echec() throws Exception {
        when(relationService.envoyerDemandeAmitie(1L, 2L)).thenReturn("Erreur");

        mockMvc.perform(post("/relations/demande")
                        .param("idEnvoyeur", "1")
                        .param("idReceveur", "2"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Erreur"));
    }
}
