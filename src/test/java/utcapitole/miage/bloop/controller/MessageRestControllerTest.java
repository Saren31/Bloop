package utcapitole.miage.bloop.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import utcapitole.miage.bloop.dto.MessageDTO;
import utcapitole.miage.bloop.model.entity.Utilisateur;
import utcapitole.miage.bloop.service.MessageService;
import utcapitole.miage.bloop.service.UtilisateurService;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class MessageRestControllerTest {

    @Mock
    private MessageService messageService;

    @Mock
    private UtilisateurService utilisateurService;

    @InjectMocks
    private MessageRestController controller;

    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
        // Monte le contrôleur REST sans contexte Spring complet
        this.mockMvc = MockMvcBuilders
                .standaloneSetup(controller)
                .build();
    }

    @Test
    void shouldGetHistorique() throws Exception {
        // Given
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setIdUser(1L);
        when(utilisateurService.getUtilisateurConnecte()).thenReturn(utilisateur);
        when(messageService.historique(1L, 2L)).thenReturn(List.of());

        // When / Then
        mockMvc.perform(get("/messages/history/2"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldSendMessage() throws Exception {
        // Given
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setIdUser(1L);
        when(utilisateurService.getUtilisateurConnecte()).thenReturn(utilisateur);
        when(messageService.envoyerMessage(1L, 2L, "coucou")).thenReturn(new MessageDTO());

        // When / Then
        mockMvc.perform(post("/messages/send")
                        .param("destId", "2")
                        .param("contenu", "coucou"))
                .andExpect(status().isOk());
    }
}
