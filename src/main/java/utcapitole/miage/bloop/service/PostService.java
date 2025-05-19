package utcapitole.miage.bloop.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import utcapitole.miage.bloop.model.entity.Post;
import utcapitole.miage.bloop.repository.PostRepository;

import java.util.List;

/**
 * Service pour gérer les opérations liées aux posts.
 */
@Service
public class PostService {

    private final PostRepository postRepository;

    /**
     * Constructeur pour injecter le repository des posts.
     *
     * @param postRepository Le repository pour interagir avec les entités Post.
     */
    @Autowired
    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    /**
     * Récupère la liste des posts d'un utilisateur spécifique.
     *
     * @param idUser L'identifiant de l'utilisateur.
     * @return La liste des posts associés à l'utilisateur.
     */
    public List<Post> getPostsByUtilisateur(long idUser) {
        return postRepository.findByUtilisateur_IdUser(idUser);
    }

    /**
     * Crée un nouveau post.
     *
     * @param post L'entité Post à créer.
     * @throws IllegalArgumentException Si le contenu du post est vide ou invalide.
     */
    public void creerPost(Post post) {
        if (post == null || post.getTextePost() == null || post.getTextePost().isBlank()) {
            throw new IllegalArgumentException("Le contenu du post ne peut pas être vide.");
        }
        post.setDatePost(java.sql.Timestamp.valueOf(java.time.LocalDateTime.now()));
        postRepository.save(post);
    }

    /**
     * Récupère un post par son identifiant.
     *
     * @param id L'identifiant du post.
     * @return Le post correspondant à l'identifiant.
     * @throws IllegalArgumentException Si l'identifiant est invalide ou si le post n'est pas trouvé.
     */
    public Post getPostParId(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("L'identifiant du post est invalide.");
        }
        return postRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("Post introuvable pour l'identifiant donné."));
    }

    /**
     * Récupère la liste des posts d'un utilisateur spécifique (méthode alternative).
     *
     * @param idUser L'identifiant de l'utilisateur.
     * @return La liste des posts associés à l'utilisateur.
     */
    public List<Post> getPostsByUtilisateurId(Long idUser) {
        return postRepository.findByUtilisateur_IdUser(idUser);
    }


    public void save(Post post) {
        postRepository.save(post);
    }
}