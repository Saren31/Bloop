package utcapitole.miage.bloop.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import utcapitole.miage.bloop.dto.MessageDTO;
import utcapitole.miage.bloop.service.MessageService;
import utcapitole.miage.bloop.service.UtilisateurService;

import java.security.Principal;

@Controller
public class ChatWebSocketController {

    private final MessageService messageService;
    private final UtilisateurService utilisateurService;
    private final SimpMessagingTemplate messagingTemplate;

    public ChatWebSocketController(MessageService messageService,
                                   UtilisateurService utilisateurService,
                                   SimpMessagingTemplate messagingTemplate) {
        this.messageService = messageService;
        this.utilisateurService = utilisateurService;
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/chat")
    public void envoyerViaWS(MessageDTO message, Principal principal) {
        long expId = utilisateurService.findByEmail(principal.getName()).getIdUser();
        MessageDTO saved = messageService.envoyerMessage(expId, message.getDestinataireId(), message.getContenu());

        String destinataireUsername = utilisateurService
                .getUtilisateurParId(message.getDestinataireId())
                .getEmailUser(); // ou .getUsername() selon ton modèle

        String envoyeurUsername = utilisateurService
                .getUtilisateurParId(expId)
                .getEmailUser(); //



        messagingTemplate.convertAndSendToUser(
                destinataireUsername,
                "/queue/messages",
                saved
        );

        messagingTemplate.convertAndSendToUser(
                envoyeurUsername,
                "/queue/messages",
                saved
        );
    }

}

