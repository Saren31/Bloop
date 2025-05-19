package utcapitole.miage.bloop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import utcapitole.miage.bloop.model.entity.Post;
import utcapitole.miage.bloop.model.entity.Reaction;
import utcapitole.miage.bloop.model.entity.Utilisateur;

import java.util.Optional;

public interface ReactionRepository extends JpaRepository<Reaction, Long> {

    int countByPostAndLiked(Post post, boolean liked);

    Optional<Reaction> findByPostAndUtilisateur(Post post, Utilisateur utilisateur);


    Optional<Reaction> findByPostIdAndUtilisateurId(Long idPost, Long idUser);
}
