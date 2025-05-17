package utcapitole.miage.bloop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import utcapitole.miage.bloop.model.entity.Message;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
    // Récupérer la conversation entre deux personnes
    List<Message> findByExpediteur_IdUserAndDestinataire_IdUserOrExpediteur_IdUserAndDestinataire_IdUserOrderByDateEnvoiAsc(
            Long exp1, Long dest1, Long exp2, Long dest2
    );
}

