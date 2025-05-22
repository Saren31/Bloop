package utcapitole.miage.bloop.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import utcapitole.miage.bloop.model.entity.Evenement;
import utcapitole.miage.bloop.model.entity.Utilisateur;
import utcapitole.miage.bloop.repository.jpa.EvenementRepository;
import utcapitole.miage.bloop.repository.jpa.UtilisateurRepository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;

/**
 * Service pour la gestion des événements.
 * Fournit des méthodes pour créer, gérer les inscriptions, les intérêts et les participants des événements.
 */
@Service
public class EvenementService {

    private final EvenementRepository evenementRepository;
    private final UtilisateurRepository utilisateurRepository;

    /**
     * Constructeur avec injection du repository d'événements.
     *
     * @param evenementRepository Le repository pour les entités Evenement.
     */
    @Autowired
    public EvenementService(EvenementRepository evenementRepository, UtilisateurRepository utilisateurRepository) {
        this.evenementRepository = evenementRepository;
        this.utilisateurRepository = utilisateurRepository;
    }

    /**
     * Crée un nouvel événement.
     *
     * @param evenement L'événement à créer.
     */
    public void creerEvenement(Evenement evenement) {
        evenementRepository.save(evenement);
    }

    /**
     * Récupère les événements organisés par un utilisateur spécifique.
     *
     * @param idUser L'identifiant de l'utilisateur organisateur.
     * @return Une liste d'événements organisés par l'utilisateur.
     */
    public List<Evenement> getEvenementsParOrganisateur(Long idUser) {
        return evenementRepository.findByOrganisateur_IdUser(idUser);
    }

    /**
     * Récupère un événement par son identifiant.
     *
     * @param id L'identifiant de l'événement.
     * @return L'événement correspondant ou null s'il n'existe pas.
     */
    public Evenement getEvenementParId(Long id) {
        return evenementRepository.findById(id).orElse(null);
    }

    /**
     * Inscrit un utilisateur à un événement.
     *
     * @param evenement  L'événement auquel inscrire l'utilisateur.
     * @param utilisateur L'utilisateur à inscrire.
     */
    public void inscrireUtilisateur(Evenement evenement, Utilisateur utilisateur) {
        if (evenement.getInscrits() == null) {
            evenement.setInscrits(new HashSet<>());
        }

        Utilisateur userFromDb = utilisateurRepository.findById(utilisateur.getIdUser())
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé : " + utilisateur.getIdUser()));

        if (!evenement.getInscrits().contains(userFromDb)) {
            evenement.getInscrits().add(userFromDb);
            evenementRepository.save(evenement);
        }
    }

    /**
     * Retire un utilisateur de la liste des participants d'un événement.
     *
     * @param evenement  L'événement concerné.
     * @param utilisateur L'utilisateur à retirer.
     */
    public void retirerUtilisateur(Evenement evenement, Utilisateur utilisateur) {
        if (evenement.getInscrits().contains(utilisateur)) {
            evenement.getInscrits().remove(utilisateur);
            evenementRepository.save(evenement);
        }
    }

    /**
     * Marque un utilisateur comme intéressé par un événement.
     *
     * @param evenement  L'événement concerné.
     * @param utilisateur L'utilisateur intéressé.
     */
    public void marquerInteresse(Evenement evenement, Utilisateur utilisateur) {
        if (!evenement.getInteresses().contains(utilisateur)) {
            evenement.getInteresses().add(utilisateur);
            evenementRepository.save(evenement);
        }
    }

    /**
     * Retire un utilisateur de la liste des intéressés d'un événement.
     *
     * @param evenement  L'événement concerné.
     * @param utilisateur L'utilisateur à retirer.
     */
    public void retirerInteresse(Evenement evenement, Utilisateur utilisateur) {
        if (evenement.getInteresses().contains(utilisateur)) {
            evenement.getInteresses().remove(utilisateur);
            evenementRepository.save(evenement);
        }
    }

    /**
     * Vérifie si un utilisateur est inscrit à un événement.
     *
     * @param evenement  L'événement concerné.
     * @param utilisateur L'utilisateur à vérifier.
     * @return true si l'utilisateur est inscrit, sinon false.
     */
    public boolean estInscrit(Evenement evenement, Utilisateur utilisateur) {
        return evenement.getInscrits().contains(utilisateur);
    }

    /**
     * Vérifie si un utilisateur est intéressé par un événement.
     *
     * @param evenement  L'événement concerné.
     * @param utilisateur L'utilisateur à vérifier.
     * @return true si l'utilisateur est intéressé, sinon false.
     */
    public boolean estInteresse(Evenement evenement, Utilisateur utilisateur) {
        return evenement.getInteresses().contains(utilisateur);
    }
    public List<Evenement> getAllEvents() {
        return evenementRepository.findAll();
    }

    /**
     * Récupère les événements auxquels un utilisateur est inscrit, triés par date de début.
     *
     * @param idUser L'identifiant de l'utilisateur.
     * @return Une liste d'événements triés par date de début.
     */
    public List<Evenement> getEvenementsOuUtilisateurEstInscrit(Long idUser) {
        List<Evenement> evenements = new ArrayList<>(evenementRepository.findByInscrits_IdUser(idUser));
        evenements.sort(Comparator.comparing(Evenement::getDateDebut));
        return evenements;
    }

    public List<Evenement> getEvenementsDesAutresUtilisateurs(Long idUser) {
        Utilisateur organisateur = new Utilisateur();
        organisateur.setIdUser(idUser);
        return evenementRepository.findByOrganisateurIsNot(organisateur.getIdUser());
    }

}