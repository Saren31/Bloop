package utcapitole.miage.bloop.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import utcapitole.miage.bloop.dto.MessageDTO;
import utcapitole.miage.bloop.service.MessageService;
import utcapitole.miage.bloop.service.UtilisateurService;

import java.security.Principal;

/**
 * Contrôleur WebSocket pour gérer les messages envoyés via STOMP.
 * Permet l'envoi de messages en temps réel entre utilisateurs.
 */
@Controller
public class ChatWebSocketController {

    private final MessageService messageService;
    private final UtilisateurService utilisateurService;
    private final SimpMessagingTemplate messagingTemplate;

    /**
     * Constructeur pour injecter les services nécessaires.
     *
     * @param messageService Service pour la gestion des messages.
     * @param utilisateurService Service pour la gestion des utilisateurs.
     * @param messagingTemplate Template pour envoyer des messages via WebSocket.
     */
    public ChatWebSocketController(MessageService messageService,
                                   UtilisateurService utilisateurService,
                                   SimpMessagingTemplate messagingTemplate) {
        this.messageService = messageService;
        this.utilisateurService = utilisateurService;
        this.messagingTemplate = messagingTemplate;
    }

    /**
     * Gère les messages envoyés à l'endpoint "/chat".
     * Sauvegarde le message, puis l'envoie aux utilisateurs concernés via WebSocket.
     *
     * @param message DTO contenant les informations du message (destinataire, contenu, etc.).
     * @param principal Objet représentant l'utilisateur actuellement connecté.
     */
    @MessageMapping("/chat")
    public void envoyerViaWS(MessageDTO message, Principal principal) {
        // Récupère l'ID de l'expéditeur à partir de l'utilisateur connecté
        long expId = utilisateurService.findByEmail(principal.getName()).getIdUser();

        // Sauvegarde le message et récupère le DTO du message sauvegardé
        MessageDTO saved = messageService.envoyerMessage(expId, message.getDestinataireId(), message.getContenu());

        // Récupère les noms d'utilisateur (ou emails) de l'expéditeur et du destinataire
        String destinataireUsername = utilisateurService
                .getUtilisateurById(message.getDestinataireId())
                .getUsername();

        String envoyeurUsername = utilisateurService
                .getUtilisateurById(expId)
                .getUsername();

        // Envoie le message au destinataire via WebSocket
        messagingTemplate.convertAndSendToUser(
                destinataireUsername,
                "/queue/messages",
                saved
        );

        // Envoie le message à l'expéditeur via WebSocket
        messagingTemplate.convertAndSendToUser(
                envoyeurUsername,
                "/queue/messages",
                saved
        );
    }

    @MessageMapping("/delete")
    public void supprimerMessageViaWS(MessageDTO message, Principal principal) {
        // Vérifie que l'utilisateur courant est bien l'expéditeur
        long expId = utilisateurService.findByEmail(principal.getName()).getIdUser();
        Long messageId = message.getId();
        // Supprime le message et récupère l'ID du destinataire
        Long destinataireId = messageService.supprimerMessage(messageId, expId);
        // Récupère les emails pour l'envoi WebSocket
        String destinataireUsername = utilisateurService.getUtilisateurById(destinataireId).getUsername();
        String envoyeurUsername = principal.getName();
        // Notifie l'expéditeur et le destinataire
        messagingTemplate.convertAndSendToUser(envoyeurUsername, "/queue/deleted",
                java.util.Map.of("messageId", messageId));
        messagingTemplate.convertAndSendToUser(destinataireUsername, "/queue/deleted",
                java.util.Map.of("messageId", messageId));
    }
}