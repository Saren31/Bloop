package utcapitole.miage.bloop.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import utcapitole.miage.bloop.model.entity.Utilisateur;
import utcapitole.miage.bloop.repository.UtilisateurRepository;
import utcapitole.miage.bloop.service.EmailService;

/**
 * Contrôleur pour gérer les requêtes liées au profil utilisateur.
 */
@Controller
@RequestMapping("/profil")
public class ProfilController {

    @GetMapping("/voirProfil")
    public String afficherMonProfil(HttpSession session, Model model) {

        if (session.getAttribute("utilisateur") == null) {
            Utilisateur mockUser = new Utilisateur();
            mockUser.setIdUser(1L);
            session.setAttribute("utilisateur", mockUser);
        }

        Utilisateur sessionUser = (Utilisateur) session.getAttribute("utilisateur");

        if (sessionUser == null) {
            return "accueil";
        }

        Utilisateur utilisateurComplet = utilisateurService.getUtilisateurParId(sessionUser.getIdUser());

        if (utilisateurComplet == null) {
            return "accueil";
        }

        model.addAttribute("utilisateur", utilisateurComplet);
        return "voirProfil";
    }
