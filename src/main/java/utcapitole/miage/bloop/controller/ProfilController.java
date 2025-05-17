package utcapitole.miage.bloop.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import utcapitole.miage.bloop.model.entity.Evenement;
import utcapitole.miage.bloop.model.entity.Utilisateur;
import utcapitole.miage.bloop.repository.UtilisateurRepository;
import utcapitole.miage.bloop.service.EmailService;
import utcapitole.miage.bloop.service.UtilisateurService;

import java.util.List;

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

        List<Evenement> evenements = utilisateurService.getEvenementsParUtilisateur(utilisateur);


        if (evenements == null) {
            evenements = List.of();
        }


        evenements = evenements.stream()
                .filter(e -> e != null)
                .toList();

        model.addAttribute("evenements", evenements);


        System.out.println("evenements.size() = " + evenements.size());

        return "voirProfil";
    }
}