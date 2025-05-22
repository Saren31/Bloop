package utcapitole.miage.bloop.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import utcapitole.miage.bloop.model.entity.Commentaire;
import java.util.List;

/**
 * Interface de repository pour l'entité Commentaire.
 * Fournit des méthodes pour effectuer des opérations CRUD sur les commentaires
 * et des requêtes personnalisées.
 */
public interface CommentaireRepository extends JpaRepository<Commentaire, Long> {

    /**
     * Récupère la liste des commentaires associés à un post spécifique.
     *
     * @param postId L'identifiant du post.
     * @return Une liste de commentaires associés au post donné.
     */
    List<Commentaire> findByPostIdPost(Long postId);
}