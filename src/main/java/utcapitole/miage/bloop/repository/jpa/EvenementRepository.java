package utcapitole.miage.bloop.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import utcapitole.miage.bloop.model.entity.Evenement;
import utcapitole.miage.bloop.model.entity.Utilisateur;

import java.util.List;

/**
 * Interface de repository pour l'entité Evenement.
 * Fournit des méthodes pour effectuer des opérations CRUD sur les événements
 * ainsi que des requêtes personnalisées.
 */
public interface EvenementRepository extends JpaRepository<Evenement, Long> {

    /**
     * Récupère la liste des événements organisés par un utilisateur spécifique
     * en fonction de son identifiant.
     *
     * @param idUser L'identifiant de l'utilisateur organisateur.
     * @return Une liste d'événements organisés par l'utilisateur donné.
     */
    List<Evenement> findByOrganisateur_IdUser(Long idUser);

    /**
     * Récupère la liste des événements organisés par un utilisateur spécifique.
     *
     * @param utilisateur L'utilisateur organisateur.
     * @return Une liste d'événements organisés par l'utilisateur donné.
     */
    List<Evenement> findByOrganisateur(Utilisateur utilisateur);

    /**
     * Récupère la liste des événements auxquels un utilisateur spécifique est inscrit
     * en fonction de son identifiant.
     *
     * @param idUser L'identifiant de l'utilisateur inscrit.
     * @return Une liste d'événements auxquels l'utilisateur donné est inscrit.
     */
    List<Evenement> findByInscrits_IdUser(Long idUser);

    @Query("SELECT e FROM Evenement e WHERE e.organisateur.idUser <> :idUser")
    List<Evenement> findByOrganisateurIsNot(@Param("idUser") Long idUser);

}