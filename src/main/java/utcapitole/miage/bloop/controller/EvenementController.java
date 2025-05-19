package utcapitole.miage.bloop.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import utcapitole.miage.bloop.model.entity.Evenement;
import utcapitole.miage.bloop.model.entity.Utilisateur;
import utcapitole.miage.bloop.service.EvenementService;
import utcapitole.miage.bloop.service.UtilisateurService;

import java.util.List;

@Controller
@RequestMapping("/evenement")
public class EvenementController {

    private final EvenementService evenementService;

    private final UtilisateurService utilisateurService;

    @Autowired
    public EvenementController(EvenementService evenementService, UtilisateurService utilisateurService) {
        this.evenementService = evenementService;
        this.utilisateurService = utilisateurService;
    }

    @GetMapping("/creer")
    public String afficherFormulaire(Model model) {
        model.addAttribute("evenement", new Evenement());
        return "creerEvenement";
    }

    @PostMapping("/creer")
    public String creerEvenement(@ModelAttribute Evenement evenement, HttpSession session, Model model) {
        Utilisateur utilisateur = utilisateurService.getUtilisateurConnecte();

        if (utilisateur == null) {
            return "login";
        }

        evenement.setOrganisateur(utilisateur);
        evenementService.creerEvenement(evenement);
        model.addAttribute("message", "Événement créé avec succès !");
        return "redirect:/profil/voirProfil";
    }

    @GetMapping("/mesEvenements")
    public String afficherMesEvenements(Model model) {
        Utilisateur utilisateur = utilisateurService.getUtilisateurConnecte();
        if (utilisateur == null) return "login";

        List<Evenement> evenements = evenementService.getEvenementsParOrganisateur(utilisateur.getIdUser());
        model.addAttribute("evenements", evenements);
        return "mesEvenements";
    }

}
