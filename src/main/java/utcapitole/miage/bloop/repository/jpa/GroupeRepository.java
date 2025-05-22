package utcapitole.miage.bloop.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import utcapitole.miage.bloop.model.entity.Groupe;

/**
 * Interface de repository pour l'entité Groupe.
 * Fournit des méthodes pour effectuer des opérations CRUD sur les groupes.
 */
@Repository
public interface GroupeRepository extends JpaRepository<Groupe, Long> {
}