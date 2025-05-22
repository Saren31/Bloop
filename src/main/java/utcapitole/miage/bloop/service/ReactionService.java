package utcapitole.miage.bloop.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import utcapitole.miage.bloop.model.entity.Post;
import utcapitole.miage.bloop.model.entity.Reaction;
import utcapitole.miage.bloop.model.entity.Utilisateur;
import utcapitole.miage.bloop.repository.jpa.ReactionRepository;

import java.util.Optional;

/**
 * Service pour la gestion des réactions (like/dislike) sur les posts.
 * Fournit des méthodes pour ajouter, supprimer et compter les réactions.
 */
@Service
public class ReactionService {

    private final ReactionRepository reactionRepository;
    private final PostService postService;

    private static final String DISLIKE_TYPE = "DISLIKE";

    /**
     * Constructeur avec injection des dépendances nécessaires.
     *
     * @param reactionRepository Repository pour la gestion des entités Reaction.
     * @param postService Service pour la gestion des posts.
     */
    @Autowired
    public ReactionService(ReactionRepository reactionRepository, PostService postService) {
        this.reactionRepository = reactionRepository;
        this.postService = postService;
    }

    /**
     * Active ou désactive un "like" pour un utilisateur sur un post donné.
     * Si une réaction "dislike" existe, elle est remplacée par un "like".
     *
     * @param idPost L'identifiant du post.
     * @param utilisateur L'utilisateur effectuant l'action.
     */
    public void toggleLike(Long idPost, Utilisateur utilisateur) {
        Optional<Reaction> existing = reactionRepository.findByPostIdPostAndUtilisateurIdUser(idPost, utilisateur.getIdUser());

        if (existing.isPresent()) {
            if ("LIKE".equals(existing.get().getType())) {
                reactionRepository.delete(existing.get());
            } else {
                reactionRepository.delete(existing.get());
                addLike(idPost, utilisateur);
            }
        } else {
            addLike(idPost, utilisateur);
        }
    }

    /**
     * Ajoute un "like" pour un utilisateur sur un post donné.
     *
     * @param idPost L'identifiant du post.
     * @param utilisateur L'utilisateur effectuant l'action.
     */
    private void addLike(Long idPost, Utilisateur utilisateur) {
        Post post = postService.getPostParId(idPost);
        Reaction r = new Reaction();
        r.setPost(post);
        r.setUtilisateur(utilisateur);
        r.setType("LIKE");
        r.setLiked(true);
        reactionRepository.save(r);
    }

    /**
     * Active ou désactive un "dislike" pour un utilisateur sur un post donné.
     * Si une réaction "like" existe, elle est remplacée par un "dislike".
     *
     * @param idPost L'identifiant du post.
     * @param utilisateur L'utilisateur effectuant l'action.
     */
    public void toggleDislike(Long idPost, Utilisateur utilisateur) {
        Optional<Reaction> existing = reactionRepository.findByPostIdPostAndUtilisateurIdUser(idPost, utilisateur.getIdUser());

        if (existing.isPresent()) {
            if (DISLIKE_TYPE.equals(existing.get().getType())) {
                reactionRepository.delete(existing.get());
            } else {
                reactionRepository.delete(existing.get());
                addDislike(idPost, utilisateur);
            }
        } else {
            addDislike(idPost, utilisateur);
        }
    }

    /**
     * Ajoute un "dislike" pour un utilisateur sur un post donné.
     *
     * @param idPost L'identifiant du post.
     * @param utilisateur L'utilisateur effectuant l'action.
     */
    private void addDislike(Long idPost, Utilisateur utilisateur) {
        Post post = postService.getPostParId(idPost);
        Reaction r = new Reaction();
        r.setPost(post);
        r.setUtilisateur(utilisateur);
        r.setType(DISLIKE_TYPE);
        r.setLiked(false);
        reactionRepository.save(r);
    }

    /**
     * Vérifie si un post est "liké" par un utilisateur donné.
     *
     * @param post Le post concerné.
     * @param utilisateur L'utilisateur à vérifier.
     * @return true si le post est "liké", sinon false.
     */
    public boolean isLikedBy(Post post, Utilisateur utilisateur) {
        return reactionRepository.findByPostAndUtilisateur(post, utilisateur)
                .map(Reaction::isLiked)
                .orElse(false);
    }

    /**
     * Compte le nombre de "likes" sur un post donné.
     *
     * @param post Le post concerné.
     * @return Le nombre de "likes".
     */
    public int countLikes(Post post) {
        return reactionRepository.countByPostAndType(post, "LIKE");
    }

    /**
     * Vérifie si un post est "disliké" par un utilisateur donné.
     *
     * @param post Le post concerné.
     * @param utilisateur L'utilisateur à vérifier.
     * @return true si le post est "disliké", sinon false.
     */
    public boolean isDislikedBy(Post post, Utilisateur utilisateur) {
        return reactionRepository.findByPostAndUtilisateur(post, utilisateur)
                .map(r -> DISLIKE_TYPE.equals(r.getType()))
                .orElse(false);
    }

    /**
     * Compte le nombre de "dislikes" sur un post donné.
     *
     * @param post Le post concerné.
     * @return Le nombre de "dislikes".
     */
    public int countDislikes(Post post) {
        return reactionRepository.countByPostAndType(post, DISLIKE_TYPE);
    }
}