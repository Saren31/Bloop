package utcapitole.miage.bloop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import utcapitole.miage.bloop.model.entity.Utilisateur;
import utcapitole.miage.bloop.service.ReactionService;
import utcapitole.miage.bloop.service.UtilisateurService;

/**
 * Contrôleur pour gérer les réactions (like et dislike) sur les posts.
 * Fournit des endpoints pour aimer ou ne pas aimer un post.
 */
@Controller
@RequestMapping("/reaction")
public class ReactionController {

    private final ReactionService reactionService;
    private final UtilisateurService utilisateurService;

    /**
     * Constructeur pour injecter les services nécessaires.
     *
     * @param reactionService Service pour gérer les réactions.
     * @param utilisateurService Service pour gérer les utilisateurs.
     */
    @Autowired
    public ReactionController(ReactionService reactionService, UtilisateurService utilisateurService) {
        this.reactionService = reactionService;
        this.utilisateurService = utilisateurService;
    }

    /**
     * Permet à un utilisateur connecté d'aimer ou de retirer son like d'un post.
     *
     * @param postId L'identifiant du post à liker ou à unliker.
     * @return Une redirection vers la page de profil après l'opération.
     */
    @PostMapping("/like/{postId}")
    public String likePost(@PathVariable Long postId) {
        Utilisateur utilisateur = utilisateurService.getUtilisateurConnecte();
        if (utilisateur != null) {
            reactionService.toggleLike(postId, utilisateur);
        }

        return "redirect:/profil/voirProfil";
    }

    /**
     * Permet à un utilisateur connecté de disliker ou de retirer son dislike d'un post.
     *
     * @param postId L'identifiant du post à disliker ou à retirer le dislike.
     * @return Une redirection vers la page de profil après l'opération.
     */
    @PostMapping("/dislike/{postId}")
    public String dislikePost(@PathVariable Long postId) {
        Utilisateur utilisateur = utilisateurService.getUtilisateurConnecte();
        if (utilisateur != null) {
            reactionService.toggleDislike(postId, utilisateur);
        }
        return "redirect:/profil/voirProfil";
    }

}