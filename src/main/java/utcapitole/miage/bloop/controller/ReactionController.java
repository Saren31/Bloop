package utcapitole.miage.bloop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import utcapitole.miage.bloop.model.entity.Utilisateur;
import utcapitole.miage.bloop.service.PostService;
import utcapitole.miage.bloop.service.ReactionService;
import utcapitole.miage.bloop.service.UtilisateurService;

@Controller
@RequestMapping("/reaction")
public class ReactionController {

    @Autowired
    private ReactionService reactionService;

    @Autowired
    private PostService postService;

    @Autowired
    private UtilisateurService utilisateurService;


    @PostMapping("/like/{postId}")
    public String likePost(@PathVariable Long postId) {
        Utilisateur utilisateur = utilisateurService.getUtilisateurConnecte();
        if (utilisateur != null) {
            reactionService.toggleLike(postId, utilisateur);
        }

        return "redirect:/profil/voirProfil";
    }
}
