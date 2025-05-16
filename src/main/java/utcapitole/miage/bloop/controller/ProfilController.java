package utcapitole.miage.bloop.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
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
import utcapitole.miage.bloop.model.entity.Post;
import utcapitole.miage.bloop.model.entity.Utilisateur;
import utcapitole.miage.bloop.repository.PostRepository;
import utcapitole.miage.bloop.service.EmailService;
import utcapitole.miage.bloop.service.UtilisateurService;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/profil")
public class ProfilController {

    @Autowired
    private UtilisateurService utilisateurService;

    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("user", new Utilisateur());
        return "inscription";
    }

    @PostMapping("/register_user")
    public String registerUser(@ModelAttribute Utilisateur user, HttpServletRequest request, RedirectAttributes redirectAttributes, Model model) {
        return utilisateurService.inscrireNouvelUtilisateur(user, request, model);
    }

    // ----- Yan US31 Pour créer un post et partarger sur mon profil -----//
    @Autowired
    private PostRepository postRepository;

    @GetMapping("/voirProfil")
    public String afficherMonProfil(HttpSession session, Model model) {
        Utilisateur sessionUser = (Utilisateur) session.getAttribute("utilisateur");

        if (sessionUser == null) {
            return "redirect:/connexion"; // 更安全
        }

        Utilisateur utilisateurComplet = utilisateurService.getUtilisateurParId(sessionUser.getIdUser());

        if (utilisateurComplet == null) {
            return "login";
        }

        model.addAttribute("utilisateur", utilisateurComplet);

        List<Post> posts = postRepository.findByUtilisateur_IdUser(utilisateurComplet.getIdUser());
        model.addAttribute("posts", posts);

        return "voirProfil";
    }

}

