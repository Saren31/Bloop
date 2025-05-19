package utcapitole.miage.bloop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import utcapitole.miage.bloop.model.entity.Post;
import utcapitole.miage.bloop.model.entity.Reaction;
import utcapitole.miage.bloop.model.entity.Utilisateur;

import java.util.List;

public interface ReactionRepository extends JpaRepository<Reaction, Long> {
    int countByPostAndLiked(Post post, boolean liked);

    List<Reaction> findByPostAndUtilisateur(Post post, Utilisateur utilisateur);

    long countByPostAndLikedTrue(Post post);
}
