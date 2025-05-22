package utcapitole.miage.bloop.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import utcapitole.miage.bloop.model.entity.Post;
import utcapitole.miage.bloop.model.entity.Utilisateur;

import java.util.List;

/**
 * Interface de repository pour l'entité Post.
 * Fournit des méthodes pour effectuer des opérations CRUD sur les posts
 * ainsi que des requêtes personnalisées.
 */
public interface PostRepository extends JpaRepository<Post, Long> {

    /**
     * Récupère la liste des posts créés par un utilisateur spécifique
     * en fonction de son identifiant.
     *
     * @param idUser L'identifiant de l'utilisateur.
     * @return Une liste de posts créés par l'utilisateur donné.
     */
    List<Post> findByUtilisateur_IdUser(Long idUser);

    /**
     * Supprime tous les posts associés à un utilisateur spécifique.
     *
     * @param utilisateur L'utilisateur dont les posts doivent être supprimés.
     */
    void deleteAllByUtilisateur(Utilisateur utilisateur);

    /**
     * Récupère la liste des posts créés par un utilisateur spécifique.
     *
     * @param utilisateur L'utilisateur auteur des posts.
     * @return Une liste de posts créés par l'utilisateur donné.
     */
    List<Post> findByUtilisateur(Utilisateur utilisateur);

    /**
     * Récupère la liste des posts associés à un groupe spécifique
     * en fonction de l'identifiant du groupe.
     *
     * @param groupeId L'identifiant du groupe.
     * @return Une liste de posts associés au groupe donné.
     */
    List<Post> findByGroupe_IdGroupe(Long groupeId);
}