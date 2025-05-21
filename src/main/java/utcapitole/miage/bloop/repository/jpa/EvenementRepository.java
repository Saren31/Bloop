package utcapitole.miage.bloop.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import utcapitole.miage.bloop.model.entity.Evenement;
import utcapitole.miage.bloop.model.entity.Utilisateur;

import java.util.List;

public interface EvenementRepository extends JpaRepository<Evenement, Long> {
    List<Evenement> findByOrganisateur_IdUser(Long idUser);

    List<Evenement> findByOrganisateur(Utilisateur utilisateur);

    List<Evenement> findByInscrits_IdUser(Long idUser);
}
