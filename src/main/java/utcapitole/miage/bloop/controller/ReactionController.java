package utcapitole.miage.bloop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import utcapitole.miage.bloop.model.entity.Utilisateur;
import utcapitole.miage.bloop.service.ReactionService;
import utcapitole.miage.bloop.service.UtilisateurService;

@Controller
@RequestMapping("/reaction")
public class ReactionController {

    private final ReactionService reactionService;

    private final UtilisateurService utilisateurService;

    @Autowired
    public ReactionController(ReactionService reactionService, UtilisateurService utilisateurService) {
        this.reactionService = reactionService;
        this.utilisateurService = utilisateurService;
    }

    @PostMapping("/like/{postId}")
    public String likePost(@PathVariable Long postId) {
        Utilisateur utilisateur = utilisateurService.getUtilisateurConnecte();
        if (utilisateur != null) {
            reactionService.toggleLike(postId, utilisateur);
        }

        return "redirect:/profil/voirProfil";
    }

    @PostMapping("/dislike/{postId}")
    public String dislikePost(@PathVariable Long postId) {
        Utilisateur utilisateur = utilisateurService.getUtilisateurConnecte();
        if (utilisateur != null) {
            reactionService.toggleDislike(postId, utilisateur);
        }
        return "redirect:/profil/voirProfil";
    }

}
