package utcapitole.miage.bloop.service;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import utcapitole.miage.bloop.model.entity.Post;
import utcapitole.miage.bloop.model.entity.Utilisateur;
import utcapitole.miage.bloop.repository.UtilisateurRepository;

import java.util.Optional;
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

    public String envoyerDemandeAmitie(Long idEnvoyeur, Long idReceveur) {
        if (idEnvoyeur.equals(idReceveur)) {
            return "Vous ne pouvez pas vous envoyer une demande à vous-même.";
        }

        Optional<Utilisateur> optEnvoyeur = utilisateurRepository.findById(idEnvoyeur);
        Optional<Utilisateur> optReceveur = utilisateurRepository.findById(idReceveur);

        if (optEnvoyeur.isEmpty() || optReceveur.isEmpty()) {
            return "Utilisateur non trouvé.";
        }

        Utilisateur envoyeur = optEnvoyeur.get();
        Utilisateur receveur = optReceveur.get();

        if (envoyeur.getDemandesEnvoyees().contains(receveur)) {
            return "Demande déjà envoyée.";
        }

        envoyeur.getDemandesEnvoyees().add(receveur);
        utilisateurRepository.save(envoyeur);
        utilisateurRepository.save(receveur);

        return "Demande d’amitié envoyée avec succès.";
    }

    public Utilisateur getUtilisateurParId(long idUser) {
    }
}