package utcapitole.miage.bloop.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import utcapitole.miage.bloop.model.entity.Post;
import utcapitole.miage.bloop.model.entity.Reaction;
import utcapitole.miage.bloop.model.entity.Utilisateur;

import java.util.Optional;

public interface ReactionRepository extends JpaRepository<Reaction, Long> {


    Optional<Reaction> findByPostAndUtilisateur(Post post, Utilisateur utilisateur);


    Optional<Reaction> findByPostIdPostAndUtilisateurIdUser(Long idPost, Long idUser);


    int countByPostAndType(Post post, String type);


}
