package utcapitole.miage.bloop.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import utcapitole.miage.bloop.model.entity.Evenement;
import utcapitole.miage.bloop.model.entity.Groupe;
import utcapitole.miage.bloop.model.entity.Utilisateur;
import utcapitole.miage.bloop.repository.jpa.EvenementRepository;
import utcapitole.miage.bloop.repository.jpa.GroupeRepository;
import utcapitole.miage.bloop.repository.jpa.PostRepository;
import utcapitole.miage.bloop.repository.jpa.UtilisateurRepository;

import java.util.List;
import java.util.Optional;

/**
 * Service pour gérer les opérations liées aux utilisateurs.
 */
@Service
public class UtilisateurService {

    private final UtilisateurRepository utilisateurRepository;
    private final PostRepository postRepository;
    private final EvenementRepository evenementRepository;
    private final GroupeRepository groupeRepository;

    /**
     * Constructeur pour injecter le dépôt des utilisateurs.
     *
     * @param utilisateurRepository Le dépôt pour interagir avec les utilisateurs.
     */
    @Autowired
    public UtilisateurService(UtilisateurRepository utilisateurRepository, PostRepository postRepository, EvenementRepository evenementRepository, GroupeRepository groupeRepository) {
        this.utilisateurRepository = utilisateurRepository;
        this.postRepository = postRepository;
        this.evenementRepository = evenementRepository;
        this.groupeRepository = groupeRepository;
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
    public Utilisateur getUtilisateurById(long id) {
        Optional<Utilisateur> utilisateur = utilisateurRepository.findById(id);
        return utilisateur.orElse(null);
    }

    public List<Utilisateur> getUtilisateursById(List<Long> ids) {
        return utilisateurRepository.findAllById(ids);
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
            Groupe groupePersisted = groupeRepository.findById(groupe.getIdGroupe()).orElse(null);
            if (groupePersisted != null) {
                groupePersisted.getMembres().remove(utilisateur);
                groupeRepository.save(groupePersisted);
            }
        }
        utilisateur.getGroupes().clear();

        // Retirer des amis
        for (Utilisateur ami : utilisateur.getAmis()) {
            Utilisateur amiPersisted = utilisateurRepository.findById(ami.getIdUser()).orElse(null);
            if (amiPersisted != null) {
                amiPersisted.getAmis().remove(utilisateur);
                utilisateurRepository.save(amiPersisted);
            }
        }
        utilisateur.getAmis().clear();

        // Retirer des demandes envoyées
        for (Utilisateur u : utilisateur.getDemandesEnvoyees()) {
            Utilisateur uPersisted = utilisateurRepository.findById(u.getIdUser()).orElse(null);
            if (uPersisted != null) {
                uPersisted.getDemandesRecues().remove(utilisateur);
                utilisateurRepository.save(uPersisted);
            }
        }
        utilisateur.getDemandesEnvoyees().clear();

        // Retirer des demandes reçues
        for (Utilisateur u : utilisateur.getDemandesRecues()) {
            Utilisateur uPersisted = utilisateurRepository.findById(u.getIdUser()).orElse(null);
            if (uPersisted != null) {
                uPersisted.getDemandesEnvoyees().remove(utilisateur);
                utilisateurRepository.save(uPersisted);
            }
        }
        utilisateur.getDemandesRecues().clear();

        utilisateurRepository.delete(utilisateur);
    }

    public void save(Utilisateur utilisateur) {
        utilisateurRepository.save(utilisateur);
    }

    public Utilisateur findByEmail(String email) {
        return utilisateurRepository.findByEmailUser(email);
    }

    public List<Evenement> getEvenementsParUtilisateur(Utilisateur utilisateur) {
        return evenementRepository.findByOrganisateur(utilisateur);
    }

}