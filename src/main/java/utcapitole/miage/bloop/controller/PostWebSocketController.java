package utcapitole.miage.bloop.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import utcapitole.miage.bloop.dto.PostDTO;
import utcapitole.miage.bloop.service.PostService;
import utcapitole.miage.bloop.service.UtilisateurService;

import java.security.Principal;

/**
 * Contrôleur WebSocket pour gérer les interactions en temps réel liées aux posts.
 */
@Controller
public class PostWebSocketController {

    private final SimpMessagingTemplate messagingTemplate;
    private final PostService postService;
    private final UtilisateurService utilisateurService;

    /**
     * Constructeur pour injecter les dépendances nécessaires.
     *
     * @param messagingTemplate Le template pour envoyer des messages via WebSocket.
     * @param postService Le service pour gérer les opérations liées aux posts.
     * @param utilisateurService Le service pour gérer les opérations liées aux utilisateurs.
     */
    public PostWebSocketController(SimpMessagingTemplate messagingTemplate, PostService postService, UtilisateurService utilisateurService) {
        this.messagingTemplate = messagingTemplate;
        this.postService = postService;
        this.utilisateurService = utilisateurService;
    }

    /**
     * Gère l'envoi d'un post via WebSocket.
     *
     * @param postDTO L'objet contenant les informations du post à envoyer.
     * @param principal L'utilisateur actuellement authentifié.
     */
    @MessageMapping("/post")
    public void envoyerPost(PostDTO postDTO, Principal principal) {
        // Récupérer l'identifiant de l'utilisateur expéditeur à partir de son email.
        long expId = utilisateurService.findByEmail(principal.getName()).getIdUser();

        // Sauvegarder le post dans la base de données.
        PostDTO savedPost = postService.envoyerPost(postDTO, expId);

        // Vérifier si le post est associé à un groupe.
        if (savedPost.getGroupeId() != null) {
            // Envoyer le post au groupe via WebSocket.
            messagingTemplate.convertAndSend("/topic/groupes/" + savedPost.getGroupeId(), savedPost);
        }
    }
}