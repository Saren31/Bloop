package utcapitole.miage.bloop.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import utcapitole.miage.bloop.model.entity.Post;
import utcapitole.miage.bloop.model.entity.Reaction;
import utcapitole.miage.bloop.model.entity.Utilisateur;
import utcapitole.miage.bloop.repository.ReactionRepository;

import java.util.Optional;

@Service
public class ReactionService {

    @Autowired
    private ReactionRepository reactionRepository;

    @Autowired
    private PostService postService;

    public void toggleLike(Long postId, Utilisateur utilisateur) {
        Optional<Reaction> existing = reactionRepository.findByPostIdAndUtilisateurId(postId, utilisateur.getIdUser());
        if (existing.isPresent()) {
            reactionRepository.delete(existing.get());
        } else {
            Reaction r = new Reaction();
            Post post = postService.getPostParId(postId);
            r.setPost(post);
            r.setUtilisateur(utilisateur);
            r.setType("LIKE");
            reactionRepository.save(r);
        }
    }

    public boolean isLikedBy(Post post, Utilisateur utilisateur) {
        return reactionRepository.findByPostAndUtilisateur(post, utilisateur)
                .map(Reaction::isLiked)
                .orElse(false);
    }

    public int countLikes(Post post) {
        return reactionRepository.countByPostAndLiked(post, true);
    }
}
