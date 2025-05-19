package utcapitole.miage.bloop.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import utcapitole.miage.bloop.dto.MessageDTO;
import utcapitole.miage.bloop.model.entity.Utilisateur;
import utcapitole.miage.bloop.service.MessageService;
import utcapitole.miage.bloop.service.UtilisateurService;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MessageRestController.class)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
class MessageRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private MessageService messageService;

    @MockitoBean
    private UtilisateurService utilisateurService;

    @Test
    void testGetHistorique() throws Exception {
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setIdUser(1L);
        when(utilisateurService.getUtilisateurConnecte()).thenReturn(utilisateur);
        when(messageService.historique(1L, 2L)).thenReturn(List.of());

        mockMvc.perform(get("/messages/history/2"))
                .andExpect(status().isOk());
    }

    @Test
    void testSendMessage() throws Exception {
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setIdUser(1L);
        when(utilisateurService.getUtilisateurConnecte()).thenReturn(utilisateur);
        when(messageService.envoyerMessage(1L, 2L, "coucou")).thenReturn(new MessageDTO());

        mockMvc.perform(post("/messages/send")
                        .param("destId", "2")
                        .param("contenu", "coucou"))
                .andExpect(status().isOk());
    }
}