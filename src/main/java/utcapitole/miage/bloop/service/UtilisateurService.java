package utcapitole.miage.bloop.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
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
     * Récupère la liste de tous les utilisateurs.
     *
     * @return Une liste contenant tous les utilisateurs.
     */
    public List<Utilisateur> recupererTousLesUtilisateurs() {
        return utilisateurRepository.findAll();
    }

    public Utilisateur getUtilisateurConnecte() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (Utilisateur) authentication.getPrincipal();
    }
}