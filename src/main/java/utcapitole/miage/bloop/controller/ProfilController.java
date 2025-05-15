package utcapitole.miage.bloop.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
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

import java.util.UUID;

@Controller
@RequestMapping("/profil")
public class ProfilController {

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Autowired
    private EmailService emailService;

    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("user", new Utilisateur());
        return "inscription";
    }

    @PostMapping("/register_user")
    public String registerUser(@ModelAttribute Utilisateur user, HttpServletRequest request, RedirectAttributes redirectAttributes, Model model) {

        // Vérification de l'adrrese e-mail du nouvel utilisateur
        if (!user.getEmailUser().toLowerCase().endsWith("@ut-capitole.fr")) {
            model.addAttribute("error", "L'adresse e-mail doit se terminer par @ut-capitole.fr");
            user.setEmailUser(null);
            model.addAttribute("user", user);
            return "inscription";
        }

        user.setValiderInscription(false);

        // Génération du token de validation de l'email
        String token = UUID.randomUUID().toString();
        user.setTokenInscription(token);
        utilisateurRepository.save(user);

        // Création du lien de confirmation
        String appUrl = request.getRequestURL().toString().replace(request.getRequestURI(), request.getContextPath());
        String confirmationLink = appUrl + "/confirm?token=" + token;

        // Envoi de l'e-mail
        emailService.envoyerMessageConfirmation(user.getEmailUser(), confirmationLink);

        utilisateurRepository.save(user);
        return "accueil";
    }



    // ---------------------------- Yan US06 Pour voir mon profil ---------------------------------- //
    @GetMapping("/voirProfil")
    public String afficherMonProfil(HttpSession session, Model model) {
        Utilisateur utilisateur = (Utilisateur) session.getAttribute("utilisateur");

        if (utilisateur == null) {
            return "accueil";
        }

        model.addAttribute("utilisateur", utilisateur);
        return "voirProfil";
    }
}
