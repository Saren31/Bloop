package utcapitole.miage.bloop.service;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import utcapitole.miage.bloop.model.entity.Utilisateur;
import utcapitole.miage.bloop.repository.UtilisateurRepository;

import java.util.List;
import java.util.Optional;

/**
 * Service pour gérer les opérations liées aux utilisateurs.
 */
@Service
public class UtilisateurService {

    private final UtilisateurRepository utilisateurRepository;

    /**
     * Constructeur pour injecter le dépôt des utilisateurs.
     *
     * @param utilisateurRepository Le dépôt pour interagir avec les utilisateurs.
     */
    @Autowired
    public UtilisateurService(UtilisateurRepository utilisateurRepository) {
        this.utilisateurRepository = utilisateurRepository;
    }

    /**
     * Récupère un utilisateur par son identifiant.
     *
     * @param id L'identifiant de l'utilisateur à récupérer.
     * @return Un Optional contenant l'utilisateur s'il existe, ou vide sinon.
     */
    public Optional<Utilisateur> recupererUtilisateurParId(Long id) {
        return utilisateurRepository.findById(id);
    }

    /**
     * Récupère un utilisateur par son identifiant.
     * Retourne null si l'utilisateur n'existe pas.
     *
     * @param id L'identifiant de l'utilisateur à récupérer.
     * @return L'utilisateur correspondant ou null s'il n'existe pas.
     */
    public Utilisateur getUtilisateurParId(long id) {
        Optional<Utilisateur> utilisateur = utilisateurRepository.findById(id);
        return utilisateur.orElse(null);
    }

    /**
     * Récupère la liste de tous les utilisateurs.
     *
     * @return Une liste contenant tous les utilisateurs.
     */
    public List<Utilisateur> recupererTousLesUtilisateurs() {
        return utilisateurRepository.findAll();
    }

    /**
     * Récupère l'utilisateur actuellement connecté.
     *
     * @return L'utilisateur connecté.
     */
    public Utilisateur getUtilisateurConnecte() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (Utilisateur) authentication.getPrincipal();
    }

    /**
     * Recherche les utilisateurs dont le pseudo commence par une chaîne donnée.
     *
     * @param pseudo Le début du pseudo à rechercher.
     * @return Une liste d'utilisateurs correspondant au critère.
     */
    public List<Utilisateur> rechercherParPseudo(String pseudo) {
        return utilisateurRepository.findByPseudoStartingWith(pseudo);
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
}