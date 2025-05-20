// src/test/java/utcapitole/miage/bloop/controller/ChatWebSocketControllerTest.java
package utcapitole.miage.bloop.controller;

import org.junit.jupiter.api.Test;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import utcapitole.miage.bloop.dto.MessageDTO;
import utcapitole.miage.bloop.model.entity.Utilisateur;
import utcapitole.miage.bloop.service.MessageService;
import utcapitole.miage.bloop.service.UtilisateurService;

import java.security.Principal;

import static org.mockito.Mockito.*;

class ChatWebSocketControllerTest {

    @Test
    void testEnvoyerViaWS() {
        MessageService messageService = mock(MessageService.class);
        UtilisateurService utilisateurService = mock(UtilisateurService.class);
        SimpMessagingTemplate messagingTemplate = mock(SimpMessagingTemplate.class);

        ChatWebSocketController controller = new ChatWebSocketController(messageService, utilisateurService, messagingTemplate);

        MessageDTO message = new MessageDTO();
        message.setDestinataireId(2L);
        message.setContenu("Salut");

        Principal principal = () -> "user@mail.com";
        Utilisateur exp = new Utilisateur();
        exp.setIdUser(1L);
        Utilisateur dest = new Utilisateur();
        dest.setIdUser(2L);
        dest.setEmailUser("dest@mail.com");
        exp.setEmailUser("user@mail.com");

        when(utilisateurService.findByEmail("user@mail.com")).thenReturn(exp);
        when(utilisateurService.getUtilisateurById(2L)).thenReturn(dest);
        when(utilisateurService.getUtilisateurById(1L)).thenReturn(exp);
        MessageDTO saved = new MessageDTO();
        when(messageService.envoyerMessage(1L, 2L, "Salut")).thenReturn(saved);

        controller.envoyerViaWS(message, principal);

        verify(messagingTemplate, times(1)).convertAndSendToUser("dest@mail.com", "/queue/messages", saved);
        verify(messagingTemplate, times(1)).convertAndSendToUser("user@mail.com", "/queue/messages", saved);
    }
}