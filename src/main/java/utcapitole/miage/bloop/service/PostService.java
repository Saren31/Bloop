package utcapitole.miage.bloop.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import utcapitole.miage.bloop.model.entity.Post;
import utcapitole.miage.bloop.repository.PostRepository;

import java.util.Date;
import java.util.List;

@Service
public class PostService {

    private final PostRepository postRepository;

    @Autowired
    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public List<Post> getPostsByUtilisateur(long idUser) {
        return postRepository.findByUtilisateur_IdUser(idUser);
    }

    public void creerPost(Post post) {
        if (post == null || post.getTextePost() == null || post.getTextePost().isBlank()) {
            throw new IllegalArgumentException("Le contenu du post ne peut pas être vide.");
        }
        post.setDatePost(java.sql.Timestamp.valueOf(java.time.LocalDateTime.now()));
        postRepository.save(post);
    }

    public Post getPostParId(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("L'identifiant du post est invalide.");
        }
        return postRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("Post introuvable pour l'identifiant donné."));
    }

    public List<Post> getPostsByUtilisateurId(Long idUser) {
        return postRepository.findByUtilisateur_IdUser(idUser);
    }

}
