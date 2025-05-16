package utcapitole.miage.bloop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import utcapitole.miage.bloop.model.entity.Post;
import org.springframework.web.bind.annotation.*;
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
        Utilisateur utilisateur = utilisateurService.getUtilisateurConnecte();

        if (utilisateur == null) {
            return "accueil"; // Redirige vers l'accueil si l'utilisateur n'est pas connecté
        }

        List<Post> posts = postService.getPostsByUtilisateur(utilisateur.getIdUser());

        model.addAttribute("utilisateur", utilisateur);
        model.addAttribute("posts", posts);

        return "voirProfil";
    }
    // voir le profil d'un autre user par son id
    @GetMapping("/voir/{id}")
    public String voirProfilAutre(@PathVariable Long id, Model model, Authentication authentication) {
        Utilisateur moi = (Utilisateur) authentication.getPrincipal();

        Utilisateur autre = utilisateurService.getUtilisateurParId(id);
        if (autre == null ) {
            return "accueil"; // redirection vers accueil
        }
        if (autre.getIdUser() == moi.getIdUser() ) {
            return "redirect:/profil/voirProfil"; // redirection vers ton propre profil
        }

        model.addAttribute("utilisateur", autre);
        return "VoirAutreProfil";
    }


}