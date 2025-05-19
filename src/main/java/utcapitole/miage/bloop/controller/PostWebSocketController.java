package utcapitole.miage.bloop.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import utcapitole.miage.bloop.dto.PostDTO;
import utcapitole.miage.bloop.service.GroupeService;
import utcapitole.miage.bloop.service.PostService;
import utcapitole.miage.bloop.service.UtilisateurService;

import java.security.Principal;

@Controller
public class PostWebSocketController {

    private final SimpMessagingTemplate messagingTemplate;
    private final PostService postService;
    private final GroupeService groupeService;
    private final UtilisateurService utilisateurService;

    public PostWebSocketController(SimpMessagingTemplate messagingTemplate, PostService postService, GroupeService groupeService, UtilisateurService utilisateurService) {
        this.messagingTemplate = messagingTemplate;
        this.postService = postService;
        this.groupeService = groupeService;
        this.utilisateurService = utilisateurService;
    }

    @MessageMapping("/post")
    public void envoyerPost(PostDTO postDTO, Principal principal) {
        long expId = utilisateurService.findByEmail(principal.getName()).getIdUser();
        // Sauvegarder le post dans la base de données
        PostDTO savedPost = postService.envoyerPost(postDTO, expId);

        // Vérifier si le post appartient à un groupe
        if (savedPost.getGroupeId() != null) {
            // Envoyer le post au groupe via WebSocket
            messagingTemplate.convertAndSend("/topic/groupes/" + savedPost.getGroupeId(), savedPost);
        }
    }
}