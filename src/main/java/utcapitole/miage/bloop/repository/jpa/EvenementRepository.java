package utcapitole.miage.bloop.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import utcapitole.miage.bloop.model.entity.Evenement;
import utcapitole.miage.bloop.model.entity.Utilisateur;

import java.util.List;

public interface EvenementRepository extends JpaRepository<Evenement, Long> {
    List<Evenement> findByOrganisateur_IdUser(Long idUser);

    List<Evenement> findByOrganisateur(Utilisateur utilisateur);

    List<Evenement> findByInscrits_IdUser(Long idUser);

    @Query("SELECT e FROM Evenement e WHERE e.organisateur.idUser <> :idUser")
    List<Evenement> findByOrganisateurIsNot(@Param("idUser") Long idUser);

}
