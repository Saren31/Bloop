package utcapitole.miage.bloop.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import utcapitole.miage.bloop.model.entity.Groupe;
import utcapitole.miage.bloop.model.entity.Utilisateur;
import utcapitole.miage.bloop.repository.PostRepository;
import utcapitole.miage.bloop.repository.UtilisateurRepository;

import java.util.List;
import java.util.Optional;

/**
 * Service pour gérer les opérations liées aux utilisateurs.
 */
@Service
public class UtilisateurService {

    private final UtilisateurRepository utilisateurRepository;
    private final PostRepository postRepository;

    /**
     * Constructeur pour injecter le dépôt des utilisateurs.
     *
     * @param utilisateurRepository Le dépôt pour interagir avec les utilisateurs.
     */
    @Autowired
    public UtilisateurService(UtilisateurRepository utilisateurRepository, PostRepository postRepository) {
        this.utilisateurRepository = utilisateurRepository;
        this.postRepository = postRepository;
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

    @Transactional
    public void supprimerUtilisateurEtRelations(long idUser) {
        Utilisateur utilisateur = utilisateurRepository.findById(idUser).orElseThrow();
        // Supprimer les posts
        postRepository.deleteAllByUtilisateur(utilisateur);

        // Retirer des groupes
        for (Groupe groupe : utilisateur.getGroupes()) {
            groupe.getMembres().remove(utilisateur);
        }
        utilisateur.getGroupes().clear();

        // Retirer des amis
        for (Utilisateur ami : utilisateur.getAmis()) {
            ami.getAmis().remove(utilisateur);
        }
        utilisateur.getAmis().clear();

        // Retirer des demandes envoyées/reçues
        for (Utilisateur u : utilisateur.getDemandesEnvoyees()) {
            u.getDemandesRecues().remove(utilisateur);
        }
        utilisateur.getDemandesEnvoyees().clear();

        for (Utilisateur u : utilisateur.getDemandesRecues()) {
            u.getDemandesEnvoyees().remove(utilisateur);
        }
        utilisateur.getDemandesRecues().clear();

        utilisateurRepository.delete(utilisateur);
    }

    public void save(Utilisateur utilisateur) {
        utilisateurRepository.save(utilisateur);
    }

}