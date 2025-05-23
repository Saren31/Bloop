package utcapitole.miage.bloop.service;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import utcapitole.miage.bloop.model.entity.Utilisateur;
import utcapitole.miage.bloop.repository.jpa.UtilisateurRepository;

import java.util.UUID;

/**
 * Service pour gérer l'authentification et l'inscription des utilisateurs.
 */
@Service
public class AuthService {

    private final UtilisateurRepository utilisateurRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    /**
     * Constructeur pour injecter les dépendances nécessaires.
     *
     * @param utilisateurRepository Le dépôt pour interagir avec les utilisateurs.
     * @param passwordEncoder       L'encodeur de mots de passe pour sécuriser les mots de passe.
     * @param emailService          Le service pour envoyer des e-mails.
     */
    public AuthService(UtilisateurRepository utilisateurRepository,
                       PasswordEncoder passwordEncoder,
                       EmailService emailService) {
        this.utilisateurRepository = utilisateurRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
    }

    /**
     * Enregistre un nouvel utilisateur dans le système.
     *
     * @param user    L'utilisateur à enregistrer.
     * @param request L'objet HttpServletRequest pour obtenir des informations sur la requête.
     * @param model   Le modèle pour ajouter des attributs à la vue.
     * @return Le nom de la vue à afficher après l'inscription.
     */
    public String enregistrerUtilisateur(Utilisateur user, HttpServletRequest request, Model model) {
        if (!emailValide(user.getUsername())) {
            System.out.println(user.getUsername());
            return erreur(model, user, "L'adresse e-mail doit se terminer par @ut-capitole.fr");
        }

        if (utilisateurRepository.findByEmailUser(user.getUsername()) != null) {
            return erreur(model, user, "L'adresse e-mail est déjà utilisée");
        }

        user.setValiderInscription(false);
        user.setMdpUser(passwordEncoder.encode(user.getMdpUser()));
        user.setTokenInscription(UUID.randomUUID().toString());

        utilisateurRepository.save(user);

        String appUrl = request.getRequestURL().toString().replace(request.getRequestURI(), request.getContextPath());
        String confirmationLink = appUrl + "/confirm?token=" + user.getTokenInscription();
        emailService.envoyerMessageConfirmation(user.getUsername(), confirmationLink);

        return "accueil";
    }

    /**
     * Vérifie si une adresse e-mail est valide.
     *
     * @param email L'adresse e-mail à vérifier.
     * @return Vrai si l'adresse e-mail est valide, faux sinon.
     */
    private boolean emailValide(String email) {
        return email != null && email.toLowerCase().endsWith("@ut-capitole.fr");
    }

    /**
     * Ajoute un message d'erreur au modèle et réinitialise l'adresse e-mail de l'utilisateur.
     *
     * @param model   Le modèle pour ajouter des attributs à la vue.
     * @param user    L'utilisateur concerné.
     * @param message Le message d'erreur à afficher.
     * @return Le nom de la vue d'inscription.
     */
    private String erreur(Model model, Utilisateur user, String message) {
        model.addAttribute("error", message);
        user.setEmailUser(null);
        model.addAttribute("user", user);
        return "inscription";
    }
}