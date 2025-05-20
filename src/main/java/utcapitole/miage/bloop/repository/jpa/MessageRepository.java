package utcapitole.miage.bloop.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import utcapitole.miage.bloop.model.entity.Message;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
    // Récupérer la conversation entre deux personnes
    @Query("SELECT m FROM Message m WHERE " +
            "(m.expediteur.idUser = :id1 AND m.destinataire.idUser = :id2) OR " +
            "(m.expediteur.idUser = :id2 AND m.destinataire.idUser = :id1) " +
            "ORDER BY m.dateEnvoi ASC")
    List<Message> historique(Long id1, Long id2);
}

