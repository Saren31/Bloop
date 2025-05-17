package utcapitole.miage.bloop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import utcapitole.miage.bloop.dto.MessageDTO;
import utcapitole.miage.bloop.model.entity.Message;
import utcapitole.miage.bloop.service.MessageService;
import utcapitole.miage.bloop.service.UtilisateurService;

import java.security.Principal;

@Controller
public class ChatWebSocketController {
    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    @Autowired
    private UtilisateurService utilisateurService;
    @Autowired
    private MessageService messageService;

    @MessageMapping("/chat")
    public void envoyerViaWS(MessageDTO message, Principal principal) {
        Long expId = utilisateurService.findByEmail(principal.getName()).getIdUser();
        // Sauvegarde le message
        Message saved = messageService.envoyerMessage(expId, message.getDestinataireId(), message.getContenu());
        // Notifie le destinataire en temps réel
        messagingTemplate.convertAndSendToUser(
                String.valueOf(message.getDestinataireId()), // <- par défaut c’est le nom, à adapter !
                "/queue/messages",
                saved
        );
    }
}
