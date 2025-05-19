// src/test/java/utcapitole/miage/bloop/controller/PostWebSocketControllerTest.java
package utcapitole.miage.bloop.controller;

import org.junit.jupiter.api.Test;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import utcapitole.miage.bloop.dto.PostDTO;
import utcapitole.miage.bloop.model.entity.Utilisateur;
import utcapitole.miage.bloop.service.GroupeService;
import utcapitole.miage.bloop.service.PostService;
import utcapitole.miage.bloop.service.UtilisateurService;

import java.security.Principal;

import static org.mockito.Mockito.*;

class PostWebSocketControllerTest {

    @Test
    void testEnvoyerPost() {
        SimpMessagingTemplate messagingTemplate = mock(SimpMessagingTemplate.class);
        PostService postService = mock(PostService.class);
        UtilisateurService utilisateurService = mock(UtilisateurService.class);

        PostWebSocketController controller = new PostWebSocketController(
                messagingTemplate, postService, utilisateurService);

        PostDTO postDTO = new PostDTO();
        postDTO.setGroupeId(42L);

        Principal principal = () -> "user@mail.com";
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setIdUser(1L);

        when(utilisateurService.findByEmail("user@mail.com")).thenReturn(utilisateur);

        PostDTO savedPost = new PostDTO();
        savedPost.setGroupeId(42L);
        when(postService.envoyerPost(postDTO, 1L)).thenReturn(savedPost);

        controller.envoyerPost(postDTO, principal);

        verify(messagingTemplate, times(1))
                .convertAndSend("/topic/groupes/42", savedPost);
    }
}