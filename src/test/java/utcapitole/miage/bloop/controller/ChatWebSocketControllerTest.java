package utcapitole.miage.bloop.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import utcapitole.miage.bloop.dto.MessageDTO;
import utcapitole.miage.bloop.model.entity.Utilisateur;
import utcapitole.miage.bloop.service.MessageService;
import utcapitole.miage.bloop.service.UtilisateurService;

import java.security.Principal;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ChatWebSocketControllerTest {

    @Mock
    private MessageService messageService;

    @Mock
    private UtilisateurService utilisateurService;

    @Mock
    private SimpMessagingTemplate messagingTemplate;

    @InjectMocks
    private ChatWebSocketController controller;

    @Test
    void envoyerViaWS_shouldSendToBothUsers() {
        // Préparation du message
        MessageDTO message = new MessageDTO();
        message.setDestinataireId(2L);
        message.setContenu("Salut");

        // Mock du Principal
        Principal principal = mock(Principal.class);
        when(principal.getName()).thenReturn("user@mail.com");

        // Utilisateurs expéditeur et destinataire
        Utilisateur utilisateurConnecte = new Utilisateur();
        utilisateurConnecte.setIdUser(1L);
        utilisateurConnecte.setEmailUser("user@mail.com");

        Utilisateur dest = new Utilisateur();
        dest.setIdUser(2L);
        dest.setEmailUser("dest@mail.com");

        // Mocks des services
        when(utilisateurService.findByEmail("user@mail.com")).thenReturn(utilisateurConnecte);
        when(utilisateurService.getUtilisateurById(2L)).thenReturn(dest);

        MessageDTO saved = new MessageDTO();
        when(messageService.envoyerMessage(1L, 2L, "Salut")).thenReturn(saved);

        // Appel de la méthode sous test
        controller.envoyerViaWS(message, principal);

        // Vérifications
        verify(messagingTemplate).convertAndSendToUser("dest@mail.com", "/queue/messages", saved);
        verify(messagingTemplate).convertAndSendToUser("user@mail.com", "/queue/messages", saved);
    }
}
