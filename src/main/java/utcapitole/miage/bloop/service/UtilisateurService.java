package utcapitole.miage.bloop.service;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import utcapitole.miage.bloop.model.entity.Utilisateur;
import utcapitole.miage.bloop.repository.UtilisateurRepository;

import java.util.UUID;

@Service
public class UtilisateurService {

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;

    public String inscrireNouvelUtilisateur(Utilisateur user, HttpServletRequest request, Model model) {
        if (!estEmailValide(user.getEmailUser())) {
            return handleErreur(model, user, "L'adresse e-mail doit se terminer par @ut-capitole.fr");
        }

        if (utilisateurExiste(user.getEmailUser())) {
            return handleErreur(model, user, "L'adresse e-mail est déjà utilisée");
        }

        preparerUtilisateurPourSauvegarde(user);
        utilisateurRepository.save(user);
        envoyerLienDeConfirmation(user, request);

        return "accueil";
    }

    private boolean estEmailValide(String email) {
        return email != null && email.toLowerCase().endsWith("@ut-capitole.fr");
    }

    private boolean utilisateurExiste(String email) {
        return utilisateurRepository.findByEmailUser(email) != null;
    }

    private void preparerUtilisateurPourSauvegarde(Utilisateur user) {
        user.setValiderInscription(false);
        user.setMdpUser(passwordEncoder.encode(user.getMdpUser()));
        user.setTokenInscription(UUID.randomUUID().toString());
    }

    private void envoyerLienDeConfirmation(Utilisateur user, HttpServletRequest request) {
        String appUrl = request.getRequestURL().toString().replace(request.getRequestURI(), request.getContextPath());
        String confirmationLink = appUrl + "/confirm?token=" + user.getTokenInscription();
        emailService.envoyerMessageConfirmation(user.getEmailUser(), confirmationLink);
    }

    private String handleErreur(Model model, Utilisateur user, String message) {
        model.addAttribute("error", message);
        user.setEmailUser(null);
        model.addAttribute("user", user);
        return "inscription";
    }
}
