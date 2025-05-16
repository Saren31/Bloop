package utcapitole.miage.bloop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import utcapitole.miage.bloop.model.entity.Post;
import utcapitole.miage.bloop.model.entity.Utilisateur;
import utcapitole.miage.bloop.service.PostService;
import utcapitole.miage.bloop.service.UtilisateurService;

import java.util.List;

@Controller
@RequestMapping("/profil")
public class ProfilController {

    private final UtilisateurService utilisateurService;
    private final PostService postService;

    @Autowired
    public ProfilController(UtilisateurService utilisateurService, PostService postService) {
        this.utilisateurService = utilisateurService;
        this.postService = postService;
    }

    @GetMapping("/voirProfil")
    public String afficherMonProfil(Model model) {
        Utilisateur utilisateur = getUtilisateurConnecte();

        if (utilisateur == null) {
            return "accueil"; // Redirige vers l'accueil si l'utilisateur n'est pas connecté
        }

        List<Post> posts = postService.getPostsByUtilisateur(utilisateur.getIdUser());

        model.addAttribute("utilisateur", utilisateur);
        model.addAttribute("posts", posts);

        return "voirProfil";
    }

    private Utilisateur getUtilisateurConnecte() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (Utilisateur) authentication.getPrincipal();
    }
}