package utcapitole.miage.bloop.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import utcapitole.miage.bloop.model.entity.Groupe;

@Repository
public interface GroupeRepository extends JpaRepository<Groupe, Long> {
}
