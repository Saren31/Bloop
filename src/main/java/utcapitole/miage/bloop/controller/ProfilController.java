package utcapitole.miage.bloop.controller;

import jakarta.servlet.http.HttpServletRequest;
import jdk.jshell.execution.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import utcapitole.miage.bloop.model.entity.Utilisateur;
import utcapitole.miage.bloop.service.EmailService;
import utcapitole.miage.bloop.service.PostService;
import utcapitole.miage.bloop.service.UtilisateurService;

import java.util.UUID;

@Controller
@RequestMapping("/profil")
public class ProfilController {

    private final UtilisateurService utilisateurService;
    private PostService postService;

    @Autowired
    public ProfilController(UtilisateurService utilisateurService, PostService postService) {
        this.utilisateurService = utilisateurService;
        this.postService = postService;
    }

    @Autowired

    @GetMapping("/voirProfil")
    public String afficherMonProfil(HttpSession session, Model model) {
        Utilisateur utilisateur = (Utilisateur) session.getAttribute("utilisateur");

        if (utilisateur == null) {
            return "accueil";
        }

        List<Post> posts = postService.getPostsByUtilisateur(utilisateur.getIdUser());

        model.addAttribute("utilisateur", utilisateur);
        model.addAttribute("posts", posts);

        return "voirProfil";
    }
}

