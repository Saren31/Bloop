package utcapitole.miage.bloop.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import utcapitole.miage.bloop.model.entity.Post;
import utcapitole.miage.bloop.model.entity.Reaction;
import utcapitole.miage.bloop.model.entity.Utilisateur;

import java.util.List;
import java.util.Optional;

/**
 * Interface de repository pour l'entité Reaction.
 * Fournit des méthodes pour effectuer des opérations CRUD sur les réactions
 * ainsi que des requêtes personnalisées.
 */
public interface ReactionRepository extends JpaRepository<Reaction, Long> {

    /**
     * Récupère une réaction spécifique associée à un post et un utilisateur donnés.
     *
     * @param post Le post auquel la réaction est associée.
     * @param utilisateur L'utilisateur ayant effectué la réaction.
     * @return Un Optional contenant la réaction si elle existe, sinon vide.
     */
    Optional<Reaction> findByPostAndUtilisateur(Post post, Utilisateur utilisateur);

    /**
     * Récupère une réaction spécifique en fonction de l'identifiant du post
     * et de l'identifiant de l'utilisateur.
     *
     * @param idPost L'identifiant du post.
     * @param idUser L'identifiant de l'utilisateur.
     * @return Un Optional contenant la réaction si elle existe, sinon vide.
     */
    Optional<Reaction> findByPostIdPostAndUtilisateurIdUser(Long idPost, Long idUser);

    /**
     * Compte le nombre de réactions d'un type spécifique associées à un post donné.
     *
     * @param post Le post auquel les réactions sont associées.
     * @param type Le type de réaction (par exemple "like", "dislike").
     * @return Le nombre de réactions du type spécifié pour le post donné.
     */
    int countByPostAndType(Post post, String type);

}
      Optional<Reaction> findByPostAndUtilisateur(Post post, Utilisateur utilisateur);
      Optional<Reaction> findByPostIdPostAndUtilisateurIdUser(Long idPost, Long idUser);