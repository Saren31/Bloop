package utcapitole.miage.bloop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import utcapitole.miage.bloop.model.entity.Post;
import utcapitole.miage.bloop.model.entity.Utilisateur;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByUtilisateur_IdUser(Long idUser);

    void deleteAllByUtilisateur(Utilisateur utilisateur);
}
