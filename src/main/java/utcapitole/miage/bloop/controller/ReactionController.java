package utcapitole.miage.bloop.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import utcapitole.miage.bloop.model.entity.Post;
import utcapitole.miage.bloop.model.entity.Utilisateur;
import utcapitole.miage.bloop.service.PostService;
import utcapitole.miage.bloop.service.ReactionService;

@Controller
@RequestMapping("/reaction")
public class ReactionController {

    @Autowired
    private ReactionService reactionService;

    @Autowired
    private PostService postService;

    @PostMapping("/{postId}/like")
    public String liker(@PathVariable Long postId, HttpSession session) {
        Utilisateur utilisateur = (Utilisateur) session.getAttribute("utilisateur");
        Post post = postService.getPostParId(postId);
        reactionService.likerPost(post, utilisateur);
        return "redirect:/profil/voirProfil";
    }


    @PostMapping("/{postId}/dislike")
    public String disliker(@PathVariable Long postId, HttpSession session) {
        Utilisateur utilisateur = (Utilisateur) session.getAttribute("utilisateur");
        Post post = postService.getPostParId(postId);
        reactionService.dislikerPost(post, utilisateur);
        return "redirect:/profil/voirProfil";
    }

    @PostMapping("/{postId}/annuler")
    public String annuler(@PathVariable Long postId, HttpSession session) {
        Utilisateur utilisateur = (Utilisateur) session.getAttribute("utilisateur");
        Post post = postService.getPostParId(postId);
        reactionService.annulerReaction(post, utilisateur);
        return "redirect:/profil/voirProfil";
    }
}
