package utcapitole.miage.bloop.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import utcapitole.miage.bloop.model.entity.Message;

import java.util.List;

/**
 * Interface de repository pour l'entité Message.
 * Fournit des méthodes pour effectuer des opérations CRUD sur les messages
 * ainsi qu'une requête personnalisée pour récupérer l'historique des conversations.
 */
public interface MessageRepository extends JpaRepository<Message, Long> {

    /**
     * Récupère l'historique des messages échangés entre deux utilisateurs spécifiques,
     * triés par date d'envoi dans l'ordre croissant.
     *
     * @param id1 L'identifiant du premier utilisateur.
     * @param id2 L'identifiant du second utilisateur.
     * @return Une liste de messages échangés entre les deux utilisateurs.
     */
    @Query("SELECT m FROM Message m WHERE " +
            "(m.expediteur.idUser = :id1 AND m.destinataire.idUser = :id2) OR " +
            "(m.expediteur.idUser = :id2 AND m.destinataire.idUser = :id1) " +
            "ORDER BY m.dateEnvoi ASC")
    List<Message> historique(Long id1, Long id2);
}