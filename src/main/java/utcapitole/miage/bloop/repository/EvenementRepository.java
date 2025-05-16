package utcapitole.miage.bloop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import utcapitole.miage.bloop.model.entity.Evenement;

import java.util.List;

public interface EvenementRepository extends JpaRepository<Evenement, Long> {
    List<Evenement> findByOrganisateur_IdUser(Long idUser);
}
