package utcapitole.miage.bloop.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import utcapitole.miage.bloop.model.entity.Utilisateur;
import utcapitole.miage.bloop.repository.UtilisateurRepository;
import utcapitole.miage.bloop.service.EmailService;
import utcapitole.miage.bloop.service.UtilisateurService;

/**
 * Contrôleur pour gérer les requêtes liées au profil utilisateur.
 */
@Controller
@RequestMapping("/profil")
public class ProfilController {

    private final UtilisateurService utilisateurService;

    @Autowired
    public ProfilController(UtilisateurService utilisateurService) {
        this.utilisateurService = utilisateurService;
    }

    @GetMapping("/voirProfil")
    public String afficherMonProfil(Model model) {
        Utilisateur utilisateur = utilisateurService.getUtilisateurConnecte();

        if (utilisateur == null || utilisateurService.getUtilisateurParId(utilisateur.getIdUser()) == null) {
            return "accueil";
        }

        model.addAttribute("utilisateur", utilisateur);
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